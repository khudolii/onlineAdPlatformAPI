package com.onlineadplatform.logic.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineadplatform.logic.AppConstants;
import com.onlineadplatform.logic.payload.ClientMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthEntryPoint implements AuthenticationEntryPoint {
    private ClientMessageResponse clientMessageResponse;

    @Autowired
    public JWTAuthEntryPoint(ClientMessageResponse clientMessageResponse) {
        this.clientMessageResponse = clientMessageResponse;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        clientMessageResponse.setMessage("Invalid login credentials");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(clientMessageResponse);
        httpServletResponse.setContentType(AppConstants.SECURITY_CONTENT_TYPE);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.getWriter().println(jsonResponse);
    }
}
