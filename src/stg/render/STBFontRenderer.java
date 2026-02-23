package stg.render;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * STB字体渲染器
 * 使用STB Truetype库实现高质量的文本渲染，支持汉字等复杂字符
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public class STBFontRenderer {
	private static STBFontRenderer instance;
	
	private STBTTFontinfo fontInfo;
	private ByteBuffer fontBuffer;
	private Map<Integer, GlyphCache> glyphCache;
	private String fontPath;
	
	/**
	 * 字形缓存类
	 */
	private static class GlyphCache {
		int textureId;
		int width;
		int height;
		int xOff;
		int yOff;
		int advanceWidth;
		
		GlyphCache(int textureId, int width, int height, int xOff, int yOff, int advanceWidth) {
			this.textureId = textureId;
			this.width = width;
			this.height = height;
			this.xOff = xOff;
			this.yOff = yOff;
			this.advanceWidth = advanceWidth;
		}
	}
	
	/**
	 * 私有构造函数
	 */
	private STBFontRenderer() {
		glyphCache = new HashMap<>();
		fontPath = "resources/fonts/OPPO Sans 4.0.ttf";
		initFont();
	}
	
	/**
	 * 获取STB字体渲染器实例
	 * @return STB字体渲染器实例
	 */
	public static synchronized STBFontRenderer getInstance() {
		if (instance == null) {
			instance = new STBFontRenderer();
		}
		return instance;
	}
	
	/**
	 * 初始化字体
	 */
	private void initFont() {
		try {
			// 尝试使用不同的路径加载字体
			String[] paths = {
				fontPath,
				"fonts/OPPO Sans 4.0.ttf",
				"resources/fonts/OPPO Sans 4.0.ttf"
			};
			
			byte[] fontBytes = null;
			for (String path : paths) {
				try {
					fontBytes = Files.readAllBytes(Paths.get(path));
					System.out.println("字体加载成功: " + path + " (" + fontBytes.length + " bytes)");
					break;
				} catch (IOException e) {
					System.err.println("字体加载失败: " + path);
				}
			}
			
			if (fontBytes == null) {
				throw new RuntimeException("无法加载字体文件");
			}
			
			fontBuffer = MemoryUtil.memAlloc(fontBytes.length);
			fontBuffer.put(fontBytes);
			fontBuffer.flip();
			
			fontInfo = STBTTFontinfo.create();
			if (!STBTruetype.stbtt_InitFont(fontInfo, fontBuffer)) {
				throw new RuntimeException("无法初始化字体信息");
			}
			
			System.out.println("STB字体渲染器初始化成功");
		} catch (Exception e) {
			System.err.println("字体初始化失败: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 渲染文本
	 * @param text 文本内容
	 * @param x X坐标
	 * @param y Y坐标
	 * @param fontSize 字体大小
	 * @param color 颜色 [r, g, b, a]
	 */
	public void renderText(String text, float x, float y, float fontSize, float[] color) {
		if (fontInfo == null || fontBuffer == null) {
			System.err.println("字体未初始化，无法渲染文本");
			return;
		}
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(color[0], color[1], color[2], color[3]);
		
		float scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, fontSize);
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			IntBuffer pXOff = stack.mallocInt(1);
			IntBuffer pYOff = stack.mallocInt(1);
			IntBuffer pAdvanceWidth = stack.mallocInt(1);
			IntBuffer pLeftSideBearing = stack.mallocInt(1);
			IntBuffer pAscent = stack.mallocInt(1);
			IntBuffer pDescent = stack.mallocInt(1);
			IntBuffer pLineGap = stack.mallocInt(1);
			
			STBTruetype.stbtt_GetFontVMetrics(fontInfo, pAscent, pDescent, pLineGap);
			int ascent = pAscent.get(0);
			
			float currentX = x;
			float baseline = y;
			
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				
				int cacheKey = (int)c | ((int)fontSize << 16);
				GlyphCache cached = glyphCache.get(cacheKey);
				
				if (cached == null) {
					cached = createGlyphCache(c, fontSize, scale, stack,
						pWidth, pHeight, pXOff, pYOff, pAdvanceWidth, pLeftSideBearing);
					glyphCache.put(cacheKey, cached);
				}
				
				if (cached.textureId > 0 && cached.width > 0 && cached.height > 0) {
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, cached.textureId);
					
					float charX = currentX + cached.xOff;
					float charY = baseline - cached.height - cached.yOff;
					
					GL11.glBegin(GL11.GL_QUADS);
					GL11.glTexCoord2f(0, 1); GL11.glVertex2f(charX, charY);
					GL11.glTexCoord2f(1, 1); GL11.glVertex2f(charX + cached.width, charY);
					GL11.glTexCoord2f(1, 0); GL11.glVertex2f(charX + cached.width, charY + cached.height);
					GL11.glTexCoord2f(0, 0); GL11.glVertex2f(charX, charY + cached.height);
					GL11.glEnd();
					
					GL11.glDisable(GL11.GL_TEXTURE_2D);
				}
				
				currentX += cached.advanceWidth * scale;
			}
		}
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	/**
	 * 创建字形缓存
	 */
	private GlyphCache createGlyphCache(char c, float fontSize, float scale, MemoryStack stack,
			IntBuffer pWidth, IntBuffer pHeight, IntBuffer pXOff, IntBuffer pYOff,
			IntBuffer pAdvanceWidth, IntBuffer pLeftSideBearing) {
		
		int glyphIndex = STBTruetype.stbtt_FindGlyphIndex(fontInfo, c);
		
		if (glyphIndex == 0) {
			return new GlyphCache(0, 0, 0, 0, 0, (int)(fontSize * 0.5f / scale));
		}
		
		ByteBuffer bitmap = STBTruetype.stbtt_GetGlyphBitmap(
			fontInfo, scale, scale, glyphIndex,
			pWidth, pHeight, pXOff, pYOff
		);
		
		int bitmapWidth = pWidth.get(0);
		int bitmapHeight = pHeight.get(0);
		int xOff = pXOff.get(0);
		int yOff = pYOff.get(0);
		
		STBTruetype.stbtt_GetGlyphHMetrics(fontInfo, glyphIndex, pAdvanceWidth, pLeftSideBearing);
		int advanceWidth = pAdvanceWidth.get(0);
		
		if (bitmap == null || bitmapWidth <= 0 || bitmapHeight <= 0) {
			return new GlyphCache(0, 0, 0, 0, 0, advanceWidth);
		}
		
		ByteBuffer rgbaBitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight * 4);
		for (int j = 0; j < bitmapWidth * bitmapHeight; j++) {
			byte alpha = bitmap.get(j);
			rgbaBitmap.put((byte) 255);
			rgbaBitmap.put((byte) 255);
			rgbaBitmap.put((byte) 255);
			rgbaBitmap.put(alpha);
		}
		rgbaBitmap.flip();
		
		int textureId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 
			bitmapWidth, bitmapHeight, 0,
			GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, rgbaBitmap);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		return new GlyphCache(textureId, bitmapWidth, bitmapHeight, xOff, yOff, advanceWidth);
	}
	
	/**
	 * 清理资源
	 */
	public void cleanup() {
		for (GlyphCache cached : glyphCache.values()) {
			if (cached.textureId > 0) {
				GL11.glDeleteTextures(cached.textureId);
			}
		}
		glyphCache.clear();
		
		if (fontBuffer != null) {
			MemoryUtil.memFree(fontBuffer);
			fontBuffer = null;
		}
		
		if (fontInfo != null) {
			fontInfo.free();
			fontInfo = null;
		}
		
		System.out.println("STB字体渲染器资源清理完成");
	}
}
