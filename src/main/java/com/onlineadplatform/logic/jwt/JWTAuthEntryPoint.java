package com.onlineadplatform.logic.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineadplatform.logic.AppConstants;
import com.onlineadplatform.logic.controller.AdvertisementController;
import com.onlineadplatform.logic.payload.ClientMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ClientMessageResponse clientMessageResponse;
    public static final Logger logger = LoggerFactory.getLogger(AdvertisementController.class);

    @Autowired
    public JWTAuthEntryPoint(ClientMessageResponse clientMessageResponse) {
        this.clientMessageResponse = clientMessageResponse;
    }

    /*
    * Return info about the invalid credentials to client.
    * */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        clientMessageResponse.setMessage("Invalid login credentials");
        logger.error(clientMessageResponse.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(clientMessageResponse);
        httpServletResponse.setContentType(AppConstants.SECURITY_CONTENT_TYPE);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.getWriter().println(jsonResponse);
    }
}
