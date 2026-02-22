package stg.util;

import org.lwjgl.opengl.GL11;

public class GLErrorChecker {
    /**
     * 检查OpenGL错误
     * @return 错误代码，如果没有错误返回GL11.GL_NO_ERROR
     */
    public static int checkError() {
        return GL11.glGetError();
    }
    
    /**
     * 检查OpenGL错误并记录日志
     * @param message 错误上下文信息
     * @return 如果有错误返回true，否则返回false
     */
    public static boolean checkError(String message) {
        int error = checkError();
        if (error != GL11.GL_NO_ERROR) {
            String errorString = getErrorString(error);
            LogUtil.error("GLErrorChecker", message + ": " + errorString + " (0x" + Integer.toHexString(error) + ")");
            return true;
        }
        return false;
    }
    
    /**
     * 将OpenGL错误代码转换为可读的错误信息
     * @param error 错误代码
     * @return 错误信息字符串
     */
    public static String getErrorString(int error) {
        switch (error) {
            case GL11.GL_NO_ERROR:
                return "No error";
            case GL11.GL_INVALID_ENUM:
                return "Invalid enum";
            case GL11.GL_INVALID_VALUE:
                return "Invalid value";
            case GL11.GL_INVALID_OPERATION:
                return "Invalid operation";
            case GL11.GL_STACK_OVERFLOW:
                return "Stack overflow";
            case GL11.GL_STACK_UNDERFLOW:
                return "Stack underflow";
            case GL11.GL_OUT_OF_MEMORY:
                return "Out of memory";
            default:
                return "Unknown error";
        }
    }
}