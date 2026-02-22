package stg.service.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

/**
 * 文本渲染器实现
 * 负责高效渲染文本
 */
public class TextRenderer {
    private static final int MAX_QUADS = 1000;
    private static final int VERTEX_SIZE = 5; // x, y, z, u, v
    private static final int BUFFER_SIZE = MAX_QUADS * 4 * VERTEX_SIZE * 4;
    
    private GLRenderer renderer;
    private ShaderProgram textShader;
    private int vbo;
    private int vao;
    private float[] vertices;
    private int vertexCount;
    private FontManager fontManager;
    private java.nio.FloatBuffer vertexBuffer;
    
    /**
     * 构造函数
     * @param renderer GLRenderer实例
     */
    public TextRenderer(GLRenderer renderer) {
        this.renderer = renderer;
        this.fontManager = FontManager.getInstance();
        this.vertices = new float[MAX_QUADS * 4 * VERTEX_SIZE];
        this.vertexCount = 0;
        this.vertexBuffer = org.lwjgl.BufferUtils.createFloatBuffer(MAX_QUADS * 4 * VERTEX_SIZE);
        
        init();
    }
    
    /**
     * 初始化
     */
    private void init() {
        try {
            System.out.println("【渲染】初始化文本渲染器");
            
            // 创建文本着色器
            textShader = ShaderProgram.createTextShader();
            if (textShader == null) {
                System.err.println("【渲染】文本着色器创建失败");
                return;
            }
            
            // 创建VAO和VBO
            vao = GL30.glGenVertexArrays();
            vbo = GL15.glGenBuffers();
            
            // 配置VAO
            GL30.glBindVertexArray(vao);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BUFFER_SIZE, GL15.GL_DYNAMIC_DRAW);
            
            // 位置属性
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, VERTEX_SIZE * 4, 0);
            GL20.glEnableVertexAttribArray(0);
            
            // 纹理坐标属性
            GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, VERTEX_SIZE * 4, 3 * 4);
            GL20.glEnableVertexAttribArray(1);
            
            // 解绑
            GL30.glBindVertexArray(0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            
            System.out.println("【渲染】文本渲染器初始化成功");
        } catch (Exception e) {
            System.err.println("【渲染】初始化文本渲染器失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 渲染文本
     * @param text 文本内容
     * @param x X坐标
     * @param y Y坐标
     * @param fontName 字体名称
     * @param fontSize 字体大小
     * @param color 颜色
     */
    public void renderText(String text, float x, float y, String fontName, float fontSize, Color color) {
        try {
            // 加载字体
            FontManager.FontData fontData = fontManager.getOrLoadFont(fontName, "fonts/" + fontName + ".ttf", (int) fontSize);
            if (fontData == null) {
                System.err.println("【渲染】无法加载字体: " + fontName);
                return;
            }
            
            // 渲染文本
            try (MemoryStack stack = MemoryStack.stackPush()) {
                STBTTAlignedQuad quad = STBTTAlignedQuad.mallocStack(stack);
                float[] xpos = {x};
                float[] ypos = {y};
                
                for (char c : text.toCharArray()) {
                    if (c == '\n') {
                        xpos[0] = x;
                        ypos[0] -= fontSize * 1.5f;
                        continue;
                    }
                    
                    STBTruetype.stbtt_GetBakedQuad(
                        fontData.getCharData(),
                        fontData.getBitmapWidth(),
                        fontData.getBitmapHeight(),
                        c - 32,
                        xpos,
                        ypos,
                        quad,
                        true
                    );
                    
                    // 添加四边形顶点
                    addQuad(
                        quad.x0(), quad.y1(), 0,
                        quad.s0(), quad.t1(),
                        quad.x1(), quad.y1(), 0,
                        quad.s1(), quad.t1(),
                        quad.x0(), quad.y0(), 0,
                        quad.s0(), quad.t0(),
                        quad.x1(), quad.y0(), 0,
                        quad.s1(), quad.t0()
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("【渲染】渲染文本失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 添加四边形顶点
     */
    private void addQuad(float x1, float y1, float z1, float u1, float v1,
                         float x2, float y2, float z2, float u2, float v2,
                         float x3, float y3, float z3, float u3, float v3,
                         float x4, float y4, float z4, float u4, float v4) {
        if (vertexCount + 4 > MAX_QUADS * 4) {
            flush();
        }
        
        int index = vertexCount * VERTEX_SIZE;
        
        // 顶点 1
        vertices[index] = x1;
        vertices[index + 1] = y1;
        vertices[index + 2] = z1;
        vertices[index + 3] = u1;
        vertices[index + 4] = v1;
        
        // 顶点 2
        vertices[index + 5] = x2;
        vertices[index + 6] = y2;
        vertices[index + 7] = z2;
        vertices[index + 8] = u2;
        vertices[index + 9] = v2;
        
        // 顶点 3
        vertices[index + 10] = x3;
        vertices[index + 11] = y3;
        vertices[index + 12] = z3;
        vertices[index + 13] = u3;
        vertices[index + 14] = v3;
        
        // 顶点 4
        vertices[index + 15] = x4;
        vertices[index + 16] = y4;
        vertices[index + 17] = z4;
        vertices[index + 18] = u4;
        vertices[index + 19] = v4;
        
        vertexCount += 4;
    }
    
    /**
     * 批量渲染文本
     */
    public void batchRender() {
        if (vertexCount > 0) {
            flush();
        }
    }
    
    /**
     * 刷新渲染
     */
    private void flush() {
        if (vertexCount == 0) return;
        
        try {
            System.out.println("【渲染】刷新文本渲染，顶点数: " + vertexCount);
            
            // 绑定VAO和VBO
            GL30.glBindVertexArray(vao);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            
            // 上传顶点数据
            vertexBuffer.clear();
            vertexBuffer.put(vertices, 0, vertexCount * VERTEX_SIZE);
            vertexBuffer.flip();
            GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertexBuffer);
            
            // 使用文本着色器
            textShader.use();
            
            // 设置正交投影矩阵
            if (renderer != null) {
                int width = renderer.getWidth();
                int height = renderer.getHeight();
                float[] projectionMatrix = {
                    2.0f / width, 0.0f, 0.0f, 0.0f,
                    0.0f, -2.0f / height, 0.0f, 0.0f,
                    0.0f, 0.0f, -1.0f, 0.0f,
                    -1.0f, 1.0f, 0.0f, 1.0f
                };
                textShader.setUniformMatrix4("uProjection", projectionMatrix);
            }
            
            // 启用纹理
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            textShader.setUniform("uTexture", 0);
            
            // 绘制
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
            
            // 重置顶点计数
            vertexCount = 0;
        } catch (Exception e) {
            System.err.println("【渲染】刷新文本渲染失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 清理资源
     */
    public void cleanup() {
        try {
            System.out.println("【渲染】清理文本渲染器资源");
            
            // 清理VAO和VBO
            if (vao != 0) {
                GL30.glDeleteVertexArrays(vao);
                vao = 0;
            }
            
            if (vbo != 0) {
                GL15.glDeleteBuffers(vbo);
                vbo = 0;
            }
            
            // 清理着色器
            if (textShader != null) {
                textShader.cleanup();
                textShader = null;
            }
            
            vertices = null;
            vertexBuffer = null;
        } catch (Exception e) {
            System.err.println("【渲染】清理文本渲染器资源失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
