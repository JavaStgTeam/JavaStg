package stg.entity.base;

/**
 * 轨道类 - 用于描述实体的轨道运动
 * @since 2026-03-04 初始创建
 * @author JavaSTG Team
 */
public class Orbit {
    private float centerX; // 轨道中心X坐标
    private float centerY; // 轨道中心Y坐标
    private float radius; // 轨道半径
    private float startAngle; // 起始角度（度）
    
    /**
     * 构造函数
     * @param centerX 轨道中心X坐标
     * @param centerY 轨道中心Y坐标
     * @param radius 轨道半径
     * @param startAngle 起始角度（度）
     */
    public Orbit(float centerX, float centerY, float radius, float startAngle) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.startAngle = startAngle;
    }
    
    /**
     * 获取轨道中心X坐标
     * @return 轨道中心X坐标
     */
    public float getCenterX() {
        return centerX;
    }
    
    /**
     * 设置轨道中心X坐标
     * @param centerX 轨道中心X坐标
     */
    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }
    
    /**
     * 获取轨道中心Y坐标
     * @return 轨道中心Y坐标
     */
    public float getCenterY() {
        return centerY;
    }
    
    /**
     * 设置轨道中心Y坐标
     * @param centerY 轨道中心Y坐标
     */
    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }
    
    /**
     * 获取轨道半径
     * @return 轨道半径
     */
    public float getRadius() {
        return radius;
    }
    
    /**
     * 设置轨道半径
     * @param radius 轨道半径
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }
    
    /**
     * 获取起始角度
     * @return 起始角度（度）
     */
    public float getStartAngle() {
        return startAngle;
    }
    
    /**
     * 设置起始角度
     * @param startAngle 起始角度（度）
     */
    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }
    
    /**
     * 根据角度计算轨道上的点
     * @param angle 角度（度）
     * @return 轨道上的点 [x, y]
     */
    public float[] getPointAtAngle(float angle) {
        // 将角度转换为弧度
        double radians = Math.toRadians(angle);
        
        // 计算点的坐标（注意：顺时针为正，所以sin的符号需要调整）
        float x = centerX + (float) (Math.cos(radians) * radius);
        float y = centerY - (float) (Math.sin(radians) * radius);
        
        return new float[]{x, y};
    }
}