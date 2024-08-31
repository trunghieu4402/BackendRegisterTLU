//package com.example.ElearningTLU.Config;
//
//import io.jsonwebtoken.io.IOException;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
////@Component
////@Order(Ordered.HIGHEST_PRECEDENCE)
////public class SimpleCORSFilter  implements Filter {
////    @Value("${app.client.url}")
////    private String clientAppUrl = "";
////
////    public SimpleCORSFilter() {
////    }
////
////    @Override
////    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
////            throws IOException, ServletException, java.io.IOException {
////        HttpServletResponse response = (HttpServletResponse) res;
////        HttpServletRequest request = (HttpServletRequest) req;
////        Map<String, String> map = new HashMap<>();
////        String originHeader = request.getHeader("origin");
////        response.setHeader("Access-Control-Allow-Origin", originHeader);
////        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
////        response.setHeader("Access-Control-Max-Age", "3600");
////        response.setHeader("Access-Control-Allow-Headers", "*");
////
////        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
////            response.setStatus(HttpServletResponse.SC_OK);
////        } else {
////            chain.doFilter(req, res);
////        }
////    }
////    @Override
////    public void init(FilterConfig filterConfig) {
////    }
////
////    @Override
////    public void destroy() {
////    }
////}
//@Component
//public class SimpleCORSFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException, java.io.IOException {
//        HttpServletResponse response = (HttpServletResponse) res;
//        HttpServletRequest request = (HttpServletRequest) req;
//
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, content-type");
//
//        chain.doFilter(req, res);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // Khởi tạo filter nếu cần
//    }
//
//    @Override
//    public void destroy() {
//        // Hủy filter nếu cần
//    }
//}
