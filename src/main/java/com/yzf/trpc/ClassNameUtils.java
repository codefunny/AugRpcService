package com.yzf.trpc;

public class ClassNameUtils {
    public static String getClassName(Class clazz) {
        return clazz.getName();
    }

    public static String getOuterClassName(Class clazz) {
        int spliterIndex = clazz.getName().lastIndexOf("$");
        return spliterIndex > 0 ? clazz.getName().substring(0, spliterIndex) : clazz.getName();
    }
}
