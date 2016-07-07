package com.everhomes.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.core.io.ClassPathResource;

public class AllJunitTestCase extends TestCase {
    public static Test suite() throws Exception {
        String currentPath = "com/everhomes/test/junit";
        ClassPathResource resource = new ClassPathResource(currentPath);
        String filePath = resource.getFile().getAbsolutePath();
        String rootPath = filePath.substring(0, filePath.length() - currentPath.length());

        List<Class<?>> classList = new ArrayList<Class<?>>();
        loadAllTestClass(rootPath, currentPath, classList);

        TestSuite ts = new TestSuite(currentPath);
        for (Class<?> cls : classList) {
            ts.addTest(new JUnit4TestAdapter(cls));
            // ts.addTestSuite(cls);
        }

        return ts;
    }

    private static void loadAllTestClass(String rootPath, String currentPath, List<Class<?>> classList)
        throws Exception {
        String suffix = ".class";
        if (currentPath.endsWith(".class")) {
            String classPath = currentPath.substring(0, currentPath.length() - suffix.length());
            classPath = classPath.replaceAll("/", ".");
            Class<?> cls = Class.forName(classPath);
            classList.add(cls);
            System.out.println("load class: " + classPath);
        } else {
            File currentFile = new File(rootPath, currentPath);
            for (File file : currentFile.listFiles()) {
                String filePath = currentPath + "/" + file.getName();
                loadAllTestClass(rootPath, filePath, classList);
            }
        }
    }
}
