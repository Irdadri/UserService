package com.example.userservice.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

@Log
public class AuthEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException
    {
        String ErrMsg = "Userid e/o Password non corrette!";

        log.warning("Errore Sicurezza: " + authException.getMessage());

        // Authentication failed, send error response.
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");

        PrintWriter writer = response.getWriter();
        writer.println(ErrMsg);
    }

    @Override
    public void afterPropertiesSet()
    {
        setRealmName("REAME");
        super.afterPropertiesSet();
    }
}
