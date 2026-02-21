package stg.service.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

public class VertexBuffer {
    private int vaoId;
    private int vboId;
    private int vertexCount;
    
    public void upload(float[] vertices) {
        vertexCount = vertices.length / 7; // xyz + rgba = 7 components per vertex
        
        // 创建VAO
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        
        // 创建VBO
        vboId = GL20.glGenBuffers();
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vboId);
        
        // 上传顶点数据
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.flip();
        GL20.glBufferData(GL20.GL_ARRAY_BUFFER, buffer, GL20.GL_STATIC_DRAW);
        
        // 设置顶点属性
        // 位置属性 (xyz)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 7 * 4, 0);
        GL20.glEnableVertexAttribArray(0);
        
        // 颜色属性 (rgba)
        GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 7 * 4, 3 * 4);
        GL20.glEnableVertexAttribArray(1);
        
        // 解绑
        GL30.glBindVertexArray(0);
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
    }
    
    public void uploadWithTexture(float[] vertices) {
        vertexCount = vertices.length / 9; // xyz + uv + rgba = 9 components per vertex
        
        // 创建VAO
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        
        // 创建VBO
        vboId = GL20.glGenBuffers();
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vboId);
        
        // 上传顶点数据
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.flip();
        GL20.glBufferData(GL20.GL_ARRAY_BUFFER, buffer, GL20.GL_STATIC_DRAW);
        
        // 设置顶点属性
        // 位置属性 (xyz)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 9 * 4, 0);
        GL20.glEnableVertexAttribArray(0);
        
        // 纹理坐标属性 (uv)
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 9 * 4, 3 * 4);
        GL20.glEnableVertexAttribArray(1);
        
        // 颜色属性 (rgba)
        GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, 9 * 4, 5 * 4);
        GL20.glEnableVertexAttribArray(2);
        
        // 解绑
        GL30.glBindVertexArray(0);
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
    }
    
    public void uploadWithTexCoords(float[] vertices) {
        vertexCount = vertices.length / 5; // xyz + uv = 5 components per vertex
        
        // 创建VAO
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        
        // 创建VBO
        vboId = GL20.glGenBuffers();
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vboId);
        
        // 上传顶点数据
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.flip();
        GL20.glBufferData(GL20.GL_ARRAY_BUFFER, buffer, GL20.GL_STATIC_DRAW);
        
        // 设置顶点属性
        // 位置属性 (xyz)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5 * 4, 0);
        GL20.glEnableVertexAttribArray(0);
        
        // 纹理坐标属性 (uv)
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5 * 4, 3 * 4);
        GL20.glEnableVertexAttribArray(1);
        
        // 解绑
        GL30.glBindVertexArray(0);
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
    }
    
    public void draw(int drawMode) {
        GL30.glBindVertexArray(vaoId);
        GL11.glDrawArrays(drawMode, 0, vertexCount);
        GL30.glBindVertexArray(0);
    }
    
    public void draw() {
        draw(GL11.GL_TRIANGLES);
    }
    
    public void cleanup() {
        if (vboId != 0) {
            GL20.glDeleteBuffers(vboId);
            vboId = 0;
        }
        if (vaoId != 0) {
            GL30.glDeleteVertexArrays(vaoId);
            vaoId = 0;
        }
        vertexCount = 0;
    }
    
    public int getVertexCount() {
        return vertexCount;
    }
}
