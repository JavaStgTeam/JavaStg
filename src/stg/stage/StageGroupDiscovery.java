package stg.stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import stg.util.LogUtil;

/**
 * 关卡组发现器 - 负责发现所有 StageGroup 的子类
 * @since 2026-02-16
 * @date 2026-02-26 优化关卡扫描逻辑
 */
public class StageGroupDiscovery {
    
    /** 扫描的包名数组（支持多包扫描） */
    private static final String[] SCAN_PACKAGES = {
        "user.stageGroup"
    };
    
    private final List<String> warnings = new ArrayList<>();
    private final List<String> errors   = new ArrayList<>();
    
    /**
     * 发现所有 StageGroup 的子类，按 order 排序
     * @return 排序后的 StageGroup 子类列表（永不返回 null）
     */
    public List<Class<?>> discoverStageGroupClasses() {
        List<Class<?>> discovered = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        
        for (String pkg : SCAN_PACKAGES) {
            List<Class<?>> found = scanPackage(pkg, seen);
            discovered.addAll(found);
        }
        
        // ─────────────────────────────────────────
        // 排序：按 order 字段升序，未设置 order 按类名排序
        // ─────────────────────────────────────────
        List<Class<?>> sorted = sortByOrder(discovered);
        
        // ─────────────────────────────────────────
        // 打印扫描报告
        // ─────────────────────────────────────────
        printDiscoveryReport(sorted);
        
        return sorted;
    }
    
    /**
     * 扫描单个包，返回所有有效的 StageGroup 子类
     */
    private List<Class<?>> scanPackage(String packageName, Set<String> seen) {
        List<Class<?>> classes = new ArrayList<>();
        
        // ─────────────────────────────────────────
        // Step 1: 注解扫描（主方式）
        // ─────────────────────────────────────────
        List<Class<?>> annotated = stg.util.AnnotationScanner.scanClassesWithAnnotation(
            packageName, StageGroupInfo.class);
        
        for (Class<?> clazz : annotated) {
            if (isValidStageGroupClass(clazz)) {
                if (seen.add(clazz.getName())) {
                    classes.add(clazz);
                    LogUtil.debug("StageGroupDiscovery", "注解扫描发现: " + clazz.getName());
                }
            }
        }
        
        // ─────────────────────────────────────────
        // Step 2: 直接遍历 classpath 兜底（防止注解扫描漏扫）
        // ─────────────────────────────────────────
        scanClasspathForPackage(packageName, classes, seen);
        
        return classes;
    }
    
    /**
     * 遍历整个 classpath，直接查找 .class 文件（兜底扫描）
     * 兼容 IDE 开发环境和打包后的 JAR
     */
    private void scanClasspathForPackage(String packageName, List<Class<?>> out, Set<String> seen) {
        String classpath = System.getProperty("java.class.path");
        if (classpath == null) return;
        
        String packagePath = packageName.replace('.', '/').toLowerCase();
        
        for (String pathEntry : classpath.split(File.pathSeparator)) {
            File entry = new File(pathEntry);
            
            if (entry.isDirectory()) {
                // 文件系统目录：递归找包路径下的 .class 文件
                scanDirectoryForPackage(entry, entry, packageName, packagePath, out, seen);
            } else if (entry.getName().endsWith(".jar")) {
                // JAR 文件：直接扫描 jar 内的类
                scanJarForPackage(entry, packageName, packagePath, out, seen);
            }
        }
    }
    
    /**
     * 递归扫描文件系统目录
     */
    private void scanDirectoryForPackage(File root, File dir, String packageName,
                                         String packagePathLower, List<Class<?>> out, Set<String> seen) {
        if (!dir.exists() || !dir.isDirectory()) return;
        
        File[] files = dir.listFiles();
        if (files == null) return;
        
        // 计算相对路径
        String relPath = dir.getAbsolutePath().substring(root.getAbsolutePath().length())
                               .replace(File.separatorChar, '/');
        if (relPath.startsWith("/")) relPath = relPath.substring(1);
        
        // 非目标包则跳过（大小写不敏感）
        if (!relPath.isEmpty() && !relPath.toLowerCase().startsWith(packagePathLower)) {
            return;
        }
        
        for (File f : files) {
            if (f.isDirectory()) {
                scanDirectoryForPackage(root, f, packageName, packagePathLower, out, seen);
            } else if (f.getName().endsWith(".class")) {
                String simpleName = f.getName().substring(0, f.getName().length() - 6);
                String fullClassName = (relPath.isEmpty() ? packageName : relPath.replace('/', '.') + "." + simpleName);
                fullClassName = normalizeClassName(fullClassName);
                
                tryLoadAndAdd(fullClassName, out, seen);
            }
        }
    }
    
