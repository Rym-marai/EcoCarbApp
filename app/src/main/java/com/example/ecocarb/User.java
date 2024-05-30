package com.example.ecocarb;

public class User {
    public String username;
    public String email;
    public String password;
    public Integer score;



    public User(String username, String email, String password, Integer score) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.score = score;
    }
}