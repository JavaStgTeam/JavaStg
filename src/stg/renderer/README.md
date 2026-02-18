# renderer 包说明

## 功能概述

**renderer 包**是游戏的渲染系统包，包含了游戏渲染相关的类。渲染系统负责将游戏世界中的实体绘制到屏幕上，是游戏视觉表现的核心。

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| GameRenderer | 游戏渲染器类，实现了游戏的渲染逻辑 |

## 主要功能

### GameRenderer 类
- **渲染管理**：负责管理游戏的渲染过程
- **实体渲染**：渲染游戏世界中的所有实体
- **分层渲染**：支持不同层级的渲染（如背景、实体、UI等）
- **视觉效果**：支持各种视觉效果，如透明度、旋转等
- **性能优化**：优化渲染性能，确保游戏流畅运行
- **坐标转换**：处理游戏世界坐标到屏幕坐标的转换

## 类结构

```java
public class GameRenderer {
    private Graphics2D g;
    private int screenWidth;
    private int screenHeight;
    private CoordinateSystem coordinateSystem;
    
    public GameRenderer(int screenWidth, int screenHeight) {
        // 初始化...
    }
    
    public void setGraphics(Graphics2D g) {
        this.g = g;
    }
    
    public void render(GameWorld gameWorld) {
        // 渲染游戏世界...
    }
    
    public void renderEntity(Obj entity) {
        // 渲染单个实体...
    }
    
    public void renderBackground() {
        // 渲染背景...
    }
    
    public void renderUI() {
        // 渲染UI...
    }
    
    // 其他方法...
}
```

## 使用示例

### 创建和使用渲染器

```java
// 创建渲染器
GameRenderer renderer = new GameRenderer(
    800,  // 屏幕宽度
    600   // 屏幕高度
);

// 在游戏循环中使用渲染器
public void render(Graphics2D g) {
    // 设置渲染器的图形上下文
    renderer.setGraphics(g);
    
    // 渲染游戏世界
    renderer.render(gameWorld);
}

// 自定义渲染逻辑
public void customRender() {
    // 开始渲染
    renderer.beginRender();
    
    // 渲染背景
    renderer.renderBackground();
    
    // 渲染游戏实体
    for (Obj entity : gameWorld.getObjects()) {
        renderer.renderEntity(entity);
    }
    
    // 渲染UI
    renderer.renderUI();
    
    // 结束渲染
    renderer.endRender();
}
```

### 渲染优化

```java
// 批量渲染相似实体
public void batchRender(List<Bullet> bullets) {
    // 开始批量渲染
    renderer.beginBatch();
    
    // 渲染所有子弹
    for (Bullet bullet : bullets) {
        renderer.renderEntity(bullet);
    }
    
    // 结束批量渲染
    renderer.endBatch();
}

// 使用缓冲图像
public void useBufferedImage() {
    // 创建缓冲图像
    BufferedImage buffer = new BufferedImage(
        screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB
    );
    
    // 获取缓冲图像的图形上下文
    Graphics2D bufferG = buffer.createGraphics();
    
    // 渲染到缓冲图像
    renderer.setGraphics(bufferG);
    renderer.render(gameWorld);
    
    // 将缓冲图像绘制到屏幕
    g.drawImage(buffer, 0, 0, null);
    
    // 释放资源
    bufferG.dispose();
}
```

## 设计说明

1. **单一职责**：GameRenderer 类专注于渲染功能
2. **性能优化**：实现了多种渲染优化技术
3. **可扩展性**：支持自定义渲染逻辑和视觉效果
4. **分层设计**：通过分层渲染实现复杂的视觉效果

## 开发建议

- 当需要修改渲染逻辑时，修改 GameRenderer 类
- 当需要添加新的渲染效果时，在 GameRenderer 类中添加相应的方法
- 考虑渲染性能，避免在渲染过程中进行复杂的计算
- 使用缓冲图像和批量渲染等技术提高渲染效率
- 为不同类型的实体实现合适的渲染方法
- 考虑不同屏幕分辨率和比例的适配