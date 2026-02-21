package stg.service.render;

import java.awt.Color;
import java.awt.Font;

public interface IRenderer {
    void init();
    void beginFrame();
    void endFrame();
    
    void drawCircle(float x, float y, float radius, Color color);
    void drawRect(float x, float y, float width, float height, Color color);
    void drawLine(float x1, float y1, float x2, float y2, Color color);
    void drawText(String text, float x, float y, Font font, Color color);
    void drawImage(Object image, float x, float y, float width, float height);
    
    void setColor(Color color);
    void setFont(Font font);
    
    void cleanup();
    boolean isInitialized();
}
