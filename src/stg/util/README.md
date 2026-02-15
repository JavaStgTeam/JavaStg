# stg.util 包

## 功能描述
stg.util 包是 JavaStg 游戏引擎的工具系统包，提供了游戏开发中常用的各种工具类，包括音频管理、坐标转换、事件总线、渲染工具、资源管理和数学工具等。这些工具类为游戏引擎的其他部分提供了基础支持，使得游戏开发更加高效和便捷。

## 包含文件
1. **AudioManager.java**：音频管理类，负责加载和播放游戏音频（背景音乐、音效等）。
2. **CoordinateSystem.java**：坐标系统工具类，提供坐标转换功能，将屏幕坐标系转换为画布中心原点坐标系。
3. **EventBus.java**：事件总线，用于游戏对象间的通信，支持事件的发布和订阅。
4. **RenderUtils.java**：渲染工具类，提供统一的渲染设置和优化方法。
5. **ResourceManager.java**：资源管理类，负责加载和管理游戏资源（图片等）。
6. **math/**：数学工具包，包含以下文件：
   - **MathUtils.java**：数学工具类，提供常用的数学计算方法。
   - **RandomGenerator.java**：随机数生成器，提供随机数的生成方法。
   - **Vector2.java**：二维向量类，提供向量的基本运算方法。

## 核心功能
1. **音频管理**：实现游戏音频的加载、播放、暂停、恢复和停止等功能，支持背景音乐和音效的管理，支持音量调节。
2. **坐标系统**：实现屏幕坐标系和画布中心原点坐标系之间的转换，提供画布边界的计算方法。
3. **事件总线**：实现游戏对象间的通信，支持事件的发布和订阅，使用泛型和函数式接口提供类型安全的事件处理。
4. **渲染工具**：提供统一的渲染设置和优化方法，支持抗锯齿和高质量渲染设置。
5. **资源管理**：实现游戏资源的加载和管理，支持从文件系统和类路径加载资源，支持资源的缓存和卸载。
6. **数学工具**：提供常用的数学计算方法、随机数生成方法和二维向量的基本运算方法。

## 设计理念
stg.util 包采用了工具类设计模式，通过静态方法和单例模式提供功能：
- **单例模式**：对于需要全局访问的工具类（如 `AudioManager`、`EventBus`、`ResourceManager`），使用单例模式确保全局只有一个实例。
- **静态方法**：对于不需要维护状态的工具类（如 `RenderUtils`、`MathUtils`），使用静态方法提供功能。
- **泛型和函数式接口**：使用泛型和函数式接口（如 `Consumer`）提供类型安全和简洁的 API。
- **资源缓存**：使用哈希表缓存已加载的资源，避免重复加载，提高性能。

这种设计使得工具类具有良好的可访问性和可扩展性，可以方便地在游戏引擎的各个部分使用。

## 依赖关系
- **AudioManager**：依赖 `javax.sound.sampled` 包，用于音频处理。
- **CoordinateSystem**：无外部依赖。
- **EventBus**：依赖 `java.util.concurrent.ConcurrentHashMap`，用于线程安全的事件订阅管理。
- **RenderUtils**：依赖 `java.awt.Graphics2D` 和 `java.awt.RenderingHints`，用于渲染设置。
- **ResourceManager**：依赖 `java.awt.image.BufferedImage` 和 `javax.imageio.ImageIO`，用于图片加载。
- **MathUtils**：无外部依赖。
- **RandomGenerator**：无外部依赖。
- **Vector2**：无外部依赖。

## 使用示例

### 使用音频管理器
```java
// 获取音频管理器实例
AudioManager audioManager = AudioManager.getInstance();

// 设置音频音量
audioManager.setMusicVolume(0.7f);
audioManager.setSoundVolume(1.0f);

// 播放背景音乐
audioManager.playMusic("bgm.wav", true); // true 表示循环播放

// 暂停背景音乐
audioManager.pauseMusic();

// 恢复背景音乐
audioManager.resumeMusic();

// 停止背景音乐
audioManager.stopMusic();

// 播放音效
audioManager.playSoundEffect("shot.wav");

// 清理音频资源
audioManager.clearAll();
```

### 使用坐标系统
```java
// 创建坐标系统
CoordinateSystem coordinateSystem = new CoordinateSystem(800, 600);

// 更新画布尺寸
coordinateSystem.updateCanvasSize(1024, 768);

// 转换坐标
float[] screenCoords = coordinateSystem.toScreenCoords(100, 50); // 中心原点坐标转屏幕坐标
float[] centerCoords = coordinateSystem.toCenterCoords(512, 384); // 屏幕坐标转中心原点坐标

// 获取画布边界
float leftBound = coordinateSystem.getLeftBound();
float rightBound = coordinateSystem.getRightBound();
float topBound = coordinateSystem.getTopBound();
float bottomBound = coordinateSystem.getBottomBound();

// 获取画布中心
float centerX = coordinateSystem.getCenterX();
float centerY = coordinateSystem.getCenterY();
```

### 使用事件总线
```java
// 获取事件总线实例
EventBus eventBus = EventBus.getInstance();

// 定义事件类
class PlayerMovedEvent {
    private final float x;
    private final float y;
    
    public PlayerMovedEvent(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
}

// 创建订阅者对象
Object subscriber = new Object();

// 订阅事件
eventBus.subscribe(subscriber, PlayerMovedEvent.class, event -> {
    System.out.println("Player moved to: (" + event.getX() + ", " + event.getY() + ")");
});

// 发布事件
eventBus.publish(new PlayerMovedEvent(100, 50));

// 取消特定订阅
eventBus.unsubscribe(subscriber, PlayerMovedEvent.class, event -> {
    System.out.println("Player moved to: (" + event.getX() + ", " + event.getY() + ")");
});

// 取消订阅者的所有订阅
eventBus.unsubscribeAll(subscriber);

// 清理所有订阅
eventBus.clear();
```

### 使用渲染工具
```java
// 在绘制方法中使用
@Override
public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    
    // 启用抗锯齿
    RenderUtils.enableAntiAliasing(g2d);
    
    // 绘制图形
    g2d.setColor(Color.RED);
    g2d.fillOval(100, 100, 50, 50);
    
    // 启用高质量渲染
    RenderUtils.enableHighQualityRendering(g2d);
    
    // 绘制文本
    g2d.setColor(Color.WHITE);
    g2d.setFont(new Font("Arial", Font.BOLD, 24));
    g2d.drawString("Hello World", 200, 200);
    
    // 重置渲染设置
    RenderUtils.resetRenderingHints(g2d);
}
```

### 使用资源管理器
```java
// 获取资源管理器实例
ResourceManager resourceManager = ResourceManager.getInstance();

// 设置资源路径
resourceManager.setResourcePath("resources/");

// 加载图片
BufferedImage playerImage = resourceManager.loadImage("player.png");
BufferedImage enemyImage = resourceManager.loadImage("enemy.png", "images");

// 检查图片是否已加载
boolean hasPlayerImage = resourceManager.hasImage("player.png");

// 获取已加载的图片
BufferedImage loadedImage = resourceManager.getImage("player.png");

// 卸载图片
resourceManager.unloadImage("player.png");

// 清理所有图片
resourceManager.clearImages();
```

### 使用数学工具
```java
// 使用 MathUtils
float angle = MathUtils.degToRad(45); // 角度转弧度
float distance = MathUtils.distance(0, 0, 100, 100); // 计算两点间距离
float clampedValue = MathUtils.clamp(5, 0, 10); // 限制值在范围内

// 使用 RandomGenerator
RandomGenerator random = RandomGenerator.getInstance();
int randomInt = random.nextInt(100); // 生成0-99的随机整数
float randomFloat = random.nextFloat(0, 1); // 生成0-1的随机浮点数
boolean randomBool = random.nextBoolean(); // 生成随机布尔值

// 使用 Vector2
Vector2 position = new Vector2(100, 50);
Vector2 velocity = new Vector2(1, 0);
Vector2 newPosition = position.add(velocity.multiply(10)); // 位置更新
float length = velocity.length(); // 计算向量长度
Vector2 normalized = velocity.normalize(); // 归一化向量
```

## 扩展建议
1. **添加更多音频格式支持**：可以扩展 `AudioManager` 类，支持更多音频格式，如 MP3、OGG 等。
2. **添加资源热重载**：可以扩展 `ResourceManager` 类，支持资源的热重载，当资源文件变化时自动重新加载。
3. **添加更多数学工具**：可以扩展 `math` 包，添加更多数学工具类，如矩阵运算、四元数等。
4. **添加文件系统工具**：可以添加文件系统工具类，用于文件的读写、目录的遍历等。
5. **添加时间工具**：可以添加时间工具类，用于游戏时间的管理、帧率的计算等。
6. **添加网络工具**：可以添加网络工具类，用于游戏的网络通信、多人游戏等。
7. **添加配置管理工具**：可以添加配置管理工具类，用于游戏配置的加载、保存和管理。

## 关键方法

### AudioManager 类
- `getInstance()`：获取音频管理器实例。
- `setResourcePath(String path)`：设置资源路径。
- `setMusicVolume(float volume)`：设置背景音乐音量。
- `setSoundVolume(float volume)`：设置音效音量。
- `loadMusic(String filename)`：加载背景音乐。
- `loadSoundEffect(String filename)`：加载音效。
- `playMusic(String filename, boolean loop)`：播放背景音乐。
- `stopMusic()`：停止背景音乐。
- `pauseMusic()`：暂停背景音乐。
- `resumeMusic()`：恢复背景音乐。
- `playSoundEffect(String filename)`：播放音效。
- `unloadMusic(String filename)`：卸载背景音乐。
- `unloadSoundEffect(String filename)`：卸载音效。
- `clearMusic()`：清理所有背景音乐。
- `clearSoundEffects()`：清理所有音效。
- `clearAll()`：清理所有音频资源。
- `isMusicPlaying()`：检查背景音乐是否正在播放。
- `isMusicLoaded(String filename)`：检查背景音乐是否已加载。
- `isSoundEffectLoaded(String filename)`：检查音效是否已加载。

### CoordinateSystem 类
- `CoordinateSystem(int canvasWidth, int canvasHeight)`：构造函数，创建坐标系统对象。
- `updateCanvasSize(int width, int height)`：更新画布尺寸。
- `toScreenCoords(float x, float y)`：将画布中心原点坐标转换为屏幕左上角原点坐标。
- `toCenterCoords(float screenX, float screenY)`：将屏幕左上角原点坐标转换为画布中心原点坐标。
- `getCenterX()`：获取画布中心X坐标。
- `getCenterY()`：获取画布中心Y坐标。
- `getCanvasWidth()`：获取画布宽度。
- `getCanvasHeight()`：获取画布高度。
- `getLeftBound()`：获取画布左边界(中心原点坐标系)。
- `getRightBound()`：获取画布右边界(中心原点坐标系)。
- `getTopBound()`：获取画布上边界(中心原点坐标系)。
- `getBottomBound()`：获取画布下边界(中心原点坐标系)。

### EventBus 类
- `getInstance()`：获取事件总线实例。
- `subscribe(Object subscriber, Class<T> eventType, Consumer<T> handler)`：订阅事件。
- `publish(T event)`：发布事件。
- `unsubscribe(Object subscriber, Class<T> eventType, Consumer<T> handler)`：取消订阅。
- `unsubscribeAll(Object subscriber)`：取消订阅者的所有订阅。
- `clear()`：清理所有订阅。

### RenderUtils 类
- `enableAntiAliasing(Graphics2D g)`：启用抗锯齿渲染。
- `enableHighQualityRendering(Graphics2D g)`：启用高质量渲染设置。
- `resetRenderingHints(Graphics2D g)`：重置渲染设置为默认值。

### ResourceManager 类
- `getInstance()`：获取资源管理器实例。
- `setResourcePath(String path)`：设置资源路径。
- `loadImage(String filename)`：加载图片。
- `loadImage(String filename, String subPath)`：加载指定子路径下的图片。
- `unloadImage(String filename)`：卸载图片。
- `clearImages()`：清理所有图片。
- `getImage(String filename)`：获取已加载的图片。
- `hasImage(String filename)`：检查图片是否已加载。

### MathUtils 类
- `degToRad(float degrees)`：角度转弧度。
- `radToDeg(float radians)`：弧度转角度。
- `distance(float x1, float y1, float x2, float y2)`：计算两点间距离。
- `clamp(float value, float min, float max)`：限制值在范围内。
- `lerp(float start, float end, float t)`：线性插值。

### RandomGenerator 类
- `getInstance()`：获取随机数生成器实例。
- `nextInt(int bound)`：生成0到bound-1的随机整数。
- `nextInt(int min, int max)`：生成min到max的随机整数。
- `nextFloat()`：生成0-1的随机浮点数。
- `nextFloat(float min, float max)`：生成min到max的随机浮点数。
- `nextBoolean()`：生成随机布尔值。

### Vector2 类
- `Vector2(float x, float y)`：构造函数，创建二维向量对象。
- `add(Vector2 other)`：向量加法。
- `subtract(Vector2 other)`：向量减法。
- `multiply(float scalar)`：向量乘法（标量）。
- `divide(float scalar)`：向量除法（标量）。
- `length()`：计算向量长度。
- `lengthSquared()`：计算向量长度的平方。
- `normalize()`：归一化向量。
- `dot(Vector2 other)`：计算向量点积。
- `angle()`：计算向量角度。
- `set(float x, float y)`：设置向量坐标。
- `getX()`：获取X坐标。
- `getY()`：获取Y坐标。