package stg.entity.laser;

/**
 * 激光颜色枚举 - 对应 laser1.png 中的9种激光颜色
 * @since 2026-04-11
 */
public enum LaserColor {
    RED(0, 0.0f, 1.0f/9.0f, "红色"),
    PURPLE(1, 1.0f/9.0f, 2.0f/9.0f, "紫色"),
    DARK_BLUE(2, 2.0f/9.0f, 3.0f/9.0f, "深蓝色"),
    BLUE(3, 3.0f/9.0f, 4.0f/9.0f, "蓝色"),
    CYAN(4, 4.0f/9.0f, 5.0f/9.0f, "青色"),
    GREEN(5, 5.0f/9.0f, 6.0f/9.0f, "绿色"),
    YELLOW(6, 6.0f/9.0f, 7.0f/9.0f, "黄色"),
    ORANGE(7, 7.0f/9.0f, 8.0f/9.0f, "橙色"),
    GRAY(8, 8.0f/9.0f, 1.0f, "灰色/黑色");
    
    private final int index;
    private final float textureYStart;
    private final float textureYEnd;
    private final String name;
    
    LaserColor(int index, float textureYStart, float textureYEnd, String name) {
        this.index = index;
        this.textureYStart = textureYStart;
        this.textureYEnd = textureYEnd;
        this.name = name;
    }
    
    public int getIndex() {
        return index;
    }
    
    public float getTextureYStart() {
        return textureYStart;
    }
    
    public float getTextureYEnd() {
        return textureYEnd;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * 根据索引获取激光颜色
     * @param index 索引
     * @return 激光颜色枚举
     */
    public static LaserColor getByIndex(int index) {
        for (LaserColor color : values()) {
            if (color.index == index) {
                return color;
            }
        }
        return RED; // 默认返回红色
    }
}