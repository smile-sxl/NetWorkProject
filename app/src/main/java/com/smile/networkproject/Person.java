package com.smile.networkproject;

/**
 * author: smile .
 * date: On 2018/8/25
 */
public class Person {

    String name;
    int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "name:'" + name + ", age:" + age;
    }
}
