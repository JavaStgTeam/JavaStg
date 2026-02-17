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

### 输入系统
- **GameCanvas 统一处理**：游戏输入由 GameCanvas 类统一管理
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

### 关卡系统
- **StageGroupDiscovery**：负责发现所有StageGroup的子类
- **StageGroupFactory**：负责创建StageGroup实例
- **StageGroupManager**：负责管理StageGroup实例

## 项目结构

```
JavaSTG/
├── src/
│   ├── Main/                       # 程序入口包
│   ├── stg/                        # 游戏引擎核心
│   │   ├── base/                   # 基础组件
│   │   ├── game/                  # 游戏核心
│   │   │   ├── bullet/            # 子弹系统
│   │   │   ├── enemy/             # 敌人系统
│   │   │   ├── event/             # 事件系统
│   │   │   ├── item/              # 物品系统
│   │   │   ├── laser/             # 激光系统
│   │   │   ├── obj/               # 游戏对象
│   │   │   ├── player/            # 玩家系统
│   │   │   ├── stage/             # 关卡系统
│   │   │   │   ├── StageGroup.java            # 关卡组基类
│   │   │   │   ├── StageGroupManager.java     # 关卡组管理器
│   │   │   │   ├── StageGroupDiscovery.java   # 关卡组发现器
│   │   │   │   ├── StageGroupFactory.java      # 关卡组工厂
│   │   │   │   └── StageGroupInfo.java         # 关卡组信息注解
│   │   │   └── ui/               # 用户界面
│   │   │       └── GameCanvas.java            # 游戏画布（统一处理输入）
│   │   └── util/                   # 工具类
│   │       └── math/             # 数学工具
│   └── user/                       # 用户自定义内容
│       ├── bullet/                # 用户子弹
│       ├── enemy/                 # 用户敌人
│       ├── item/                  # 用户物品
│       ├── laser/                 # 用户激光
│       ├── player/                # 用户玩家
│       ├── stage/                 # 用户关卡
│       └── stageGroup/            # 用户关卡组
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

