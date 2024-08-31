//package com.example.ElearningTLU.config;
//
//
//import com.example.ElearningTLU.Filter.JwtRequestFilter;
//import com.example.ElearningTLU.Services.UserService.UserDetailsServiceImpl;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class WebSecurityConfiguration {
//    private final JwtRequestFilter requestFilter;
//    private UserDetailsServiceImpl uerService;
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf(AbstractHttpConfigurer::disable)
//                .addFilterBefore(requestFilter,UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests(request->
//                        request.requestMatchers("/thanglong/**")
//                                .permitAll()
////                                .requestMatchers(HttpMethod.POST,"/admin/**").hasAuthority(Role.ADMIN.name())
////                                .requestMatchers(HttpMethod.PUT,"/admin/**").hasAuthority(Role.ADMIN.name())
////                                .requestMatchers(HttpMethod.DELETE,"/admin/**").hasAuthority(Role.ADMIN.name())
////                                .requestMatchers(HttpMethod.GET,"/customer/**").hasAuthority(Role.CUSTOMER.name())
//                                .anyRequest()
//                                .authenticated())
//                .sessionManagement(s -> s
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//}
