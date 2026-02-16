# JavaSTG - 自动发现机制修复产品需求文档

## Overview
- **Summary**: 修复 JavaSTG 游戏引擎中关卡组自动发现机制的可靠性问题，将基于文件系统扫描的不可靠实现替换为基于注解的更可靠方案。
- **Purpose**: 解决自动发现机制在不同环境下可能失败的问题，确保关卡组能够在任何运行环境中稳定加载。
- **Target Users**: 游戏开发者和关卡设计师，确保他们创建的关卡组能够被系统正确发现和加载。

## Goals
- 解决自动发现机制依赖文件系统扫描的问题
- 实现基于注解的关卡组自动发现机制
- 确保在不同运行环境（IDE、JAR包、不同操作系统）下的一致性
- 保持与现有代码的兼容性
- 提高系统的可测试性和可维护性

## Non-Goals (Out of Scope)
- 不修改关卡组的核心功能和生命周期
- 不改变现有关卡组的创建方式
- 不影响其他系统模块的功能
- 不引入新的外部依赖

## Background & Context
- 现有的自动发现机制通过扫描文件系统来查找关卡组类，这种方法在不同环境下表现不一致
- 在 JAR 包中运行时，文件系统扫描可能失败，导致关卡组无法被发现
- 不同操作系统的文件路径处理差异也可能导致扫描失败
- 基于注解的方案可以避免这些问题，提供更可靠的发现机制

## Functional Requirements
- **FR-1**: 实现 `StageGroupInfo` 注解，用于标记关卡组类
- **FR-2**: 修改 `StageGroupManager` 的 `discoverStageGroups` 方法，使用注解扫描替代文件系统扫描
- **FR-3**: 确保新的发现机制能够正确处理所有现有关卡组
- **FR-4**: 提供详细的错误处理和日志记录
- **FR-5**: 保持与现有代码的向后兼容性

## Non-Functional Requirements
- **NFR-1**: 可靠性 - 新的发现机制必须在所有运行环境中稳定工作
- **NFR-2**: 性能 - 注解扫描的性能开销必须合理，不影响游戏启动时间
- **NFR-3**: 可维护性 - 代码必须清晰易读，有良好的注释
- **NFR-4**: 可测试性 - 新的实现必须易于单元测试

## Constraints
- **Technical**: 必须使用 Java 反射 API 实现注解扫描，不引入外部依赖
- **Business**: 修复必须在 8-12 小时内完成
- **Dependencies**: 依赖 Java 核心库中的反射和注解功能

## Assumptions
- 所有关卡组类都位于 `user.stageGroup` 包及其子包中
- 关卡组类都继承自 `StageGroup` 基类
- 关卡组类都有一个接受 `GameCanvas` 参数的构造函数

## Acceptance Criteria

### AC-1: 注解定义
- **Given**: 开发环境已设置
- **When**: 检查代码库
- **Then**: 存在 `StageGroupInfo` 注解，包含必要的属性（name、description、difficulty）
- **Verification**: `human-judgment`

### AC-2: 注解扫描实现
- **Given**: 注解扫描机制已实现
- **When**: 运行游戏
- **Then**: 系统能够正确发现并加载所有使用 `@StageGroupInfo` 注解标记的关卡组
- **Verification**: `programmatic`

### AC-3: 环境兼容性
- **Given**: 在不同环境下运行（IDE、JAR包、不同操作系统）
- **When**: 启动游戏
- **Then**: 关卡组能够在所有环境中被正确发现和加载
- **Verification**: `human-judgment`

### AC-4: 向后兼容性
- **Given**: 现有关卡组代码未修改
- **When**: 运行游戏
- **Then**: 系统能够继续发现和加载现有关卡组
- **Verification**: `programmatic`

### AC-5: 错误处理
- **Given**: 存在错误的关卡组配置
- **When**: 启动游戏
- **Then**: 系统能够捕获并记录错误，同时继续加载其他有效的关卡组
- **Verification**: `programmatic`

## Open Questions
- [ ] 是否需要支持在多个包中扫描关卡组？
- [ ] 注解扫描的性能开销是否在可接受范围内？
- [ ] 是否需要为现有关卡组添加注解，还是保持向后兼容？