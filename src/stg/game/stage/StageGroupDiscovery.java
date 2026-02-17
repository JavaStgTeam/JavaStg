package stg.game.stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import stg.game.util.AnnotationScanner;

/**
 * 关卡组发现器 - 负责发现所有StageGroup的子类
 * @since 2026-02-16
 */
public class StageGroupDiscovery {
    
    /**
     * 发现所有StageGroup的子类
     * @return 发现的StageGroup子类列表
     * @throws IOException 当扫描过程中发生IO异常时
     */
    public List<Class<?>> discoverStageGroupClasses() throws IOException {
        List<Class<?>> discoveredClasses = new ArrayList<>();
        
        System.out.println("开始发现关卡组...");
        
        // 1. 首先尝试使用注解扫描发现关卡组
        discoveredClasses.addAll(discoverByAnnotation());
        
        // 2. 如果没有找到关卡组，尝试使用传统方法
        if (discoveredClasses.isEmpty()) {
            discoveredClasses.addAll(discoverByTraditionalLoading());
            
            // 3. 如果仍然没有找到关卡组，尝试扫描文件系统
            if (discoveredClasses.isEmpty()) {
                discoveredClasses.addAll(discoverByFilesystem());
            }
        }
        
        System.out.println("发现关卡组完成，共发现 " + discoveredClasses.size() + " 个关卡组类");
        return discoveredClasses;
    }
    
    /**
     * 通过注解扫描发现关卡组
     * @return 发现的关卡组类列表
     */
    private List<Class<?>> discoverByAnnotation() {
        List<Class<?>> classes = new ArrayList<>();
        
        System.out.println("尝试使用注解扫描发现关卡组...");
        try {
            List<Class<?>> annotatedClasses = AnnotationScanner.scanClassesWithAnnotation("user.stageGroup", StageGroupInfo.class);
            
            for (Class<?> clazz : annotatedClasses) {
                if (isValidStageGroupClass(clazz)) {
                    classes.add(clazz);
                    System.out.println("通过注解扫描发现关卡组类: " + clazz.getName());
                }
            }
        } catch (Exception e) {
            System.err.println("注解扫描失败: " + e.getMessage());
        }
        
        return classes;
    }
    
    /**
     * 通过传统类加载方式发现关卡组
     * @return 发现的关卡组类列表
     */
    private List<Class<?>> discoverByTraditionalLoading() {
        List<Class<?>> classes = new ArrayList<>();
        
        System.out.println("注解扫描未发现关卡组，尝试使用传统方法...");
        
        // 直接尝试加载已知的关卡组类
        String[] stageGroupClasses = {
            "user.stageGroup.CustomStageGroup"
        };
        
        for (String className : stageGroupClasses) {
            try {
                Class<?> clazz = Class.forName(className);
                if (isValidStageGroupClass(clazz)) {
                    classes.add(clazz);
                    System.out.println("通过传统加载发现关卡组类: " + className);
                }
            } catch (ClassNotFoundException e) {
                // 类未找到，继续尝试其他类
            }
        }
        
        return classes;
    }
    
    /**
     * 通过文件系统扫描发现关卡组
     * @return 发现的关卡组类列表
     * @throws IOException 当扫描过程中发生IO异常时
     */
    private List<Class<?>> discoverByFilesystem() throws IOException {
        List<Class<?>> classes = new ArrayList<>();
        
        System.out.println("直接加载失败，尝试使用文件系统扫描发现关卡组...");
        
        // 扫描多个包
        String[] packageNames = {
            StageGroup.class.getPackage().getName(),
            "user.stageGroup"
        };
        
        for (String packageName : packageNames) {
            String packagePath = packageName.replace('.', '/');
            
            // 获取包下的所有类文件
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();
                
                if ("file".equals(protocol)) {
                    String filePath = resource.getFile();
                    File directory = new File(filePath);
                    
                    if (directory.exists()) {
                        File[] files = directory.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                if (file.isFile() && file.getName().endsWith(".class")) {
                                    String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                                    try {
                                        Class<?> clazz = Class.forName(className);
                                        if (isValidStageGroupClass(clazz)) {
                                            classes.add(clazz);
                                            System.out.println("通过文件系统扫描发现关卡组类: " + className);
                                        }
                                    } catch (ClassNotFoundException e) {
                                        // 类未找到，继续尝试其他类
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return classes;
    }
    
    /**
     * 检查是否是有效的StageGroup子类
     * @param clazz 要检查的类
     * @return 如果是有效的StageGroup子类则返回true
     */
    private boolean isValidStageGroupClass(Class<?> clazz) {
        return StageGroup.class.isAssignableFrom(clazz) && 
               !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()) && 
               clazz != StageGroup.class;
    }
}