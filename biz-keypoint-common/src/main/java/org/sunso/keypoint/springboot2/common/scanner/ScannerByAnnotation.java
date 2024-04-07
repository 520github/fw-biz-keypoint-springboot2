package org.sunso.keypoint.springboot2.common.scanner;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author sunso520
 * @Title:ScannerByAnnotation
 * @Description: <br>
 * @Created on 2024/4/7 15:45
 */
public class ScannerByAnnotation {

    public void scanPackageName(String packageName, ScannerAnnotationHandler handler) throws URISyntaxException, ClassNotFoundException {
        URL resource = ClassLoader.getSystemClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File packageDir = new File(resource.toURI());
        File[] files = packageDir.listFiles();
        for(File file: files) {
            scanFile(file, handler);
        }
    }

    public void scanFile(File file, ScannerAnnotationHandler handler) throws ClassNotFoundException {
        if (file.listFiles() != null) {
            for(File subFile: file.listFiles()) {
                scanFile(subFile, handler);
            }
            return;
        }
        String path = file.getPath();
        if (!path.endsWith(".class")) {
            return;
        }
        String tag = "classes/";
        int index = path.indexOf(tag);
        String className = path.substring(index + tag.length())
                .replaceAll("/", "\\.");
        className = className.substring(0, className.length() -6);
        Class<?> clazz = Class.forName(className);
        scanClass(clazz, handler);
    }

    public void scanClass(Class<?> clazz, ScannerAnnotationHandler handler) {
        if (!handler.classContainAnnotation(clazz)) {
            return;
        }
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method: methods) {
            if (!handler.methodContainAnnotation(clazz, method)) {
                continue;
            }
            handler.doWithContainAnnotation(clazz, method);
        }
    }
}
