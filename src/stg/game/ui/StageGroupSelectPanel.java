package stg.game.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import stg.base.KeyStateProvider;
import stg.game.stage.StageGroup;
import stg.util.ResourceManager;
import user.player.PlayerType;

/**
 * 关卡组选择界面 - 允许玩家选择要挑战的关卡组
 * 将类移动到stg.game.ui包内，保持与其他UI组件的一致性
 * @Time 2026-01-30 实现KeyStateProvider以支持虚拟键盘 * @Time 2026-02-01 添加背景图片支持\n\t * @since 2026-01-30
 */
public class StageGroupSelectPanel extends JPanel implements KeyStateProvider {
    private static final long serialVersionUID = 1L;
    private static final Color BG_COLOR = new Color(10, 10, 20);
    private static final Color SELECTED_COLOR = new Color(255, 200, 100);
    private static final Color UNSELECTED_COLOR = new Color(200, 200, 200);
    private static final Color LOCKED_COLOR = new Color(100, 100, 100);

    private int selectedIndex = 0;
    private List<StageGroup> stageGroups;
    private PlayerType selectedPlayerType;
    private Timer animationTimer;
    private int animationFrame = 0;
    private ResourceManager resourceManager;

    // 按键状态跟踪 - 供虚拟键盘使用
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean zPressed = false;
    private boolean shiftPressed = false;
    private boolean xPressed = false;

    public interface StageGroupSelectCallback {
        void onStageGroupSelected(StageGroup stageGroup, PlayerType playerType);
        void onBack();
    }

    private StageGroupSelectCallback callback;

    public StageGroupSelectPanel(StageGroupSelectCallback callback, PlayerType playerType) {
        this.callback = callback;
        this.selectedPlayerType = playerType;
        this.resourceManager = ResourceManager.getInstance();
        this.stageGroups = new ArrayList<>();
        
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        animationTimer = new Timer(16, e -> {
            animationFrame++;
            repaint();
        });
        animationTimer.start();
    }

    /**
     * 设置关卡组列表 - 用于更新可解锁的关卡组
     * 将方法移动到stg.game.ui包内，保持与其他UI组件的一致性
     * @Time 2026-02-01 实现KeyStateProvider以支持虚拟键盘 * @Time 2026-02-02 添加背景图片支持\n\t * @since 2026-02-01
     * @param groups 关卡组列表     */
    public void setStageGroups(List<StageGroup> groups) {
        this.stageGroups = groups != null ? groups : new ArrayList<>();
        this.selectedIndex = Math.max(0, Math.min(selectedIndex, stageGroups.size() - 1));
        repaint();
    }

    /**
     * 添加关卡组 - 用于动态解锁新关卡组
     * 将方法移动到stg.game.ui包内，保持与其他UI组件的一致性
     * @Time 2026-02-02 实现KeyStateProvider以支持虚拟键盘 * @Time 2026-02-03 添加背景图片支持\n\t * @since 2026-02-02
     * @param group 关卡组     */
    public void addStageGroup(StageGroup group) {
        if (group != null) {
            stageGroups.add(group);
            repaint();
        }
    }

