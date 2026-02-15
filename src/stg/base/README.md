# stg.base 包

## 功能描述
stg.base 包是 JavaStg 游戏引擎的基础组件包，提供了窗口管理、键盘输入处理等基础功能。这些组件是游戏引擎的底层支撑，为上层游戏逻辑提供了必要的运行环境。

## 包含文件
1. **Window.java**：游戏主窗口类，负责创建和管理游戏窗口，包含游戏画布、虚拟键盘面板和游戏状态面板。
2. **VirtualKeyboardPanel.java**：虚拟键盘面板，显示玩家按键状态，支持不同界面的按键状态切换。
3. **KeyStateProvider.java**：按键状态提供者接口，定义了获取按键状态的方法，支持标题界面的虚拟键盘显示。

## 主要功能
1. **窗口管理**：通过 `Window` 类创建和管理游戏主窗口，包含三个面板（左侧操作说明、中间游戏画布、右侧游戏状态）。
2. **虚拟键盘**：通过 `VirtualKeyboardPanel` 类显示玩家按键状态，提供直观的按键反馈。
3. **按键状态接口**：通过 `KeyStateProvider` 接口统一按键状态的获取方式，支持不同界面的按键状态切换。
4. **玩家初始化**：通过 `Window` 类的 `initializePlayer` 方法初始化玩家并设置初始位置。

## 设计理念
stg.base 包采用了模块化设计，将窗口管理和键盘输入处理分离为不同的组件，便于维护和扩展。同时，通过接口定义（如 `KeyStateProvider`）实现了组件间的解耦，提高了代码的可测试性和可扩展性。

## 依赖关系
- 依赖 Java 标准库中的 AWT 和 Swing 包用于界面渲染
- 依赖 `stg.game` 包中的类（如 `GameLoop`、`GameCanvas` 等）
- 依赖 `stg.util` 包中的工具类（如 `RenderUtils`）

## 使用示例
```java
// 创建游戏窗口
Window window = new Window(false);

// 显示标题界面
TitleScreen titleScreen = new TitleScreen(new TitleScreen.TitleCallback() {
    // 实现回调方法
});

// 更新虚拟键盘以显示标题界面的按键状态
window.getVirtualKeyboardPanel().setKeyStateProvider(titleScreen);

// 初始化玩家
window.initializePlayer();

// 获取游戏画布
GameCanvas gameCanvas = window.getGameCanvas();
```

## 关键方法
1. **Window 类**：
   - `Window(boolean initPlayer)`：构造函数，创建游戏窗口
   - `initializePlayer()`：初始化玩家并设置初始位置
   - `getGameCanvas()`：获取游戏画布
   - `getVirtualKeyboardPanel()`：获取虚拟键盘面板
   - `getCenterPanel()`：获取中间面板，用于显示不同界面

2. **VirtualKeyboardPanel 类**：
   - `VirtualKeyboardPanel(KeyStateProvider keyStateProvider)`：构造函数，创建虚拟键盘面板
   - `setKeyStateProvider(KeyStateProvider provider)`：设置按键状态提供者

3. **KeyStateProvider 接口**：
   - `isUpPressed()`：获取上方向键是否按下
   - `isDownPressed()`：获取下方向键是否按下
   - `isLeftPressed()`：获取左方向键是否按下
   - `isRightPressed()`：获取右方向键是否按下
   - `isZPressed()`：获取Z键是否按下
   - `isShiftPressed()`：获取Shift键是否按下
   - `isXPressed()`：获取X键是否按下