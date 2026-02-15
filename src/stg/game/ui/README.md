# stg.game.ui 包

## 功能描述
stg.game.ui 包是 JavaStg 游戏引擎的用户界面系统包，提供了游戏的各种界面组件，包括游戏画布、游戏状态面板、关卡组选择面板和标题界面。用户界面系统负责处理游戏的输入、渲染和用户交互，是玩家与游戏引擎之间的桥梁。

## 包含文件
1. **GameCanvas.java**：游戏画布类，负责游戏的渲染和输入处理，实现了 `KeyStateProvider` 接口。
2. **GameStatusPanel.java**：游戏状态面板，显示游戏信息，如分数、生命值、残机数和符卡数等。
3. **StageGroupSelectPanel.java**：关卡组选择面板，允许玩家选择要挑战的关卡组，支持关卡组的解锁状态显示。
4. **TitleScreen.java**：标题界面，游戏主菜单和角色选择，支持背景图片和标题音乐播放。

## 核心功能
1. **游戏画布**：实现游戏的渲染和输入处理，包括按键状态跟踪、游戏对象渲染和游戏状态更新。
2. **游戏状态显示**：显示游戏的各种状态信息，如分数、生命值、残机数和符卡数等。
3. **关卡组选择**：允许玩家选择要挑战的关卡组，支持关卡组的解锁状态显示和难度级别显示。
4. **标题界面**：提供游戏的主菜单，支持开始游戏、退出游戏等操作，支持背景图片和标题音乐播放。
5. **虚拟键盘支持**：实现了 `KeyStateProvider` 接口，支持虚拟键盘输入。
6. **动画效果**：为界面元素添加动画效果，增强用户体验。

## 设计理念
stg.game.ui 包采用了面向对象的设计模式，通过不同的类负责不同的界面功能：
- **GameCanvas**：负责游戏的核心渲染和输入处理，是游戏的主要显示区域。
- **GameStatusPanel**：负责显示游戏的状态信息，是游戏的辅助显示区域。
- **StageGroupSelectPanel**：负责关卡组的选择，是游戏的配置界面。
- **TitleScreen**：负责游戏的主菜单，是游戏的入口界面。

这种设计使得界面系统具有良好的模块化和可扩展性，可以方便地添加新的界面组件和功能。

## 依赖关系
- 依赖 `stg.base.KeyStateProvider` 接口，用于提供按键状态。
- 依赖 `stg.game.player.Player` 类，用于游戏画布中的玩家管理。
- 依赖 `stg.util.CoordinateSystem` 类，用于坐标转换。
- 依赖 `stg.game.GameRenderer` 类，用于游戏对象的渲染。
- 依赖 `stg.game.GameWorld` 类，用于游戏世界的管理。
- 依赖 `stg.game.stage.StageGroup` 类，用于关卡组的管理。
- 依赖 `stg.util.ResourceManager` 类，用于资源加载。
- 依赖 `stg.util.AudioManager` 类，用于音频管理。

## 使用示例

### 创建游戏画布
```java
// 创建游戏画布
GameCanvas gameCanvas = new GameCanvas();
gameCanvas.setSize(800, 600);

// 设置游戏状态面板
GameStatusPanel gameStatusPanel = new GameStatusPanel();
gameCanvas.setGameStatusPanel(gameStatusPanel);

// 设置玩家
gameCanvas.setPlayer(0, 0);

// 添加到窗口
JFrame frame = new JFrame("东方STG引擎");
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.getContentPane().add(gameCanvas);
frame.pack();
frame.setVisible(true);

// 在游戏循环中更新和渲染游戏
while (true) {
    gameCanvas.update();
    gameCanvas.repaint();
    Thread.sleep(16); // 约60FPS
}
```

