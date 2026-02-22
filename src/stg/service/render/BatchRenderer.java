package stg.service.render;

import java.awt.Color;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * 批量渲染器实现
 * 优化渲染性能，减少OpenGL调用次数
 */
public class BatchRenderer {
    private static final int MAX_VERTICES = 10000;
    private static final int VERTEX_SIZE = 7; // x, y, z, r, g, b, a
    private static final int TEXTURE_VERTEX_SIZE = 5; // x, y, z, u, v
    private static final int BUFFER_SIZE = MAX_VERTICES * VERTEX_SIZE * 4;
    private static final int TEXTURE_BUFFER_SIZE = MAX_VERTICES * TEXTURE_VERTEX_SIZE * 4;
    
    private float[] vertices;
    private float[] textureVertices;
    private int vertexCount;
    private int textureVertexCount;
    private int vbo;
    private int textureVbo;
    private int vao;
    private int textureVao;
    private ShaderProgram shader;
    private ShaderProgram textureShader;
    private boolean inBatch;
    private boolean inTextureBatch;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureVertexBuffer;
    
    /**
     * 构造函数
     * @param shader 默认着色器
     */
    public BatchRenderer(ShaderProgram shader) {
        this(shader, null);
    }
    
    /**
     * 构造函数
     * @param shader 默认着色器
     * @param textureShader 纹理着色器
     */
    public BatchRenderer(ShaderProgram shader, ShaderProgram textureShader) {
        this.shader = shader;
        this.textureShader = textureShader;
        this.vertices = new float[MAX_VERTICES * VERTEX_SIZE];
        this.textureVertices = new float[MAX_VERTICES * TEXTURE_VERTEX_SIZE];
        this.vertexCount = 0;
        this.textureVertexCount = 0;
        this.inBatch = false;
        this.inTextureBatch = false;
        this.vertexBuffer = BufferUtils.createFloatBuffer(MAX_VERTICES * VERTEX_SIZE);
        this.textureVertexBuffer = BufferUtils.createFloatBuffer(MAX_VERTICES * TEXTURE_VERTEX_SIZE);
        
        initBuffers();
    }
    
    /**
     * 初始化缓冲区
     */
    private void initBuffers() {
        try {
            System.out.println("【渲染】初始化批量渲染器缓冲区");
            
            // 创建顶点缓冲区对象
            vbo = GL15.glGenBuffers();
            textureVbo = GL15.glGenBuffers();
            
            // 创建顶点数组对象
            vao = GL30.glGenVertexArrays();
            textureVao = GL30.glGenVertexArrays();
            
            // 配置颜色渲染的VAO
            GL30.glBindVertexArray(vao);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BUFFER_SIZE, GL15.GL_DYNAMIC_DRAW);
            
            // 位置属性 (x, y, z)
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, VERTEX_SIZE * 4, 0);
            GL20.glEnableVertexAttribArray(0);
            
