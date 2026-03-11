package stg.entity.bullet;

/**
 * 子弹精灵图元素信息封装类
 * 用于管理bullet1.png精灵图的元素坐标和大小信息
 */
public class BulletSpriteSheet {
    // 精灵图基本信息
    public static final int SPRITE_SHEET_WIDTH = 256;
    public static final int SPRITE_SHEET_HEIGHT = 256;
    
    // 元素大小常量
    public static final int SIZE_16x16 = 16;
    public static final int SIZE_32x32 = 32;
    
    // 元素类型枚举
    public enum BulletType {
        ARROW,      // 箭头形状子弹
        CIRCLE,     // 圆形子弹
        STAR,       // 星形子弹
        EXPLOSION,  // 爆炸效果
        CIRCLE_STYLE_1, // 圆形子弹样式1
        CIRCLE_STYLE_2, // 圆形子弹样式2
        CIRCLE_STYLE_3, // 圆形子弹样式3
        CIRCLE_STYLE_4, // 圆形子弹样式4
        CIRCLE_STYLE_5, // 圆形子弹样式5
        CIRCLE_STYLE_6, // 圆形子弹样式6
        CIRCLE_STYLE_7, // 圆形子弹样式7
        CIRCLE_STYLE_8, // 圆形子弹样式8
        CIRCLE_STYLE_9, // 圆形子弹样式9
        CIRCLE_STYLE_10, // 圆形子弹样式10
        CIRCLE_STYLE_11  // 圆形子弹样式11
    }
    
    /**
     * 子弹元素类
     * 封装单个子弹元素的坐标和大小信息
     */
    public static class BulletElement {
        private final int x;          // X坐标
        private final int y;          // Y坐标
        private final int width;      // 宽度
        private final int height;     // 高度
        private final BulletType type; // 子弹类型
        
        /**
         * 构造函数
         * @param x X坐标
         * @param y Y坐标
         * @param width 宽度
         * @param height 高度
         * @param type 子弹类型
         */
        public BulletElement(int x, int y, int width, int height, BulletType type) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.type = type;
        }
        
        /**
         * 获取X坐标
         * @return X坐标
         */
        public int getX() {
            return x;
        }
        
        /**
         * 获取Y坐标
         * @return Y坐标
         */
        public int getY() {
            return y;
        }
        
        /**
         * 获取宽度
         * @return 宽度
         */
        public int getWidth() {
            return width;
        }
        
        /**
         * 获取高度
         * @return 高度
         */
        public int getHeight() {
            return height;
        }
        
