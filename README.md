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
- ✅ 保留了javax.sound.sampled作为回退方案

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

