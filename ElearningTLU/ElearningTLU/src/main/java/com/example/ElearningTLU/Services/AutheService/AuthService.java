package com.example.ElearningTLU.Services.AutheService;

import com.example.ElearningTLU.Dto.Response.AuthenticateResponse;
import com.example.ElearningTLU.Dto.Request.AuthenticationRequest;
//import com.example.ElearningTLU.Dto.Request.IntrospectRequest;
//import com.example.ElearningTLU.Dto.Response.IntrospectResponse;
import com.example.ElearningTLU.Entity.Person;
import com.example.ElearningTLU.Utils.JWTToken;
import com.example.ElearningTLU.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AutheServiceImpl{
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private JWTToken generateToken;

    public boolean CheckUsername(String email)
    {
        return this.personRepository.findByUserNameOrPersonId(email).isEmpty();
    }
    public ResponseEntity<?> Authenticate(AuthenticationRequest request)
    {
        if(CheckUsername(request.getUserName()))
        {
            return new ResponseEntity<>("Tai Khoan Khong Ton tai",HttpStatus.NOT_FOUND);
        }
        Person person = this.personRepository.findByUserNameOrPersonId(request.getUserName()).get();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate =passwordEncoder.matches(request.getPassword(),person.getPassword());
        if(!authenticate)
        {
            return new ResponseEntity<>("Password is not correct", HttpStatus.NOT_ACCEPTABLE);
        }
        AuthenticateResponse authenticateResponse = new AuthenticateResponse();
        authenticateResponse.setToken(this.generateToken.generateToken(person));
        authenticateResponse.setAuthenticateActive(true);

        return new ResponseEntity<>(authenticateResponse,HttpStatus.OK);
    }

    }


