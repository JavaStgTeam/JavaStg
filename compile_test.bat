@echo off

set SRC_DIR=src
set BIN_DIR=bin

REM 创建输出目录
if not exist %BIN_DIR% mkdir %BIN_DIR%

REM 编译核心类
javac -d %BIN_DIR% ^
%SRC_DIR%\stg\game\stage\StageGroup.java ^
%SRC_DIR%\stg\game\stage\Stage.java ^
%SRC_DIR%\stg\game\stage\StageGroupManager.java ^
%SRC_DIR%\stg\game\stage\StageGroupInfo.java ^
%SRC_DIR%\stg\game\util\AnnotationScanner.java ^
%SRC_DIR%\user\stageGroup\CustomStageGroup.java ^
%SRC_DIR%\user\stageGroup\__MountainPathStageGroup.java ^
%SRC_DIR%\user\stage\TestStage.java ^
%SRC_DIR%\user\stage\__MountainPathStage.java ^
%SRC_DIR%\stg\game\ui\GameCanvas.java ^
%SRC_DIR%\stg\game\GameWorld.java ^
%SRC_DIR%\stg\game\GameLoop.java ^
%SRC_DIR%\stg\game\GameRenderer.java ^
%SRC_DIR%\stg\game\GameStateManager.java ^
%SRC_DIR%\stg\game\InputHandler.java ^
%SRC_DIR%\stg\game\CollisionSystem.java ^
%SRC_DIR%\stg\base\Window.java ^
%SRC_DIR%\stg\base\KeyStateProvider.java ^
%SRC_DIR%\stg\base\VirtualKeyboardPanel.java ^
%SRC_DIR%\stg\game\player\Player.java ^
%SRC_DIR%\stg\game\enemy\Enemy.java ^
%SRC_DIR%\stg\game\bullet\Bullet.java ^
%SRC_DIR%\stg\game\item\Item.java ^
%SRC_DIR%\stg\game\laser\Laser.java ^
%SRC_DIR%\stg\util\EventBus.java ^
%SRC_DIR%\stg\util\ResourceManager.java ^
%SRC_DIR%\stg\util\math\Vector2.java ^
%SRC_DIR%\stg\util\math\MathUtils.java ^
%SRC_DIR%\stg\util\math\RandomGenerator.java ^
%SRC_DIR%\stg\util\RenderUtils.java ^
%SRC_DIR%\stg\util\AudioManager.java ^
%SRC_DIR%\stg\util\CoordinateSystem.java ^
TestStageGroupDiscovery.java

REM 检查编译是否成功
if %errorlevel% equ 0 (
    echo 编译成功！
    echo 运行测试...
    java -cp %BIN_DIR% TestStageGroupDiscovery
) else (
    echo 编译失败！
    pause
)
