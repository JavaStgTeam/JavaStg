# base 包说明

## 功能概述

**base 包**是游戏的基础组件包，提供窗口管理、键盘输入等基础功能。这些组件是游戏运行的基础，被其他模块广泛使用。

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| Window | 游戏窗口管理类，负责创建和管理游戏主窗口 |
| VirtualKeyboardPanel | 虚拟键盘面板，显示按键状态 |
| KeyStateProvider | 键盘状态提供者接口，为其他组件提供键盘输入状态 |

## 主要功能

### Window 类
- 创建和管理游戏主窗口
- 处理窗口事件和大小调整
- 提供对游戏画布的访问
- 管理窗口的显示和隐藏

### VirtualKeyboardPanel 类
- 显示键盘按键状态
- 支持自定义按键布局
- 与 KeyStateProvider 接口配合使用
- 可在标题界面和游戏界面中使用

### KeyStateProvider 接口
- 定义键盘状态获取方法
- 为其他组件提供统一的键盘输入接口
- 支持不同的键盘输入实现

## 使用示例

### 创建游戏窗口

```java
// 创建窗口（参数为是否启用调试模式）
Window window = new Window(false);

// 获取游戏画布
GameCanvas gameCanvas = window.getGameCanvas();

// 显示窗口
window.setVisible(true);
```

### 使用虚拟键盘面板

```java
// 创建虚拟键盘面板
VirtualKeyboardPanel keyboardPanel = new VirtualKeyboardPanel(gameCanvas);

// 添加到界面
frame.add(keyboardPanel);
```

## 设计说明

1. **职责单一**：每个类只负责特定的基础功能
2. **接口分离**：通过接口定义功能契约，便于扩展
3. **依赖最小化**：尽量减少对其他包的依赖
4. **可测试性**：设计时考虑测试的便利性

## 开发建议

- 当需要修改窗口行为时，修改 Window 类
- 当需要扩展键盘输入功能时，实现 KeyStateProvider 接口
- 当需要自定义虚拟键盘显示时，修改 VirtualKeyboardPanel 类
- 保持基础组件的稳定性，避免频繁修改