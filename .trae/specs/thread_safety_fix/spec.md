# JavaSTG 线程安全修复 - 产品需求文档

## Overview
- **Summary**: 修复 JavaSTG 游戏引擎中的线程安全问题，包括并发修改异常、内存泄漏和线程中断处理等问题，确保游戏稳定运行和资源正确管理。
- **Purpose**: 解决游戏引擎中的线程安全隐患，提升游戏稳定性，防止游戏崩溃和内存泄漏，改善长期运行性能。
- **Target Users**: 游戏开发者和玩家，确保游戏在各种场景下都能稳定运行。

## Goals
- 修复 GameWorld 集合并发修改问题，防止 ConcurrentModificationException
- 解决 EventBus 内存泄漏问题，确保对象被正确回收
- 完善 GameLoop 线程中断处理，确保线程安全退出
- 优化 GameCanvas 按键状态处理，防止竞态条件
- 评估并改进 StageGroupManager 单例模式实现

## Non-Goals (Out of Scope)
- 重构整个游戏引擎架构
- 修改游戏核心逻辑和玩法
- 优化图形渲染性能
- 增加新功能或特性

## Background & Context
JavaSTG 游戏引擎当前存在多个线程安全问题，主要包括：
1. GameWorld 使用非线程安全的 ArrayList，在多线程环境下可能导致并发修改异常
2. EventBus 使用强引用存储订阅者，可能导致内存泄漏
3. GameLoop 线程中断处理不完整，可能导致线程无法正确退出
4. GameCanvas 按键状态存在竞态条件风险
5. StageGroupManager 单例模式存在测试困难风险

这些问题可能导致游戏崩溃、内存泄漏和性能下降，严重影响游戏体验。

## Functional Requirements
- **FR-1**: GameWorld 集合并发安全
  - 替换非线程安全的 ArrayList 为线程安全的集合
  - 确保多线程环境下的安全访问
  - 防止 ConcurrentModificationException

- **FR-2**: EventBus 内存泄漏修复
  - 使用弱引用或其他机制防止内存泄漏
  - 确保对象被正确回收
  - 提供自动或半自动的订阅管理

- **FR-3**: GameLoop 线程安全
  - 完善线程中断处理
  - 确保线程正确退出
  - 避免资源泄露

- **FR-4**: GameCanvas 输入处理安全
  - 修复按键状态竞态条件
  - 确保输入处理的线程安全

- **FR-5**: StageGroupManager 单例改进
  - 评估单例模式实现
  - 提高测试可行性
  - 确保线程安全

## Non-Functional Requirements
- **NFR-1**: 性能要求
  - 修复后游戏帧率不应明显下降
  - 内存使用应保持稳定
  - 线程安全操作不应成为性能瓶颈

- **NFR-2**: 兼容性要求
  - 修复应保持向后兼容
  - 现有代码应无需大量修改
  - API 接口应保持稳定

- **NFR-3**: 可靠性要求
  - 修复后游戏不应因线程安全问题崩溃
  - 内存泄漏应得到有效解决
  - 长时间运行应保持稳定

## Constraints
- **Technical**: Java 语言环境，现有代码架构
- **Business**: 最小化代码改动，确保稳定性
- **Dependencies**: 无外部依赖，使用 Java 标准库

## Assumptions
- 游戏引擎运行在多线程环境下（主线程、EDT 线程、GameLoop 线程）
- 读操作远多于写操作的场景
- 实体数量在可控范围内（几百到几千）
- 目标帧率为 60 FPS

## Acceptance Criteria

### AC-1: GameWorld 线程安全
- **Given**: 多线程环境下运行游戏
- **When**: 同时进行实体添加/删除和碰撞检测
- **Then**: 游戏不应抛出 ConcurrentModificationException
- **Verification**: `programmatic`
- **Notes**: 验证在高并发场景下的稳定性

### AC-2: EventBus 内存泄漏修复
- **Given**: 长时间运行游戏并创建/销毁大量对象
- **When**: 游戏对象订阅事件后被销毁
- **Then**: 内存使用应保持稳定，无明显增长
- **Verification**: `programmatic`
- **Notes**: 使用内存分析工具验证对象回收情况

### AC-3: GameLoop 线程正确退出
- **Given**: 游戏结束或窗口关闭
- **When**: 线程被中断
- **Then**: 线程应安全退出，无资源泄露
- **Verification**: `programmatic`
- **Notes**: 验证线程状态和资源释放

### AC-4: GameCanvas 输入处理稳定
- **Given**: 快速连续按键操作
- **When**: 多个线程同时访问按键状态
- **Then**: 输入处理应准确无误，无竞态条件
- **Verification**: `human-judgment`
- **Notes**: 手动测试快速输入场景

### AC-5: StageGroupManager 可测试性
- **Given**: 编写单元测试
- **When**: 测试 StageGroupManager 功能
- **Then**: 测试应能够独立运行，无单例依赖问题
- **Verification**: `programmatic`
- **Notes**: 确保测试覆盖各种场景

## Open Questions
- [ ] GameWorld 集合选择：CopyOnWriteArrayList 还是其他线程安全集合？
- [ ] EventBus 实现方式：WeakReference 还是 WeakHashMap？
- [ ] GameLoop 线程管理：如何确保线程安全退出？
- [ ] GameCanvas 按键状态：如何避免竞态条件？
- [ ] StageGroupManager 单例：是否需要修改实现方式？
