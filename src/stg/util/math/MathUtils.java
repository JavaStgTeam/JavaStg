package stg.util.math;

/**
 * 数学工具类
 * 创建stg.util.math包，添加数学工具函数
 * @since 2026-01-20
 */
public class MathUtils {

	/**
	 * 将角度从度转换为弧度
	 */
	public static float toRadians(float degrees) {
		return (float)Math.toRadians(degrees);
	}

	/**
	 * 将角度从弧度转换为度
	 */
	public static float toDegrees(float radians) {
		return (float)Math.toDegrees(radians);
	}

	/**
	 * 线性插值
	 * @param a 起始值
	 * @param b 结束值
	 * @param t 插值因子 [0, 1]
	 */
	public static float lerp(float a, float b, float t) {
		return a + (b - a) * t;
	}

	/**
	 * 向量线性插值
	 */
	public static Vector2 lerp(Vector2 a, Vector2 b, float t) {
		return new Vector2(
			lerp(a.x, b.x, t),
			lerp(a.y, b.y, t)
		);
	}

	/**
	 * 限制值在指定范围
	 */
	public static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * 限制整数值在指定范围
	 */
	public static int clamp(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * 平滑阻尼（用于平滑过渡）
	 * @param current 当前值
	 * @param target 目标值
	 * @param factor 阻尼因子 [0, 1]，值越小越平滑
	 */
	public static float damp(float current, float target, float factor) {
		return lerp(current, target, factor);
	}

	/**
	 * 平滑阻尼（向量版本）
	 */
	public static Vector2 damp(Vector2 current, Vector2 target, float factor) {
		return lerp(current, target, factor);
	}

	/**
	 * 计算两点间的平方距离（比distance更快，适用于距离比较）
	 */
	public static float distanceSquared(float x1, float y1, float x2, float y2) {
		float dx = x2 - x1;
		float dy = y2 - y1;
		return dx * dx + dy * dy;
	}

	/**
	 * 计算两点间的距离
	 */
	public static float distance(float x1, float y1, float x2, float y2) {
		return (float)Math.sqrt(distanceSquared(x1, y1, x2, y2));
	}

	/**
	 * 规范化角度到 [-180, 180] 或 [-π, π]
	 * @param angle 角度（度数）
	 */
	public static float normalizeAngle(float angle) {
		while (angle > 180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}

	/**
	 * 规范化弧度到 [-π, π]
	 */
	public static float normalizeRadians(float radians) {
		while (radians > Math.PI) radians -= 2 * Math.PI;
		while (radians < -Math.PI) radians += 2 * Math.PI;
		return (float)radians;
	}

	/**
	 * 检查点是否在矩形内
	 * @param px 点的X坐标
	 * @param py 点的Y坐标
	 * @param rectX 矩形左上角X
	 * @param rectY 矩形左上角Y
	 * @param rectW 矩形宽度
	 * @param rectH 矩形高度
	 */
	public static boolean pointInRect(float px, float py, float rectX, float rectY, float rectW, float rectH) {
		return px >= rectX && px <= rectX + rectW && py >= rectY && py <= rectY + rectH;
	}

	/**
	 * 检查点是否在圆形内
	 * @param px 点的X坐标
	 * @param py 点的Y坐标
	 * @param cx 圆心X
	 * @param cy 圆心Y
	 * @param radius 半径
	 */
	public static boolean pointInCircle(float px, float py, float cx, float cy, float radius) {
		return distanceSquared(px, py, cx, cy) <= radius * radius;
	}

	/**
	 * 检查两个圆是否相交
	 */
	public static boolean circleCollision(float x1, float y1, float r1, float x2, float y2, float r2) {
		return distanceSquared(x1, y1, x2, y2) <= (r1 + r2) * (r1 + r2);
	}

	/**
	 * 弹跳反射
	 * @param vx X速度
	 * @param vy Y速度
	 * @param normalX 法线X（归一化）
	 * @param normalY 法线Y（归一化）
	 * @return 反射后的速度向量
	 */
	public static Vector2 reflect(float vx, float vy, float normalX, float normalY) {
		float dot = vx * normalX + vy * normalY;
		return new Vector2(
			vx - 2 * dot * normalX,
			vy - 2 * dot * normalY
		);
	}

	/**
	 * 简单的随机数（伪随机）
	 * @param min 最小值
	 * @param max 最大值
	 */
	public static float randomRange(float min, float max) {
		return min + (float)Math.random() * (max - min);
	}

	/**
	 * 简单的随机整数
	 */
	public static int randomInt(int min, int max) {
		return min + (int)(Math.random() * (max - min + 1));
	}
}

