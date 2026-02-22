package stg.util;

import org.lwjgl.openal.AL10;

public class ALErrorChecker {
    /**
     * 检查OpenAL错误
     * @return 错误代码，如果没有错误返回AL10.AL_NO_ERROR
     */
    public static int checkError() {
        return AL10.alGetError();
    }
    
    /**
     * 检查OpenAL错误并记录日志
     * @param message 错误上下文信息
     * @return 如果有错误返回true，否则返回false
     */
    public static boolean checkError(String message) {
        int error = checkError();
        if (error != AL10.AL_NO_ERROR) {
            String errorString = getErrorString(error);
            LogUtil.error("ALErrorChecker", message + ": " + errorString + " (0x" + Integer.toHexString(error) + ")");
            return true;
        }
        return false;
    }
    
    /**
     * 将OpenAL错误代码转换为可读的错误信息
     * @param error 错误代码
     * @return 错误信息字符串
     */
    public static String getErrorString(int error) {
        switch (error) {
            case AL10.AL_NO_ERROR:
                return "No error";
            case AL10.AL_INVALID_NAME:
                return "Invalid name";
            case AL10.AL_INVALID_ENUM:
                return "Invalid enum";
            case AL10.AL_INVALID_VALUE:
                return "Invalid value";
            case AL10.AL_INVALID_OPERATION:
                return "Invalid operation";
            case AL10.AL_OUT_OF_MEMORY:
                return "Out of memory";
            default:
                return "Unknown error";
        }
    }
}