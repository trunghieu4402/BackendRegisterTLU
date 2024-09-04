package com.example.ElearningTLU.Entity;

import lombok.Data;

@Data
public class emailResponse {
    private String username;
    private String password;
    final String Link="link.com";
    @Override
    public String toString() {
        return "Thông Tin Tài Khoản: \n"+
                "username: " + username + "\n"+
                "password: " + password + "\n"+
                "Go to website : "+getLink()+"\n";
    }
}
