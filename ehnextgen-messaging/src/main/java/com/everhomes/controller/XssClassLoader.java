package com.everhomes.controller;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

public class XssClassLoader extends ClassLoader {

    public static final String SUFFIX = ".class";

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        String path = packageToPath(name) + SUFFIX;
        InputStream resource = getResourceAsStream(path);

        if (name.startsWith("java.")) {
            return super.loadClass(name);
        }

        try {
            byte[] bytes = StreamUtils.copyToByteArray(resource);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.loadClass(name);
    }

    private String packageToPath(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }
}
