package com.web.entity;



public class User {

    private int id;
    private String name;
    private String email;
    private String nickName;
    private int role;

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
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getNickName() {
        return nickName;
    }


    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
