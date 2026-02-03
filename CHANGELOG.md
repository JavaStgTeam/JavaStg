# 更新日志

## 2026-02-03

### 修复
- 修复大量编译错误，确保项目可正常编译
- 修正包声明与文件位置不匹配的错误

### 重构
- 将 `StageGroup` 和 `StageGroupManager` 从 `user` 包移动到 `stg.game.stage` 包
- 优化核心游戏类结构和性能

### 修改
- 更新核心文件、游戏对象类、UI相关文件和用户自定义实现

### 删除
- 删除 `src/user/player/Player.java`
- 删除 `src/user/stage/StageCompletionCondition.java`
- 删除 `src/user/stage/StageGroup.java` 和 `StageGroupManager.java`

## 2026-02-02

### 修复
- 解决编译失败问题：修复包声明与文件位置不匹配的错误

### 清理
- 删除 `src/user/` 目录下的重复示例文件，避免编译器混淆
- 删除所有示例类：
  - `src/user/enemy/BasicEnemyExample.java`
  - `src/user/item/PowerUpExample.java`
  - `src/user/laser/StraightLaserExample.java`
  - `src/user/player/BasicPlayerExample.java`
- 删除所有测试类：
  - `src/user/stage/SimpleStageTest.java`
  - `src/user/stage/StageSystemTest.java`
  - `src/user/stage/StageTest.java`
- 确保文件位置与包声明完全匹配

### 新增
- 创建了 `CODE_STYLE.md` 文件，制定了完整的Java代码规范，存放于doc目录中
- 规范文档包含项目结构、命名规范、代码风格、注释规范、异常处理、性能优化、安全规范、测试规范、代码质量工具、版本控制和代码审查等方面

## 2026-02-01

### 清理
- 删除临时锁文件、重复的示例文件、冗余基类文件及编译产物
- 清理了 `examples/`、`logs/` 和 `bin/` 目录

### 修改
- 更新所有文件导入语句，确保引用正确的 `stg.game` 包基类
- 简化 `compile_and_run.bat`，移除外部库依赖
- 将文档文件从英文修改为中文

### 新增
- 为 `src` 和 `ai_debug` 目录添加中文 `README.md` 文件

### 修复
- 修正包引用错误，解决编译问题
- 验证项目可正常编译运行

## 2026-01-31

### 新增
- 在 `src/user/` 目录下创建了 `bullet`、`enemy`、`item`、`laser`、`player`、`stage` 六个文件夹结构
- 将所有基类的子类文件复制到 `user` 目录的对应文件夹中

### 修改
- 删除了 `src/stg/game/` 目录下的所有子类文件，只保留基类文件
- 修改了项目中所有引用旧包路径的地方，改为使用新的 `user` 包路径
- 更新了 `Main.java`、`TitleScreen.java`、`StageGroupSelectPanel.java`、`GameCanvas.java` 等核心文件中的包引用
- 修改了 `event` 包中的文件，将旧包路径引用改为新的 `user` 包路径

### 结构调整
- 重构了项目结构，将子类文件从 `stg.game` 包移动到 `user` 包
- 保留了 `stg.game` 包中的基类文件，确保子类可以正确继承

### 说明
- 完成了将基类的所有子类放入 `user` 目录并创建对应文件夹的任务
- 确保了修改后的项目能够正常编译和运行

## 2026-01-30

### 新增
- 在根目录创建了更新日志文件 `UPDATES.md`
- 确立了使用日期号代替版本号的更新记录格式

### 修改
- 完善了 `README.md` 文件，添加了详细的项目文档
  - 项目简介：更新了项目背景和开发历程
  - 功能特性：详细描述了界面系统、玩家系统、子弹系统、敌人系统、渲染系统和数学工具
  - 项目结构：提供了完整的目录结构和文件说明
  - 操作说明：添加了详细的按键功能表
  - 编译运行：提供了Windows用户的一键编译、打包和运行命令
  - 技术栈：列出了使用的技术和框架
  - 开发计划：添加了已完成和待完成的功能
  - 后续优化方向：提出了渲染框架的探索计划

### 说明
- 本文件用于记录每天的更新内容
- 格式：按日期分组，每个日期下包含新增、修改、删除等分类
- 每天的更新内容请添加到对应日期的章节中