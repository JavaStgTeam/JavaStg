package stg.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日志工具类 - 统一管理日志输出
 * @since 2026-02-16
 */
public class LogUtil {
    
    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }
    
    private static Level currentLevel = Level.INFO;
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    private LogUtil() {}
    
    public static void setLevel(Level level) {
        currentLevel = level;
    }
    
    public static void debug(String tag, String message) {
        log(Level.DEBUG, tag, message, null);
    }
    
    public static void info(String tag, String message) {
        log(Level.INFO, tag, message, null);
    }
    
    public static void warn(String tag, String message) {
        log(Level.WARN, tag, message, null);
    }
    
    public static void error(String tag, String message) {
        log(Level.ERROR, tag, message, null);
    }
    
    public static void error(String tag, String message, Throwable t) {
        log(Level.ERROR, tag, message, t);
    }
    
    private static void log(Level level, String tag, String message, Throwable t) {
        if (level.ordinal() < currentLevel.ordinal()) {
            return;
        }
        
        String timestamp = LocalDateTime.now().format(timeFormatter);
        String levelStr = String.format("%-5s", level.name());
        String output = String.format("[%s] %s [%s] %s", timestamp, levelStr, tag, message);
        
        if (level == Level.ERROR) {
            System.err.println(output);
            if (t != null) {
                System.err.println("  异常类型: " + t.getClass().getName());
                System.err.println("  异常信息: " + t.getMessage());
                if (level == Level.DEBUG) {
                    StackTraceElement[] stackTrace = t.getStackTrace();
                    for (int i = 0; i < Math.min(5, stackTrace.length); i++) {
                        System.err.println("    at " + stackTrace[i].toString());
                    }
                }
            }
        } else {
            System.out.println(output);
            if (t != null) {
                System.out.println("  异常: " + t.getClass().getName() + " - " + t.getMessage());
            }
        }
    }
}
