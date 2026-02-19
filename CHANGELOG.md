# 更新日志

## 2026-02-19

### 修复
- 修复暂停菜单无法使用的问题（ESC键无反应）
- 修复暂停时按键没有效果的问题
- 修复返回主菜单后重新进入关卡没有重新开始的问题
- 修复选择"重新开始"后没有反应的问题

## 2026-02-18

### 架构修改
- 对项目架构进行了较大修改

### 清理工作
- 清理了旧的 game 目录残留文件
- 确保项目结构清晰整洁

## 2026-02-17

### 架构重构
- 统一输入处理系统：移除未使用的 `InputHandler` 类，使用 `GameCanvas` 统一处理
- 优化碰撞检测：移除 `GameWorld` 中的碰撞检测逻辑，统一到 `CollisionSystem`
- 分离 `StageGroupManager` 职责：新增 `StageGroupDiscovery` 和 `StageGroupFactory` 类
- 统一 `Obj` 子类设计模式：修改 `Item` 类，移除强制实现的抽象方法

### 其他
- 验证所有重构功能正常工作，无回归问题
- 更新项目文档，反映架构变更
- 清理临时测试文件

## 2026-02-16

### 修复
- 修复GameWorld类中updateBullets、updateEnemies和updateItems方法的遍历问题，使用从后往前遍历的方式避免并发修改异常

### 坐标系统一
- 完成坐标系统一化项目，统一所有游戏对象使用固定360*480游戏逻辑坐标系
- 修复玩家类的边界检测逻辑，使用游戏逻辑坐标系的固定边界
- 修复敌人类的坐标系使用，确保敌人移动和边界检测使用游戏逻辑坐标系
- 修复Boss类的入场逻辑，使用游戏逻辑坐标系的顶部边界
- 修复DefaultEnemy类的无限递归调用问题

## 2026-02-15

### 修复
- 修复注释乱码问题，确保代码注释清晰完整

### 优化
- 统一代码风格，将所有测试代码类名改为大驼峰命名法
- 优化代码注释，确保符合Javadoc规范
- 提升代码可读性和可维护性

### 线程安全修复
- GameWorld使用CopyOnWriteArrayList确保集合操作线程安全
- EventBus使用ConcurrentHashMap确保事件处理线程安全
- GameLoop优化线程中断处理，确保线程安全退出

### 文档更新
- 更新相关文档，反映最新的线程安全修复和代码风格统一

## 2026-02-14

### 新增
- 创建 `EnemySpellcard` 基类，实现符卡系统
- 创建 `Boss` 基类，实现Boss战系统
- 实现符卡阶段生命值管理，每个阶段有独立血量
- 实现符卡名称在右上角显示功能

### 修改
- 重构Boss战系统架构，将生命值管理移至符卡类
- 优化GameRenderer类，添加符卡信息渲染功能
- 修改GameCanvas类，传递实际画布尺寸给渲染器

### 功能增强
- Boss入场和退场动画
- 符卡阶段视觉效果增强
- 生命值条根据符卡类型显示不同颜色
- 符卡名称显示带有半透明背景，提升可读性

## 2026-02-13

### 修复
- 修复敌人边界反弹问题，使敌人能够在碰到版边时正确改变方向
- 实现子弹与敌人的碰撞检测，使子弹能够对敌人造成伤害

### 清理
- 删除测试文件，清理项目结构

## 2026-02-11

### 修复
- 修复关卡组加载问题，修改 `StageGroupManager.java` 文件，使用当前线程的上下文类加载器来加载关卡组类

### 修改
- 调整默认子弹速度

## 2026-02-10

### 新增
- 创建了 `user.bullet.SimpleBullet` 类，实现了简单的子弹功能

### 修改
- 修改了 `stg.game.player.Player` 类，添加了 `GameWorld` 引用和相关方法
- 修改了 `stg.game.ui.GameCanvas` 类，在设置玩家时传递 `GameWorld` 引用
- 完善了 `user.player.DefaultPlayer` 类的 `shoot()` 方法，实现发射两个主炮的功能


## 2026-02-07

### 新增
- 创建了 `user.player` 包
- 在 `user.player` 包中创建了 `DefaultPlayer` 类，实现发射两个主炮的功能

### 修改
- 修改了 `GameCanvas` 类，使用 `DefaultPlayer` 作为默认自机

### 测试
- 编译并运行了游戏，验证默认自机发射两个主炮的功能正常

## 2026-02-06

### 依赖移除
- 解除项目对 `user` 包的所有依赖
- 移除了多个文件中对 `user.*` 包的引用

### 修改
- `Main.java` - 移除对 `user.player.PlayerType` 的依赖，简化游戏启动流程
- `Window.java` - 移除对 `user.player.PlayerType` 的依赖，修改玩家初始化方法
- `GameCanvas.java` - 移除对 `user.player.PlayerType` 的依赖，简化玩家设置方法
- `TitleScreen.java` - 移除对 `user.player.PlayerType` 的依赖，简化菜单流程
- `StageGroupSelectPanel.java` - 移除对 `user.player.PlayerType` 的依赖，简化关卡选择界面
- `StageGroupManager.java` - 移除对 `user.stage` 包的依赖，不再添加默认关卡组
- `Player.java` - 移除对 `user.bullet` 和 `user.player` 包的依赖，简化射击逻辑
- `IPlayer.java` - 移除对 `user.player.Option` 的依赖
- `IGameWorld.java` - 移除对 `user.enemy.EnemyBullet` 和 `user.laser.EnemyLaser` 的依赖
- `GameWorld.java` - 移除对 `user.enemy.EnemyBullet` 和 `user.laser.EnemyLaser` 的依赖
- `GameRenderer.java` - 移除对 `user.enemy.EnemyBullet` 和 `user.laser.EnemyLaser` 的依赖
- `CollisionSystem.java` - 移除对 `user.enemy.EnemyBullet` 和 `user.laser.EnemyLaser` 的依赖

### 修复
- 修复 `StageCompletionCondition.java` 文件中的乱码字符
- 修复 `Player.java`、`Enemy.java` 和 `Item.java` 文件中的构造函数调用问题
- 移除所有对 `getGameCanvas()` 方法的调用，使用默认画布尺寸

### 测试
- 成功编译项目，无错误
- 成功运行项目，验证依赖移除后的功能完整性

## 2026-02-04

### 修复
- 修复 `user.player.Option.java` 文件中的乱码问题
- 调整 `Option.java` 文件的注释格式和代码布局
- 解决项目编译过程中的循环依赖问题

### 重构
- 修改 `stg.game.obj.Obj` 类，移除对 `GameCanvas` 的依赖，简化构造函数
- 更新 `stg.game.player.Player` 类，使其符合新的 `Obj` 构造函数签名

### 优化
- 简化核心类之间的依赖关系，提高代码可维护性
- 改进类的设计，减少耦合度

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