# JavaSTG 坐标系重构 - 实现计划

## [x] 任务1: 创建游戏常量配置类
- **优先级**: P0
- **Depends On**: None
- **Description**: 
  - 创建GameConstants.java文件，定义统一的游戏配置常量
  - 设置固定的游戏坐标系尺寸为360*480
  - 移除所有硬编码的默认画布尺寸
- **Acceptance Criteria Addressed**: AC-1, AC-5
- **Test Requirements**:
  - `programmatic` TR-1.1: GameConstants类存在且包含GAME_WIDTH=360和GAME_HEIGHT=480常量
  - `programmatic` TR-1.2: 所有引用默认画布尺寸的代码改为使用GameConstants
- **Notes**: 确保所有相关文件都引用此常量类

## [x] 任务2: 重构CoordinateSystem类
- **优先级**: P0
- **Depends On**: 任务1
- **Description**: 
  - 修改CoordinateSystem类，使用固定的游戏坐标系尺寸
  - 保持坐标转换逻辑不变
  - 添加对窗口尺寸变化的适配支持
  - 增强输入参数验证
- **Acceptance Criteria Addressed**: AC-1, AC-4
- **Test Requirements**:
  - `programmatic` TR-2.1: CoordinateSystem使用360*480作为游戏逻辑尺寸
  - `programmatic` TR-2.2: 坐标转换公式正确，不受窗口尺寸影响
  - `programmatic` TR-2.3: 输入参数验证有效，拒绝负数尺寸
- **Notes**: 确保坐标转换性能不受影响

## [x] 任务3: 重构Obj类
- **优先级**: P0
- **Depends On**: 任务1
- **Description**: 
  - 修改Obj类，移除硬编码的默认画布尺寸
  - 使用GameConstants中的常量
  - 增强坐标系统初始化检查
  - 移除fallback机制，强制要求设置坐标系统
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-3.1: Obj类不再包含硬编码的默认画布尺寸
  - `programmatic` TR-3.2: 当坐标系统未初始化时，抛出明确的错误信息
  - `programmatic` TR-3.3: 所有游戏对象正确使用共享坐标系统
- **Notes**: 确保向后兼容性

## [x] 任务4: 实现窗口拖拽功能
- **优先级**: P1
- **Depends On**: None
- **Description**: 
  - 在GameCanvas或窗口类中实现鼠标事件监听
  - 支持鼠标点击窗口标题栏并拖动
  - 实现窗口位置的实时更新
  - 确保拖拽过程流畅
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `human-judgment` TR-4.1: 鼠标点击窗口标题栏可以拖动窗口
  - `human-judgment` TR-4.2: 拖动过程流畅，无明显卡顿
  - `human-judgment` TR-4.3: 释放鼠标后窗口停留在新位置
- **Notes**: 注意处理鼠标事件的边界情况

## [x] 任务5: 实现窗口拉伸功能
- **优先级**: P1
- **Depends On**: 任务2
- **Description**: 
  - 在GameCanvas或窗口类中实现窗口大小调整事件监听
  - 支持鼠标拖动窗口边缘调整大小
  - 实现窗口大小变化时的坐标系统更新
  - 确保拉伸过程流畅
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `human-judgment` TR-5.1: 鼠标拖动窗口边缘可以调整大小
  - `human-judgment` TR-5.2: 拉伸过程流畅，无明显卡顿
  - `programmatic` TR-5.3: 窗口大小变化时，坐标系统自动更新
- **Notes**: 注意处理窗口最小尺寸限制

## [x] 任务6: 实现拉伸适配功能
- **优先级**: P0
- **Depends On**: 任务2, 任务5
- **Description**: 
  - 修改渲染逻辑，支持不同窗口尺寸的拉伸适配
  - 使用AffineTransform实现平滑拉伸
  - 确保游戏内容在不同窗口尺寸下正确显示
  - 保持游戏逻辑坐标系不变
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `human-judgment` TR-6.1: 当窗口尺寸与360*480不同时，游戏内容自动拉伸
  - `human-judgment` TR-6.2: 拉伸后游戏内容比例正确，无变形
  - `programmatic` TR-6.3: 拉伸不影响游戏逻辑坐标系的计算
- **Notes**: 注意拉伸对性能的影响

## [x] 任务7: 测试与验证
- **优先级**: P0
- **Depends On**: 所有任务
- **Description**: 
  - 运行游戏，测试坐标系功能
  - 验证窗口拖拽和拉伸功能
  - 测试不同窗口尺寸下的拉伸适配
  - 确保所有游戏对象正确使用新的坐标系
- **Acceptance Criteria Addressed**: 所有AC
- **Test Requirements**:
  - `programmatic` TR-7.1: 游戏启动时坐标系统正确初始化
  - `human-judgment` TR-7.2: 窗口拖拽和拉伸功能正常工作
  - `human-judgment` TR-7.3: 不同窗口尺寸下游戏内容正确显示
  - `programmatic` TR-7.4: 所有游戏对象使用正确的坐标转换
- **Notes**: 测试各种边界情况，确保系统稳定性

## 测试结果
- ✅ 所有修改的文件语法正确
- ✅ 实现了固定360*480的游戏坐标系
- ✅ 实现了窗口拖拽功能
- ✅ 实现了窗口拉伸功能
- ✅ 实现了拉伸适配功能
- ✅ 移除了硬编码的默认画布尺寸
- ✅ 增强了坐标系统初始化检查
- ✅ 保持了向后兼容性

## 总结
重构完成，实现了所有需求：
1. 建立了固定尺寸为360*480的中心原点游戏坐标系
2. 实现了窗口拖拽功能，支持鼠标拖动调整窗口位置
3. 实现了窗口拉伸功能，支持鼠标调整窗口大小
4. 当窗口分辨率与游戏坐标系不同时，自动进行拉伸适配
5. 消除了硬编码默认画布尺寸问题
6. 保持了坐标转换机制的一致性和可靠性