        /**
         * 获取子弹类型
         * @return 子弹类型
         */
        public BulletType getType() {
            return type;
        }
    }
    
    // 第1列：箭头形状子弹（16x16）
    public static final BulletElement[] ARROW_BULLETS = new BulletElement[] {
        new BulletElement(0, 0, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 16, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 32, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 48, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 64, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 80, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 96, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 112, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 128, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 144, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 160, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 176, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 192, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 208, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 224, SIZE_16x16, SIZE_16x16, BulletType.ARROW),
        new BulletElement(0, 240, SIZE_16x16, SIZE_16x16, BulletType.ARROW)
    };
    
    // 第2列：圆形子弹（16x16）
    public static final BulletElement[] CIRCLE_BULLETS = new BulletElement[] {
        new BulletElement(16, 0, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 16, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 32, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 48, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 64, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 80, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 96, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 112, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 128, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 144, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 160, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 176, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 192, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 208, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 224, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE),
        new BulletElement(16, 240, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE)
    };
    
    // 第3列：星形子弹（16x16）
    public static final BulletElement[] STAR_BULLETS = new BulletElement[] {
        new BulletElement(32, 0, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 16, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 32, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 48, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 64, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 80, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 96, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 112, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 128, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 144, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 160, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 176, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 192, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 208, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 224, SIZE_16x16, SIZE_16x16, BulletType.STAR),
        new BulletElement(32, 240, SIZE_16x16, SIZE_16x16, BulletType.STAR)
    };
    
    // 第4列：爆炸效果（32x32）
    public static final BulletElement[] EXPLOSION_EFFECTS = new BulletElement[] {
        new BulletElement(48, 0, SIZE_32x32, SIZE_32x32, BulletType.EXPLOSION),
        new BulletElement(48, 32, SIZE_32x32, SIZE_32x32, BulletType.EXPLOSION),
        new BulletElement(48, 64, SIZE_32x32, SIZE_32x32, BulletType.EXPLOSION),
        new BulletElement(48, 96, SIZE_32x32, SIZE_32x32, BulletType.EXPLOSION),
        new BulletElement(48, 128, SIZE_32x32, SIZE_32x32, BulletType.EXPLOSION),
        new BulletElement(48, 160, SIZE_32x32, SIZE_32x32, BulletType.EXPLOSION),
        new BulletElement(48, 192, SIZE_32x32, SIZE_32x32, BulletType.EXPLOSION),
        new BulletElement(48, 224, SIZE_32x32, SIZE_32x32, BulletType.EXPLOSION)
    };
    
    // 第5列：圆形子弹（样式1，16x16）
    public static final BulletElement[] CIRCLE_STYLE_1_BULLETS = new BulletElement[] {
        new BulletElement(80, 0, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 16, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 32, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 48, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 64, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 80, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 96, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 112, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 128, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 144, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 160, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 176, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 192, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 208, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 224, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1),
        new BulletElement(80, 240, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_1)
    };
    
    // 第6列：圆形子弹（样式2，16x16）
    public static final BulletElement[] CIRCLE_STYLE_2_BULLETS = new BulletElement[] {
        new BulletElement(96, 0, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 16, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 32, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 48, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 64, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 80, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 96, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 112, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 128, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 144, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 160, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 176, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 192, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 208, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 224, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2),
        new BulletElement(96, 240, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_2)
    };
    
    // 第7列：圆形子弹（样式3，16x16）
    public static final BulletElement[] CIRCLE_STYLE_3_BULLETS = new BulletElement[] {
        new BulletElement(112, 0, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 16, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 32, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 48, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 64, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 80, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 96, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 112, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 128, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 144, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 160, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 176, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 192, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 208, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 224, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3),
        new BulletElement(112, 240, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_3)
    };
    
    // 第8列：圆形子弹（样式4，16x16）
    public static final BulletElement[] CIRCLE_STYLE_4_BULLETS = new BulletElement[] {
        new BulletElement(128, 0, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 16, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 32, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 48, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 64, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 80, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 96, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 112, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 128, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 144, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 160, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 176, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 192, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 208, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 224, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4),
        new BulletElement(128, 240, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_4)
    };
    
    // 第9列：圆形子弹（样式5，16x16）
    public static final BulletElement[] CIRCLE_STYLE_5_BULLETS = new BulletElement[] {
        new BulletElement(144, 0, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 16, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 32, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 48, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 64, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 80, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 96, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 112, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 128, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 144, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 160, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 176, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 192, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 208, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 224, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5),
        new BulletElement(144, 240, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_5)
    };
    
    // 第10列：圆形子弹（样式6，16x16）
    public static final BulletElement[] CIRCLE_STYLE_6_BULLETS = new BulletElement[] {
        new BulletElement(160, 0, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 16, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 32, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 48, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 64, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 80, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 96, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 112, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 128, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 144, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 160, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 176, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 192, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 208, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 224, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6),
        new BulletElement(160, 240, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_6)
    };
    
    // 第11列：圆形子弹（样式7，16x16）
    public static final BulletElement[] CIRCLE_STYLE_7_BULLETS = new BulletElement[] {
        new BulletElement(176, 0, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 16, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 32, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 48, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 64, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 80, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 96, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 112, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 128, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 144, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 160, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 176, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 192, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 208, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 224, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7),
        new BulletElement(176, 240, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_7)
    };
    
    // 第12列：圆形子弹（样式8，16x16）
    public static final BulletElement[] CIRCLE_STYLE_8_BULLETS = new BulletElement[] {
        new BulletElement(192, 0, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 16, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 32, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 48, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 64, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 80, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 96, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 112, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 128, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 144, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 160, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 176, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 192, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 208, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 224, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8),
        new BulletElement(192, 240, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_8)
    };
    
    // 第13列：圆形子弹（样式9，16x16）
    public static final BulletElement[] CIRCLE_STYLE_9_BULLETS = new BulletElement[] {
        new BulletElement(208, 0, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 16, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 32, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 48, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 64, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 80, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 96, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 112, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 128, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 144, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 160, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 176, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 192, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 208, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 224, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9),
        new BulletElement(208, 240, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_9)
    };
    
    // 第14列：圆形子弹（样式10，16x16）
    public static final BulletElement[] CIRCLE_STYLE_10_BULLETS = new BulletElement[] {
        new BulletElement(224, 0, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 16, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 32, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 48, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 64, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 80, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 96, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 112, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 128, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 144, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 160, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 176, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 192, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 208, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 224, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10),
        new BulletElement(224, 240, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_10)
    };
    
    // 第15列：圆形子弹（样式11，16x16）
    public static final BulletElement[] CIRCLE_STYLE_11_BULLETS = new BulletElement[] {
        new BulletElement(240, 0, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 16, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 32, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 48, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 64, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 80, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 96, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 112, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 128, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 144, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 160, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 176, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 192, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 208, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 224, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11),
        new BulletElement(240, 240, SIZE_16x16, SIZE_16x16, BulletType.CIRCLE_STYLE_11)
    };
    
    /**
     * 获取所有子弹元素数组
     * @return 包含所有子弹元素的二维数组
     */
    public static BulletElement[][] getAllBulletElements() {
        return new BulletElement[][] {
            ARROW_BULLETS,
            CIRCLE_BULLETS,
            STAR_BULLETS,
            EXPLOSION_EFFECTS,
            CIRCLE_STYLE_1_BULLETS,
            CIRCLE_STYLE_2_BULLETS,
            CIRCLE_STYLE_3_BULLETS,
            CIRCLE_STYLE_4_BULLETS,
            CIRCLE_STYLE_5_BULLETS,
            CIRCLE_STYLE_6_BULLETS,
            CIRCLE_STYLE_7_BULLETS,
            CIRCLE_STYLE_8_BULLETS,
            CIRCLE_STYLE_9_BULLETS,
            CIRCLE_STYLE_10_BULLETS,
            CIRCLE_STYLE_11_BULLETS
        };
    }
    
    /**
     * 根据类型获取子弹元素数组
     * @param type 子弹类型
     * @return 对应类型的子弹元素数组
     */
    public static BulletElement[] getBulletElementsByType(BulletType type) {
        switch (type) {
            case ARROW:
                return ARROW_BULLETS;
            case CIRCLE:
                return CIRCLE_BULLETS;
            case STAR:
                return STAR_BULLETS;
            case EXPLOSION:
                return EXPLOSION_EFFECTS;
            case CIRCLE_STYLE_1:
                return CIRCLE_STYLE_1_BULLETS;
            case CIRCLE_STYLE_2:
                return CIRCLE_STYLE_2_BULLETS;
            case CIRCLE_STYLE_3:
                return CIRCLE_STYLE_3_BULLETS;
            case CIRCLE_STYLE_4:
                return CIRCLE_STYLE_4_BULLETS;
            case CIRCLE_STYLE_5:
                return CIRCLE_STYLE_5_BULLETS;
            case CIRCLE_STYLE_6:
                return CIRCLE_STYLE_6_BULLETS;
            case CIRCLE_STYLE_7:
                return CIRCLE_STYLE_7_BULLETS;
            case CIRCLE_STYLE_8:
                return CIRCLE_STYLE_8_BULLETS;
            case CIRCLE_STYLE_9:
                return CIRCLE_STYLE_9_BULLETS;
            case CIRCLE_STYLE_10:
                return CIRCLE_STYLE_10_BULLETS;
            case CIRCLE_STYLE_11:
                return CIRCLE_STYLE_11_BULLETS;
            default:
                return new BulletElement[0];
        }
    }
    
    /**
     * 获取精灵图宽度
     * @return 精灵图宽度
     */
    public static int getSpriteSheetWidth() {
        return SPRITE_SHEET_WIDTH;
    }
    
    /**
     * 获取精灵图高度
     * @return 精灵图高度
     */
    public static int getSpriteSheetHeight() {
        return SPRITE_SHEET_HEIGHT;
    }
}