    /**
     * 扫描单个 JAR 文件内的指定包
     */
    private void scanJarForPackage(File jarFile, String packageName,
                                   String packagePathLower, List<Class<?>> out, Set<String> seen) {
        JarFile jar = null;
        try {
            jar = new JarFile(jarFile);
            Enumeration<JarEntry> entries = jar.entries();
            
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                
                if (!entry.isDirectory() && entryName.endsWith(".class")
                    && entryName.toLowerCase().startsWith(packagePathLower)) {
                    
                    String className = entryName.substring(0, entryName.length() - 6)
                                              .replace('/', '.');
                    className = normalizeClassName(className);
                    tryLoadAndAdd(className, out, seen);
                }
            }
        } catch (Exception e) {
            errors.add("扫描 JAR [" + jarFile.getName() + "] 失败: " + e.getMessage());
        } finally {
            if (jar != null) try { jar.close(); } catch (Exception ignored) {}
        }
    }
    
    /**
     * 标准化类名大小写（首字母大写，其余保持原样）
     */
    private String normalizeClassName(String className) {
        int dot = className.lastIndexOf('.');
        if (dot > 0 && dot < className.length() - 1) {
            return className.substring(0, dot) + "." 
                 + Character.toUpperCase(className.charAt(dot + 1))
                 + className.substring(dot + 2);
        }
        return className;
    }
    
    /**
     * 尝试加载类并添加到结果列表（有效 StageGroup 子类才加）
     */
    private void tryLoadAndAdd(String className, List<Class<?>> out, Set<String> seen) {
        if (!seen.add(className)) return; // 已处理过
        
        try {
            Class<?> clazz = Class.forName(className, false, 
                Thread.currentThread().getContextClassLoader());
            
            if (isValidStageGroupClass(clazz)) {
                out.add(clazz);
                LogUtil.debug("StageGroupDiscovery", "文件系统扫描发现: " + className);
            }
        } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
            // 正常情况，内部类或依赖缺失
        } catch (Exception e) {
            warnings.add("加载类 [" + className + "] 失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查是否是有效的 StageGroup 子类（非抽象、非接口、不是 StageGroup 自身）
     */
    private boolean isValidStageGroupClass(Class<?> clazz) {
        return StageGroup.class.isAssignableFrom(clazz)
            && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())
            && !clazz.isInterface()
            && clazz != StageGroup.class;
    }
    
    /**
     * 按 StageGroupInfo.order 排序，数值越小越靠前
     * 未设置 order（默认0）或数值相同，按类名字母序
     */
    private List<Class<?>> sortByOrder(List<Class<?>> classes) {
        List<Class<?>> sorted = new ArrayList<>(classes);
        sorted.sort(new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> a, Class<?> b) {
                int orderA = getOrderValue(a);
                int orderB = getOrderValue(b);
                if (orderA != orderB) {
                    return Integer.compare(orderA, orderB);
                }
                return a.getName().compareTo(b.getName()); // 稳定排序
            }
        });
        return sorted;
    }
    
    /**
     * 获取类的 order 值（注解中定义，默认0）
     */
    private int getOrderValue(Class<?> clazz) {
        try {
            StageGroupInfo info = clazz.getAnnotation(StageGroupInfo.class);
            return (info != null) ? info.order() : 0;
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * 打印发现报告
     */
    private void printDiscoveryReport(List<Class<?>> discovered) {
        System.out.println("═══════════════════════════════════════");
        System.out.println("   关卡组自动发现报告");
        System.out.println("═══════════════════════════════════════");
        System.out.println("  扫描包: " + java.util.Arrays.toString(SCAN_PACKAGES));
        System.out.println("  发现关卡组数量: " + discovered.size());
        
        if (!discovered.isEmpty()) {
            System.out.println("  排序后的关卡组列表:");
            for (int i = 0; i < discovered.size(); i++) {
                Class<?> c = discovered.get(i);
                int order = getOrderValue(c);
                System.out.printf("    [%2d] order=%d  %s%n", i + 1, order, c.getSimpleName());
            }
        }
        
        if (!warnings.isEmpty()) {
            System.out.println("  ⚠  警告 (" + warnings.size() + "):");
            for (String w : warnings) {
                System.out.println("    - " + w);
            }
        }
        
        if (!errors.isEmpty()) {
            System.out.println("  ❌ 错误 (" + errors.size() + "):");
            for (String e : errors) {
                System.out.println("    - " + e);
            }
        }
        
        System.out.println("═══════════════════════════════════════");
        
        if (discovered.isEmpty()) {
            System.err.println("【严重】未发现任何关卡组！请检查:");
            System.err.println("  1. user.stageGroup 包下是否有继承 StageGroup 的类");
            System.err.println("  2. 这些类是否标记了 @StageGroupInfo 注解");
            System.err.println("  3. 类是否可以成功编译（检查编译错误）");
        }
    }
}