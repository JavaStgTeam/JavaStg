package stg.render;

import stg.util.CoordinateSystem;

/**
 * 相机类
 * 管理游戏世界的视图，处理世界坐标到屏幕坐标的转换
 */
public class Camera {
    private float x;
    private float y;
    private float zoom;
    private final CoordinateSystem coordinateSystem;
    
    /**
     * 构造函数
     * @param screenWidth 屏幕宽度
     * @param screenHeight 屏幕高度
     */
    public Camera(int screenWidth, int screenHeight) {
        this.x = 0;
        this.y = 0;
        this.zoom = 1.0f;
        this.coordinateSystem = new CoordinateSystem(screenWidth, screenHeight);
    }
    
    /**
     * 更新屏幕尺寸
     * @param width 新的屏幕宽度
     * @param height 新的屏幕高度
     */
    public void updateScreenSize(int width, int height) {
        coordinateSystem.updateScreenSize(width, height);
    }
    
    /**
     * 设置相机位置
     * @param x 相机X坐标
     * @param y 相机Y坐标
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * 移动相机
     * @param dx X方向移动量
     * @param dy Y方向移动量
     */
    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }
    
    /**
     * 设置缩放比例
     * @param zoom 缩放比例，1.0为正常大小
     */
    public void setZoom(float zoom) {
        this.zoom = Math.max(0.1f, Math.min(5.0f, zoom)); // 限制缩放范围
    }
    
    /**
     * 获取缩放比例
     * @return 缩放比例
     */
    public float getZoom() {
        return zoom;
    }
    
    /**
     * 获取相机X坐标
     * @return 相机X坐标
     */
    public float getX() {
        return x;
    }
    
    /**
     * 获取相机Y坐标
     * @return 相机Y坐标
     */
    public float getY() {
        return y;
    }
    
    /**
     * 将世界坐标转换为屏幕坐标
     * @param worldX 世界X坐标
     * @param worldY 世界Y坐标
     * @return 屏幕坐标数组 [x, y]
     */
    public float[] worldToScreen(float worldX, float worldY) {
        // 应用相机偏移
        float adjustedX = worldX - x;
        float adjustedY = worldY - y;
        
        // 应用缩放
        adjustedX *= zoom;
        adjustedY *= zoom;
        
        // 使用坐标系统进行最终转换
        return coordinateSystem.toScreenCoords(adjustedX, adjustedY);
    }
    
    /**
     * 将屏幕坐标转换为世界坐标
     * @param screenX 屏幕X坐标
     * @param screenY 屏幕Y坐标
     * @return 世界坐标数组 [x, y]
     */
    public float[] screenToWorld(float screenX, float screenY) {
        // 使用坐标系统进行初始转换
        float[] gameCoords = coordinateSystem.toGameCoords(screenX, screenY);
        
        // 应用缩放逆变换
        float adjustedX = gameCoords[0] / zoom;
        float adjustedY = gameCoords[1] / zoom;
        
        // 应用相机偏移逆变换
        adjustedX += x;
        adjustedY += y;
        
        return new float[]{adjustedX, adjustedY};
    }
    
    /**
     * 获取坐标系统
     * @return 坐标系统
     */
    public CoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }
}