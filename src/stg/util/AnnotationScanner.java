package stg.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import stg.util.LogUtil;

/**
 * 注解扫描工具类
 * 用于扫描指定包下带有特定注解的类
 */
public class AnnotationScanner {

    /**
     * 扫描指定包下带有特定注解的类
     * @param packageName 包名
     * @param annotationClass 注解类
     * @return 带有指定注解的类列表
     */
    public static List<Class<?>> scanClassesWithAnnotation(String packageName, Class<?> annotationClass) {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        
        try {
            // 获取类加载器
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = AnnotationScanner.class.getClassLoader();
            }
            
            // 将包名转换为路径
            String packagePath = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(packagePath);
            
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();
                
                if ("file".equals(protocol)) {
                    // 处理文件系统中的类
                    String filePath = URLDecoder.decode(resource.getFile(), "UTF-8");
                    scanDirectory(new File(filePath), packageName, annotationClass, annotatedClasses, classLoader);
                } else if ("jar".equals(protocol)) {
                    // 处理JAR包中的类
                    String jarPath = resource.getPath().substring(5, resource.getPath().indexOf('!'));
                    JarFile jarFile = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                    scanJarFile(jarFile, packagePath, packageName, annotationClass, annotatedClasses, classLoader);
                    jarFile.close();
                }
            }
        } catch (Exception e) {
            LogUtil.error("AnnotationScanner", "扫描注解类时发生错误", e);
        }
        
        return annotatedClasses;
    }
    
    /**
     * 扫描目录中的类
     * @param directory 目录
     * @param packageName 包名
     * @param annotationClass 注解类
     * @param annotatedClasses 结果列表
     * @param classLoader 类加载器
     */
    private static void scanDirectory(File directory, String packageName, Class<?> annotationClass, 
                                     List<Class<?>> annotatedClasses, ClassLoader classLoader) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }
        
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        
        for (File file : files) {
            if (file.isDirectory()) {
                // 递归扫描子目录
                String subPackageName = packageName + "." + file.getName();
                scanDirectory(file, subPackageName, annotationClass, annotatedClasses, classLoader);
            } else if (file.getName().endsWith(".class")) {
                // 处理类文件
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                checkClass(className, annotationClass, annotatedClasses, classLoader);
            }
        }
    }
    
    /**
     * 扫描JAR文件中的类
     * @param jarFile JAR文件
     * @param packagePath 包路径
     * @param packageName 包名
     * @param annotationClass 注解类
     * @param annotatedClasses 结果列表
     * @param classLoader 类加载器
     */
    private static void scanJarFile(JarFile jarFile, String packagePath, String packageName, 
                                  Class<?> annotationClass, List<Class<?>> annotatedClasses, 
                                  ClassLoader classLoader) {
        Enumeration<JarEntry> entries = jarFile.entries();
        
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            
            if (entryName.startsWith(packagePath) && entryName.endsWith(".class")) {
                // 处理类文件
                String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
                checkClass(className, annotationClass, annotatedClasses, classLoader);
            }
        }
    }
    
    /**
     * 检查类是否带有指定注解
     * @param className 类名
     * @param annotationClass 注解类
     * @param annotatedClasses 结果列表
     * @param classLoader 类加载器
     */
    private static void checkClass(String className, Class<?> annotationClass, 
                                  List<Class<?>> annotatedClasses, ClassLoader classLoader) {
        try {
            Class<?> clazz = Class.forName(className, false, classLoader);
            // 直接检查注解
            if (clazz.isAnnotationPresent((Class<? extends java.lang.annotation.Annotation>) annotationClass)) {
                annotatedClasses.add(clazz);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("加载类失败: " + className);
        } catch (Exception e) {
            System.err.println("检查类注解时发生错误: " + className);
        }
    }
}
