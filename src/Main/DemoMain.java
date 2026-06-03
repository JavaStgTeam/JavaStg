package Main;

import stg.base.Window;
import stg.stage.StageGroup;
import user.player.DefaultPlayer;
import user.stageGroup.__DemoStageGroup;

/**
 * 功能演示入口 - 跳过菜单直接展示已实现的主要功能
 * 
 * <h3>运行方式</h3>
 * <pre>java -cp "bin;lib/*" Main.DemoMain</pre>
 * 
 * <h3>演示流程（3个关卡自动切换）</h3>
 * <ol>
 *   <li><b>敌机展示</b>：__FairyEnemy → __MidFairyEnemy → Elf（纹理渲染+自动射击）</li>
 *   <li><b>激光与Boss展示</b>：9色放射状激光 + __StandbyBoss（3符卡）</li>
 *   <li><b>Boss符卡展示</b>：__MinorikoBoss（非符x2 + 符卡x2，完整符卡系统）</li>
 * </ol>
 * 
 * <h3>当前已实现的功能模块</h3>
 * <table>
 * <tr><th>模块</th><th>已实现类型</th></tr>
 * <tr><td>敌机</td><td>__FairyEnemy, __MidFairyEnemy, Elf</td></tr>
 * <tr><td>Boss</td><td>__StandbyBoss(3符卡), __MinorikoBoss(2非符+2符卡)</td></tr>
 * <tr><td>子弹</td><td>SimpleDownBullet</td></tr>
 * <tr><td>激光</td><td>TestLaser (9种LaserColor, 贴图渲染)</td></tr>
 * <tr><td>玩家</td><td>DefaultPlayer, __ReimuPlayer, __ShinySilvergunPlayer</td></tr>
 * </table>
 * 
 * @since 2026-06-03
 */
public class DemoMain {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║     JavaSTG 功能演示 Demo                ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  关卡1: 敌机展示 (Fairy/MidFairy/Elf)    ║");
        System.out.println("║  关卡2: 激光与Boss (9色激光+StandbyBoss) ║");
        System.out.println("║  关卡3: Boss符卡 (MinorikoBoss)          ║");
        System.out.println("╚══════════════════════════════════════════╝");

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("未捕获异常: " + throwable.getMessage());
            throwable.printStackTrace();
            System.exit(1);
        });

        try {
            Window window = new Window();
            System.out.println("Window initialized");

            StageGroup stageGroup = new __DemoStageGroup(window.getGameWorld());
            DefaultPlayer player = new DefaultPlayer(0.0f, -200.0f);

            window.startDirectGame(stageGroup, player);
        } catch (Exception e) {
            System.err.println("启动失败: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}