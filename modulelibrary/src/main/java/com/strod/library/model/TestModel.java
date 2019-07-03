package com.strod.library.model;

/**
 * Created by laiying on 2019/7/2.
 */
public class TestModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "name='" + name + '\'' +
                '}';
    }
}
