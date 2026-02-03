package stg.util.math;

import java.util.Random;

/**
 * 随机数生成器类
 * 创建stg.util.math包，添加随机数生成器
 * 简化类,移除不必要的功能
 * @since 2026-01-20
 */
public class RandomGenerator {
	private Random random;

	public RandomGenerator() {
		this.random = new Random();
	}

	public RandomGenerator(long seed) {
		this.random = new Random(seed);
	}

	public int randomInt(int min, int max) {
		return min + random.nextInt(max - min + 1);
	}

	public int randomInt(int max) {
		return random.nextInt(max + 1);
	}

	public float randomRange(float min, float max) {
		return min + random.nextFloat() * (max - min);
	}

	public float randomFloat(float max) {
		return random.nextFloat() * max;
	}

	public long randomLong(long min, long max) {
		return min + (long)(random.nextDouble() * (max - min + 1));
	}

	public boolean randomBoolean() {
		return random.nextBoolean();
	}

	public <T> T choice(T[] array) {
		if (array == null || array.length == 0) {
			throw new IllegalArgumentException("Array cannot be null or empty");
		}
		return array[random.nextInt(array.length)];
	}

	public void setSeed(long seed) {
		random.setSeed(seed);
	}

	public Random getRandom() {
		return random;
	}

	public Vector2 randomVector2(float xRange, float yRange) {
		return new Vector2(randomRange(-xRange, xRange), randomRange(-yRange, yRange));
	}

	public java.awt.Color randomColor() {
		return new java.awt.Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	public java.awt.Color randomColorAlpha() {
		return new java.awt.Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}
}

