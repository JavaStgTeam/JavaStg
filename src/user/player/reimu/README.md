# Reimu Player 自机实现

## 概述
使用reimu.png精灵表实现的自机角色，包含站立、左移、右移动画。

## 文件结构
- `reimu.png` - Reimu精灵表图片
- `reimu_sprite_documentation.md` - 精灵表文档，包含每个元素的位置和大小
- `__ReimuPlayer.java` - Reimu自机实现类
- `__ReimuPlayerTest.java` - 测试类

## 精灵表布局

### 第一行：站立帧（8帧）
- 位置：(0, 0) 到 (336, 0)
- 大小：48x48像素/帧

### 第二行：左移帧（8帧）
- 位置：(0, 48) 到 (336, 48)
- 大小：48x48像素/帧

### 第三行：右移帧（8帧）
- 位置：(0, 96) 到 (336, 96)
- 大小：48x48像素/帧

### 下方：子弹和特效
- 位置：(0, 144) 及以下
- 大小：32x32像素（子弹），64x32像素（特效）

## 使用方法

### 1. 加载Reimu自机
```java
// 创建Reimu自机实例
__ReimuPlayer reimu = new __ReimuPlayer(0, 0);

// 加载纹理（在GamePanel中）
GLRenderer renderer = new GLRenderer();
int textureId = renderer.loadTexture("src/user/player/reimu/reimu.png");
reimu.setReimuTextureId(textureId);

// 添加到游戏世界
gameWorld.addObject(reimu);
```

### 2. 控制方法
- 方向键：移动
- Shift键：低速模式
- Z键：射击

## 动画系统
- 站立动画：8帧循环
- 左移动画：8帧循环
- 右移动画：8帧循环
- 动画速度：每5帧切换一次

## 渲染特点
- 支持无敌闪烁效果
- 低速模式显示受击判定点
- 纹理加载失败时使用红色圆形作为 fallback

## 测试
运行 `__ReimuPlayerTest.java` 测试自机的创建、纹理加载和动画更新。

## 注意事项
- 确保reimu.png文件存在于正确的路径
- 纹理加载需要OpenGL环境
- 在实际游戏中，纹理加载应在GamePanel的初始化阶段完成
