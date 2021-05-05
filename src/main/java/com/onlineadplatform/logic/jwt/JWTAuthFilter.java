package com.onlineadplatform.logic.jwt;

import com.onlineadplatform.logic.AppConstants;
import com.onlineadplatform.logic.controller.AdvertisementController;
import com.onlineadplatform.logic.entities.ACLUser;
import com.onlineadplatform.logic.service.ACLUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JWTTokenProvider jwtTokenProvider;
    private final ACLUserDetailsService aclUserDetailsService;
    public static final Logger logger = LoggerFactory.getLogger(JWTAuthFilter.class);

    @Autowired
    public JWTAuthFilter(JWTTokenProvider jwtTokenProvider, ACLUserDetailsService aclUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.aclUserDetailsService = aclUserDetailsService;
    }

    /*
    * Filtering the request for user authorization. Checking the received token for correctness.
    * */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwToken = getJWTFromRequest(httpServletRequest);
            if (StringUtils.hasText(jwToken) && jwtTokenProvider.validateJWTToken(jwToken)) {
                String userName = jwtTokenProvider.getUserIdFromToken(jwToken);
                logger.info("Start validation to user - " + userName);
                UserDetails user = aclUserDetailsService.loadUserByUsername(userName);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            logger.error("doFilterInternal: " + e);
        }
        logger.info("User can authorization.");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /*
    Getting a token from a request. With a security header and token prefix.
    */
    private String getJWTFromRequest(HttpServletRequest request) {
        String token = request.getHeader(AppConstants.SECURITY_HEADER_STRING);
        if (StringUtils.hasText(token) && token.startsWith(AppConstants.SECURITY_TOKEN_PREFIX)) {
            return token.split(" ")[1];
        }
        return null;
    }
}
