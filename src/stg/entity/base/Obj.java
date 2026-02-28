package stg.entity.base;

import java.awt.Color;
import java.awt.Graphics2D;

import org.lwjgl.opengl.GL11;

import stg.render.IRenderable;
import stg.render.IRenderer;
import stg.util.CoordinateSystem;
import stg.util.objectpool.ObjectPoolConfig;
import stg.util.objectpool.ObjectPoolManager;

/**
 * 游戏物体基类 - 所有游戏中的物体都继承自此类
 * @since 1.0
 * @author JavaSTG Team
 * @date 2026-01-19 初始创建
 * @date 2026-01-20 将类移动到stg.game.obj
 * @date 2026-01-29 功能优化
 * @date 2026-02-16 重构坐标系统，使用固定360*480游戏逻辑尺寸
 * @date 2026-02-18 迁移到stg.entity.base包
 * @date 2026-02-21 添加对象池支持
 * @date 2026-02-26 添加加载素材方法和纹理渲染支持
 * @date 2026-02-22 将对象池配置独立到 ObjectPoolConfig 类，支持@Pooled注解自动注册
 */
public abstract class Obj implements IRenderable {
    protected float x; // X坐标
    protected float y; // Y坐标
    protected float vx; // X方向速度
    protected float vy; // Y方向速度
    protected float size; // 物体大小
    protected Color color; // 物体颜色
    protected float hitboxRadius; // 碰撞判定半径
    protected boolean active; // 激活状态
    protected int frame; // 帧计数器
    
    // 坐标系统（用于动态坐标转换）
    private static CoordinateSystem sharedCoordinateSystem;
    
    // 静态初始化块：当Obj类被加载时执行
    static {
        // 初始化对象池系统
        ObjectPoolConfig.initialize();
    }

    /**
     * 设置共享的坐标系统
     * @param coordinateSystem 坐标系统实例
     */
    public static void setSharedCoordinateSystem(CoordinateSystem coordinateSystem) {
        sharedCoordinateSystem = coordinateSystem;
    }

    /**
     * 获取共享的坐标系统
     * @return 坐标系统实例
     */
    public static CoordinateSystem getSharedCoordinateSystem() {
        return sharedCoordinateSystem;
    }

    /**
     * 检查坐标系统是否已初始化
     * @return 是否已初始化
     */
    public static boolean isCoordinateSystemInitialized() {
        return sharedCoordinateSystem != null;
    }

