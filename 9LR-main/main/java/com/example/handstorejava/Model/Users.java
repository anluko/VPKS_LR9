package com.example.handstorejava.Model;

public class Users {

    private String name, mail, password, image;

    public Users()
    {

    }

    public Users(String name, String mail, String password, String image) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
