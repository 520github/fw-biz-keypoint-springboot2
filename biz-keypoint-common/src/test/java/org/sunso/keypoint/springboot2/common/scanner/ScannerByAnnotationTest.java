package org.sunso.keypoint.springboot2.common.scanner;

import java.lang.reflect.Method;
import java.net.URISyntaxException;

/**
 * @author sunso520
 * @Title:ScannerByAnnotationTest
 * @Description: <br>
 * @Created on 2024/4/7 16:04
 */
public class ScannerByAnnotationTest {

    public static void main(String[] args) throws URISyntaxException, ClassNotFoundException {
        String packageName = "org.sunso.keypoint.springboot2.common.scanner";
        new ScannerByAnnotation().scanPackageName(packageName, new ScannerAnnotationHandler() {
            @Override
            public boolean classContainAnnotation(Class<?> clazz) {
                return true;
            }

            @Override
            public boolean methodContainAnnotation(Class<?> clazz, Method method) {
                return method.isAnnotationPresent(Scanner.class);
            }

            @Override
            public void doWithContainAnnotation(Class<?> clazz, Method method) {
                Scanner scanner = method.getAnnotation(Scanner.class);
                System.out.println(String.format("class[%s], key[%s], name[%s], parentId[%d]",
                        clazz.getName(), scanner.key(), scanner.name(), scanner.parentId()));
            }
        });
    }
}
