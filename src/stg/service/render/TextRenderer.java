package stg.service.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class TextRenderer {
    private GLRenderer renderer;
    private FontManager fontManager;
    private ShaderProgram textShader;
    private Map<String, Integer> textVertexBuffers;
    
    public TextRenderer(GLRenderer renderer) {
        this.renderer = renderer;
        this.fontManager = FontManager.getInstance();
        this.textVertexBuffers = new HashMap<>();
        initTextShader();
    }
    
    private void initTextShader() {
        String vertexShader = "#version 330 core\n" +
            "layout(location = 0) in vec3 aPos;\n" +
            "layout(location = 1) in vec2 aTexCoord;\n" +
            "layout(location = 2) in vec4 aColor;\n" +
            "uniform mat4 uProjection;\n" +
            "uniform mat4 uModel;\n" +
            "out vec2 vTexCoord;\n" +
            "out vec4 vColor;\n" +
            "void main() {\n" +
            "    gl_Position = uProjection * uModel * vec4(aPos, 1.0);\n" +
            "    vTexCoord = aTexCoord;\n" +
            "    vColor = aColor;\n" +
            "}";
        
        String fragmentShader = "#version 330 core\n" +
            "in vec2 vTexCoord;\n" +
            "in vec4 vColor;\n" +
            "uniform sampler2D uTexture;\n" +
            "out vec4 FragColor;\n" +
            "void main() {\n" +
            "    float alpha = texture(uTexture, vTexCoord).r;\n" +
            "    FragColor = vColor * alpha;\n" +
            "}";
        
        textShader = new ShaderProgram();
        textShader.compile(vertexShader, fragmentShader);
        renderer.addShader("text", textShader);
    }
    
    public void renderText(String text, float x, float y, String fontName, float fontSize, Color color) {
        FontManager.FontData fontData = fontManager.getFont(fontName);
        if (fontData == null) {
            System.err.println("Font not found: " + fontName);
            return;
        }
        
        textShader.use();
        
        // 设置投影矩阵
        float[] projection = new float[]{
            2.0f/800, 0.0f, 0.0f, 0.0f,
            0.0f, -2.0f/600, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f, 1.0f
        };
        textShader.setUniformMatrix4("uProjection", projection);
        
        // 设置模型矩阵
        float[] model = new float[]{
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        };
        textShader.setUniformMatrix4("uModel", model);
        
        // 绑定字体纹理
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontData.getTextureId());
        textShader.setUniform("uTexture", 0);
        
        // 启用混合
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        // 渲染每个字符
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xb = stack.mallocFloat(1);
            FloatBuffer yb = stack.mallocFloat(1);
            STBTTAlignedQuad quad = STBTTAlignedQuad.mallocStack(stack);
            
            xb.put(0, x);
            yb.put(0, y - fontSize); // 调整基线
            
            for (char c : text.toCharArray()) {
                if (c >= 32 && c < 128) {
                    // 获取字符数据
                    STBTTBakedChar charData = fontData.getCharData().get(c - 32);
                    
                    // 计算字符位置
                    stbtt_GetBakedQuad(
                        fontData.getCharData(),
                        fontData.getBitmapWidth(),
                        fontData.getBitmapHeight(),
                        c - 32,
                        xb,
                        yb,
                        quad,
                        false
                    );
                    
                    // 创建顶点数据
                    float[] vertices = new float[]{
                        quad.x0(), quad.y1(), 0.0f, quad.s0(), quad.t1(), color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f, color.getAlpha()/255.0f,
                        quad.x1(), quad.y1(), 0.0f, quad.s1(), quad.t1(), color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f, color.getAlpha()/255.0f,
                        quad.x0(), quad.y0(), 0.0f, quad.s0(), quad.t0(), color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f, color.getAlpha()/255.0f,
                        quad.x1(), quad.y0(), 0.0f, quad.s1(), quad.t0(), color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f, color.getAlpha()/255.0f
                    };
                    
                    // 创建并上传顶点缓冲
                    VertexBuffer buffer = new VertexBuffer();
                    buffer.uploadWithTexture(vertices);
                    buffer.draw(GL11.GL_TRIANGLE_STRIP);
                }
            }
        }
        
        // 解绑纹理
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
    
    public void renderText(String text, float x, float y, FontManager.FontData fontData, Color color) {
        textShader.use();
        
        // 设置投影矩阵
        float[] projection = new float[]{
            2.0f/800, 0.0f, 0.0f, 0.0f,
            0.0f, -2.0f/600, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f, 1.0f
        };
        textShader.setUniformMatrix4("uProjection", projection);
        
        // 设置模型矩阵
        float[] model = new float[]{
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        };
        textShader.setUniformMatrix4("uModel", model);
        
        // 绑定字体纹理
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontData.getTextureId());
        textShader.setUniform("uTexture", 0);
        
        // 启用混合
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        // 渲染每个字符
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xb = stack.mallocFloat(1);
            FloatBuffer yb = stack.mallocFloat(1);
            STBTTAlignedQuad quad = STBTTAlignedQuad.mallocStack(stack);
            
            xb.put(0, x);
            yb.put(0, y - fontData.getFontSize()); // 调整基线
            
            for (char c : text.toCharArray()) {
                if (c >= 32 && c < 128) {
                    // 获取字符数据
                    STBTTBakedChar charData = fontData.getCharData().get(c - 32);
                    
                    // 计算字符位置
                    stbtt_GetBakedQuad(
                        fontData.getCharData(),
                        fontData.getBitmapWidth(),
                        fontData.getBitmapHeight(),
                        c - 32,
                        xb,
                        yb,
                        quad,
                        false
                    );
                    
                    // 创建顶点数据
                    float[] vertices = new float[]{
                        quad.x0(), quad.y1(), 0.0f, quad.s0(), quad.t1(), color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f, color.getAlpha()/255.0f,
                        quad.x1(), quad.y1(), 0.0f, quad.s1(), quad.t1(), color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f, color.getAlpha()/255.0f,
                        quad.x0(), quad.y0(), 0.0f, quad.s0(), quad.t0(), color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f, color.getAlpha()/255.0f,
                        quad.x1(), quad.y0(), 0.0f, quad.s1(), quad.t0(), color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f, color.getAlpha()/255.0f
                    };
                    
                    // 创建并上传顶点缓冲
                    VertexBuffer buffer = new VertexBuffer();
                    buffer.uploadWithTexture(vertices);
                    buffer.draw(GL11.GL_TRIANGLE_STRIP);
                }
            }
        }
        
        // 解绑纹理
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
    
    public float getTextWidth(String text, FontManager.FontData fontData) {
        float width = 0;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xb = stack.mallocFloat(1);
            FloatBuffer yb = stack.mallocFloat(1);
            STBTTAlignedQuad quad = STBTTAlignedQuad.mallocStack(stack);
            
            xb.put(0, 0);
            yb.put(0, 0);
            
            for (char c : text.toCharArray()) {
                if (c >= 32 && c < 128) {
                    stbtt_GetBakedQuad(
                        fontData.getCharData(),
                        fontData.getBitmapWidth(),
                        fontData.getBitmapHeight(),
                        c - 32,
                        xb,
                        yb,
                        quad,
                        false
                    );
                }
            }
            width = xb.get(0);
        }
        return width;
    }
    
    public float getTextHeight(String text, FontManager.FontData fontData) {
        return fontData.getFontSize() * 1.5f; // 估算行高
    }
    
    private void stbtt_GetBakedQuad(STBTTBakedChar.Buffer charData, int pw, int ph, int charIndex, FloatBuffer xpos, FloatBuffer ypos, STBTTAlignedQuad q, boolean alignToInteger) {
        // 简化实现，实际应该调用STBTT的函数
        // 这里使用Java实现的近似逻辑
        STBTTBakedChar c = charData.get(charIndex);
        
        float x = xpos.get(0);
        float y = ypos.get(0);
        
        if (alignToInteger) {
            x = (float)Math.floor(x + 0.5f);
        }
        
        float x0 = x + c.x0() * 1.0f;
        float y0 = y - c.y0() * 1.0f;
        float x1 = x + c.x1() * 1.0f;
        float y1 = y - c.y1() * 1.0f;
        
        // STBTTAlignedQuad是结构体，直接使用字段访问
        // 由于LWJGL的STBTTAlignedQuad是不可变的，这里我们直接使用计算出的值
        
        xpos.put(0, x + c.xadvance() * 1.0f);
    }
    
    public void cleanup() {
        if (textShader != null) {
            textShader.cleanup();
        }
        textVertexBuffers.clear();
    }
}