    private void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                selectedIndex = (selectedIndex - 1 + stageGroups.size()) % stageGroups.size();
                repaint();
                break;
            case KeyEvent.VK_DOWN:
                selectedIndex = (selectedIndex + 1) % stageGroups.size();
                repaint();
                break;
            case KeyEvent.VK_Z:
            case KeyEvent.VK_ENTER:
                if (!stageGroups.isEmpty() && stageGroups.get(selectedIndex).isUnlockable()) {
                    callback.onStageGroupSelected(stageGroups.get(selectedIndex), selectedPlayerType);
                }
                break;
            case KeyEvent.VK_ESCAPE:
                callback.onBack();
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        // 绘制背景
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0, 0, width, height);

        // 绘制标题
        stg.util.RenderUtils.enableAntiAliasing(g2d);
        g2d.setFont(new Font("Microsoft YaHei", Font.BOLD, 48));
        g2d.setColor(Color.WHITE);
        String title = "选择关卡组";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, width / 2 - titleWidth / 2, 100);

        // 绘制玩家信息
        g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 20));
        g2d.setColor(Color.LIGHT_GRAY);
        String playerInfo = "当前自机: " + selectedPlayerType.name();
        int playerInfoWidth = g2d.getFontMetrics().stringWidth(playerInfo);
        g2d.drawString(playerInfo, width / 2 - playerInfoWidth / 2, 150);

        // 绘制关卡组列表
        g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 24));
        g2d.setColor(Color.WHITE);
        drawStageGroups(g2d, width, height);

        // 绘制操作提示
        drawControls(g2d, width, height);
    }

    private void drawStageGroups(Graphics2D g2d, int width, int height) {
        if (stageGroups.isEmpty()) {
            g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 24));
            g2d.setColor(Color.GRAY);
            String noGroupsMsg = "暂无可用关卡组";
            int msgWidth = g2d.getFontMetrics().stringWidth(noGroupsMsg);
            g2d.drawString(noGroupsMsg, width / 2 - msgWidth / 2, height / 2);
            return;
        }

        int startY = 200;
        int itemHeight = 60;
        int maxVisibleItems = Math.min(6, stageGroups.size());
        int scrollOffset = Math.max(0, selectedIndex - maxVisibleItems / 2);
        scrollOffset = Math.min(scrollOffset, stageGroups.size() - maxVisibleItems);

        for (int i = scrollOffset; i < Math.min(scrollOffset + maxVisibleItems, stageGroups.size()); i++) {
            StageGroup group = stageGroups.get(i);
            int y = startY + (i - scrollOffset) * itemHeight;
            boolean isSelected = (i == selectedIndex);
            boolean isUnlockable = group.isUnlockable();

            // 绘制背景矩形
            if (isSelected) {
                g2d.setColor(new Color(255, 200, 100, 50));
                g2d.fillRoundRect(width / 2 - 300, y - 15, 600, 50, 10, 10);
                g2d.setColor(SELECTED_COLOR);
            } else if (!isUnlockable) {
                g2d.setColor(LOCKED_COLOR);
            } else {
                g2d.setColor(UNSELECTED_COLOR);
            }

            // 绘制关卡组信息
            if (isUnlockable || isSelected) {
                g2d.setFont(new Font("Microsoft YaHei", Font.BOLD, 20));
                String groupName = group.getDisplayName();
                int nameWidth = g2d.getFontMetrics().stringWidth(groupName);
                g2d.drawString(groupName, width / 2 - 280, y + 10);

                // 绘制难度
                g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
                String difficulty = "难度: " + group.getDifficulty().getDisplayName();
                g2d.drawString(difficulty, width / 2 - 280, y + 35);

                // 绘制描述
                g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
                String description = group.getDescription();
                if (description.length() > 30) {
                    description = description.substring(0, 30) + "...";
                }
                g2d.drawString(description, width / 2, y + 10);

                // 绘制关卡数量
                int stageCount = group.getStageCount();
                String stageInfo = "关卡数: " + stageCount;
                g2d.drawString(stageInfo, width / 2, y + 35);
            }

            // 绘制选中标记
            if (isSelected) {
                g2d.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
                g2d.drawString(">", width / 2 - 310, y + 15);
            }



            // 绘制锁定标记
            if (!isUnlockable) {
                g2d.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
                g2d.drawString("[锁定]", width - 150, y + 15);
            }
        }
    }

    private void drawControls(Graphics2D g2d, int width, int height) {

        g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        g2d.setColor(Color.GRAY);
        g2d.drawString("上下 选择关卡组", width / 2 - 100, height - 60);
        g2d.drawString("Z/Enter 确认选择", width / 2 - 100, height - 40);
        g2d.drawString("ESC  返回", width / 2 - 100, height - 20);
    }

    // 虚拟键盘接口实现
    @Override
    public boolean isUpPressed() { return upPressed; }
    @Override
    public boolean isDownPressed() { return downPressed; }
    @Override
    public boolean isLeftPressed() { return leftPressed; }
    @Override
    public boolean isRightPressed() { return rightPressed; }
    @Override
    public boolean isZPressed() { return zPressed; }
    @Override
    public boolean isShiftPressed() { return shiftPressed; }
    @Override
    public boolean isXPressed() { return xPressed; }

    public void setUpPressed(boolean upPressed) { this.upPressed = upPressed; }
    public void setDownPressed(boolean downPressed) { this.downPressed = downPressed; }
    public void setLeftPressed(boolean leftPressed) { this.leftPressed = leftPressed; }
    public void setRightPressed(boolean rightPressed) { this.rightPressed = rightPressed; }
    public void setZPressed(boolean zPressed) { this.zPressed = zPressed; }
    public void setShiftPressed(boolean shiftPressed) { this.shiftPressed = shiftPressed; }
    public void setXPressed(boolean xPressed) { this.xPressed = xPressed; }

    /**
     * 停止动画计时
     */
    public void stopAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }
}

