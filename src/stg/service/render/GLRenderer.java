package stg.service.render;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

/**
 * OpenGL渲染器实现
 * 负责初始化OpenGL上下文和着色器，实现基本渲染操作
 */
public class GLRenderer implements IRenderer {
    private boolean initialized = false;
    private Color currentColor;
    private Font currentFont;
    
    // 批量渲染器
    private BatchRenderer batchRenderer;
    private ShaderProgram defaultShader;
    private ShaderProgram textureShader;
    
    // 纹理管理器
    private TextureManager textureManager;
    
    // 批量渲染模式标志
    private boolean inBatchMode = false;
    
    // 窗口句柄
    private long window;
    
    // 窗口尺寸
    private int width = 800;
    private int height = 600;
    
    // 文本渲染器
    private TextRenderer textRenderer;
    
    /**
     * 无参构造函数
     */
    public GLRenderer() {
    }
    
    /**
     * 构造函数
     * @param window 窗口句柄
     */
    public GLRenderer(long window) {
        this.window = window;
    }
    
    /**
     * 构造函数
     * @param window 窗口句柄
     * @param width 窗口宽度
     * @param height 窗口高度
     */
    public GLRenderer(long window, int width, int height) {
        this.window = window;
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void init() {
        if (initialized) return;
        
        try {
            System.out.println("【渲染】开始初始化OpenGL渲染器");
            
            // 初始化OpenGL上下文
            System.out.println("【渲染】初始化OpenGL上下文");
            GL.createCapabilities();
            
            // 设置基本OpenGL状态
            System.out.println("【渲染】设置OpenGL状态");
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_CULL_FACE);
            
            // 创建默认着色器程序
            System.out.println("【渲染】创建默认着色器");
            try {
                defaultShader = ShaderProgram.createDefaultShader();
                if (defaultShader == null) {
                    System.err.println("【渲染】默认着色器创建失败");
                    return;
                }
            } catch (Exception e) {
                System.err.println("【渲染】创建默认着色器异常: " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            // 创建纹理着色器程序
            System.out.println("【渲染】创建纹理着色器");
            try {
                textureShader = ShaderProgram.createTextureShader();
                if (textureShader == null) {
                    System.err.println("【渲染】纹理着色器创建失败");
                    return;
                }
            } catch (Exception e) {
                System.err.println("【渲染】创建纹理着色器异常: " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            // 初始化批量渲染器
            System.out.println("【渲染】初始化批量渲染器");
            try {
                batchRenderer = new BatchRenderer(defaultShader, textureShader);
                if (batchRenderer == null) {
                    System.err.println("【渲染】批量渲染器初始化失败");
                    return;
                }
            } catch (Exception e) {
                System.err.println("【渲染】初始化批量渲染器异常: " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            // 初始化纹理管理器
            System.out.println("【渲染】初始化纹理管理器");
            try {
                textureManager = new TextureManager();
                if (textureManager == null) {
                    System.err.println("【渲染】纹理管理器初始化失败");
                    return;
                }
            } catch (Exception e) {
                System.err.println("【渲染】初始化纹理管理器异常: " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            // 初始化文本渲染器
            System.out.println("【渲染】初始化文本渲染器");
            try {
                textRenderer = new TextRenderer(this);
                if (textRenderer == null) {
                    System.err.println("【渲染】文本渲染器初始化失败");
                    return;
                }
            } catch (Exception e) {
                System.err.println("【渲染】初始化文本渲染器异常: " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            // 设置默认颜色和字体
            currentColor = Color.WHITE;
            currentFont = new Font("Arial", Font.PLAIN, 12);
            
            // 设置视口
            System.out.println("【渲染】设置视口: " + width + "x" + height);
            GL11.glViewport(0, 0, width, height);
            
            initialized = true;
            System.out.println("【渲染】OpenGL渲染器初始化成功");
        } catch (Exception e) {
            System.err.println("【渲染】OpenGL渲染器初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void beginFrame() {
        if (!initialized) {
            init();
            if (!initialized) {
                System.err.println("【渲染】渲染器未初始化，无法开始帧");
                return;
            }
        }
        
        try {
            // 清除颜色缓冲区
            System.out.println("【渲染】开始帧，清除颜色缓冲区");
            GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            
            // 使用默认着色器
            if (defaultShader != null) {
                defaultShader.use();
                
                // 设置正交投影矩阵 - 使用屏幕像素坐标
                float[] projectionMatrix = {
                    2.0f / width, 0.0f, 0.0f, 0.0f,
                    0.0f, -2.0f / height, 0.0f, 0.0f,
                    0.0f, 0.0f, -1.0f, 0.0f,
                    -1.0f, 1.0f, 0.0f, 1.0f
                };
                
                // 设置默认模型矩阵
                float[] modelMatrix = {
                    1.0f, 0.0f, 0.0f, 0.0f,
                    0.0f, 1.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 1.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 1.0f
                };
                
                // 设置默认颜色
                float r = currentColor.getRed() / 255.0f;
                float g = currentColor.getGreen() / 255.0f;
                float b = currentColor.getBlue() / 255.0f;
                float a = currentColor.getAlpha() / 255.0f;
                
                // 设置默认着色器的参数
                defaultShader.use();
                defaultShader.setUniformMatrix4("uProjection", projectionMatrix);
                defaultShader.setUniformMatrix4("uModel", modelMatrix);
                defaultShader.setUniform("uColor", r, g, b, a);
                
                // 同时设置纹理着色器的参数
                if (textureShader != null) {
                    textureShader.use();
                    textureShader.setUniformMatrix4("uProjection", projectionMatrix);
                }
                
                // 最后切换回默认着色器
                defaultShader.use();
            }
        } catch (Exception e) {
            System.err.println("【渲染】开始帧失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void endFrame() {
        if (!initialized) return;
        
        try {
            // 渲染文本
            if (textRenderer != null) {
                System.out.println("【渲染】渲染文本");
                textRenderer.batchRender();
            }
            
            // 结束使用着色器程序
            ShaderProgram.unuse();
            
            // 交换缓冲区
            if (window != 0) {
                org.lwjgl.glfw.GLFW.glfwSwapBuffers(window);
                System.out.println("【渲染】交换缓冲区");
            }
            
            System.out.println("【渲染】结束帧");
        } catch (Exception e) {
            System.err.println("【渲染】结束帧失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void beginBatch() {
        if (!initialized) {
            init();
            if (!initialized) return;
        }
        
        try {
            System.out.println("【渲染】开始批量渲染");
            inBatchMode = true;
            if (batchRenderer != null) {
                batchRenderer.begin();
            }
        } catch (Exception e) {
            System.err.println("【渲染】开始批量渲染失败: " + e.getMessage());
            e.printStackTrace();
            inBatchMode = false;
        }
    }
    
    @Override
    public void endBatch() {
        if (!initialized || !inBatchMode) {
            inBatchMode = false;
            return;
        }
        
        try {
            System.out.println("【渲染】结束批量渲染");
            if (batchRenderer != null) {
                batchRenderer.end();
            }
            inBatchMode = false;
        } catch (Exception e) {
            System.err.println("【渲染】结束批量渲染失败: " + e.getMessage());
            e.printStackTrace();
            inBatchMode = false;
        }
    }
    
    @Override
    public void drawCircle(float x, float y, float radius, Color color) {
        if (!initialized) return;
        
        try {
            if (inBatchMode && batchRenderer != null) {
                batchRenderer.drawCircle(x, y, radius, color);
            } else {
                batchRenderer.begin();
                batchRenderer.drawCircle(x, y, radius, color);
                batchRenderer.end();
            }
        } catch (Exception e) {
            System.err.println("【渲染】绘制圆形失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawRect(float x, float y, float width, float height, Color color) {
        if (!initialized) return;
        
        try {
            if (inBatchMode && batchRenderer != null) {
                batchRenderer.drawRect(x, y, width, height, color);
            } else {
                batchRenderer.begin();
                batchRenderer.drawRect(x, y, width, height, color);
                batchRenderer.end();
            }
        } catch (Exception e) {
            System.err.println("【渲染】绘制矩形失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawLine(float x1, float y1, float x2, float y2, Color color) {
        if (!initialized) return;
        
        try {
            if (inBatchMode && batchRenderer != null) {
                batchRenderer.drawLine(x1, y1, x2, y2, color);
            } else {
                batchRenderer.begin();
                batchRenderer.drawLine(x1, y1, x2, y2, color);
                batchRenderer.end();
            }
        } catch (Exception e) {
            System.err.println("【渲染】绘制线条失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawLine(float x1, float y1, float x2, float y2, float lineWidth, Color color) {
        if (!initialized) return;
        
        try {
            if (inBatchMode && batchRenderer != null) {
                batchRenderer.drawLine(x1, y1, x2, y2, lineWidth, color);
            } else {
                batchRenderer.begin();
                batchRenderer.drawLine(x1, y1, x2, y2, lineWidth, color);
                batchRenderer.end();
            }
        } catch (Exception e) {
            System.err.println("【渲染】绘制带宽度的线条失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawText(String text, float x, float y, Font font, Color color) {
        if (!initialized) return;
        
        try {
            if (textRenderer != null) {
                textRenderer.renderText(text, x, y, font.getName(), font.getSize(), color);
            }
        } catch (Exception e) {
            System.err.println("【渲染】绘制文本失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawText(String text, float x, float y, float fontSize, Color color) {
        if (!initialized) return;
        
        try {
            if (textRenderer != null) {
                textRenderer.renderText(text, x, y, currentFont.getName(), fontSize, color);
            }
        } catch (Exception e) {
            System.err.println("【渲染】绘制文本失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawImage(Object image, float x, float y, float width, float height) {
        if (!initialized) return;
        
        try {
            if (image instanceof String) {
                String textureName = (String) image;
                if (textureManager != null) {
                    int textureId = textureManager.getTexture(textureName);
                    if (textureId != -1) {
                        if (inBatchMode && batchRenderer != null) {
                            batchRenderer.drawTexture(x, y, width, height, textureId);
                        } else {
                            batchRenderer.begin();
                            batchRenderer.drawTexture(x, y, width, height, textureId);
                            batchRenderer.end();
                        }
                    } else {
                        System.err.println("【渲染】找不到纹理: " + textureName);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("【渲染】绘制图像失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawImage(Object image, float x, float y, float width, float height, float alpha) {
        if (!initialized) return;
        
        try {
            drawImage(image, x, y, width, height, 0.0f, alpha);
        } catch (Exception e) {
            System.err.println("【渲染】绘制带透明度的图像失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawImage(Object image, float x, float y, float width, float height, float rotation, float alpha) {
        if (!initialized) return;
        
        try {
            if (image instanceof String) {
                String textureName = (String) image;
                if (textureManager != null) {
                    int textureId = textureManager.getTexture(textureName);
                    if (textureId != -1) {
                        if (inBatchMode && batchRenderer != null) {
                            batchRenderer.drawTexture(x, y, width, height, textureId, rotation);
                        } else {
                            batchRenderer.begin();
                            batchRenderer.drawTexture(x, y, width, height, textureId, rotation);
                            batchRenderer.end();
                        }
                    } else {
                        System.err.println("【渲染】找不到纹理: " + textureName);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("【渲染】绘制带旋转的图像失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void setColor(Color color) {
        currentColor = color;
        
        if (!initialized) return;
        
        try {
            float r = color.getRed() / 255.0f;
            float g = color.getGreen() / 255.0f;
            float b = color.getBlue() / 255.0f;
            float a = color.getAlpha() / 255.0f;
            
            // 设置着色器中的颜色
            if (defaultShader != null) {
                defaultShader.setUniform("uColor", r, g, b, a);
            }
            
            // 设置OpenGL默认颜色
            GL11.glColor4f(r, g, b, a);
        } catch (Exception e) {
            System.err.println("【渲染】设置颜色失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void setFont(Font font) {
        currentFont = font;
    }
    
    @Override
    public void enableAntiAliasing() {
        if (!initialized) return;
        
        try {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_POINT_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
            GL11.glHint(GL11.GL_POINT_SMOOTH_HINT, GL11.GL_NICEST);
        } catch (Exception e) {
            System.err.println("【渲染】启用抗锯齿失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void disableAntiAliasing() {
        if (!initialized) return;
        
        try {
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_POINT_SMOOTH);
        } catch (Exception e) {
            System.err.println("【渲染】禁用抗锯齿失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void cleanup() {
        if (!initialized) return;
        
        try {
            System.out.println("【渲染】清理OpenGL渲染器资源");
            
            // 清理批量渲染器
            if (batchRenderer != null) {
                batchRenderer.cleanup();
                batchRenderer = null;
            }
            
            // 清理着色器程序
            if (defaultShader != null) {
                defaultShader.cleanup();
                defaultShader = null;
            }
            
            if (textureShader != null) {
                textureShader.cleanup();
                textureShader = null;
            }
            
            // 清理纹理管理器
            if (textureManager != null) {
                textureManager.cleanup();
                textureManager = null;
            }
            
            // 清理文本渲染器
            if (textRenderer != null) {
                textRenderer.cleanup();
                textRenderer = null;
            }
            
            initialized = false;
            System.out.println("【渲染】OpenGL渲染器资源清理完成");
        } catch (Exception e) {
            System.err.println("【渲染】清理资源失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 设置视口
     * @param x 视口左上角X坐标
     * @param y 视口左上角Y坐标
     * @param width 视口宽度
     * @param height 视口高度
     */
    public void setViewport(int x, int y, int width, int height) {
        if (!initialized) return;
        
        try {
            GL11.glViewport(x, y, width, height);
            this.width = width;
            this.height = height;
        } catch (Exception e) {
            System.err.println("【渲染】设置视口失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 获取纹理管理器
     * @return TextureManager实例
     */
    public TextureManager getTextureManager() {
        return textureManager;
    }
    
    /**
     * 获取窗口宽度
     * @return 窗口宽度
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * 获取窗口高度
     * @return 窗口高度
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * 设置窗口尺寸
     * @param width 窗口宽度
     * @param height 窗口高度
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        if (initialized) {
            setViewport(0, 0, width, height);
        }
    }
}
