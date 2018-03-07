package com.sd.demo.domain;

/**
 * Created by sunda on 17-6-13.
 */
public class User {

    private int id;

    private int nameByteLen;
    private String name;


    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getNameByteLen() {
        return nameByteLen;
    }

    public void setNameByteLen(int nameByteLen) {
        this.nameByteLen = nameByteLen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameByteLen = name.length();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
