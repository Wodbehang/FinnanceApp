package com.idk.finnanceapp;

public class User {
    private String username;
    private String path;
    private String password;
    private String email;
    public void User(String username, String path, String email, String password){
        this.username = username;
        this.path = path;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPath() {
        return path;
    }
    public String getPassword() {return password;}
    public String getEmail() {return email;}
    public void setEmail(String email){
        this.email = email;
    }
}
