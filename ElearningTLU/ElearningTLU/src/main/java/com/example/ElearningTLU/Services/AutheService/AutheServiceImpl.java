package com.example.ElearningTLU.Services.AutheService;

import com.example.ElearningTLU.Dto.Request.AuthenticationRequest;
import com.example.ElearningTLU.Dto.Request.IntrospectRequest;
import com.example.ElearningTLU.Dto.Response.IntrospectResponse;
import org.springframework.http.ResponseEntity;

public interface AutheServiceImpl {
    public boolean CheckUsername(String email);
    public ResponseEntity<?> Authenticate(AuthenticationRequest request);
//    public IntrospectResponse INTROSPECT_RESPONSE(IntrospectRequest introspectRequest);
}
