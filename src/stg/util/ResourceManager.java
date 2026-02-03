package stg.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * 资源管理类 - 负责加载和管理游戏资源（图片等）
 * @since 2026-01-24
 */
public class ResourceManager {
	private static final ResourceManager instance = new ResourceManager();
	private final Map<String, BufferedImage> images = new HashMap<>();
	private String resourcePath;
	
	private ResourceManager() {
		this.resourcePath = "resources/";
	}
	
	public static ResourceManager getInstance() {
		return instance;
	}
	
	public void setResourcePath(String path) {
		this.resourcePath = path;
	}
	
	public BufferedImage loadImage(String filename) {
		if (images.containsKey(filename)) {
			return images.get(filename);
		}
		
		// 尝试从文件系统加载
		File file = new File(resourcePath + filename);
		if (file.exists()) {
			try {
				BufferedImage image = ImageIO.read(file);
				images.put(filename, image);
				System.out.println("【资源】从文件系统加载图片: " + filename);
				return image;
			} catch (IOException e) {
				System.err.println("【资源加载失败】无法加载图片 " + filename);
				e.printStackTrace();
				return null;
			}
		}
		
		// 尝试从类路径加载
		try {
			BufferedImage image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(resourcePath + filename));
			images.put(filename, image);
			System.out.println("【资源】从类路径加载图片 " + filename);
			return image;
		} catch (IOException e) {
			System.err.println("【资源加载失败】无法加载图片 " + filename);
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			System.err.println("【资源加载失败】图片文件不存在: " + resourcePath + filename);
			return null;
		}
	}
	
	public BufferedImage loadImage(String filename, String subPath) {
		String fullPath = subPath + "/" + filename;
		if (images.containsKey(fullPath)) {
			return images.get(fullPath);
		}
		
		// 尝试从文件系统加载
		File file = new File(resourcePath + fullPath);
		if (file.exists()) {
			try {
				BufferedImage image = ImageIO.read(file);
				images.put(fullPath, image);
				System.out.println("【资源】从文件系统加载图片: " + fullPath);
				return image;
			} catch (IOException e) {
				System.err.println("【资源加载失败】无法加载图片 " + fullPath);
				e.printStackTrace();
				return null;
			}
		}
		
		// 尝试从类路径加载
		try {
			BufferedImage image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(resourcePath + fullPath));
			images.put(fullPath, image);
			System.out.println("【资源】从类路径加载图片 " + fullPath);
			return image;
		} catch (IOException e) {
			System.err.println("【资源加载失败】无法加载图片 " + fullPath);
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			System.err.println("【资源加载失败】图片文件不存在: " + resourcePath + fullPath);
			return null;
		}
	}
	
	public void unloadImage(String filename) {
		images.remove(filename);
	}
	
	public void clearImages() {
		images.clear();
	}
	
	public BufferedImage getImage(String filename) {
		return images.get(filename);
	}
	
	public boolean hasImage(String filename) {
		return images.containsKey(filename);
	}
}

