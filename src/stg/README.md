# stg 包结构说明

## 包结构总览

```
stg/
├── base/          # 基础组件包
├── core/          # 核心逻辑包
├── demo/          # 演示代码包
├── entity/        # 游戏实体包
├── event/         # 事件系统包
├── renderer/      # 渲染系统包
├── stage/         # 关卡系统包
├── ui/            # 用户界面包
├── util/          # 工具类包
└── README.md      # 包结构说明
```

## 各包功能说明

### base 包
- **功能**：提供游戏基础组件和接口
- **主要类**：Window、VirtualKeyboardPanel、KeyStateProvider
- **说明**：包含窗口管理、键盘输入等基础功能

### core 包
- **功能**：游戏核心逻辑实现
- **主要类**：GameWorld、CollisionSystem、GameStateManager、GameLoop
- **说明**：包含游戏世界管理、碰撞检测、状态管理等核心功能

### demo 包
- **功能**：演示和测试代码
- **主要类**：ResourceDemoWindow、ResourceTest
- **说明**：用于展示游戏功能和测试资源加载

### entity 包
- **功能**：游戏实体管理
- **子包**：base、bullet、enemy、item、laser、player
- **说明**：包含所有游戏实体类，如敌人、玩家、子弹等

### event 包
- **功能**：游戏事件系统
- **主要类**：各种事件类（如BulletFiredEvent、EnemyDestroyedEvent等）
- **说明**：用于游戏内事件的分发和处理

### renderer 包
- **功能**：游戏渲染系统
- **主要类**：GameRenderer
- **说明**：负责游戏画面的渲染

### stage 包
- **功能**：关卡系统
- **主要类**：Stage、StageGroup、StageGroupManager等
- **说明**：管理游戏关卡和关卡组

### ui 包
- **功能**：用户界面系统
- **主要类**：GameCanvas、TitleScreen、GameStatusPanel等
- **说明**：包含游戏界面元素和交互逻辑

### util 包
- **功能**：工具类集合
- **子包**：math
- **说明**：提供各种工具类，如资源管理、数学工具等

## 设计原则

1. **功能分离**：每个包负责特定的功能领域
2. **层次清晰**：从基础组件到核心逻辑，层次分明
3. **扩展性强**：便于添加新功能和修改现有功能
4. **可读性高**：包结构清晰，便于理解和维护

## 使用说明

- 核心游戏逻辑使用 `stg.core` 包中的类
- 游戏实体使用 `stg.entity` 包中的类
- 关卡相关使用 `stg.stage` 包中的类
- 界面相关使用 `stg.ui` 包中的类
- 工具类使用 `stg.util` 包中的类

## 开发建议

- 新增功能时，根据功能性质选择合适的包
- 遵循现有的包结构和命名规范
- 保持类的职责单一，避免过大的类
- 为新增的类添加适当的注释和文档