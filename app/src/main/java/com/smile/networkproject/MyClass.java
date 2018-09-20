package com.smile.networkproject;

/**
 * author: smile .
 * date: On 2018/9/2
 */
public class MyClass {

    private String className;
    private Class<?> myClass;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class getMyClass() {
        return myClass;
    }

    public void setMyClass(Class<?> myClass) {
        this.myClass = myClass;
    }

    public MyClass(String className, Class<?> myClass) {
        this.className = className;
        this.myClass = myClass;
    }
}
