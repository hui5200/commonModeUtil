package com.ailin.Pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user")
public class User {

    @Id
    private String userId;
    private String name;
    private String sex;
    private String address;
    private int age;

    public User(String userId, String name, String sex, String address, int age) {
        this.userId = userId;
        this.name = name;
        this.sex = sex;
        this.address = address;
        this.age = age;
    }
    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}
