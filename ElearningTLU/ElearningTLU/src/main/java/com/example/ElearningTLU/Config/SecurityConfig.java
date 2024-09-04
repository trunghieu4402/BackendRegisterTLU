package com.example.ElearningTLU.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${signerKey}")
     String SecretKey;
    private String[] PUBLIC_ENDPOINTS={"authenticate/**","chat/**"};
    private String[] PRIVATE_ENDPOINT={"admin/**"};
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        System.out.println(http.getObject());
         http.authorizeHttpRequests(request->
                 request.requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINTS).permitAll()
                         .requestMatchers(HttpMethod.POST,"/websocket/**").permitAll()
                         .requestMatchers(HttpMethod.GET,"/websocket/**").permitAll()
                         .requestMatchers(HttpMethod.GET,PUBLIC_ENDPOINTS).permitAll()
                         .requestMatchers(HttpMethod.POST,PRIVATE_ENDPOINT).hasAuthority("SCOPE_ADMIN")
                         .requestMatchers(HttpMethod.GET,PRIVATE_ENDPOINT).hasAuthority("SCOPE_ADMIN")
                         .requestMatchers(HttpMethod.POST,"teacher/**").hasAuthority("SCOPE_TEACHER")
                         .requestMatchers(HttpMethod.GET,"teacher/**").hasAuthority("SCOPE_TEACHER")
//                         .requestMatchers(HttpMethod.POST,"/admin/**").hasAuthority("SCOPE_ADMIN")
//                         .requestMatchers(HttpMethod.GET,"/student/**").hasAuthority("SCOPE_STUDENT")
//                         .requestMatchers(HttpMethod.GET,"teacher/**").hasAuthority("SCOPE_TEACHER")
                         .anyRequest().authenticated()
         );
         http.oauth2ResourceServer(oauthen->
                 oauthen.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
         );
         http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    @Bean
    JwtDecoder jwtDecoder()
    {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SecretKey.getBytes(),"HS512");
             return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                     .macAlgorithm(MacAlgorithm.HS512)
                     .build()
                     ;
    }
}
