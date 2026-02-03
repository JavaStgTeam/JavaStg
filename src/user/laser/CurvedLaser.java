package user.laser;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import stg.game.laser.Laser;

/**
 * 曲线激光类 - 继承自Laser，具有可调节长度的拖尾系统
 * @since 2026-01-21
 */
public class CurvedLaser extends Laser {
	private float vx;
	private float vy;
	private List<Point> trailPoints;
	private int maxTrailLength;
	private float trailWidth;

	private static class Point {
		float x;
		float y;
		float width;

		Point(float x, float y, float width) {
			this.x = x;
			this.y = y;
			this.width = width;
		}
	}

	public CurvedLaser(float x, float y, float angle, float length, float width, Color color,
					   float vx, float vy, int maxTrailLength) {
		this(x, y, angle, length, width, color, 60, 10, vx, vy, maxTrailLength);
	}

	public CurvedLaser(float x, float y, float angle, float length, float width, Color color, int warningTime, int damage, float vx, float vy, int maxTrailLength) {
		super(x, y, angle, length, width, color, warningTime, damage);
		this.vx = vx;
		this.vy = vy;
		this.maxTrailLength = maxTrailLength;
		this.trailWidth = width;
		this.trailPoints = new ArrayList<>();
	}

	@Override
	protected void initBehavior() {
	}

	@Override
	protected void onUpdate() {
		if (active) {
			trailPoints.add(new Point(x, y, width));

			if (trailPoints.size() > maxTrailLength) {
				trailPoints.remove(0);
			}
		}
	}

	@Override
	protected void onMove() {
		if (active) {
			x += vx;
			y += vy;

			if (vx != 0 || vy != 0) {
				angle = (float)Math.atan2(vy, vx);
			}
		}
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	protected void renderLaser(Graphics2D g2d) {
		for (int i = 0; i < trailPoints.size() - 1; i++) {
			Point p1 = trailPoints.get(i);
			Point p2 = trailPoints.get(i + 1);

			// 直接计算屏幕坐标，因为 GameCanvas 可能没有 toScreenCoords 方法
			float screenStartX = p1.x + gameCanvas.getWidth() / 2.0f;
			float screenStartY = gameCanvas.getHeight() / 2.0f - p1.y;
			float screenEndX = p2.x + gameCanvas.getWidth() / 2.0f;
			float screenEndY = gameCanvas.getHeight() / 2.0f - p2.y;

			float alpha = (float)(i + 1) / trailPoints.size();
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();

			g2d.setColor(new Color(r, g, b, (int)(alpha * 200)));
			float segmentWidth = p1.width * alpha;
			g2d.setStroke(new BasicStroke(segmentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.drawLine((int)screenStartX, (int)screenStartY, (int)screenEndX, (int)screenEndY);
		}

		if (!trailPoints.isEmpty()) {
			Point head = trailPoints.get(trailPoints.size() - 1);
			float screenHeadX = head.x + gameCanvas.getWidth() / 2.0f;
			float screenHeadY = gameCanvas.getHeight() / 2.0f - head.y;

			g2d.setColor(color);
			g2d.fillOval((int)(screenHeadX - head.width/2), (int)(screenHeadY - head.width/2),
						(int)head.width, (int)head.width);
		}
	}

	@Override
	public boolean checkCollision(float px, float py) {
		if (!active || !visible) return false;

		for (int i = 0; i < trailPoints.size() - 1; i++) {
			Point p1 = trailPoints.get(i);
			Point p2 = trailPoints.get(i + 1);

			float dx = p2.x - p1.x;
			float dy = p2.y - p1.y;
			float segmentAngle = (float)Math.atan2(dy, dx);
			float segmentLength = (float)Math.sqrt(dx * dx + dy * dy);

			float distance = pointToLineDistance(px, py, p1.x, p1.y, segmentAngle, segmentLength);
			if (distance < p1.width / 2.0f) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isOutOfBounds(int width, int height) {
		float leftBound = -width / 2.0f - length;
		float rightBound = width / 2.0f + length;
		float topBound = -height / 2.0f - length;
		float bottomBound = height / 2.0f + length;
		return x < leftBound || x > rightBound || y < topBound || y > bottomBound;
	}

	public float getVx() { return vx; }
	public float getVy() { return vy; }
	public int getMaxTrailLength() { return maxTrailLength; }
	public float getTrailWidth() { return trailWidth; }
	public int getCurrentTrailLength() { return trailPoints.size(); }

	public void setVx(float vx) { this.vx = vx; }
	public void setVy(float vy) { this.vy = vy; }
	public void setMaxTrailLength(int maxTrailLength) { this.maxTrailLength = maxTrailLength; }
	public void setTrailWidth(float trailWidth) { this.trailWidth = trailWidth; }

	public void setSpeed(float speed, float angle) {
		this.vx = (float)Math.cos(angle) * speed;
		this.vy = (float)Math.sin(angle) * speed;
	}

	@Override
	protected void onTaskStart() {
	}

	@Override
	protected void onTaskEnd() {
	}
}