### 使用游戏状态面板
```java
// 创建游戏状态面板
GameStatusPanel gameStatusPanel = new GameStatusPanel();

// 设置游戏状态
// 设置分数
gameStatusPanel.setScore(1000);

// 添加分数
gameStatusPanel.addScore(500);

// 设置生命值
gameStatusPanel.setLives(3);

// 减少生命值
gameStatusPanel.loseLife();

// 设置符卡数
gameStatusPanel.setSpellCards(2);

// 使用符卡
gameStatusPanel.useSpellCard();

// 添加到窗口
JFrame frame = new JFrame("东方STG引擎");
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.getContentPane().add(gameStatusPanel, BorderLayout.EAST);
frame.pack();
frame.setVisible(true);
```

### 使用关卡组选择面板
```java
// 创建关卡组选择回调
StageGroupSelectPanel.StageGroupSelectCallback callback = new StageGroupSelectPanel.StageGroupSelectCallback() {
    @Override
    public void onStageGroupSelected(StageGroup stageGroup) {
        System.out.println("选择了关卡组: " + stageGroup.getDisplayName());
        // 开始游戏
    }
    
    @Override
    public void onBack() {
        System.out.println("返回");
        // 返回标题界面
    }
};

// 创建关卡组选择面板
StageGroupSelectPanel stageGroupSelectPanel = new StageGroupSelectPanel(callback);

// 设置关卡组列表
List<StageGroup> stageGroups = new ArrayList<>();
// 添加关卡组
stageGroups.add(new StageGroup("测试关卡组", "测试用的关卡组", StageGroup.Difficulty.NORMAL, gameCanvas));
stageGroupSelectPanel.setStageGroups(stageGroups);

// 添加到窗口
JFrame frame = new JFrame("东方STG引擎");
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.getContentPane().add(stageGroupSelectPanel);
frame.pack();
frame.setVisible(true);
```

### 使用标题界面
```java
// 创建标题回调
TitleScreen.TitleCallback callback = new TitleScreen.TitleCallback() {
    @Override
    public void onStageGroupSelect() {
        System.out.println("进入关卡组选择");
        // 显示关卡组选择面板
    }
    
    @Override
    public void onGameStart(StageGroup stageGroup) {
        System.out.println("开始游戏: " + stageGroup.getDisplayName());
        // 开始游戏
    }
    
    @Override
    public void onExit() {
        System.out.println("退出游戏");
        // 退出游戏
    }
};

// 创建标题界面
TitleScreen titleScreen = new TitleScreen(callback);

// 添加到窗口
JFrame frame = new JFrame("东方STG引擎");
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.getContentPane().add(titleScreen);
frame.pack();
frame.setVisible(true);

// 停止标题音乐
titleScreen.stopTitleMusic();
```

## 扩展建议
1. **添加更多界面组件**：可以添加更多的界面组件，如暂停菜单、游戏结束界面、设置界面等。
2. **实现界面动画**：可以为界面元素添加更多的动画效果，如淡入淡出、滑动、缩放等，增强用户体验。
3. **支持自定义主题**：可以添加界面主题系统，支持用户自定义界面的颜色、字体、背景等。
4. **实现界面国际化**：可以添加界面国际化支持，支持多语言显示。
5. **添加界面音效**：可以为界面操作添加音效，如按钮点击、菜单切换等音效。
6. **实现界面布局系统**：可以添加界面布局系统，支持不同分辨率和屏幕尺寸的自适应。
7. **添加界面粒子效果**：可以为界面添加粒子效果，如标题界面的背景粒子、菜单选中时的粒子效果等。

## 关键方法