    /**
     * 要求坐标系统必须已初始化
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public static void requireCoordinateSystem() {
        if (sharedCoordinateSystem == null) {
            throw new IllegalStateException(
                "CoordinateSystem not initialized. " +
                "Please call Obj.setSharedCoordinateSystem() before using game objects."
            );
        }
    }

    /**
     * 将游戏坐标转换为屏幕坐标
     * @param worldX 游戏世界X坐标
     * @param worldY 游戏世界Y坐标
     * @return 屏幕坐标数组 [x, y]
     */
    public static float[] toScreenCoords(float worldX, float worldY) {
        requireCoordinateSystem();
        return sharedCoordinateSystem.toScreenCoords(worldX, worldY);
    }

    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param vx X方向速度
     * @param vy Y方向速度
     * @param size 物体大小
     * @param color 物体颜色
     */
    public Obj(float x, float y, float vx, float vy, float size, Color color) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.size = size;
        this.color = color;
        this.hitboxRadius = size;
        this.active = true;
        this.frame = 0;
        initBehavior();
    }

    /**
     * 初始化行为参数
     * 在构造函数中调用，用于初始化行为参数
     */
    protected void initBehavior() {
        // 子类可以重写此方法初始化行为参数
    }

    /**
     * 实现每帧的自定义更新逻辑
     */
    protected void onUpdate() {
        // 子类可以重写此方法实现每帧的自定义更新逻辑
    }

    /**
     * 实现自定义移动逻辑
     */
    protected void onMove() {
        // 子类可以重写此方法实现自定义移动逻辑
    }

    /**
     * 更新物体状态
     */
    public void update() {
        frame++;

        // 调用自定义更新逻辑
        onUpdate();

        // 调用自定义移动逻辑
        onMove();

        // 更新位置
        x += vx;
        y += vy;
    }

    /**
     * 渲染物体（Java2D版本）
     * @param g 图形上下文
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public void render(Graphics2D g) {
        if (!active) return;

        requireCoordinateSystem();
        float[] screenCoords = toScreenCoords(x, y);
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];

        g.setColor(color);
        g.fillOval((int)(screenX - size/2), (int)(screenY - size/2), (int)size, (int)size);
    }

    /**
     * 渲染物体（IRenderer版本，支持OpenGL）
     * @param renderer 渲染器
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public void render(IRenderer renderer) {
        if (!active) return;

        requireCoordinateSystem();
        float[] screenCoords = toScreenCoords(x, y);
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        float a = color.getAlpha() / 255.0f;
        renderer.drawCircle(screenX, screenY, size/2, r, g, b, a);
    }
    
    /**
     * 渲染物体（使用纹理，支持OpenGL）
     * @param renderer 渲染器
     * @param textureId 纹理ID
     * @param texX 素材在图片内的X坐标
     * @param texY 素材在图片内的Y坐标
     * @param texWidth 素材宽度
     * @param texHeight 素材高度
     * @param imgWidth 图片总宽度
     * @param imgHeight 图片总高度
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public void render(IRenderer renderer, int textureId, float texX, float texY, float texWidth, float texHeight, float imgWidth, float imgHeight) {
        if (!active) return;

        requireCoordinateSystem();
        float[] screenCoords = toScreenCoords(x, y);
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];
        
        // 启用纹理和混合
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        
        // 设置颜色为白色，这样纹理的颜色会正常显示
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        
        // 计算纹理坐标（归一化到0-1范围）
        float texCoordX1 = texX / imgWidth;
        float texCoordY1 = texY / imgHeight;
        float texCoordX2 = (texX + texWidth) / imgWidth;
        float texCoordY2 = (texY + texHeight) / imgHeight;
        
        // 绘制四边形，调整纹理坐标以修复上下颠倒的问题
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(texCoordX1, texCoordY2); // 左上角
        GL11.glVertex2f(screenX - size/2, screenY - size/2);
        GL11.glTexCoord2f(texCoordX2, texCoordY2); // 右上角
        GL11.glVertex2f(screenX + size/2, screenY - size/2);
        GL11.glTexCoord2f(texCoordX2, texCoordY1); // 右下角
        GL11.glVertex2f(screenX + size/2, screenY + size/2);
        GL11.glTexCoord2f(texCoordX1, texCoordY1); // 左下角
        GL11.glVertex2f(screenX - size/2, screenY + size/2);
        GL11.glEnd();
        
        // 禁用纹理和混合
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * 在屏幕中渲染物体
     * @param renderer 渲染器
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public void renderOnScreen(IRenderer renderer) {
        render(renderer);
    }

    /**
     * 检查物体是否超出边界
     * @return 是否超出边界
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public boolean isOutOfBounds() {
        requireCoordinateSystem();
        CoordinateSystem cs = sharedCoordinateSystem;
        float leftBound = cs.getLeftBound() - size;
        float rightBound = cs.getRightBound() + size;
        float topBound = cs.getTopBound() - size;
        float bottomBound = cs.getBottomBound() + size;
        return x < leftBound || x > rightBound || y < topBound || y > bottomBound;
    }

    /**
     * 移动到指定坐标
     * @param x 目标X坐标
     * @param y 目标Y坐标
     */
    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 检查物体是否激活
     * @return 是否激活
     */
    public boolean isActive() {
        return active;
    }

    /**
     * 设置物体激活状态
     * @param active 是否激活
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * 获取X坐标
     * @return X坐标
     */
    public float getX() {
        return x;
    }

    /**
     * 获取Y坐标
     * @return Y坐标
     */
    public float getY() {
        return y;
    }

    /**
     * 获取物体大小
     * @return 物体大小
     */
    public float getSize() {
        return size;
    }

    /**
     * 获取物体颜色
     * @return 物体颜色
     */
    public Color getColor() {
        return color;
    }

    /**
     * 重置物体状态
     */
    public void reset() {
        this.active = true;
        this.frame = 0;
    }

    /**
     * 设置X坐标
     * @param x X坐标
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * 设置Y坐标
     * @param y Y坐标
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * 设置X方向速度
     * @param vx X方向速度
     */
    public void setVx(float vx) {
        this.vx = vx;
    }

    /**
     * 设置Y方向速度
     * @param vy Y方向速度
     */
    public void setVy(float vy) {
        this.vy = vy;
    }

    /**
     * 获取X方向速度
     * @return X方向速度
     */
    public float getVx() {
        return vx;
    }

    /**
     * 获取Y方向速度
     * @return Y方向速度
     */
    public float getVy() {
        return vy;
    }

    /**
     * 设置碰撞判定半径
     * @param hitboxRadius 碰撞判定半径
     */
    public void setHitboxRadius(float hitboxRadius) {
        this.hitboxRadius = hitboxRadius;
    }

    /**
     * 获取碰撞判定半径
     * @return 碰撞判定半径
     */
    public float getHitboxRadius() {
        return hitboxRadius;
    }
    
    /**
     * 获取渲染层级
     * @return 渲染层级
     */
    @Override
    public int getRenderLayer() {
        return 3;
    }
    
    // ==================== 对象池操作方法 ====================
    
    /**
     * 从对象池创建实例
     * 注意：使用此方法前需要先调用 ObjectPoolConfig.initialize() 初始化对象池
     * @param <T> 对象类型
     * @param clazz 对象类型的 Class
     * @param args 构造函数参数
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T extends Obj> T create(Class<T> clazz, Object... args) {
        try {
            // 尝试从对象池获取或创建对象
            try {
                // 获取或创建对象池
                ObjectPoolManager.getInstance().getOrCreatePool(clazz);
                
                // 尝试从对象池获取
                T object = ObjectPoolManager.getInstance().acquire(clazz);
                if (object != null) {
                    // 设置对象的属性
                    if (args.length >= 2) {
                        // 处理不同类型的参数
                        if (args[0] instanceof Number) {
                            object.setX(((Number) args[0]).floatValue());
                        }
                        if (args[1] instanceof Number) {
                            object.setY(((Number) args[1]).floatValue());
                        }
                    }
                    return object;
                }
            } catch (Exception poolEx) {
                // 对象池获取失败，记录异常
                System.err.println("Object pool acquire failed: " + poolEx.getMessage());
            }
            
            // 直接创建对象
            try {
                // 查找匹配的构造函数
                for (java.lang.reflect.Constructor<?> constructor : clazz.getConstructors()) {
                    if (constructor.getParameterCount() == args.length) {
                        T object = (T) constructor.newInstance(args);
                        
                        // 将新创建的对象添加到对象池中（如果对象池存在）
                        try {
                            ObjectPoolManager.getInstance().release(object);
                            // 重新从对象池获取，这样可以确保对象被正确跟踪
                            return ObjectPoolManager.getInstance().acquire(clazz);
                        } catch (Exception e) {
                            // 忽略异常，直接返回创建的对象
                        }
                        
                        return object;
                    }
                }
                throw new IllegalArgumentException("No suitable constructor found for " + clazz.getName());
            } catch (Exception ex) {
                throw new RuntimeException("Failed to create object: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create object: " + e.getMessage(), e);
        }
    }
    
    /**
     * 回收对象到对象池
     * @param <T> 对象类型
     * @param object 要回收的对象
     */
    public static <T extends Obj> void release(T object) {
        if (object == null) {
            return;
        }
        
        try {
            ObjectPoolManager.getInstance().release(object);
        } catch (Exception e) {
            // 如果对象池未初始化或失败，忽略异常
            // 对象会被 GC 回收
        }
    }
    
    /**
     * 加载素材
     * 使用OpenGL加载图片，支持从图片中截取特定区域的素材
     * @param path 图片文件路径
     * @param x 素材在图片内的X坐标
     * @param y 素材在图片内的Y坐标
     * @param width 素材宽度
     * @param height 素材高度
     * @return 纹理ID
     */
    public static int loadTexture(String path, float x, float y, float width, float height) {
        // 直接使用GL11来加载纹理，不创建新的GLRenderer实例
        // 这样可以确保在同一个OpenGL上下文中加载纹理
        int textureId = -1;
        
        try (org.lwjgl.system.MemoryStack stack = org.lwjgl.system.MemoryStack.stackPush()) {
            java.nio.IntBuffer widthBuffer = stack.mallocInt(1);
            java.nio.IntBuffer heightBuffer = stack.mallocInt(1);
            java.nio.IntBuffer channelsBuffer = stack.mallocInt(1);
            
            // 尝试从文件系统直接读取
            java.nio.file.Path filePath = java.nio.file.Paths.get(path);
            if (!java.nio.file.Files.exists(filePath)) {
                // 如果文件不存在，尝试从类路径读取
                System.out.println("文件系统中找不到图片文件: " + path + "，尝试从类路径读取");
                // 从类路径读取图片
                java.io.InputStream inputStream = Obj.class.getClassLoader().getResourceAsStream(path);
                if (inputStream == null) {
                    System.err.println("图片文件不存在: " + path);
                    return -1;
                }
                
                // 读取输入流到字节数组
                byte[] bytes = inputStream.readAllBytes();
                inputStream.close();
                
                // 分配内存并填充数据
                java.nio.ByteBuffer buffer = org.lwjgl.system.MemoryUtil.memAlloc(bytes.length);
                buffer.put(bytes);
                buffer.flip();
                
                // 加载图片
                java.nio.ByteBuffer image = org.lwjgl.stb.STBImage.stbi_load_from_memory(buffer, widthBuffer, heightBuffer, channelsBuffer, 4);
                org.lwjgl.system.MemoryUtil.memFree(buffer);
                
                if (image == null) {
                    System.err.println("加载图片失败: " + org.lwjgl.stb.STBImage.stbi_failure_reason());
                    return -1;
                }
                
                // 创建纹理
                textureId = org.lwjgl.opengl.GL11.glGenTextures();
                org.lwjgl.opengl.GL11.glBindTexture(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, textureId);
                
                // 设置纹理参数
                org.lwjgl.opengl.GL11.glTexParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER, org.lwjgl.opengl.GL11.GL_LINEAR);
                org.lwjgl.opengl.GL11.glTexParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER, org.lwjgl.opengl.GL11.GL_LINEAR);
                
                // 保存宽度和高度值
                int imgWidth = widthBuffer.get();
                int imgHeight = heightBuffer.get();
                
                // 上传纹理数据
                org.lwjgl.opengl.GL11.glTexImage2D(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, 0, org.lwjgl.opengl.GL11.GL_RGBA, imgWidth, imgHeight, 0, org.lwjgl.opengl.GL11.GL_RGBA, org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE, image);
                
                // 释放图片数据
                org.lwjgl.stb.STBImage.stbi_image_free(image);
                
                System.out.println("从类路径加载纹理成功: " + path + " (" + imgWidth + "x" + imgHeight + ")");
            } else {
                // 从文件系统直接读取
                java.nio.ByteBuffer image = org.lwjgl.stb.STBImage.stbi_load(path, widthBuffer, heightBuffer, channelsBuffer, 4);
                
                if (image == null) {
                    System.err.println("加载图片失败: " + org.lwjgl.stb.STBImage.stbi_failure_reason());
                    return -1;
                }
                
                // 创建纹理
                textureId = org.lwjgl.opengl.GL11.glGenTextures();
                org.lwjgl.opengl.GL11.glBindTexture(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, textureId);
                
                // 设置纹理参数
                org.lwjgl.opengl.GL11.glTexParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER, org.lwjgl.opengl.GL11.GL_LINEAR);
                org.lwjgl.opengl.GL11.glTexParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER, org.lwjgl.opengl.GL11.GL_LINEAR);
                
                // 保存宽度和高度值
                int imgWidth = widthBuffer.get();
                int imgHeight = heightBuffer.get();
                
                // 上传纹理数据
                org.lwjgl.opengl.GL11.glTexImage2D(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, 0, org.lwjgl.opengl.GL11.GL_RGBA, imgWidth, imgHeight, 0, org.lwjgl.opengl.GL11.GL_RGBA, org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE, image);
                
                // 释放图片数据
                org.lwjgl.stb.STBImage.stbi_image_free(image);
                
                System.out.println("从文件系统加载纹理成功: " + path + " (" + imgWidth + "x" + imgHeight + ")");
            }
            
        } catch (Exception e) {
            System.err.println("加载纹理失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return textureId;
    }
}
