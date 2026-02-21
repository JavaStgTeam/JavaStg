package stg.service.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class ShaderProgram {
    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    
    public void compile(String vertexSource, String fragmentSource) {
        // 创建顶点着色器
        vertexShaderId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexShaderId, vertexSource);
        GL20.glCompileShader(vertexShaderId);
        
        // 检查顶点着色器编译状态
        if (GL20.glGetShaderi(vertexShaderId, GL20.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Vertex shader compilation failed: " + GL20.glGetShaderInfoLog(vertexShaderId));
        }
        
        // 创建片段着色器
        fragmentShaderId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentShaderId, fragmentSource);
        GL20.glCompileShader(fragmentShaderId);
        
        // 检查片段着色器编译状态
        if (GL20.glGetShaderi(fragmentShaderId, GL20.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Fragment shader compilation failed: " + GL20.glGetShaderInfoLog(fragmentShaderId));
        }
        
        // 创建着色器程序
        programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShaderId);
        GL20.glAttachShader(programId, fragmentShaderId);
        GL20.glLinkProgram(programId);
        
        // 检查着色器程序链接状态
        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Shader program linking failed: " + GL20.glGetProgramInfoLog(programId));
        }
        
        // 链接成功后可以删除着色器对象
        GL20.glDetachShader(programId, vertexShaderId);
        GL20.glDetachShader(programId, fragmentShaderId);
        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);
    }
    
    public void use() {
        GL20.glUseProgram(programId);
    }
    
    public void setUniform(String name, float value) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location != -1) {
            GL20.glUniform1f(location, value);
        }
    }
    
    public void setUniform(String name, int value) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location != -1) {
            GL20.glUniform1i(location, value);
        }
    }
    
    public void setUniform(String name, boolean value) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location != -1) {
            GL20.glUniform1i(location, value ? 1 : 0);
        }
    }
    
    public void setUniformMatrix4(String name, float[] matrix) {
        int location = GL20.glGetUniformLocation(programId, name);
        if (location != -1) {
            FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
            buffer.put(matrix);
            buffer.flip();
            GL20.glUniformMatrix4fv(location, false, buffer);
        }
    }
    
    public void cleanup() {
        if (programId != 0) {
            GL20.glDeleteProgram(programId);
            programId = 0;
        }
    }
    
    public int getProgramId() {
        return programId;
    }
}
