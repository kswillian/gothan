package com.kaminski.gothan.model;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String name;
    private String cpf;
    private String email;
    private String password;
    private String imgUrl;
    private int yearsOld;

    public User() {
    }

    public User(String id, String name, String cpf, String email, String password, String imgUrl, int yearsOld) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.imgUrl = imgUrl;
        this.yearsOld = yearsOld;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getYearsOld() {
        return yearsOld;
    }

    public void setYearsOld(int yearsOld) {
        this.yearsOld = yearsOld;
    }

    public void register(){

    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", yearsOld=" + yearsOld +
                '}';
    }
}
