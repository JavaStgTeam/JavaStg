package stg.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import stg.base.KeyStateProvider;
import stg.service.render.IRenderer;
import stg.stage.StageGroup;
import stg.util.ResourceManager;

/**
 * 关卡组选择界面 - 允许玩家选择要挑战的关卡组
 * @since 2026-01-30
 */
public class StageGroupSelectPanel implements KeyStateProvider {
    private static final Color BG_COLOR = new Color(10, 10, 20);
    private static final Color SELECTED_COLOR = new Color(255, 200, 100);
    private static final Color UNSELECTED_COLOR = new Color(200, 200, 200);
    private static final Color LOCKED_COLOR = new Color(100, 100, 100);

    private int selectedIndex = 0;
    private List<StageGroup> stageGroups;
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
        void onStageGroupSelected(StageGroup stageGroup);
        void onBack();
    }

    private StageGroupSelectCallback callback;

    public StageGroupSelectPanel(StageGroupSelectCallback callback) {
        this.callback = callback;
        this.resourceManager = ResourceManager.getInstance();
        this.stageGroups = new ArrayList<>();
    }

    /**
     * 设置关卡组列表 - 用于更新可解锁的关卡组
     * @param groups 关卡组列表
     */
    public void setStageGroups(List<StageGroup> groups) {
        this.stageGroups = groups != null ? groups : new ArrayList<>();
        this.selectedIndex = Math.max(0, Math.min(selectedIndex, stageGroups.size() - 1));
    }

    /**
     * 添加关卡组 - 用于动态解锁新关卡组
     * @param group 关卡组
     */
    public void addStageGroup(StageGroup group) {
        if (group != null) {
            stageGroups.add(group);
        }
    }

    /**
     * 处理键盘输入
     * @param key 按键代码
     */
    public void handleKeyPress(int key) {
        switch (key) {
            case org.lwjgl.glfw.GLFW.GLFW_KEY_UP:
                selectedIndex = (selectedIndex - 1 + stageGroups.size()) % stageGroups.size();
                break;
            case org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN:
                selectedIndex = (selectedIndex + 1) % stageGroups.size();
                break;
            case org.lwjgl.glfw.GLFW.GLFW_KEY_Z:
            case org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER:
                if (!stageGroups.isEmpty() && stageGroups.get(selectedIndex).isUnlockable()) {
                    callback.onStageGroupSelected(stageGroups.get(selectedIndex));
                }
                break;
            case org.lwjgl.glfw.GLFW.GLFW_KEY_X:
            case org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE:
                callback.onBack();
                break;
        }
    }

    /**
     * 使用OpenGL渲染器绘制关卡组选择界面
     * @param renderer OpenGL渲染器
     * @param width 窗口宽度
     * @param height 窗口高度
     */
    public void render(IRenderer renderer, int width, int height) {
        // 绘制背景
        renderer.setColor(BG_COLOR);
        renderer.drawRect(0, 0, width, height, BG_COLOR);

        // 绘制标题
        Font titleFont = new Font("Microsoft YaHei", Font.BOLD, 48);
        renderer.setFont(titleFont);
        renderer.setColor(Color.WHITE);
        String title = "选择关卡组";
        // 简化处理，假设文本宽度
        int titleWidth = 250;
        renderer.drawText(title, width / 2 - titleWidth / 2, 100, titleFont, Color.WHITE);

        // 绘制关卡组列表
        drawStageGroups(renderer, width, height);

        // 绘制操作提示
        drawControls(renderer, width, height);
    }

    private void drawStageGroups(IRenderer renderer, int width, int height) {
        if (stageGroups.isEmpty()) {
            Font msgFont = new Font("Microsoft YaHei", Font.PLAIN, 24);
            renderer.setFont(msgFont);
            renderer.setColor(Color.GRAY);
            String noGroupsMsg = "暂无可用关卡组";
            // 简化处理，假设文本宽度
            int msgWidth = 150;
            renderer.drawText(noGroupsMsg, width / 2 - msgWidth / 2, height / 2, msgFont, Color.GRAY);
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
                renderer.setColor(new Color(255, 200, 100, 50));
                renderer.drawRect(width / 2 - 300, y - 15, 600, 50, new Color(255, 200, 100, 50));
                renderer.setColor(SELECTED_COLOR);
            } else if (!isUnlockable) {
                renderer.setColor(LOCKED_COLOR);
            } else {
                renderer.setColor(UNSELECTED_COLOR);
            }

            // 绘制关卡组信息
            if (isUnlockable || isSelected) {
                Font nameFont = new Font("Microsoft YaHei", Font.BOLD, 20);
                renderer.setFont(nameFont);
                String groupName = group.getDisplayName();
                renderer.drawText(groupName, width / 2 - 280, y + 10, nameFont, isSelected ? SELECTED_COLOR : UNSELECTED_COLOR);

                // 绘制难度
                Font difficultyFont = new Font("Microsoft YaHei", Font.PLAIN, 16);
                renderer.setFont(difficultyFont);
                String difficulty = "难度: " + group.getDifficulty().getDisplayName();
                renderer.drawText(difficulty, width / 2 - 280, y + 35, difficultyFont, isSelected ? SELECTED_COLOR : UNSELECTED_COLOR);

                // 绘制描述
                Font descFont = new Font("Microsoft YaHei", Font.PLAIN, 14);
                renderer.setFont(descFont);
                String description = group.getDescription();
                if (description.length() > 30) {
                    description = description.substring(0, 30) + "...";
                }
                renderer.drawText(description, width / 2, y + 10, descFont, isSelected ? SELECTED_COLOR : UNSELECTED_COLOR);

                // 绘制关卡数量
                int stageCount = group.getStageCount();
                String stageInfo = "关卡数: " + stageCount;
                renderer.drawText(stageInfo, width / 2, y + 35, descFont, isSelected ? SELECTED_COLOR : UNSELECTED_COLOR);
            }

            // 绘制选中标记
            if (isSelected) {
                Font selectFont = new Font("Microsoft YaHei", Font.BOLD, 24);
                renderer.setFont(selectFont);
                renderer.drawText(">", width / 2 - 310, y + 15, selectFont, SELECTED_COLOR);
            }

            // 绘制锁定标记
            if (!isUnlockable) {
                Font lockFont = new Font("Microsoft YaHei", Font.BOLD, 16);
                renderer.setFont(lockFont);
                renderer.drawText("[锁定]", width - 150, y + 15, lockFont, LOCKED_COLOR);
            }
        }
    }

    private void drawControls(IRenderer renderer, int width, int height) {
        Font hintFont = new Font("Microsoft YaHei", Font.PLAIN, 14);
        renderer.setFont(hintFont);
        renderer.setColor(Color.GRAY);
        renderer.drawText("上下 选择关卡组", width / 2 - 100, height - 60, hintFont, Color.GRAY);
        renderer.drawText("Z/Enter 确认选择", width / 2 - 100, height - 40, hintFont, Color.GRAY);
        renderer.drawText("X/ESC  返回", width / 2 - 100, height - 20, hintFont, Color.GRAY);
    }

    /**
     * 更新动画帧
     */
    public void update() {
        animationFrame++;
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
}
