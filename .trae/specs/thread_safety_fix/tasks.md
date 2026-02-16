# JavaSTG 线程安全修复 - 实现计划

## [x] Task 1: 修复 GameWorld 集合并发修改问题
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 将 GameWorld 中的 ArrayList 替换为 CopyOnWriteArrayList
  - 修改迭代器遍历为增强 for 循环
  - 移除 Collections.unmodifiableList() 包装
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 验证多线程环境下无 ConcurrentModificationException
  - `programmatic` TR-1.2: 验证碰撞检测和实体管理功能正常
  - `programmatic` TR-1.3: 验证游戏帧率保持稳定
- **Notes**: 选择 CopyOnWriteArrayList 是因为游戏场景读多写少，适合这种集合的性能特点

## [x] Task 2: 修复 EventBus 内存泄漏问题
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 修改 EventBus 实现，添加 WeakHashMap 存储订阅者
  - 修改 subscribe() 方法，添加 subscriber 参数
  - 添加 unsubscribeAll() 方法
  - 在游戏对象销毁时调用 unsubscribeAll()
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-2.1: 验证长时间运行后内存使用稳定
  - `programmatic` TR-2.2: 验证对象被正确回收
  - `programmatic` TR-2.3: 验证事件发布和订阅功能正常
- **Notes**: 使用 WeakHashMap 可以自动清理失效订阅者，同时支持手动管理

## [x] Task 3: 完善 GameLoop 线程中断处理
- **Priority**: P1
- **Depends On**: None
- **Description**: 
  - 修改 GameLoop 类，添加线程中断处理
  - 确保线程安全退出
  - 优化线程状态管理
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-3.1: 验证线程能够正确响应中断
  - `programmatic` TR-3.2: 验证线程安全退出，无资源泄露
  - `programmatic` TR-3.3: 验证游戏循环正常启动和停止
- **Notes**: 确保线程中断处理的完整性，避免线程卡死

## [x] Task 4: 修复 GameCanvas 按键状态竞态条件
- **Priority**: P1
- **Depends On**: None
- **Description**: 
  - 分析 GameCanvas 按键状态管理
  - 添加适当的同步机制
  - 确保多线程环境下的安全访问
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `human-judgment` TR-4.1: 验证快速连续按键操作无异常
  - `programmatic` TR-4.2: 验证多线程环境下按键状态一致性
  - `programmatic` TR-4.3: 验证输入处理功能正常
- **Notes**: 可以使用 volatile 变量或同步块确保按键状态的线程安全

## [x] Task 5: 改进 StageGroupManager 单例模式
- **Priority**: P2
- **Depends On**: None
- **Description**: 
  - 评估 StageGroupManager 单例实现
  - 改进测试可行性
  - 确保线程安全
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-5.1: 验证单例模式线程安全
  - `programmatic` TR-5.2: 验证测试代码能够独立运行
  - `programmatic` TR-5.3: 验证 StageGroupManager 功能正常
- **Notes**: 考虑使用枚举单例或其他测试友好的实现方式

## [x] Task 6: 集成测试和性能验证
- **Priority**: P1
- **Depends On**: Task 1, Task 2, Task 3, Task 4, Task 5
- **Description**: 
  - 运行完整的游戏测试
  - 验证所有线程安全修复生效
  - 测试游戏性能和稳定性
  - 进行内存泄漏检测
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-3, AC-4, AC-5
- **Test Requirements**:
  - `programmatic` TR-6.1: 验证所有功能正常工作
  - `programmatic` TR-6.2: 验证内存使用稳定
  - `programmatic` TR-6.3: 验证游戏帧率保持在目标水平
  - `human-judgment` TR-6.4: 验证游戏体验流畅无异常
- **Notes**: 使用内存分析工具和性能测试工具进行全面验证

## [x] Task 7: 文档更新和代码清理
- **Priority**: P2
- **Depends On**: Task 6
- **Description**: 
  - 更新相关文档
  - 清理临时测试代码
  - 优化代码注释
  - 确保代码风格一致
- **Acceptance Criteria Addressed**: None
- **Test Requirements**:
  - `human-judgment` TR-7.1: 验证文档完整准确
  - `human-judgment` TR-7.2: 验证代码整洁规范
- **Notes**: 保持代码库的可维护性和可读性