            // 颜色属性 (r, g, b, a)
            GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, VERTEX_SIZE * 4, 3 * 4);
            GL20.glEnableVertexAttribArray(1);
            
            // 配置纹理渲染的VAO
            GL30.glBindVertexArray(textureVao);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureVbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, TEXTURE_BUFFER_SIZE, GL15.GL_DYNAMIC_DRAW);
            
            // 位置属性 (x, y, z)
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, TEXTURE_VERTEX_SIZE * 4, 0);
            GL20.glEnableVertexAttribArray(0);
            
            // 纹理坐标属性 (u, v)
            GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, TEXTURE_VERTEX_SIZE * 4, 3 * 4);
            GL20.glEnableVertexAttribArray(1);
            
            // 解绑
            GL30.glBindVertexArray(0);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            
            System.out.println("【渲染】批量渲染器缓冲区初始化成功");
        } catch (Exception e) {
            System.err.println("【渲染】初始化批量渲染器缓冲区失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 开始批量渲染
     */
    public void begin() {
        if (inBatch || inTextureBatch) {
            System.err.println("【渲染】批量渲染已经开始");
            return;
        }
        
        try {
            System.out.println("【渲染】开始批量渲染");
            vertexCount = 0;
            textureVertexCount = 0;
            inBatch = true;
            inTextureBatch = false;
        } catch (Exception e) {
            System.err.println("【渲染】开始批量渲染失败: " + e.getMessage());
            e.printStackTrace();
            inBatch = false;
            inTextureBatch = false;
        }
    }
    
    /**
     * 结束批量渲染
     */
    public void end() {
        if (!inBatch && !inTextureBatch) {
            System.err.println("【渲染】批量渲染尚未开始");
            return;
        }
        
        try {
            // 渲染颜色批量
            if (inBatch && vertexCount > 0) {
                flushColorBatch();
            }
            
            // 渲染纹理批量
            if (inTextureBatch && textureVertexCount > 0) {
                flushTextureBatch();
            }
            
            inBatch = false;
            inTextureBatch = false;
            System.out.println("【渲染】结束批量渲染");
        } catch (Exception e) {
            System.err.println("【渲染】结束批量渲染失败: " + e.getMessage());
            e.printStackTrace();
            inBatch = false;
            inTextureBatch = false;
        }
    }
    
    /**
     * 绘制矩形
     * @param x X坐标
     * @param y Y坐标
     * @param width 宽度
     * @param height 高度
     * @param color 颜色
     */
    public void drawRect(float x, float y, float width, float height, Color color) {
        if (!inBatch) {
            System.err.println("【渲染】批量渲染尚未开始");
            return;
        }
        
        try {
            // 检查是否需要切换到颜色批量
            if (inTextureBatch) {
                flushTextureBatch();
                inTextureBatch = false;
                inBatch = true;
            }
            
            // 检查缓冲区容量
            if (vertexCount + 4 > MAX_VERTICES) {
                flushColorBatch();
            }
            
            // 计算顶点位置
            float x2 = x + width;
            float y2 = y + height;
            
            // 提取颜色分量
            float r = color.getRed() / 255.0f;
            float g = color.getGreen() / 255.0f;
            float b = color.getBlue() / 255.0f;
            float a = color.getAlpha() / 255.0f;
            
            // 添加顶点
            addVertex(x, y, 0, r, g, b, a);
            addVertex(x2, y, 0, r, g, b, a);
            addVertex(x, y2, 0, r, g, b, a);
            addVertex(x2, y2, 0, r, g, b, a);
        } catch (Exception e) {
            System.err.println("【渲染】绘制矩形失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 绘制圆形
     * @param x X坐标
     * @param y Y坐标
     * @param radius 半径
     * @param color 颜色
     */
    public void drawCircle(float x, float y, float radius, Color color) {
        if (!inBatch) {
            System.err.println("【渲染】批量渲染尚未开始");
            return;
        }
        
        try {
            // 检查是否需要切换到颜色批量
            if (inTextureBatch) {
                flushTextureBatch();
                inTextureBatch = false;
                inBatch = true;
            }
            
            int segments = 32;
            float angleIncrement = (float) (Math.PI * 2 / segments);
            
            // 检查缓冲区容量
            if (vertexCount + segments + 1 > MAX_VERTICES) {
                flushColorBatch();
            }
            
            // 提取颜色分量
            float r = color.getRed() / 255.0f;
            float g = color.getGreen() / 255.0f;
            float b = color.getBlue() / 255.0f;
            float a = color.getAlpha() / 255.0f;
            
            // 添加中心顶点
            addVertex(x, y, 0, r, g, b, a);
            
            // 添加边缘顶点
            for (int i = 0; i <= segments; i++) {
                float angle = i * angleIncrement;
                float nx = x + (float) Math.cos(angle) * radius;
                float ny = y + (float) Math.sin(angle) * radius;
                addVertex(nx, ny, 0, r, g, b, a);
            }
        } catch (Exception e) {
            System.err.println("【渲染】绘制圆形失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 绘制线条
     * @param x1 起点X坐标
     * @param y1 起点Y坐标
     * @param x2 终点X坐标
     * @param y2 终点Y坐标
     * @param color 颜色
     */
    public void drawLine(float x1, float y1, float x2, float y2, Color color) {
        drawLine(x1, y1, x2, y2, 1.0f, color);
    }
    
    /**
     * 绘制带宽度的线条
     * @param x1 起点X坐标
     * @param y1 起点Y坐标
     * @param x2 终点X坐标
     * @param y2 终点Y坐标
     * @param width 线宽
     * @param color 颜色
     */
    public void drawLine(float x1, float y1, float x2, float y2, float width, Color color) {
        if (!inBatch) {
            System.err.println("【渲染】批量渲染尚未开始");
            return;
        }
        
        try {
            // 检查是否需要切换到颜色批量
            if (inTextureBatch) {
                flushTextureBatch();
                inTextureBatch = false;
                inBatch = true;
            }
            
            // 计算线条方向和垂直方向
            float dx = x2 - x1;
            float dy = y2 - y1;
            float length = (float) Math.sqrt(dx * dx + dy * dy);
            
            if (length == 0) return;
            
            // 单位向量
            float ux = dx / length;
            float uy = dy / length;
            
            // 垂直单位向量
            float vx = -uy * (width / 2);
            float vy = ux * (width / 2);
            
            // 计算四个顶点
            float x3 = x1 + vx;
            float y3 = y1 + vy;
            float x4 = x1 - vx;
            float y4 = y1 - vy;
            float x5 = x2 + vx;
            float y5 = y2 + vy;
            float x6 = x2 - vx;
            float y6 = y2 - vy;
            
            // 检查缓冲区容量
            if (vertexCount + 4 > MAX_VERTICES) {
                flushColorBatch();
            }
            
            // 提取颜色分量
            float r = color.getRed() / 255.0f;
            float g = color.getGreen() / 255.0f;
            float b = color.getBlue() / 255.0f;
            float a = color.getAlpha() / 255.0f;
            
            // 添加顶点
            addVertex(x3, y3, 0, r, g, b, a);
            addVertex(x4, y4, 0, r, g, b, a);
            addVertex(x5, y5, 0, r, g, b, a);
            addVertex(x6, y6, 0, r, g, b, a);
        } catch (Exception e) {
            System.err.println("【渲染】绘制线条失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 绘制纹理
     * @param x X坐标
     * @param y Y坐标
     * @param width 宽度
     * @param height 高度
     * @param textureId 纹理ID
     */
    public void drawTexture(float x, float y, float width, float height, int textureId) {
        drawTexture(x, y, width, height, textureId, 0.0f);
    }
    
    /**
     * 绘制带旋转的纹理
     * @param x X坐标
     * @param y Y坐标
     * @param width 宽度
     * @param height 高度
     * @param textureId 纹理ID
     * @param rotation 旋转角度（弧度）
     */
    public void drawTexture(float x, float y, float width, float height, int textureId, float rotation) {
        if (!inBatch) {
            System.err.println("【渲染】批量渲染尚未开始");
            return;
        }
        
        try {
            // 检查是否需要切换到纹理批量
            if (!inTextureBatch) {
                if (vertexCount > 0) {
                    flushColorBatch();
                }
                inTextureBatch = true;
                inBatch = false;
            }
            
            // 检查缓冲区容量
            if (textureVertexCount + 4 > MAX_VERTICES) {
                flushTextureBatch();
            }
            
            // 计算旋转后的顶点
            float centerX = x + width / 2;
            float centerY = y + height / 2;
            float cos = (float) Math.cos(rotation);
            float sin = (float) Math.sin(rotation);
            
            // 计算四个顶点
            float x1 = centerX + cos * (-width / 2) - sin * (-height / 2);
            float y1 = centerY + sin * (-width / 2) + cos * (-height / 2);
            float x2 = centerX + cos * (width / 2) - sin * (-height / 2);
            float y2 = centerY + sin * (width / 2) + cos * (-height / 2);
            float x3 = centerX + cos * (-width / 2) - sin * (height / 2);
            float y3 = centerY + sin * (-width / 2) + cos * (height / 2);
            float x4 = centerX + cos * (width / 2) - sin * (height / 2);
            float y4 = centerY + sin * (width / 2) + cos * (height / 2);
            
            // 添加顶点和纹理坐标
            addTextureVertex(x1, y1, 0, 0, 0);
            addTextureVertex(x2, y2, 0, 1, 0);
            addTextureVertex(x3, y3, 0, 0, 1);
            addTextureVertex(x4, y4, 0, 1, 1);
        } catch (Exception e) {
            System.err.println("【渲染】绘制纹理失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 添加顶点
     */
    private void addVertex(float x, float y, float z, float r, float g, float b, float a) {
        int index = vertexCount * VERTEX_SIZE;
        vertices[index] = x;
        vertices[index + 1] = y;
        vertices[index + 2] = z;
        vertices[index + 3] = r;
        vertices[index + 4] = g;
        vertices[index + 5] = b;
        vertices[index + 6] = a;
        vertexCount++;
    }
    
    /**
     * 添加纹理顶点
     */
    private void addTextureVertex(float x, float y, float z, float u, float v) {
        int index = textureVertexCount * TEXTURE_VERTEX_SIZE;
        textureVertices[index] = x;
        textureVertices[index + 1] = y;
        textureVertices[index + 2] = z;
        textureVertices[index + 3] = u;
        textureVertices[index + 4] = v;
        textureVertexCount++;
    }
    
    /**
     * 刷新颜色批量
     */
    private void flushColorBatch() {
        if (vertexCount == 0) return;
        
        try {
            System.out.println("【渲染】刷新颜色批量，顶点数: " + vertexCount);
            
            // 绑定VAO和VBO
            GL30.glBindVertexArray(vao);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            
            // 准备顶点数据
            vertexBuffer.clear();
            vertexBuffer.put(vertices, 0, vertexCount * VERTEX_SIZE);
            vertexBuffer.flip();
            
            // 上传顶点数据
            GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertexBuffer);
            
            // 使用着色器
            if (shader != null) {
                shader.use();
            }
            
            // 绘制
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, vertexCount);
            
            // 重置顶点计数
            vertexCount = 0;
        } catch (Exception e) {
            System.err.println("【渲染】刷新颜色批量失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 刷新纹理批量
     */
    private void flushTextureBatch() {
        if (textureVertexCount == 0) return;
        
        try {
            System.out.println("【渲染】刷新纹理批量，顶点数: " + textureVertexCount);
            
            // 绑定VAO和VBO
            GL30.glBindVertexArray(textureVao);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureVbo);
            
            // 准备顶点数据
            textureVertexBuffer.clear();
            textureVertexBuffer.put(textureVertices, 0, textureVertexCount * TEXTURE_VERTEX_SIZE);
            textureVertexBuffer.flip();
            
            // 上传顶点数据
            GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, textureVertexBuffer);
            
            // 使用纹理着色器
            if (textureShader != null) {
                textureShader.use();
            } else if (shader != null) {
                shader.use();
            }
            
            // 启用纹理
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            
            // 绘制
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, textureVertexCount);
            
            // 重置顶点计数
            textureVertexCount = 0;
        } catch (Exception e) {
            System.err.println("【渲染】刷新纹理批量失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 清理资源
     */
    public void cleanup() {
        try {
            System.out.println("【渲染】清理批量渲染器资源");
            
            // 清理VAO
            if (vao != 0) {
                GL30.glDeleteVertexArrays(vao);
                vao = 0;
            }
            
            if (textureVao != 0) {
                GL30.glDeleteVertexArrays(textureVao);
                textureVao = 0;
            }
            
            // 清理VBO
            if (vbo != 0) {
                GL15.glDeleteBuffers(vbo);
                vbo = 0;
            }
            
            if (textureVbo != 0) {
                GL15.glDeleteBuffers(textureVbo);
                textureVbo = 0;
            }
            
            vertices = null;
            textureVertices = null;
            vertexBuffer = null;
            textureVertexBuffer = null;
            shader = null;
            textureShader = null;
        } catch (Exception e) {
            System.err.println("【渲染】清理批量渲染器资源失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
