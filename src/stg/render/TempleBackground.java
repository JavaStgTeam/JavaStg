package stg.render;

/**
 * 寺庙风格默认背景
 * 使用2D分条透视模拟从 temple.lua 移植的3D寺庙背景效果
 * 素材: resources/images/temple/ground.png, road.png, pillar.png
 * @since 2026-06-07
 */
public class TempleBackground {

	private int groundTexId = -1;
	private int roadTexId = -1;
	private int pillarTexId = -1;
	private float scrollOffset = 0;
	private boolean texturesLoaded = false;

	private static final float SCROLL_SPEED = 0.004f;
	private static final int NUM_STRIPS = 16;
	private static final int NUM_PILLAR_DEPTHS = 4;

	public TempleBackground() {
	}

	public void loadTextures(GLRenderer renderer) {
		String[] basePaths = {
			"resources/images/temple/",
			"images/temple/"
		};

		for (String base : basePaths) {
			groundTexId = renderer.loadTexture(base + "ground.png");
			if (groundTexId != -1) {
				roadTexId = renderer.loadTexture(base + "road.png");
				pillarTexId = renderer.loadTexture(base + "pillar.png");
				break;
			}
		}

		texturesLoaded = groundTexId != -1 && roadTexId != -1 && pillarTexId != -1;
		if (texturesLoaded) {
			System.out.println("Temple background textures loaded (ground=" + groundTexId
				+ ", road=" + roadTexId + ", pillar=" + pillarTexId + ")");
		} else {
			System.err.println("Temple background: texture loading failed, using fallback color");
		}
	}

	public void render(IRenderer renderer, float panelWidth, float panelHeight) {
		scrollOffset += SCROLL_SPEED;

		if (!texturesLoaded) {
			renderer.drawRect(0, 0, panelWidth, panelHeight, 0.05f, 0.05f, 0.1f, 1.0f);
			return;
		}

		renderGroundAndRoad(renderer, panelWidth, panelHeight);
		renderPillars(renderer, panelWidth, panelHeight);
		renderFog(renderer, panelWidth, panelHeight);
	}

	private void renderGroundAndRoad(IRenderer renderer, float panelWidth, float panelHeight) {
		float stripHeight = panelHeight / NUM_STRIPS;
		float texDelta = 0.06f;
		float texSliceHeight = texDelta + 0.002f;
		float maxRoadHalfWidth = panelWidth * 0.14f;

		for (int i = 0; i < NUM_STRIPS; i++) {
			float t = (float) i / NUM_STRIPS;
			float stripTop = panelHeight - (i + 1) * stripHeight;
			float stripBottom = panelHeight - i * stripHeight;

			float roadHalfWidth = maxRoadHalfWidth * (1.0f - t);
			float roadLeft = panelWidth / 2 - roadHalfWidth;
			float roadRight = panelWidth / 2 + roadHalfWidth;

			float texY = scrollOffset + i * texDelta;

			if (roadLeft > 0) {
				renderer.drawImage(groundTexId, 0, stripTop, roadLeft, stripHeight,
					0, texY, 1, texSliceHeight);
			}

			if (roadRight < panelWidth) {
				renderer.drawImage(groundTexId, roadRight, stripTop, panelWidth - roadRight, stripHeight,
					0, texY, 1, texSliceHeight);
			}

			if (roadRight > roadLeft) {
				renderer.drawImage(roadTexId, roadLeft, stripTop, roadRight - roadLeft, stripHeight,
					0, texY, 1, texSliceHeight);
			}
		}
	}

	private void renderPillars(IRenderer renderer, float panelWidth, float panelHeight) {
		float basePillarW = panelWidth * 0.055f;
		float basePillarH = panelHeight * 0.10f;
		float maxRoadHalfWidth = panelWidth * 0.14f;

		for (int d = 0; d < NUM_PILLAR_DEPTHS; d++) {
			float t = 0.08f + d * 0.20f;
			float y = panelHeight * (0.12f + d * 0.20f);

			float roadHalfWidth = maxRoadHalfWidth * (1.0f - t);
			float scale = 1.0f - t;

			float pillarW = basePillarW * scale;
			float pillarH = basePillarH * scale;

			float gap = panelWidth * 0.015f * scale;
			float leftX = panelWidth / 2 - roadHalfWidth - gap - pillarW / 2;
			float rightX = panelWidth / 2 + roadHalfWidth + gap - pillarW / 2;

			renderer.drawImage(pillarTexId, leftX, y - pillarH / 2, pillarW, pillarH);
			renderer.drawImage(pillarTexId, rightX, y - pillarH / 2, pillarW, pillarH);
		}
	}

	private void renderFog(IRenderer renderer, float panelWidth, float panelHeight) {
		float stripHeight = panelHeight / NUM_STRIPS;
		int fogStartStrip = NUM_STRIPS / 2;

		for (int i = fogStartStrip; i < NUM_STRIPS; i++) {
			float t = (float) i / NUM_STRIPS;
			float stripTop = panelHeight - (i + 1) * stripHeight;
			float alpha = (t - 0.5f) * 0.5f;
			if (alpha > 0) {
				renderer.drawRect(0, stripTop, panelWidth, stripHeight, 1, 1, 1, alpha);
			}
		}
	}

	public boolean isLoaded() {
		return texturesLoaded;
	}

	public void reset() {
		scrollOffset = 0;
	}
}
