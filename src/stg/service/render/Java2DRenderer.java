package stg.service.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

public class Java2DRenderer implements IRenderer {
    private Graphics2D graphics;
    private boolean initialized;
    private Color currentColor;
    private Font currentFont;
    
    @Override
    public void init() {
        initialized = true;
        currentColor = Color.WHITE;
        currentFont = new Font("Monospace", Font.PLAIN, 12);
    }
    
    @Override
    public void beginFrame() {
        // Java2D不需要特殊的帧开始处理
    }
    
    @Override
    public void endFrame() {
        // Java2D不需要特殊的帧结束处理
    }
    
    @Override
    public void drawCircle(float x, float y, float radius, Color color) {
        if (graphics == null) return;
        
        Color oldColor = graphics.getColor();
        graphics.setColor(color != null ? color : currentColor);
        
        int intX = Math.round(x - radius);
        int intY = Math.round(y - radius);
        int diameter = Math.round(radius * 2);
        
        graphics.fillOval(intX, intY, diameter, diameter);
        graphics.setColor(oldColor);
    }
    
    @Override
    public void drawRect(float x, float y, float width, float height, Color color) {
        if (graphics == null) return;
        
        Color oldColor = graphics.getColor();
        graphics.setColor(color != null ? color : currentColor);
        
        graphics.fillRect(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
        graphics.setColor(oldColor);
    }
    
    @Override
    public void drawLine(float x1, float y1, float x2, float y2, Color color) {
        if (graphics == null) return;
        
        Color oldColor = graphics.getColor();
        graphics.setColor(color != null ? color : currentColor);
        
        graphics.drawLine(Math.round(x1), Math.round(y1), Math.round(x2), Math.round(y2));
        graphics.setColor(oldColor);
    }
    
    @Override
    public void drawText(String text, float x, float y, Font font, Color color) {
        if (graphics == null || text == null) return;
        
        Color oldColor = graphics.getColor();
        Font oldFont = graphics.getFont();
        
        graphics.setColor(color != null ? color : currentColor);
        graphics.setFont(font != null ? font : currentFont);
        
        graphics.drawString(text, Math.round(x), Math.round(y));
        
        graphics.setColor(oldColor);
        graphics.setFont(oldFont);
    }
    
    @Override
    public void drawImage(Object image, float x, float y, float width, float height) {
        if (graphics == null || !(image instanceof Image)) return;
        
        graphics.drawImage((Image) image, Math.round(x), Math.round(y), Math.round(width), Math.round(height), null);
    }
    
    @Override
    public void setColor(Color color) {
        if (color != null) {
            currentColor = color;
        }
    }
    
    @Override
    public void setFont(Font font) {
        if (font != null) {
            currentFont = font;
        }
    }
    
    @Override
    public void cleanup() {
        graphics = null;
        initialized = false;
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    public void setGraphics(Graphics2D graphics) {
        this.graphics = graphics;
        if (graphics != null) {
            // 启用抗锯齿
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
    }
    
    public Graphics2D getGraphics() {
        return graphics;
    }
}