package user.player;

import java.awt.*;
import java.awt.image.BufferedImage;
import stg.game.player.Player;
import stg.game.ui.GameCanvas;
import stg.util.ResourceManager;

/**
 * 带图片资源的玩家示例 - 演示如何使用ResourceManager加载和显示图片
 * @since 2026-01-24
 */
public class PlayerWithImage extends Player {
	private BufferedImage playerImage;
	private final ResourceManager resourceManager;
	
	public PlayerWithImage(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		this.resourceManager = ResourceManager.getInstance();
		loadPlayerImage();
	}
	
	private void loadPlayerImage() {
		playerImage = resourceManager.loadImage("player.png");
		
		if (playerImage == null) {
			System.out.println("【警告】玩家图片加载失败，使用默认绘制");
		} else {
			System.out.println("【资源】玩家图片加载成功: " + 
				playerImage.getWidth() + "x" + playerImage.getHeight());
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		if (playerImage != null) {
			GameCanvas gameCanvas = getGameCanvas();
			if (gameCanvas == null) return;
			
			int width = gameCanvas.getWidth();
			int height = gameCanvas.getHeight();
			float screenX = getX() + width / 2.0f;
			float screenY = height / 2.0f - getY();
			
			int drawX = (int)(screenX - playerImage.getWidth() / 2.0f);
			int drawY = (int)(screenY - playerImage.getHeight() / 2.0f);
			
			g.drawImage(playerImage, drawX, drawY, null);
		} else {
			super.render(g);
		}
	}
	
	public BufferedImage getPlayerImage() {
		return playerImage;
	}
	
	public void setPlayerImage(BufferedImage image) {
		this.playerImage = image;
	}
	
	public void reloadPlayerImage(String filename) {
		playerImage = resourceManager.loadImage(filename);
	}
}
