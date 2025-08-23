package com.example.classloader;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author zrq
 * @time 2025/8/23 15:30
 * @description
 */
public class ResourceLoader {

    /**
     * 获取指定包名及其子包下的所有Class文件。
     *
     * @param packageName 包名，例如："com.example.app"
     * @return 包含所有Class文件的Set集合
     */
    public static Set<Class<?>> getClasses(String packageName) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        String packageDirName = packageName.replace('.', '/');

        try {
            // 获取当前线程的类加载器
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            // 通过类加载器获取包下的所有URL资源
            Enumeration<URL> urls = classLoader.getResources(packageDirName);

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();

                if ("file".equals(protocol)) {
                    // 如果是文件系统路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassesInDir(packageName, filePath, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是JAR包
                    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    findClassesInJar(jar, packageDirName, classes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * 在文件系统中递归查找Class文件。
     */
    private static void findClassesInDir(String packageName, String packagePath, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] dirfiles = dir.listFiles(file -> (file.isDirectory() || file.getName().endsWith(".class")));

        if (dirfiles == null) {
            return;
        }

        for (File file : dirfiles) {
            if (file.isDirectory()) {
                // 递归遍历子目录
                findClassesInDir(packageName + "." + file.getName(), file.getAbsolutePath(), classes);
            } else {
                // 如果是Class文件，则加载它
                // 去掉.class后缀
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 在JAR包中查找Class文件。
     */
    private static void findClassesInJar(JarFile jar, String packageDirName, Set<Class<?>> classes) {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }

            if (name.startsWith(packageDirName) && name.endsWith(".class") && !entry.isDirectory()) {
                String className = name.substring(0, name.length() - 6).replace('/', '.');
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
