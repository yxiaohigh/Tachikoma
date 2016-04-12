package com.h.tachikoma.entity;

import android.databinding.BaseObservable;

/**
 * Created by tony on 2016/4/11.
 */
public class Student extends BaseObservable {
    private String name;
    private String addr;

    public Student()  {
    }

    public Student(String name, String addr) {
        this.name = name;
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return this.addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
