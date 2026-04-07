package stg.util;

/**
 * 边界工具类 - 提供边界检查相关的功能
 * @since 26-04-07 初始创建
 * @author JavaSTG Team
 */
public class BoundsUtil {
    /**
     * 检查物体是否超出边界
     * @param x 物体X坐标
     * @param y 物体Y坐标
     * @param size 物体大小
     * @param coordinateSystem 坐标系统
     * @return 是否超出边界
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public static boolean isOutOfBounds(float x, float y, float size, CoordinateSystem coordinateSystem) {
        if (coordinateSystem == null) {
            throw new IllegalStateException(
                "CoordinateSystem not initialized. " +
                "Please call Obj.setSharedCoordinateSystem() before using game objects."
            );
        }
        
        float leftBound = coordinateSystem.getLeftBound() - size;
        float rightBound = coordinateSystem.getRightBound() + size;
        float topBound = coordinateSystem.getTopBound() - size;
        float bottomBound = coordinateSystem.getBottomBound() + size;
        return x < leftBound || x > rightBound || y < topBound || y > bottomBound;
    }
}