### GameCanvas 类
- `GameCanvas()`：构造函数，创建游戏画布对象。
- `setSize(int width, int height)`：设置画布尺寸。
- `setUpPressed(boolean upPressed)`：设置上键按下状态。
- `setDownPressed(boolean downPressed)`：设置下键按下状态。
- `setLeftPressed(boolean leftPressed)`：设置左键按下状态。
- `setRightPressed(boolean rightPressed)`：设置右键按下状态。
- `setZPressed(boolean zPressed)`：设置Z键按下状态。
- `setShiftPressed(boolean shiftPressed)`：设置Shift键按下状态。
- `setXPressed(boolean xPressed)`：设置X键按下状态。
- `isUpPressed()`：检查上键是否按下。
- `isDownPressed()`：检查下键是否按下。
- `isLeftPressed()`：检查左键是否按下。
- `isRightPressed()`：检查右键是否按下。
- `isZPressed()`：检查Z键是否按下。
- `isShiftPressed()`：检查Shift键是否按下。
- `isXPressed()`：检查X键是否按下。
- `setGameStatusPanel(GameStatusPanel gameStatusPanel)`：设置游戏状态面板。
- `setPlayer(float x, float y)`：设置玩家。
- `getPlayer()`：获取玩家。
- `resetGame()`：重置游戏。
- `setStageGroup(stg.game.stage.StageGroup stageGroup)`：设置关卡组。
- `requestFocusInWindow()`：请求焦点。
- `update()`：更新游戏。
- `getCoordinateSystem()`：获取坐标系统。
- `getWorld()`：获取游戏世界。
- `paint(java.awt.Graphics g)`：绘制游戏。

### GameStatusPanel 类
- `GameStatusPanel()`：构造函数，创建游戏状态面板对象。
- `setScore(int score)`：设置分数。
- `addScore(int points)`：添加分数。
- `setHighScore(int highScore)`：设置最高分。
- `setLives(int lives)`：设置生命值。
- `loseLife()`：减少生命值。
- `setSpellCards(int spellCards)`：设置符卡数。
- `useSpellCard()`：使用符卡。
- `setMaxScore(int maxScore)`：设置最大分数。
- `getScore()`：获取分数。
- `getHighScore()`：获取最高分。
- `getLives()`：获取生命值。
- `getSpellCards()`：获取符卡数。
- `getMaxScore()`：获取最大分数。

### StageGroupSelectPanel 类
- `StageGroupSelectPanel(StageGroupSelectCallback callback)`：构造函数，创建关卡组选择面板对象。
- `setStageGroups(List<StageGroup> groups)`：设置关卡组列表。
- `addStageGroup(StageGroup group)`：添加关卡组。
- `stopAnimation()`：停止动画计时。
- `isUpPressed()`：检查上键是否按下。
- `isDownPressed()`：检查下键是否按下。
- `isLeftPressed()`：检查左键是否按下。
- `isRightPressed()`：检查右键是否按下。
- `isZPressed()`：检查Z键是否按下。
- `isShiftPressed()`：检查Shift键是否按下。
- `isXPressed()`：检查X键是否按下。
- `setUpPressed(boolean upPressed)`：设置上键按下状态。
- `setDownPressed(boolean downPressed)`：设置下键按下状态。
- `setLeftPressed(boolean leftPressed)`：设置左键按下状态。
- `setRightPressed(boolean rightPressed)`：设置右键按下状态。
- `setZPressed(boolean zPressed)`：设置Z键按下状态。
- `setShiftPressed(boolean shiftPressed)`：设置Shift键按下状态。
- `setXPressed(boolean xPressed)`：设置X键按下状态。

### TitleScreen 类
- `TitleScreen(TitleCallback callback)`：构造函数，创建标题界面对象。
- `stopTitleMusic()`：停止标题音乐。
- `isUpPressed()`：检查上键是否按下。
- `isDownPressed()`：检查下键是否按下。
- `isLeftPressed()`：检查左键是否按下。
- `isRightPressed()`：检查右键是否按下。
- `isZPressed()`：检查Z键是否按下。
- `isShiftPressed()`：检查Shift键是否按下。
- `isXPressed()`：检查X键是否按下。
- `setUpPressed(boolean upPressed)`：设置上键按下状态。
- `setDownPressed(boolean downPressed)`：设置下键按下状态。
- `setLeftPressed(boolean leftPressed)`：设置左键按下状态。
- `setRightPressed(boolean rightPressed)`：设置右键按下状态。
- `setZPressed(boolean zPressed)`：设置Z键按下状态。
- `setShiftPressed(boolean shiftPressed)`：设置Shift键按下状态。
- `setXPressed(boolean xPressed)`：设置X键按下状态。