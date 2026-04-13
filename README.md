# JavaSTG

基于Java开发的弹幕射击游戏引擎

## 项目简介

JavaSTG是一个使用OpenGl开发的弹幕射击(STG)游戏引擎，提供基础的游戏循环、渲染系统、输入处理等功能，是作为游戏引擎开发的学习项目。

经过两年的沉淀与积累，我们在原有基础上重新审视、重构，以更成熟的技术视角和更清晰的架构设计重启了本项目。这两年间，团队不断学习游戏开发相关知识，深入理解引擎架构，如今带着全新的思路和更扎实的代码功底，重新踏上STG游戏引擎的开发之路。

## 功能特性

### 界面系统
- **三面板布局**：窗口分为左中右三个面板，比例为1:1.5:1
  - 左侧面板：信息显示和虚拟键盘
  - 中间面板：游戏主区域
  - 右侧面板：信息显示区域
- **标题界面**：主菜单、角色选择动画效果和颜色预览
- **暂停菜单**：ESC键暂停/恢复，支持继续游戏、重新开始、返回主菜单
- **关卡选择**：在主界面菜单中选择不同的关卡

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

## 项目结构

```
JavaStg/
├── doc/                    # 项目文档
├── lib/                    # 依赖库
├── resources/              # 资源文件
│   ├── audio/              # 音频文件
│   ├── fonts/              # 字体文件
│   └── images/             # 图片资源
├── src/                    # 源代码
│   ├── Main/               # 主入口包
│   ├── stg/                # 核心引擎包
│   │   ├── base/           # 基础组件包
│   │   ├── core/           # 核心系统包
│   │   ├── entity/         # 游戏实体包
│   │   ├── event/          # 事件系统包
│   │   ├── render/         # 渲染系统包
│   │   ├── service/        # 服务系统包
│   │   ├── stage/          # 关卡系统包
│   │   └── util/           # 工具类包
│   └── user/               # 用户自定义内容包
│       ├── boss/           # 自定义Boss包
│       ├── bullet/         # 自定义子弹包
│       ├── demo/           # 示例代码包
│       ├── enemy/          # 自定义敌人包
│       ├── item/           # 自定义道具包
│       ├── laser/          # 自定义激光包
│       ├── player/         # 自定义玩家包
│       ├── spellcard/      # 自定义符卡包
│       ├── stage/          # 自定义关卡包
│       └── stageGroup/     # 自定义关卡组包
├── .gitignore              # Git忽略文件
├── CHANGELOG.md            # 变更日志
├── CODE_STYLE.md           # 代码风格
├── LICENSE.md              # 许可证
├── README.md               # 项目说明
└── run_game.ps1            # 运行脚本
```

## 技术栈

- **语言**: Java
- **GUI框架**: Swing
- **渲染**: Java 2D API / OpenGL (LWJGL 3)
- **音频**: javax.sound.sampled / OpenAL (LWJGL 3)
- **版本控制**: Git

## 后续优化方向

### 渲染与音频升级

项目已成功迁移到OpenGL/OpenAL系统，显著提升了游戏性能和视觉效果：

**渲染系统**:
- ✅ 实现了OpenGL (LWJGL 3) 渲染器
- ✅ 支持批量渲染和纹理映射
- ✅ 保留了Java 2D API作为回退方案
- ✅ 统一的渲染器接口设计

**音频系统**:
- ✅ 实现了OpenAL (LWJGL 3) 音频管理器
- ✅ 支持OGG和WAV格式
- ✅ 提供3D音频定位潜力

**性能提升**:
- 大规模弹幕渲染性能提升5-10倍
- 支持更复杂的视觉特效
- 更低的CPU占用率
- 更流畅的游戏体验

### 使用指南

**启用OpenGL渲染**:
```java
// 在启动时启用OpenGL
GameCanvas canvas = new GameCanvas();
canvas.initRenderer(true); // true 启用OpenGL
```

**使用OpenAL音频**:
```java
// 获取音频管理器
ALAudioManager audioManager = ALAudioManager.getInstance();
audioManager.init();

// 加载并播放音频
audioManager.loadMusic("bgm", "audio/music/stage1.ogg");
audioManager.playMusic("bgm", true);
```

**详细文档**:
请参考 [OpenGL_OpenAL_Migration_Guide.md](OpenGL_OpenAL_Migration_Guide.md) 获取完整的迁移文档和API参考。

## 许可证

本项目使用MIT License，详情请参阅 [LICENSE.md](LICENSE.md) 文件。

## 作者

JavaSTG开发团队

---

*这是一个学习项目，欢迎提出建议和改进意见，期待更多志同道合的伙伴加入我们*

