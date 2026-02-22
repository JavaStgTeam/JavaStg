package stg.service.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class VertexBuffer {
    private int vaoId;
    private List<Integer> vboIds;
    private int vertexCount;
    private int componentCount;
    private int bufferSize;
    private boolean dynamic;
    
    public VertexBuffer() {
        vboIds = new ArrayList<>();
        vertexCount = 0;
        componentCount = 0;
        bufferSize = 0;
        dynamic = false;
    }
    
    public void upload(float[] vertices, int componentsPerVertex, boolean dynamic) {
        this.componentCount = componentsPerVertex;
        this.vertexCount = vertices.length / componentsPerVertex;
        this.bufferSize = vertices.length * 4; // 4 bytes per float
        this.dynamic = dynamic;
        
        // 创建VAO
        if (vaoId == 0) {
            vaoId = GL30.glGenVertexArrays();
        }
        GL30.glBindVertexArray(vaoId);
        
        // 创建VBO
        int vboId = GL20.glGenBuffers();
        vboIds.add(vboId);
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vboId);
        
        // 上传顶点数据
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.flip();
        GL20.glBufferData(GL20.GL_ARRAY_BUFFER, buffer, dynamic ? GL20.GL_DYNAMIC_DRAW : GL20.GL_STATIC_DRAW);
        
        // 设置顶点属性
        setupVertexAttributes();
        
        // 解绑
        GL30.glBindVertexArray(0);
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
    }
    
    public void upload(float[] vertices) {
        upload(vertices, 7, false); // xyz + rgba = 7 components per vertex
    }
    
    public void uploadWithTexture(float[] vertices) {
        upload(vertices, 9, false); // xyz + uv + rgba = 9 components per vertex
    }
    
    public void uploadWithTexCoords(float[] vertices) {
        upload(vertices, 5, false); // xyz + uv = 5 components per vertex
    }
    
    public void updateData(float[] vertices) {
        if (vboIds.isEmpty()) {
            upload(vertices, componentCount, dynamic);
            return;
        }
        
        int requiredSize = vertices.length * 4;
        int currentVboId = vboIds.get(vboIds.size() - 1);
        
        // 检查是否需要扩展缓冲区
        if (requiredSize > bufferSize) {
            // 创建更大的VBO
            int newVboId = GL20.glGenBuffers();
            vboIds.add(newVboId);
            GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, newVboId);
            
            // 分配更大的空间（1.5倍增长）
            bufferSize = (int) (requiredSize * 1.5);
            FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
            buffer.put(vertices);
            buffer.flip();
            GL20.glBufferData(GL20.GL_ARRAY_BUFFER, bufferSize, dynamic ? GL20.GL_DYNAMIC_DRAW : GL20.GL_STATIC_DRAW);
            GL20.glBufferSubData(GL20.GL_ARRAY_BUFFER, 0, buffer);
            
            // 更新顶点数
            this.vertexCount = vertices.length / componentCount;
            
            // 解绑
            GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
        } else {
            // 更新现有VBO
            GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, currentVboId);
            FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
            buffer.put(vertices);
            buffer.flip();
            GL20.glBufferSubData(GL20.GL_ARRAY_BUFFER, 0, buffer);
            
            // 更新顶点数
            this.vertexCount = vertices.length / componentCount;
            
            // 解绑
            GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
        }
    }
    
    public void updateSubData(float[] vertices, int offset) {
        if (vboIds.isEmpty()) {
            return;
        }
        
        int currentVboId = vboIds.get(vboIds.size() - 1);
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, currentVboId);
        
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.flip();
        GL20.glBufferSubData(GL20.GL_ARRAY_BUFFER, offset * 4, buffer);
        
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
    }
    
    private void setupVertexAttributes() {
        int stride = componentCount * 4;
        int offset = 0;
        
        // 位置属性 (xyz)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, stride, offset);
        GL20.glEnableVertexAttribArray(0);
        offset += 3 * 4;
        
        // 纹理坐标属性 (uv) - 如果存在
        if (componentCount >= 5) {
            GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, stride, offset);
            GL20.glEnableVertexAttribArray(1);
            offset += 2 * 4;
        }
        
        // 颜色属性 (rgba) - 如果存在
        if (componentCount >= 7) {
            GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, stride, offset);
            GL20.glEnableVertexAttribArray(2);
        }
    }
    
    public void draw(int drawMode, int first, int count) {
        if (vaoId == 0 || vboIds.isEmpty()) {
            return;
        }
        
        GL30.glBindVertexArray(vaoId);
        GL11.glDrawArrays(drawMode, first, count);
        GL30.glBindVertexArray(0);
    }
    
    public void draw(int drawMode) {
        draw(drawMode, 0, vertexCount);
    }
    
    public void draw() {
        draw(GL11.GL_TRIANGLES);
    }
    
    public void cleanup() {
        for (int vboId : vboIds) {
            if (vboId != 0) {
                GL20.glDeleteBuffers(vboId);
            }
        }
        vboIds.clear();
        
        if (vaoId != 0) {
            GL30.glDeleteVertexArrays(vaoId);
            vaoId = 0;
        }
        
        vertexCount = 0;
        componentCount = 0;
        bufferSize = 0;
    }
    
    public int getVertexCount() {
        return vertexCount;
    }
    
    public int getComponentCount() {
        return componentCount;
    }
    
    public int getBufferSize() {
        return bufferSize;
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
}
