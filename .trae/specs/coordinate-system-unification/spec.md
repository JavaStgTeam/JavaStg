# JavaSTG - 自动发现机制修复实现计划

## \[ ] Task 1: 创建 StageGroupInfo 注解

* **Priority**: P0

* **Depends On**: None

* **Description**:

  * 创建 `StageGroupInfo` 注解类，包含 name、description、difficulty 属性

  * 确保注解在运行时可访问

  * 添加必要的文档注释

* **Acceptance Criteria Addressed**: AC-1

* **Test Requirements**:

  * `human-judgement` TR-1.1: 注解定义正确，包含所有必要属性

  * `programmatic` TR-1.2: 注解能够正确应用到关卡组类上

* **Notes**: 注解应该定义在合适的包中，建议放在 `stg.game.stage` 包下

## \[ ] Task 2: 实现注解扫描工具类

* **Priority**: P0

* **Depends On**: Task 1

* **Description**:

  * 创建 `AnnotationScanner` 工具类，用于扫描指定包下的注解类

  * 实现基于 Java 反射的类路径扫描

  * 提供方法获取带有特定注解的类

* **Acceptance Criteria Addressed**: AC-2

* **Test Requirements**:

  * `programmatic` TR-2.1: 能够正确扫描指定包下的注解类

  * `programmatic` TR-2.2: 性能开销在可接受范围内（启动时间增加不超过 100ms）

* **Notes**: 需要处理类路径扫描的各种边缘情况，包括 JAR 包环境

## \[ ] Task 3: 修改 StageGroupManager 的发现机制

* **Priority**: P0

* **Depends On**: Task 2

* **Description**:

  * 修改 `discoverStageGroups` 方法，使用注解扫描替代文件系统扫描

  * 保持向后兼容性，确保未使用注解的关卡组仍能被发现

  * 改进错误处理和日志记录

* **Acceptance Criteria Addressed**: AC-2, AC-4, AC-5

* **Test Requirements**:

  * `programmatic` TR-3.1: 能够正确发现并加载使用注解标记的关卡组

  * `programmatic` TR-3.2: 能够继续发现并加载未使用注解的关卡组

  * `programmatic` TR-3.3: 错误处理正确，能够捕获并记录异常

* **Notes**: 需要确保修改后的代码在所有环境下都能正常工作

## \[ ] Task 4: 为现有关卡组添加注解

* **Priority**: P1

* **Depends On**: Task 1

* **Description**:

  * 为现有的 `CustomStageGroup` 和 `__MountainPathStageGroup` 添加 `@StageGroupInfo` 注解

  * 确保注解属性与构造函数参数一致

* **Acceptance Criteria Addressed**: AC-2

* **Test Requirements**:

  * `programmatic` TR-4.1: 现有关卡组能够通过注解扫描被发现

  * `programmatic` TR-4.2: 关卡组的属性（名称、描述、难度）正确加载

* **Notes**: 这是可选的，但建议添加以充分利用新的注解机制

## \[ ] Task 5: 测试和验证

* **Priority**: P0

* **Depends On**: Task 3

* **Description**:

  * 在 IDE 中测试关卡组发现功能

  * 构建 JAR 包并测试关卡组发现功能

  * 在不同操作系统下测试（如果可能）

  * 验证性能和稳定性

* **Acceptance Criteria Addressed**: AC-2, AC-3, AC-4, AC-5

* **Test Requirements**:

  * `programmatic` TR-5.1: IDE 环境下关卡组正确发现

  * `programmatic` TR-5.2: JAR 包环境下关卡组正确发现

  * `human-judgment` TR-5.3: 不同操作系统下表现一致

  * `programmatic` TR-5.4: 启动时间在可接受范围内

* **Notes**: 需要全面测试以确保修复的可靠性

## \[ ] Task 6: 文档更新

* **Priority**: P2

* **Depends On**: Task 5

* **Description**:

  * 更新相关文档，说明新的注解机制

  * 提供创建和使用注解标记关卡组的示例

  * 更新架构文档中的自动发现机制部分

* **Acceptance Criteria Addressed**: 无直接对应，属于维护性工作

* **Test Requirements**:

  * `human-judgment` TR-6.1: 文档内容清晰准确

  * `human-judgment` TR-6.2: 示例代码可正确执行

* **Notes**: 良好的文档有助于开发者理解和使用新的机制

