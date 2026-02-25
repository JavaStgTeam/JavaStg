package stg.render;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import stg.base.KeyStateProvider;
import stg.entity.player.Player;
import user.player.DefaultPlayer;

/**
 * 玩家选择面板
 * 显示可用的玩家角色供玩家选择
 * @since 2026-02-26
 * @author JavaSTG Team
 */
public class PlayerSelectPanel extends Panel {
    private static final Color BG_COLOR = new Color(10, 10, 20);
    private static final Color SELECTED_COLOR = new Color(255, 200, 100);
    private static final Color UNSELECTED_COLOR = new Color(200, 200, 200);

    private int selectedIndex = 0;
    private List<PlayerInfo> playerInfos;
    private KeyStateProvider keyStateProvider;
    private PlayerSelectCallback callback;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean zPressed = false;
    private boolean xPressed = false;

    public interface PlayerSelectCallback {
        void onPlayerSelected(Player player);
        void onBack();
    }

    /**
     * 重置按键状态
     */
    public void resetKeyStates() {
        upPressed = false;
        downPressed = false;
        zPressed = false;
        xPressed = false;
    }

    /**
     * 玩家信息类
     */
    private static class PlayerInfo {
        private String name;
        private String description;
        private Player player;

        public PlayerInfo(String name, String description, Player player) {
            this.name = name;
            this.description = description;
            this.player = player;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Player getPlayer() {
            return player;
        }
    }

    /**
     * 构造函数
     * @param x 面板X坐标
     * @param y 面板Y坐标
     * @param width 面板宽度
     * @param height 面板高度
     * @param callback 回调接口
     */
    public PlayerSelectPanel(int x, int y, int width, int height, PlayerSelectCallback callback) {
        super(x, y, width, height);
        setBackgroundColor(0.04f, 0.04f, 0.08f, 1.0f);
        this.callback = callback;
        loadPlayers();
    }

    /**
     * 加载玩家列表
     */
    private void loadPlayers() {
        playerInfos = new ArrayList<>();
        
        // 添加默认玩家
        DefaultPlayer defaultPlayer = new DefaultPlayer(0.0f, -200.0f);
        playerInfos.add(new PlayerInfo("默认玩家", "标准射击角色", defaultPlayer));
        
        // 这里可以添加更多玩家角色
    }

    /**
     * 设置按键状态提供者
     * @param keyStateProvider 按键状态提供者
     */
    public void setKeyStateProvider(KeyStateProvider keyStateProvider) {
        this.keyStateProvider = keyStateProvider;
    }

    /**
     * 处理键盘输入
     */
    public void handleInput() {
        if (keyStateProvider == null || playerInfos == null || playerInfos.isEmpty()) return;

        // 处理上下选择
        boolean currentUpPressed = keyStateProvider.isUpPressed();
        if (currentUpPressed && !upPressed) {
            selectedIndex = (selectedIndex - 1 + playerInfos.size()) % playerInfos.size();
            upPressed = true;
        } else if (!currentUpPressed) {
            upPressed = false;
        }

        boolean currentDownPressed = keyStateProvider.isDownPressed();
        if (currentDownPressed && !downPressed) {
            selectedIndex = (selectedIndex + 1) % playerInfos.size();
            downPressed = true;
        } else if (!currentDownPressed) {
            downPressed = false;
        }

        // 处理确认
        boolean currentZPressed = keyStateProvider.isZPressed();
        if (currentZPressed && !zPressed) {
            handleSelection();
            zPressed = true;
        } else if (!currentZPressed) {
            zPressed = false;
        }

        // 处理返回
        boolean currentXPressed = keyStateProvider.isXPressed();
        if (currentXPressed && !xPressed) {
            if (callback != null) {
                callback.onBack();
            }
            xPressed = true;
        } else if (!currentXPressed) {
            xPressed = false;
        }
    }

    /**
     * 处理选择
     */
    private void handleSelection() {
        if (playerInfos != null && !playerInfos.isEmpty() && selectedIndex < playerInfos.size()) {
            PlayerInfo selectedInfo = playerInfos.get(selectedIndex);
            if (callback != null) {
                callback.onPlayerSelected(selectedInfo.getPlayer());
            }
        }
    }

    /**
     * 渲染玩家选择面板
     * @param renderer 渲染器
     */
    @Override
    public void render(IRenderer renderer) {
        // 绘制背景
        drawBackground(renderer);

        // 绘制标题
        drawTitle(renderer);

        // 绘制玩家列表
        drawPlayers(renderer);

        // 绘制操作提示
        drawHints(renderer);
    }

    /**
     * 绘制背景
     * @param renderer 渲染器
     */
    private void drawBackground(IRenderer renderer) {
        // 不绘制背景，使用标题页面的背景
    }

    /**
     * 绘制标题
     * @param renderer 渲染器
     */
    private void drawTitle(IRenderer renderer) {
        // 获取字体管理器实例
        FontManager fontManager = FontManager.getInstance();
        Font titleFont = fontManager.getTitleFont();
        String title = "选择玩家";
        float titleX = getWidth() / 2 - 100;
        float titleY = getHeight() - getHeight() / 3;

        renderer.drawText(title, titleX, titleY, titleFont, Color.WHITE);
    }

    /**
     * 绘制玩家列表
     * @param renderer 渲染器
     */
    private void drawPlayers(IRenderer renderer) {
        if (playerInfos == null || playerInfos.isEmpty()) {
            // 绘制无玩家提示
            FontManager fontManager = FontManager.getInstance();
            Font menuFont = fontManager.getMenuFont();
            String message = "没有可用的玩家角色";
            float x = getWidth() / 2 - 120;
            float y = getHeight() / 2;
            renderer.drawText(message, x, y, menuFont, UNSELECTED_COLOR);
            return;
        }

        // 获取字体管理器实例
        FontManager fontManager = FontManager.getInstance();
        Font menuFont = fontManager.getMenuFont();
        Font descriptionFont = fontManager.getHintFont();

        for (int i = 0; i < playerInfos.size(); i++) {
            PlayerInfo playerInfo = playerInfos.get(i);
            String name = playerInfo.getName();
            String description = playerInfo.getDescription();
            float x = getWidth() / 2 - 100;
            float y = getHeight() / 2 - 40 + i * 60;

            if (i == selectedIndex) {
                // 绘制选中效果
                renderer.drawText(">", x - 30, y, menuFont, SELECTED_COLOR);
                renderer.drawText(name, x, y, menuFont, SELECTED_COLOR);
                renderer.drawText(description, x + 10, y + 20, descriptionFont, SELECTED_COLOR);
            } else {
                renderer.drawText(name, x, y, menuFont, UNSELECTED_COLOR);
                renderer.drawText(description, x + 10, y + 20, descriptionFont, UNSELECTED_COLOR);
            }
        }
    }

    /**
     * 绘制操作提示
     * @param renderer 渲染器
     */
    private void drawHints(IRenderer renderer) {
        // 获取字体管理器实例
        FontManager fontManager = FontManager.getInstance();
        Font hintFont = fontManager.getHintFont();
        String hint1 = "上下 选择玩家";
        String hint2 = "Z 确认  X 返回";

        float hintX = getWidth() / 2 - 120;
        float hintY1 = getHeight() - 80;
        float hintY2 = hintY1 - 20;

        renderer.drawText(hint1, hintX, hintY1, hintFont, Color.GRAY);
        renderer.drawText(hint2, hintX, hintY2, hintFont, Color.GRAY);
    }

    /**
     * 更新面板
     */
    public void update() {
        handleInput();
    }
}