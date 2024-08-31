package com.example.ElearningTLU.Dto.Request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String userName;
    private String password;
}
