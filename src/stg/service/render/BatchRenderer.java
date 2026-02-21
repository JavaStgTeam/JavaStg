package stg.service.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class BatchRenderer {
    private static final int MAX_VERTICES = 10000 * 4; // 10000 objects * 4 vertices each
    private static final int VERTEX_SIZE = 7; // xyz + rgba
    private static final int BUFFER_SIZE = MAX_VERTICES * VERTEX_SIZE * 4;
    
    private int vaoId;
    private int vboId;
    private FloatBuffer vertexBuffer;
    private int vertexCount;
    private ShaderProgram shader;
    
    public BatchRenderer(ShaderProgram shader) {
        this.shader = shader;
        init();
    }
    
    private void init() {
        // Create VAO
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        
        // Create VBO
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BUFFER_SIZE, GL15.GL_DYNAMIC_DRAW);
        
        // Define vertex attributes
        // Position (xyz)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, VERTEX_SIZE * 4, 0);
        GL20.glEnableVertexAttribArray(0);
        
        // Color (rgba)
        GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, VERTEX_SIZE * 4, 3 * 4);
        GL20.glEnableVertexAttribArray(1);
        
        // Unbind
        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        
        // Create float buffer
        vertexBuffer = org.lwjgl.BufferUtils.createFloatBuffer(MAX_VERTICES * VERTEX_SIZE);
    }
    
    public void begin() {
        vertexCount = 0;
        vertexBuffer.clear();
    }
    
    public void drawCircle(float x, float y, float radius, Color color) {
        if (vertexCount + 32 > MAX_VERTICES) {
            flush();
        }
        
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        float a = color.getAlpha() / 255.0f;
        
        // Center vertex
        vertexBuffer.put(x).put(y).put(0.0f);
        vertexBuffer.put(r).put(g).put(b).put(a);
        vertexCount++;
        
        // Edge vertices
        int segments = 32;
        for (int i = 0; i <= segments; i++) {
            float angle = (float)(i * Math.PI * 2 / segments);
            float px = x + (float)Math.cos(angle) * radius;
            float py = y + (float)Math.sin(angle) * radius;
            
            vertexBuffer.put(px).put(py).put(0.0f);
            vertexBuffer.put(r).put(g).put(b).put(a);
            vertexCount++;
        }
    }
    
    public void drawRect(float x, float y, float width, float height, Color color) {
        if (vertexCount + 4 > MAX_VERTICES) {
            flush();
        }
        
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        float a = color.getAlpha() / 255.0f;
        
        float halfWidth = width / 2;
        float halfHeight = height / 2;
        
        // Bottom-left
        vertexBuffer.put(x - halfWidth).put(y - halfHeight).put(0.0f);
        vertexBuffer.put(r).put(g).put(b).put(a);
        
        // Bottom-right
        vertexBuffer.put(x + halfWidth).put(y - halfHeight).put(0.0f);
        vertexBuffer.put(r).put(g).put(b).put(a);
        
        // Top-left
        vertexBuffer.put(x - halfWidth).put(y + halfHeight).put(0.0f);
        vertexBuffer.put(r).put(g).put(b).put(a);
        
        // Top-right
        vertexBuffer.put(x + halfWidth).put(y + halfHeight).put(0.0f);
        vertexBuffer.put(r).put(g).put(b).put(a);
        
        vertexCount += 4;
    }
    
    public void drawLine(float x1, float y1, float x2, float y2, Color color) {
        if (vertexCount + 2 > MAX_VERTICES) {
            flush();
        }
        
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        float a = color.getAlpha() / 255.0f;
        
        // Start point
        vertexBuffer.put(x1).put(y1).put(0.0f);
        vertexBuffer.put(r).put(g).put(b).put(a);
        
        // End point
        vertexBuffer.put(x2).put(y2).put(0.0f);
        vertexBuffer.put(r).put(g).put(b).put(a);
        
        vertexCount += 2;
    }
    
    public void flush() {
        if (vertexCount == 0) {
            return;
        }
        
        // Bind VAO
        GL30.glBindVertexArray(vaoId);
        
        // Upload data
        vertexBuffer.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertexBuffer);
        
        // Draw
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
        
        // Unbind
        GL30.glBindVertexArray(0);
        
        // Reset
        vertexCount = 0;
        vertexBuffer.clear();
    }
    
    public void end() {
        flush();
    }
    
    public void cleanup() {
        GL30.glDeleteVertexArrays(vaoId);
        GL15.glDeleteBuffers(vboId);
        if (vertexBuffer != null) {
            vertexBuffer.clear();
        }
    }
    
    public int getVertexCount() {
        return vertexCount;
    }
}
