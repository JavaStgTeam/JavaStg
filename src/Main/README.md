# Main 包

## 功能描述
Main 包是 JavaStg 游戏引擎的入口点，负责初始化游戏环境、显示标题界面、处理关卡组选择以及启动游戏。

## 包含文件
- `Main.java`：游戏的主类，包含主方法和游戏启动逻辑

## 主要功能
1. **游戏初始化**：在 `main` 方法中创建游戏窗口并显示标题界面
2. **标题界面管理**：通过 `showTitleScreen` 方法显示游戏标题界面
3. **关卡组选择**：通过 `showStageGroupSelect` 方法显示关卡组选择界面
4. **游戏启动**：通过 `startGame` 方法启动选定的关卡组
5. **返回标题界面**：通过 `returnToTitle` 方法从游戏中返回标题界面

## 工作流程
1. 游戏启动时，创建 `Window` 实例并显示标题界面
2. 用户可以从标题界面选择进入关卡组选择或退出游戏
3. 在关卡组选择界面，用户可以选择要游玩的关卡组
4. 选定关卡组后，游戏初始化玩家位置并启动游戏循环
5. 游戏过程中，用户可以通过特定操作返回标题界面

## 关键方法
- `main(String[] args)`：游戏入口点
- `showTitleScreen()`：显示标题界面
- `showStageGroupSelect()`：显示关卡组选择界面
- `startGame(StageGroup stageGroup)`：启动游戏
- `returnToTitle()`：返回标题界面
- `getWindow()`：获取游戏窗口实例

## 依赖关系
- 依赖 `stg.base.Window` 提供窗口管理
- 依赖 `stg.game.player.Player` 提供玩家对象
- 依赖 `stg.game.stage.StageGroup` 和 `StageGroupManager` 提供关卡组管理
- 依赖 `stg.game.ui` 包中的界面组件