package com.example.fraud.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiLoggingFilter extends OncePerRequestFilter {
    private  static final Logger log = LoggerFactory.getLogger(ApiLoggingFilter.class);

    @Override
    protected  void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )throws ServletException, IOException{
        long start = System.currentTimeMillis();

        try{
            filterChain.doFilter(request,response);
        }finally {
            long duration =System.currentTimeMillis() - start;

            log.info("ApiRequest: method={} uri={} status={} durationMs={} traceId={} txnId={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration,
                    MDC.get("traceId"),
                    MDC.get("transactionId")
            );
        }
    }



}
