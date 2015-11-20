package com.jd.o2o;

import java.io.Serializable;

/**
 * Created by wangdongxing on 15-11-19.
 */
public class Stu implements Serializable {
    private String name;
    private int age;

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
}
