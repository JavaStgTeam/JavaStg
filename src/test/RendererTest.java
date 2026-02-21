package test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import stg.ui.GameCanvas;
import stg.service.render.IRenderer;
import stg.service.render.Java2DRenderer;

public class RendererTest {
    public static void main(String[] args) {
        // 创建窗口
        JFrame frame = new JFrame("Renderer Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        
        // 创建游戏画布
        GameCanvas canvas = new GameCanvas();
        frame.add(canvas);
        
        // 初始化玩家
        canvas.setPlayer(400, 500);
        
        // 测试渲染器
        IRenderer renderer = canvas.getRenderer();
        System.out.println("当前渲染器: " + renderer.getClass().getName());
        System.out.println("是否使用OpenGL: " + canvas.isUsingOpenGL());
        
        // 测试Java2DRenderer直接使用
        testJava2DRenderer();
        
        // 显示窗口
        frame.setVisible(true);
        
        // 启动游戏循环
        Thread gameLoop = new Thread(() -> {
            while (true) {
                canvas.update();
                canvas.repaint();
                try {
                    Thread.sleep(16); // 约60fps
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameLoop.start();
        
        // 添加窗口关闭监听器
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gameLoop.interrupt();
            }
        });
    }
    
    private static void testJava2DRenderer() {
        // 测试Java2DRenderer的基本功能
        Java2DRenderer renderer = new Java2DRenderer();
        renderer.init();
        
        System.out.println("Java2DRenderer初始化: " + renderer.isInitialized());
        
        // 创建一个缓冲图像进行测试
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // 设置渲染器的图形上下文
        renderer.setGraphics(g2d);
        
        // 测试绘制基本图形
        renderer.setColor(Color.RED);
        renderer.drawCircle(100, 100, 50, null);
        
        renderer.setColor(Color.BLUE);
        renderer.drawRect(50, 50, 100, 100, null);
        
        renderer.setColor(Color.GREEN);
        renderer.drawLine(0, 0, 200, 200, null);
        
        renderer.setColor(Color.YELLOW);
        renderer.drawText("Test", 50, 150, new Font("Arial", Font.BOLD, 24), null);
        
        g2d.dispose();
        System.out.println("Java2DRenderer功能测试完成");
        
        // 清理
        renderer.cleanup();
    }
}