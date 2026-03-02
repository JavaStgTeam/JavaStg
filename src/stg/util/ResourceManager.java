package stg.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
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
		this.resourcePath = "";
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
		
		// 尝试从类路径加载（优先）
		try {
			BufferedImage image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("resources/" + filename));
			images.put(filename, image);
			System.out.println("【资源】从类路径加载图片 " + filename);
			return image;
		} catch (IOException e) {
			LogUtil.error("ResourceManager", "无法从类路径加载图片: " + filename, e);
		} catch (IllegalArgumentException e) {
			System.err.println("【资源加载失败】图片文件不存在: resources/" + filename);
		}
		
		// 尝试从文件系统加载（备用）
		File file = new File("resources/" + filename);
		if (file.exists()) {
			try {
				BufferedImage image = ImageIO.read(file);
				images.put(filename, image);
				System.out.println("【资源】从文件系统加载图片: " + filename);
				return image;
			} catch (IOException e) {
				LogUtil.error("ResourceManager", "无法从文件系统加载图片: " + filename, e);
				return null;
			}
		}
		
		return null;
	}
	
	public BufferedImage loadImage(String filename, String subPath) {
		String fullPath = subPath + "/" + filename;
		if (images.containsKey(fullPath)) {
			return images.get(fullPath);
		}
		
		// 尝试从类路径加载（优先）
		try {
			BufferedImage image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("resources/" + fullPath));
			images.put(fullPath, image);
			System.out.println("【资源】从类路径加载图片 " + fullPath);
			return image;
		} catch (IOException e) {
			LogUtil.error("ResourceManager", "无法从类路径加载图片: " + fullPath, e);
		} catch (IllegalArgumentException e) {
			System.err.println("【资源加载失败】图片文件不存在: resources/" + fullPath);
		}
		
		// 尝试从文件系统加载（备用）
		File file = new File("resources/" + fullPath);
		if (file.exists()) {
			try {
				BufferedImage image = ImageIO.read(file);
				images.put(fullPath, image);
				System.out.println("【资源】从文件系统加载图片: " + fullPath);
				return image;
			} catch (IOException e) {
				LogUtil.error("ResourceManager", "无法从文件系统加载图片: " + fullPath, e);
				return null;
			}
		}
		
		return null;
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
	
	public ByteBuffer loadResourceAsBuffer(String path) throws IOException {
		// 尝试从类路径加载（优先）
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("resources/" + path)) {
			if (is != null) {
				byte[] data = is.readAllBytes();
				ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
				buffer.put(data);
				buffer.flip();
				System.out.println("【资源】从类路径加载资源: " + path);
				return buffer;
			}
		}
		
		// 尝试从文件系统加载（备用）
		File file = new File("resources/" + path);
		if (file.exists()) {
			byte[] data = Files.readAllBytes(file.toPath());
			ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
			buffer.put(data);
			buffer.flip();
			System.out.println("【资源】从文件系统加载资源: " + path);
			return buffer;
		}
		
		throw new IOException("Resource not found: resources/" + path);
	}
}

