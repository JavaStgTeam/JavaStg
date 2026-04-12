package stg.util;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 注解扫描工具类
 * 用于扫描指定包下带有特定注解的类
 */
public class AnnotationScanner {

    /**
     * 扫描指定包下带有特定注解的类
     * @param packageName 包名（不区分大小写）
     * @param annotationClass 注解类
     * @return 带有指定注解的类列表（去重）
     */
    public static List<Class<?>> scanClassesWithAnnotation(String packageName, Class<?> annotationClass) {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        Set<String> discoveredClassNames = new HashSet<>(); // 去重
        List<String> scanErrors = new ArrayList<>();
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = AnnotationScanner.class.getClassLoader();
        }
        
        // 将包名转换为路径
        String packagePath = packageName.replace('.', '/');
        
        try {
            Enumeration<URL> resources = classLoader.getResources(packagePath);
            
            // ─────────────────────────────────────────
            // 情况1: 文件系统（IDE/开发环境）
            // ─────────────────────────────────────────
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();
                
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(resource.getFile(), "UTF-8");
                    File directory = new File(filePath);
                    if (directory.exists() && directory.isDirectory()) {
                        // 转换为小写包路径，用于大小写不敏感匹配
                        String packagePathLower = packageName.replace('.', '/').toLowerCase();
                        scanDirectoryRecursive(directory, directory, packageName, packagePathLower, 
                                              annotationClass, annotatedClasses, discoveredClassNames, 
                                              scanErrors, classLoader);
                    }
                }
                // 注意: getResources() 对 JAR 不会返回 "file" 协议的 URL，
                // JAR 内的资源走下面 getResources("META-INF/...") 的逻辑
            }
            
            // ─────────────────────────────────────────
            // 情况2: 扫描 classpath 下所有 .class 文件（兼容 JAR 和 IDE）
            // ─────────────────────────────────────────
            scanClasspath(packageName, annotationClass, annotatedClasses, 
                         discoveredClassNames, scanErrors, classLoader);
            
        } catch (Exception e) {
            LogUtil.error("AnnotationScanner", "扫描包 [" + packageName + "] 时发生异常", e);
            scanErrors.add("包扫描异常: " + e.getMessage());
        }
        
        // 打印扫描结果摘要
        if (!scanErrors.isEmpty()) {
            for (String err : scanErrors) {
                System.err.println("[AnnotationScanner] " + err);
            }
        }
        
        LogUtil.info("AnnotationScanner", 
            String.format("扫描完成，包=[%s]，找到 %d 个带注解的类，扫描异常 %d 项",
                packageName, annotatedClasses.size(), scanErrors.size()));
        
        return annotatedClasses;
    }
    
    /**
     * 扫描 ClassPath 下所有可能的类文件（兼容 JAR + 文件系统）
     */
    private static void scanClasspath(String basePackage, Class<?> annotationClass,
                                       List<Class<?>> annotatedClasses, Set<String> seenNames,
                                       List<String> errors, ClassLoader classLoader) {
        // 获取所有 classpath 根路径
        String classpath = System.getProperty("java.class.path");
        if (classpath == null) return;
        
        // 统一转小写包路径，用于大小写不敏感匹配
        String basePackagePath = basePackage.replace('.', '/').toLowerCase();
        
        for (String path : classpath.split(File.pathSeparator)) {
            File file = new File(path);
            
            if (file.isDirectory()) {
                // 文件系统扫描：遍历 classpath 目录
                scanDirectoryRecursive(file, file, basePackage, basePackagePath, 
                                       annotationClass, annotatedClasses, seenNames, errors, classLoader);
            } else if (file.getName().endsWith(".jar")) {
                // JAR 扫描：直接扫 jar 包内的类
                scanJarForPackage(file, basePackage, basePackagePath, 
                                  annotationClass, annotatedClasses, seenNames, errors, classLoader);
            }
        }
    }
    
    /**
     * 递归扫描目录（文件系统模式）
     */
    private static void scanDirectoryRecursive(File root, File directory, String packageName,
                                               String packagePathLower, Class<?> annotationClass,
                                               List<Class<?>> annotatedClasses, Set<String> seenNames,
                                               List<String> errors, ClassLoader classLoader) {
        if (!directory.exists() || !directory.isDirectory()) return;
        
        File[] files = directory.listFiles();
        if (files == null) return;
        
        // 计算相对路径
        String relPath = directory.getAbsolutePath().substring(root.getAbsolutePath().length())
                               .replace(File.separatorChar, '/');
        if (relPath.startsWith("/")) relPath = relPath.substring(1);
        
        // 跳过非目标包的目录
        if (!relPath.isEmpty() && !relPath.toLowerCase().startsWith(packagePathLower)) {
            return;
        }
        
        for (File f : files) {
            if (f.isDirectory()) {
                scanDirectoryRecursive(root, f, packageName, packagePathLower, annotationClass,
                                      annotatedClasses, seenNames, errors, classLoader);
            } else if (f.getName().endsWith(".class")) {
                // 提取类名（大小写不敏感比较）
                String classFileName = f.getName().substring(0, f.getName().length() - 6);
                String fullClassName = (relPath.isEmpty() ? packageName : relPath.replace('/', '.') + "." + classFileName);
                
                // 大小写标准化：首字母大写，其余小写（Java 规范）
                fullClassName = normalizeClassName(fullClassName);
                
                checkClass(fullClassName, annotationClass, annotatedClasses, seenNames, errors, classLoader);
            }
        }
    }
    
    /**
     * 扫描单个 JAR 文件内指定包的类
     */
    private static void scanJarForPackage(File jarFile, String packageName,
                                          String packagePathLower, Class<?> annotationClass,
                                          List<Class<?>> annotatedClasses, Set<String> seenNames,
                                          List<String> errors, ClassLoader classLoader) {
        JarFile jar = null;
        try {
            jar = new JarFile(jarFile);
            Enumeration<JarEntry> entries = jar.entries();
            
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                
                if (!entry.isDirectory() && entryName.endsWith(".class")) {
                    // 大小写不敏感匹配包路径
                    String entryNameLower = entryName.toLowerCase();
                    if (entryNameLower.startsWith(packagePathLower)) {
                        // 提取类名并标准化大小写
                        String className = entryName.substring(0, entryName.length() - 6)
                                                 .replace('/', '.');
                        className = normalizeClassName(className);
                        checkClass(className, annotationClass, annotatedClasses, seenNames, errors, classLoader);
                    }
                }
            }
        } catch (Exception e) {
            errors.add("扫描JAR [" + jarFile.getName() + "] 失败: " + e.getMessage());
        } finally {
            if (jar != null) try { jar.close(); } catch (Exception ignored) {}
        }
    }
    
    /**
     * 类名大小写标准化：首字母大写，其余保留原样
     * 解决 Windows 文件系统大小写与 Java 类名不一致的问题
     */
    private static String normalizeClassName(String className) {
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0 && lastDot < className.length() - 1) {
            String pkg = className.substring(0, lastDot);
            String simpleName = className.substring(lastDot + 1);
            // 首字母大写，其余保持原样
            if (!simpleName.isEmpty()) {
                simpleName = Character.toUpperCase(simpleName.charAt(0)) + simpleName.substring(1);
            }
            return pkg + "." + simpleName;
        }
        return className;
    }
    
    /**
     * 检查单个类是否带注解（带去重）
     */
    private static void checkClass(String className, Class<?> annotationClass,
                                   List<Class<?>> annotatedClasses, Set<String> seenNames,
                                   List<String> errors, ClassLoader classLoader) {
        if (seenNames.contains(className)) return; // 已处理过，跳过
        
        try {
            Class<?> clazz = Class.forName(className, false, classLoader);
            if (clazz.isAnnotationPresent((Class<? extends java.lang.annotation.Annotation>) annotationClass)) {
                annotatedClasses.add(clazz);
                seenNames.add(className);
                LogUtil.debug("AnnotationScanner", "找到带注解的类: " + className);
            }
        } catch (ClassNotFoundException e) {
            // Class.forName 失败是正常的（内部类等），静默跳过
        } catch (NoClassDefFoundError e) {
            // 依赖缺失，静默跳过，避免扫描崩溃
        } catch (Exception e) {
            errors.add("检查类 [" + className + "] 时发生错误: " + e.getMessage());
        }
    }
}
