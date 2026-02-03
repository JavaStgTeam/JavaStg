# JavaSTG

基于Java开发的弹幕射击游戏引擎

## 项目简介

JavaSTG是一个使用Java Swing框架开发的弹幕射击(STG)游戏引擎，提供基础的游戏循环、渲染系统、输入处理等功能，是作为游戏引擎开发的学习项目。

经过两年的沉淀与积累，我们在原有基础上重新审视、重构，以更成熟的技术视角和更清晰的架构设计重启了本项目。这两年间，团队不断学习游戏开发相关知识，深入理解引擎架构，如今带着全新的思路和更扎实的代码功底，重新踏上STG游戏引擎的开发之路。

## 功能特性

### 界面系统
- **三面板布局**：窗口分为左中右三个面板，比例为1:1.5:1
  - 左侧面板：信息显示和虚拟键盘
  - 中间面板：游戏主区域
  - 右侧面板：信息显示区域
- **标题界面**：主菜单、角色选择（灵梦、魔理沙）、动画效果和颜色预览
- **暂停菜单**：ESC键暂停/恢复，支持继续游戏、重新开始、返回主菜单

### 玩家系统
- 键盘控制移动（上下左右方向键）
- Z键发射子弹
- Shift键切换低速模式
- 多角色支持（灵梦、魔理沙）
- 同时按下相反方向键时保持静止

### 子弹系统
- 多种子弹类型（圆形、曲线、扇形）
- 自动清理越界子弹
- 低速模式发射更大子弹
- 敌方子弹系统

### 敌人系统
- 敌人AI
- 波次管理
- 多格式关卡加载（JSON/JS/Python）

### 渲染系统
- 双缓冲渲染
- 抗锯齿支持
- 60FPS游戏循环
- 中心原点坐标系

### 数学工具
- 2D向量运算
- 碰撞检测
- 随机数生成
- 角度/弧度转换

## 项目结构

```
JavaSTG/
├── src/
│   ├── Main/
│   │   └── Main.java              # 程序入口
│   ├── stg/                        # 游戏引擎核心
│   │   ├── base/                   # 基础组件
│   │   │   ├── Window.java          # 主窗口类
│   │   │   ├── VirtualKeyboardPanel.java  # 虚拟键盘面板
│   │   │   └── KeyStateProvider.java      # 键盘状态提供者
│   │   ├── game/                  # 游戏核心
│   │   │   ├── GameLoop.java       # 游戏循环
│   │   │   ├── GameWorld.java      # 游戏世界
│   │   │   ├── GameRenderer.java   # 游戏渲染器
│   │   │   ├── InputHandler.java   # 输入处理器
│   │   │   ├── CollisionSystem.java # 碰撞系统
│   │   │   ├── GameStateManager.java # 游戏状态管理器
│   │   │   ├── PauseMenu.java      # 暂停菜单
│   │   │   ├── IGameObject.java    # 游戏对象接口
│   │   │   ├── IGameWorld.java     # 游戏世界接口
│   │   ├── game/ui/               # 用户界面
│   │   │   ├── GameCanvas.java     # 游戏画布
│   │   │   ├── TitleScreen.java    # 标题界面
│   │   │   ├── GameStatusPanel.java # 游戏状态面板
│   │   │   └── StageGroupSelectPanel.java # 关卡组选择面板
│   │   ├── game/player/            # 玩家系统
│   │   │   ├── Player.java         # 玩家基类
│   │   │   └── IPlayer.java        # 玩家接口
│   │   ├── game/bullet/            # 子弹系统
│   │   │   ├── Bullet.java         # 子弹基类
│   │   │   └── IBullet.java        # 子弹接口
│   │   ├── game/enemy/             # 敌人系统
│   │   │   ├── Enemy.java         # 敌人基类
│   │   │   └── IEnemy.java        # 敌人接口
│   │   ├── game/item/              # 物品系统
│   │   │   ├── Item.java           # 物品基类
│   │   │   └── IItem.java          # 物品接口
│   │   ├── game/laser/             # 激光系统
│   │   │   └── Laser.java          # 激光基类
│   │   ├── game/obj/               # 游戏对象
│   │   │   └── Obj.java            # 游戏对象基类
│   │   ├── game/stage/             # 关卡系统
│   │   │   ├── Stage.java          # 关卡基类
│   │   │   ├── StageGroup.java     # 关卡组
│   │   │   ├── StageGroupManager.java # 关卡组管理器
│   │   │   └── StageCompletionCondition.java # 关卡完成条件
│   │   ├── game/event/             # 事件系统
│   │   │   ├── BulletFiredEvent.java # 子弹发射事件
│   │   │   ├── EnemyDestroyedEvent.java # 敌人销毁事件
│   │   │   └── ItemCollectedEvent.java # 物品收集事件
│   │   └── util/                   # 工具类
│   │       ├── math/             # 数学工具
│   │       │   ├── Vector2.java         # 2D向量类
│   │       │   ├── MathUtils.java       # 数学工具类
│   │       │   └── RandomGenerator.java # 随机数生成器
│   │       ├── script/            # 脚本工具
│   │       ├── ResourceManager.java # 资源管理器
│   │       ├── CoordinateSystem.java # 坐标系统
│   │       ├── EventBus.java      # 事件总线
│   │       ├── RenderUtils.java   # 渲染工具
│   │       └── AudioManager.java  # 音频管理器
│   └── user/                       # 用户自定义内容
│       ├── bullet/                # 用户子弹
│       │   ├── CircularBullet.java  # 圆形弹
│       │   ├── CurvingBullet.java   # 曲线弹
│       │   ├── PlayerTrackingBullet.java # 追踪玩家弹
│       │   ├── SimpleBullet.java    # 简单弹
│       │   ├── SpiralBullet.java    # 螺旋弹
│       │   ├── SpreadBullet.java    # 扇形弹
│       │   └── TrackingBullet.java  # 追踪弹
│       ├── enemy/                 # 用户敌人
│       │   ├── BasicEnemy.java     # 基础敌人
│       │   ├── EnemyBullet.java    # 敌方子弹
│       │   ├── EnemyWithSound.java # 带声音的敌人
│       │   ├── LaserShootingEnemy.java # 激光敌人
│       │   ├── OrbitEnemy.java     # 轨道敌人
│       │   ├── RapidFireEnemy.java # 速射敌人
│       │   ├── SpiralEnemy.java    # 螺旋敌人
│       │   ├── SpreadEnemy.java    # 扇形敌人
│       │   └── TrackingEnemy.java  # 追踪敌人
│       ├── item/                  # 用户物品
│       │   ├── BombUp.java         # 炸弹增加
│       │   ├── LifeUp.java         # 生命增加
│       │   ├── PowerUp.java        # 威力增加
│       │   └── ScorePoint.java     # 分数点
│       ├── laser/                 # 用户激光
│       │   ├── CurvedLaser.java    # 曲线激光
│       │   ├── EnemyCurvedLaser.java # 敌人曲线激光
│       │   ├── EnemyLaser.java     # 敌人激光
│       │   ├── EnemyLinearLaser.java # 敌人线性激光
│       │   ├── LinearLaser.java    # 线性激光
│       │   └── SimpleLaser.java    # 简单激光
│       ├── player/                # 用户玩家
│       │   ├── CustomOption.java   # 自定义选项
│       │   ├── CustomPlayer.java   # 自定义玩家
│       │   ├── MarisaOption.java   # 魔理沙选项
│       │   ├── MarisaPlayer.java   # 魔理沙玩家
│       │   ├── Option.java         # 选项基类
│       │   ├── PlayerFactory.java  # 玩家工厂
│       │   ├── PlayerType.java     # 玩家类型枚举
│       │   ├── PlayerWithImage.java # 带图像的玩家
│       │   ├── ReimuOption.java    # 灵梦选项
│       │   └── ReimuPlayer.java    # 灵梦玩家
│       └── stage/                 # 用户关卡
│           ├── AdvancedStageGroup.java # 高级关卡组
│           ├── BeginnerStageGroup.java # 初级关卡组
│           ├── ExpertStageGroup.java # 专家关卡组
│           ├── IntermediateStageGroup.java # 中级关卡组
│           ├── SimpleStage.java    # 简单关卡
│           ├── StageState.java     # 关卡状态
│           └── WaveBasedStage.java # 波次关卡
├── bin/                           # 编译输出目录
└── README.md
```

