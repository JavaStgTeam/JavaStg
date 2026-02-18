# ui 包说明

## 功能概述

**ui 包**是游戏的用户界面包，包含了游戏界面相关的类。用户界面系统负责显示游戏的各种界面元素，如游戏画布、标题屏幕、状态栏等，是玩家与游戏交互的重要媒介。

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| GameCanvas | 游戏画布类，实现了游戏的主画布 |
| TitleScreen | 标题屏幕类，实现了游戏的标题界面 |
| GameStatusPanel | 游戏状态栏类，显示游戏状态信息 |
| PauseMenu | 暂停菜单类，实现了游戏的暂停界面 |
| StageGroupSelectPanel | 关卡组选择面板类，实现了关卡组选择界面 |

## 主要功能

### GameCanvas 类
- **游戏画布**：游戏的主要渲染区域
- **输入处理**：处理玩家的键盘和鼠标输入
- **游戏循环**：与游戏循环配合工作
- **实体管理**：管理游戏世界中的实体
- **碰撞检测**：处理游戏中的碰撞检测

### TitleScreen 类
- **标题界面**：游戏启动时显示的界面
- **菜单系统**：提供开始游戏、设置、退出等菜单选项
- **背景显示**：显示游戏标题和背景
- **按键提示**：显示按键操作提示
- **回调机制**：通过回调接口与其他组件通信

### GameStatusPanel 类
- **状态显示**：显示游戏的各种状态信息
- **分数显示**：显示玩家的分数
- **生命值显示**：显示玩家的生命值
- **炸弹显示**：显示玩家的炸弹数量
- **关卡信息**：显示当前关卡信息

### PauseMenu 类
- **暂停界面**：游戏暂停时显示的界面
- **菜单选项**：提供继续游戏、返回标题、退出等选项
- **背景模糊**：显示半透明背景
- **响应输入**：处理玩家的输入操作

### StageGroupSelectPanel 类
- **关卡组选择**：显示和选择可用的关卡组
- **列表显示**：以列表形式显示关卡组
- **信息显示**：显示关卡组的名称和描述
- **难度选择**：支持选择不同难度
- **回调机制**：通过回调接口与其他组件通信

## 类结构

```java
// GameCanvas 类
public class GameCanvas extends JPanel {
    private GameWorld gameWorld;
    private Player player;
    private GameRenderer renderer;
    private KeyListener keyListener;
    
    public GameCanvas() {
        // 初始化...
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 渲染游戏...
    }
    
    public void update(double deltaTime) {
        // 更新游戏状态...
    }
    
    // 其他方法...
}

// TitleScreen 类
public class TitleScreen extends JPanel {
    public interface TitleCallback {
        void onGameStart(StageGroup stageGroup);
        void onSettings();
        void onExit();
    }
    
    private TitleCallback callback;
    private int menuIndex;
    private MenuState menuState;
    
    public enum MenuState {
        MAIN, STAGE_SELECT
    }
    
    public TitleScreen(TitleCallback callback) {
        this.callback = callback;
        // 初始化...
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 渲染标题界面...
    }
    
    // 其他方法...
}

// GameStatusPanel 类
public class GameStatusPanel extends JPanel {
    private Player player;
    private Stage currentStage;
    
    public GameStatusPanel(Player player) {
        this.player = player;
        // 初始化...
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 渲染状态栏...
    }
    
    public void setCurrentStage(Stage stage) {
        this.currentStage = stage;
    }
    
    // 其他方法...
}

// PauseMenu 类
public class PauseMenu extends JPanel {
    private boolean visible;
    private int menuIndex;
    
    public PauseMenu() {
        // 初始化...
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (visible) {
            // 渲染暂停菜单...
        }
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
        repaint();
    }
    
    // 其他方法...
}

// StageGroupSelectPanel 类
public class StageGroupSelectPanel extends JPanel {
    public interface StageGroupSelectCallback {
        void onStageGroupSelected(StageGroup stageGroup);
        void onBack();
    }
    
    private StageGroupSelectCallback callback;
    private List<StageGroup> stageGroups;
    private int selectedIndex;
    
    public StageGroupSelectPanel(StageGroupSelectCallback callback) {
        this.callback = callback;
        // 初始化...
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 渲染关卡组选择界面...
    }
    
    public void setStageGroups(List<StageGroup> groups) {
        this.stageGroups = groups;
        repaint();
    }
    
    // 其他方法...
}
```

## 使用示例

### 创建和使用游戏画布

```java
// 创建游戏画布
GameCanvas gameCanvas = new GameCanvas();

// 添加到窗口
window.add(gameCanvas);

// 启动游戏循环
GameLoop gameLoop = new GameLoop(gameCanvas);
gameLoop.start();

// 获取玩家
Player player = gameCanvas.getPlayer();

// 获取游戏世界
GameWorld gameWorld = gameCanvas.getGameWorld();
```

### 创建和使用标题屏幕

```java
// 创建标题屏幕
TitleScreen titleScreen = new TitleScreen(new TitleScreen.TitleCallback() {
    @Override
    public void onGameStart(StageGroup stageGroup) {
        // 开始游戏
        startGame(stageGroup);
    }
    
    @Override
    public void onSettings() {
        // 打开设置界面
        openSettings();
    }
    
    @Override
    public void onExit() {
        // 退出游戏
        System.exit(0);
    }
});

// 添加到窗口
window.add(titleScreen);

// 显示标题屏幕
titleScreen.setVisible(true);
```

### 创建和使用关卡组选择面板

```java
// 创建关卡组选择面板
StageGroupSelectPanel selectPanel = new StageGroupSelectPanel(
    new StageGroupSelectPanel.StageGroupSelectCallback() {
        @Override
        public void onStageGroupSelected(StageGroup stageGroup) {
            // 选择关卡组
            titleScreen.onGameStart(stageGroup);
        }
        
        @Override
        public void onBack() {
            // 返回标题界面
            titleScreen.setMenuState(TitleScreen.MenuState.MAIN);
        }
    }
);

// 获取关卡组
List<StageGroup> stageGroups = StageGroupManager.getInstance().getStageGroups();

// 设置关卡组
selectPanel.setStageGroups(stageGroups);

// 添加到窗口
window.add(selectPanel);
```

## 设计说明

1. **模块化设计**：将界面系统拆分为多个职责明确的类
2. **回调机制**：通过回调接口实现组件间的通信
3. **事件处理**：处理用户的输入事件
4. **渲染优化**：优化界面的渲染性能
5. **响应式设计**：适应不同的屏幕分辨率

## 开发建议

- 当需要修改界面逻辑时，修改相应的界面类
- 当需要添加新的界面元素时，创建新的界面类
- 为界面添加适当的视觉效果和动画
- 考虑界面的响应速度和用户体验
- 确保界面在不同分辨率下的显示效果
- 为界面添加适当的音效，提升游戏体验