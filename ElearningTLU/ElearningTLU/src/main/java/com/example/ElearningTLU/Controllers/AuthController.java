package com.example.ElearningTLU.Controllers;

import com.example.ElearningTLU.Dto.Request.AuthenticationRequest;
import com.example.ElearningTLU.Services.AutheService.AutheServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/authenticate")
@RestController
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private final AutheServiceImpl authService;
@PostMapping("/signup")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
    return this.authService.Authenticate(authenticationRequest);
    }
}
