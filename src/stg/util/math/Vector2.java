package stg.util.math;

/**
 * 向量2D类 - 用于处理2D向量运算
 * 创建stg.util.math包，添加向量数学工具
 * @since 2026-01-20
 */
public class Vector2 {
	public float x;
	public float y;

	public Vector2() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 复制向量
	 */
	public Vector2 copy() {
		return new Vector2(x, y);
	}

	/**
	 * 向量加法
	 */
	public Vector2 add(Vector2 other) {
		return new Vector2(this.x + other.x, this.y + other.y);
	}

	/**
	 * 向量减法
	 */
	public Vector2 subtract(Vector2 other) {
		return new Vector2(this.x - other.x, this.y - other.y);
	}

	/**
	 * 向量乘法（标量）
	 */
	public Vector2 multiply(float scalar) {
		return new Vector2(this.x * scalar, this.y * scalar);
	}

	/**
	 * 向量除法（标量）
	 */
	public Vector2 divide(float scalar) {
		return new Vector2(this.x / scalar, this.y / scalar);
	}

	/**
	 * 获取向量长度
	 */
	public float length() {
		return (float)Math.sqrt(x * x + y * y);
	}

	/**
	 * 归一化向量
	 */
	public Vector2 normalize() {
		float len = length();
		if (len == 0) {
			return new Vector2(0, 0);
		}
		return divide(len);
	}

	/**
	 * 点积
	 */
	public float dot(Vector2 other) {
		return this.x * other.x + this.y * other.y;
	}

	/**
	 * 叉积（2D向量返回标量）
	 */
	public float cross(Vector2 other) {
		return this.x * other.y - this.y * other.x;
	}

	/**
	 * 计算与另一个向量的距离
	 */
	public float distanceTo(Vector2 other) {
		return subtract(other).length();
	}

	/**
	 * 旋转向量（弧度）
	 */
	public Vector2 rotate(float angle) {
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);
		return new Vector2(x * cos - y * sin, x * sin + y * cos);
	}

	/**
	 * 获取角度（弧度）
	 */
	public float angle() {
		return (float)Math.atan2(y, x);
	}

	/**
	 * 从角度创建向量（弧度）
	 */
	public static Vector2 fromAngle(float angle) {
		return new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
	}

	@Override
	public String toString() {
		return String.format("(%.2f, %.2f)", x, y);
	}
}

