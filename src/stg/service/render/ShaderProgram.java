package stg.service.render;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import stg.util.GLErrorChecker;

public class ShaderProgram {
    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private String name; // 着色器程序名称，用于调试
    private Map<String, Integer> uniformLocations; // 缓存uniform位置
    private Map<String, Object> uniformValues; // 缓存uniform值，避免重复设置
    
    // 静态变量，跟踪当前活动的着色器程序
    private static int currentProgramId = 0;
    
    /**
     * 默认构造函数
     */
    public ShaderProgram() {
        this("unnamed");
    }
    
    /**
     * 带名称的构造函数，用于调试
     * @param name 着色器程序名称
     */
    public ShaderProgram(String name) {
        this.name = name;
        this.programId = 0;
        this.vertexShaderId = 0;
        this.fragmentShaderId = 0;
        this.uniformLocations = new HashMap<>();
        this.uniformValues = new HashMap<>();
    }
    
    /**
     * 从字符串编译着色器
     * @param vertexSource 顶点着色器源代码
     * @param fragmentSource 片段着色器源代码
     */
    public void compile(String vertexSource, String fragmentSource) {
        // 初始化ID为0
        programId = 0;
        vertexShaderId = 0;
        fragmentShaderId = 0;
        
        try {
            // 创建顶点着色器
            vertexShaderId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
            GLErrorChecker.checkError("ShaderProgram.compile() - After creating vertex shader");
            GL20.glShaderSource(vertexShaderId, vertexSource);
            GL20.glCompileShader(vertexShaderId);
            GLErrorChecker.checkError("ShaderProgram.compile() - After compiling vertex shader");
            
            // 检查顶点着色器编译状态
            if (GL20.glGetShaderi(vertexShaderId, GL20.GL_COMPILE_STATUS) == 0) {
                String error = GL20.glGetShaderInfoLog(vertexShaderId);
                cleanupShaders();
                throw new RuntimeException("Vertex shader compilation failed: " + error);
            }
            
            // 创建片段着色器
            fragmentShaderId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
            GLErrorChecker.checkError("ShaderProgram.compile() - After creating fragment shader");
            GL20.glShaderSource(fragmentShaderId, fragmentSource);
            GL20.glCompileShader(fragmentShaderId);
            GLErrorChecker.checkError("ShaderProgram.compile() - After compiling fragment shader");
            
            // 检查片段着色器编译状态
            if (GL20.glGetShaderi(fragmentShaderId, GL20.GL_COMPILE_STATUS) == 0) {
                String error = GL20.glGetShaderInfoLog(fragmentShaderId);
                cleanupShaders();
                throw new RuntimeException("Fragment shader compilation failed: " + error);
            }
            
            // 创建着色器程序
            programId = GL20.glCreateProgram();
            GLErrorChecker.checkError("ShaderProgram.compile() - After creating program");
            GL20.glAttachShader(programId, vertexShaderId);
            GL20.glAttachShader(programId, fragmentShaderId);
            GLErrorChecker.checkError("ShaderProgram.compile() - After attaching shaders");
            GL20.glLinkProgram(programId);
            GLErrorChecker.checkError("ShaderProgram.compile() - After linking program");
            
            // 检查着色器程序链接状态
            if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
                String error = GL20.glGetProgramInfoLog(programId);
                cleanup();
                throw new RuntimeException("Shader program linking failed: " + error);
            }
            
            // 链接成功后可以删除着色器对象
            cleanupShaders();
        } catch (Exception e) {
            // 确保在任何异常情况下都清理资源
            cleanup();
            throw e;
        }
    }
    
    /**
     * 从文件编译着色器
     * @param vertexShaderPath 顶点着色器文件路径
     * @param fragmentShaderPath 片段着色器文件路径
     * @throws IOException 如果文件读取失败
     */
    public void compileFromFile(String vertexShaderPath, String fragmentShaderPath) throws IOException {
        String vertexSource = readShaderFile(vertexShaderPath);
        String fragmentSource = readShaderFile(fragmentShaderPath);
        compile(vertexSource, fragmentSource);
    }
    
    /**
     * 读取着色器文件内容
     * @param filePath 文件路径
     * @return 着色器源代码
     * @throws IOException 如果文件读取失败
     */
    private String readShaderFile(String filePath) throws IOException {
        StringBuilder source = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append("\n");
            }
        }
        return source.toString();
    }
    
    /**
     * 使用此着色器程序
     */
    public void use() {
        if (programId != currentProgramId) {
            GL20.glUseProgram(programId);
            GLErrorChecker.checkError("ShaderProgram.use() - After using program");
            currentProgramId = programId;
        }
    }
    
    /**
     * 停止使用任何着色器程序
     */
    public static void unuse() {
        if (currentProgramId != 0) {
            GL20.glUseProgram(0);
            GLErrorChecker.checkError("ShaderProgram.unuse() - After unusing program");
            currentProgramId = 0;
        }
    }
    
    /**
     * 获取uniform变量的位置（带缓存）
     * @param name uniform变量名
     * @return uniform变量的位置，如果不存在返回-1
     */
    private int getUniformLocationCached(String name) {
        return uniformLocations.computeIfAbsent(name, n -> GL20.glGetUniformLocation(programId, n));
    }
    
    /**
     * 设置float类型的uniform变量
     * @param name uniform变量名
     * @param value 变量值
     */
    public void setUniform(String name, float value) {
        Integer location = getUniformLocationCached(name);
        if (location != -1) {
            // 检查值是否变化
            Float cachedValue = (Float) uniformValues.get(name);
            if (cachedValue == null || cachedValue.floatValue() != value) {
                GL20.glUniform1f(location, value);
                GLErrorChecker.checkError("ShaderProgram.setUniform() - After setting float uniform");
                uniformValues.put(name, value);
            }
        }
    }
    
    /**
     * 设置int类型的uniform变量
     * @param name uniform变量名
     * @param value 变量值
     */
    public void setUniform(String name, int value) {
        Integer location = getUniformLocationCached(name);
        if (location != -1) {
            // 检查值是否变化
            Integer cachedValue = (Integer) uniformValues.get(name);
            if (cachedValue == null || cachedValue.intValue() != value) {
                GL20.glUniform1i(location, value);
                GLErrorChecker.checkError("ShaderProgram.setUniform() - After setting int uniform");
                uniformValues.put(name, value);
            }
        }
    }
    
    /**
     * 设置boolean类型的uniform变量
     * @param name uniform变量名
     * @param value 变量值
     */
    public void setUniform(String name, boolean value) {
        Integer location = getUniformLocationCached(name);
        if (location != -1) {
            // 检查值是否变化
            Boolean cachedValue = (Boolean) uniformValues.get(name);
            if (cachedValue == null || cachedValue.booleanValue() != value) {
                GL20.glUniform1i(location, value ? 1 : 0);
                GLErrorChecker.checkError("ShaderProgram.setUniform() - After setting boolean uniform");
                uniformValues.put(name, value);
            }
        }
    }
    
    /**
     * 设置vec2类型的uniform变量
     * @param name uniform变量名
     * @param x x分量
     * @param y y分量
     */
    public void setUniform(String name, float x, float y) {
        Integer location = getUniformLocationCached(name);
        if (location != -1) {
            // 检查值是否变化
            Float[] cachedValue = (Float[]) uniformValues.get(name);
            if (cachedValue == null || cachedValue[0] != x || cachedValue[1] != y) {
                GL20.glUniform2f(location, x, y);
                GLErrorChecker.checkError("ShaderProgram.setUniform() - After setting vec2 uniform");
                uniformValues.put(name, new Float[]{x, y});
            }
        }
    }
    
    /**
     * 设置vec3类型的uniform变量
     * @param name uniform变量名
     * @param x x分量
     * @param y y分量
     * @param z z分量
     */
    public void setUniform(String name, float x, float y, float z) {
        Integer location = getUniformLocationCached(name);
        if (location != -1) {
            // 检查值是否变化
            Float[] cachedValue = (Float[]) uniformValues.get(name);
            if (cachedValue == null || cachedValue[0] != x || cachedValue[1] != y || cachedValue[2] != z) {
                GL20.glUniform3f(location, x, y, z);
                GLErrorChecker.checkError("ShaderProgram.setUniform() - After setting vec3 uniform");
                uniformValues.put(name, new Float[]{x, y, z});
            }
        }
    }
    
    /**
     * 设置vec4类型的uniform变量
     * @param name uniform变量名
     * @param x x分量
     * @param y y分量
     * @param z z分量
     * @param w w分量
     */
    public void setUniform(String name, float x, float y, float z, float w) {
        Integer location = getUniformLocationCached(name);
        if (location != -1) {
            // 检查值是否变化
            Float[] cachedValue = (Float[]) uniformValues.get(name);
            if (cachedValue == null || cachedValue[0] != x || cachedValue[1] != y || cachedValue[2] != z || cachedValue[3] != w) {
                GL20.glUniform4f(location, x, y, z, w);
                GLErrorChecker.checkError("ShaderProgram.setUniform() - After setting vec4 uniform");
                uniformValues.put(name, new Float[]{x, y, z, w});
            }
        }
    }
    
    /**
     * 设置mat4类型的uniform变量
     * @param name uniform变量名
     * @param matrix 4x4矩阵
     */
    public void setUniformMatrix4(String name, float[] matrix) {
        Integer location = getUniformLocationCached(name);
        if (location != -1) {
            // 检查值是否变化
            float[] cachedValue = (float[]) uniformValues.get(name);
            if (cachedValue == null || !matricesEqual(cachedValue, matrix)) {
                FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
                buffer.put(matrix);
                buffer.flip();
                GL20.glUniformMatrix4fv(location, false, buffer);
                GLErrorChecker.checkError("ShaderProgram.setUniformMatrix4() - After setting matrix uniform");
                uniformValues.put(name, matrix.clone());
            }
        }
    }
    
    /**
     * 比较两个矩阵是否相等
     * @param a 第一个矩阵
     * @param b 第二个矩阵
     * @return 如果矩阵相等返回true，否则返回false
     */
    private boolean matricesEqual(float[] a, float[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }
    
    /**
     * 获取uniform变量的位置
     * @param name uniform变量名
     * @return uniform变量的位置，如果不存在返回-1
     */
    public int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(programId, name);
    }
    
    /**
     * 获取着色器程序ID
     * @return 着色器程序ID
     */
    public int getProgramId() {
        return programId;
    }
    
    /**
     * 获取着色器程序名称
     * @return 着色器程序名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 检查着色器程序是否有效
     * @return 如果着色器程序有效返回true，否则返回false
     */
    public boolean isValid() {
        return programId != 0;
    }
    
    /**
     * 清理着色器对象
     */
    private void cleanupShaders() {
        if (vertexShaderId != 0) {
            if (programId != 0) {
                GL20.glDetachShader(programId, vertexShaderId);
                GLErrorChecker.checkError("ShaderProgram.cleanupShaders() - After detaching vertex shader");
            }
            GL20.glDeleteShader(vertexShaderId);
            GLErrorChecker.checkError("ShaderProgram.cleanupShaders() - After deleting vertex shader");
            vertexShaderId = 0;
        }
        if (fragmentShaderId != 0) {
            if (programId != 0) {
                GL20.glDetachShader(programId, fragmentShaderId);
                GLErrorChecker.checkError("ShaderProgram.cleanupShaders() - After detaching fragment shader");
            }
            GL20.glDeleteShader(fragmentShaderId);
            GLErrorChecker.checkError("ShaderProgram.cleanupShaders() - After deleting fragment shader");
            fragmentShaderId = 0;
        }
    }
    
    /**
     * 清理所有资源
     */
    public void cleanup() {
        // 清理着色器对象
        cleanupShaders();
        
        // 清理程序对象
        if (programId != 0) {
            GL20.glDeleteProgram(programId);
            GLErrorChecker.checkError("ShaderProgram.cleanup() - After deleting program");
            programId = 0;
        }
    }
    
    /**
     * 创建默认的着色器程序（用于基本渲染）
     * @return 默认着色器程序
     */
    public static ShaderProgram createDefaultShader() {
        ShaderProgram shader = new ShaderProgram("default");
        
        String vertexShader = "#version 330 core\n" +
            "layout(location = 0) in vec3 aPos;\n" +
            "layout(location = 1) in vec4 aColor;\n" +
            "uniform mat4 uProjection;\n" +
            "uniform mat4 uModel;\n" +
            "out vec4 vColor;\n" +
            "void main() {\n" +
            "    gl_Position = uProjection * uModel * vec4(aPos, 1.0);\n" +
            "    vColor = aColor;\n" +
            "}\n";
        
        String fragmentShader = "#version 330 core\n" +
            "in vec4 vColor;\n" +
            "out vec4 FragColor;\n" +
            "void main() {\n" +
            "    FragColor = vColor;\n" +
            "}\n";
        
        shader.compile(vertexShader, fragmentShader);
        return shader;
    }
    
    /**
     * 创建纹理着色器程序（用于渲染纹理）
     * @return 纹理着色器程序
     */
    public static ShaderProgram createTextureShader() {
        ShaderProgram shader = new ShaderProgram("texture");
        
        String vertexShader = "#version 330 core\n" +
            "layout(location = 0) in vec3 aPos;\n" +
            "layout(location = 1) in vec2 aTexCoord;\n" +
            "uniform mat4 uProjection;\n" +
            "uniform mat4 uModel;\n" +
            "out vec2 vTexCoord;\n" +
            "void main() {\n" +
            "    gl_Position = uProjection * uModel * vec4(aPos, 1.0);\n" +
            "    vTexCoord = aTexCoord;\n" +
            "}\n";
        
        String fragmentShader = "#version 330 core\n" +
            "in vec2 vTexCoord;\n" +
            "uniform sampler2D uTexture;\n" +
            "out vec4 FragColor;\n" +
            "void main() {\n" +
            "    FragColor = texture(uTexture, vTexCoord);\n" +
            "}\n";
        
        shader.compile(vertexShader, fragmentShader);
        return shader;
    }
    
    /**
     * 创建文本着色器程序（用于渲染文本）
     * @return 文本着色器程序
     */
    public static ShaderProgram createTextShader() {
        ShaderProgram shader = new ShaderProgram("text");
        
        String vertexShader = "#version 330 core\n" +
            "layout(location = 0) in vec3 aPos;\n" +
            "layout(location = 1) in vec2 aTexCoord;\n" +
            "layout(location = 2) in vec4 aColor;\n" +
            "uniform mat4 uProjection;\n" +
            "uniform mat4 uModel;\n" +
            "out vec2 vTexCoord;\n" +
            "out vec4 vColor;\n" +
            "void main() {\n" +
            "    gl_Position = uProjection * uModel * vec4(aPos, 1.0);\n" +
            "    vTexCoord = aTexCoord;\n" +
            "    vColor = aColor;\n" +
            "}\n";
        
        String fragmentShader = "#version 330 core\n" +
            "in vec2 vTexCoord;\n" +
            "in vec4 vColor;\n" +
            "uniform sampler2D uTexture;\n" +
            "out vec4 FragColor;\n" +
            "void main() {\n" +
            "    float alpha = texture(uTexture, vTexCoord).r;\n" +
            "    FragColor = vColor * alpha;\n" +
            "}\n";
        
        shader.compile(vertexShader, fragmentShader);
        return shader;
    }
}