## 操作说明

| 按键 | 功能 |
|------|------|
| ↑ ↓ | 切换菜单选项 |
| ← → | 切换角色 |
| Z/Enter | 确认选择 |
| ESC | 返回/暂停 |
| ↑ | 向上移动 |
| ↓ | 向下移动 |
| ← | 向左移动 |
| → | 向右移动 |
| Z | 发射子弹 |
| Shift | 低速模式 |
| X | 预留按键 |

## 编译运行

### 快速开始

#### Windows 用户

```batch
:: 一键编译
compile.bat

:: 打包 JAR 文件
package.bat

:: 运行游戏
java -jar JavaSTG.jar
```

#### 手动编译

```batch
:: 编译项目
javac -encoding UTF-8 -d bin -sourcepath src src/Main/Main.java src/stg/**/*.java
```

### 运行游戏

#### 使用 JAR 文件（推荐）

```batch
java -jar JavaSTG.jar
```

#### 使用 classpath

```batch
java -cp "bin" Main.Main
```

## 技术栈

- **语言**: Java
- **GUI框架**: Swing
- **渲染**: Java 2D API
- **版本控制**: Git

## 开发计划

- ✅ 添加敌人系统
- ✅ 实现多种子弹模式
- ✅ 添加碰撞检测
- ⬜ 实现Boss战系统
- ⬜ 添加音效和背景音乐
- ✅ 实现关卡系统

## 后续优化方向

随着项目的发展，我们计划引入更先进的渲染框架来提升游戏性能和视觉效果。目前主要使用Java Swing和Java 2D API，虽然足以应对基础需求，但在大规模弹幕、复杂特效和高帧率渲染方面仍有提升空间。

**计划探索的渲染方案**:
- LWJGL (Lightweight Java Game Library) - OpenGL绑定
- LibGDX - 跨平台游戏开发框架
- jMonkeyEngine - Java 3D游戏引擎(可降维用于2D)

由于我们在这些现代渲染框架方面缺乏技术背景和实践经验，恳请有相关经验的高人不吝赐教，提供技术指导和最佳实践建议。任何关于渲染框架选型、架构设计、性能优化等方面的帮助都将不胜感激。

## 许可证

MIT License

## 作者

JavaSTG开发团队

---

*这是一个学习项目，欢迎提出建议和改进意见，期待更多志同道合的伙伴加入我们*

