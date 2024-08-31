package com.example.ElearningTLU.Utils;

import com.example.ElearningTLU.Dto.Request.IntrospectRequest;
import com.example.ElearningTLU.Dto.Response.IntrospectResponse;
import com.example.ElearningTLU.Entity.Person;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Data;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
@Component
public final class JWTToken {
    @NonFinal
    @Value("${signerKey}")
    public String SIGNER_KEY;

    public String getUser()
    {
        var auth= SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(auth.getAuthorities());
        return auth.getName();
    }
    public String generateToken(Person person) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(person.getUserName())
                .claim("UserId", person.getPersonId())
                .issuer("thanglong.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(5, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", person.getRole().name())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(this.SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
    public IntrospectResponse INTROSPECT_RESPONSE(IntrospectRequest introspectRequest)
    {
        var token= introspectRequest.getToken();
//
        JWSVerifier jwsVerifier = null;
        try {
            jwsVerifier = new MACVerifier(this.SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            var verified=signedJWT.verify(jwsVerifier);
//            System.out.println(verified.get);
            return IntrospectResponse.builder()
                    .valid(verified && expiryTime.after(new Date()))
                    .build();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
