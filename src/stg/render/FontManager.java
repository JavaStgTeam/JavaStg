package stg.render;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 字体管理器
 * 负责加载和管理游戏中的字体
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public class FontManager {
	private static FontManager instance;
	private Font defaultFont;
	private Font titleFont;
	private Font menuFont;
	private Font hintFont;

	private FontManager() {
		loadFonts();
	}

	/**
	 * 获取字体管理器实例
	 * @return 字体管理器实例
	 */
	public static synchronized FontManager getInstance() {
		if (instance == null) {
			instance = new FontManager();
		}
		return instance;
	}

	/**
	 * 加载字体
	 */
	private void loadFonts() {
		try {
			// 尝试不同的路径加载OPPO Sans字体
			String[] fontPaths = {
				"fonts/OPPO Sans 4.0.ttf",
				"resources/fonts/OPPO Sans 4.0.ttf",
				"fonts/OPPO+Sans+4.0.ttf",
				"fonts/OPPO_Sans_4.0.ttf"
			};
			
			InputStream fontStream = null;
			for (String path : fontPaths) {
				fontStream = FontManager.class.getClassLoader().getResourceAsStream(path);
				if (fontStream != null) {
					System.out.println("找到字体文件: " + path);
					break;
				}
			}
			
			// 尝试使用文件系统路径
			if (fontStream == null) {
				File fontFile = new File("resources/fonts/OPPO Sans 4.0.ttf");
				if (fontFile.exists()) {
					System.out.println("使用文件系统路径加载字体: " + fontFile.getAbsolutePath());
					fontStream = new FileInputStream(fontFile);
				}
			}
			
			if (fontStream != null) {
				Font loadedFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
				
				// 注册字体到系统
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(loadedFont);
				
				// 创建不同大小的字体
				defaultFont = loadedFont.deriveFont(Font.PLAIN, 12f);
				titleFont = loadedFont.deriveFont(Font.BOLD, 48f);
				menuFont = loadedFont.deriveFont(Font.BOLD, 24f);
				hintFont = loadedFont.deriveFont(Font.PLAIN, 14f);
				
				fontStream.close();
				System.out.println("字体加载成功: OPPO Sans 4.0");
			} else {
				// 如果字体加载失败，使用系统默认字体
				System.out.println("字体文件未找到，使用系统默认字体");
				// 尝试使用常见的中文字体
				String[] fallbackFonts = {"Microsoft YaHei", "SimHei", "SimSun", "Dialog"};
				String selectedFont = null;
				
				// 检查系统是否支持这些字体
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				String[] availableFonts = ge.getAvailableFontFamilyNames();
				
				for (String font : fallbackFonts) {
					for (String available : availableFonts) {
						if (font.equals(available)) {
							selectedFont = font;
							break;
						}
					}
					if (selectedFont != null) break;
				}
				
				if (selectedFont == null) selectedFont = "Dialog";
				System.out.println("使用备用字体: " + selectedFont);
				
				defaultFont = new Font(selectedFont, Font.PLAIN, 12);
				titleFont = new Font(selectedFont, Font.BOLD, 48);
				menuFont = new Font(selectedFont, Font.BOLD, 24);
				hintFont = new Font(selectedFont, Font.PLAIN, 14);
			}
		} catch (FontFormatException | IOException e) {
			// 如果字体加载失败，使用系统默认字体
			System.err.println("字体加载失败: " + e.getMessage());
			defaultFont = new Font("Microsoft YaHei", Font.PLAIN, 12);
			titleFont = new Font("Microsoft YaHei", Font.BOLD, 48);
			menuFont = new Font("Microsoft YaHei", Font.BOLD, 24);
			hintFont = new Font("Microsoft YaHei", Font.PLAIN, 14);
		}
	}

	/**
	 * 获取默认字体
	 * @return 默认字体
	 */
	public Font getDefaultFont() {
		return defaultFont;
	}

	/**
	 * 获取标题字体
	 * @return 标题字体
	 */
	public Font getTitleFont() {
		return titleFont;
	}

	/**
	 * 获取菜单字体
	 * @return 菜单字体
	 */
	public Font getMenuFont() {
		return menuFont;
	}

	/**
	 * 获取提示字体
	 * @return 提示字体
	 */
	public Font getHintFont() {
		return hintFont;
	}

	/**
	 * 根据大小获取字体
	 * @param size 字体大小
	 * @param style 字体样式
	 * @return 字体
	 */
	public Font getFont(float size, int style) {
		if (defaultFont != null) {
			return defaultFont.deriveFont(style, size);
		} else {
			return new Font("Microsoft YaHei", style, (int) size);
		}
	}
}
