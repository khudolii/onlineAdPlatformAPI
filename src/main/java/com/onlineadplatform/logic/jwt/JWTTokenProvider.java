package com.onlineadplatform.logic.jwt;

import com.onlineadplatform.logic.AppConstants;
import com.onlineadplatform.logic.entities.ACLUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenProvider {

    public static final Logger logger = LoggerFactory.getLogger(JWTTokenProvider.class);

    /*
    Generate new JWT token for user
    */
    public String generateNewJWTToken(Authentication auth) {
        Date startDateTime = new Date();
        Date expiryDateTime = new Date(startDateTime.getTime() + AppConstants.SECURITY_EXPIRATION_TIME);
        ACLUser user = (ACLUser) auth.getPrincipal();

        if (!ObjectUtils.isEmpty(user)) {
            String userId = String.valueOf(user.getUserId());

            Map<String, Object> claimsMap = new HashMap<>();
            claimsMap.put("id", userId);
            claimsMap.put("userName", user.getUsername());
            claimsMap.put("firstName", user.getFirstName());
            claimsMap.put("lastName", user.getLastName());

            return Jwts.builder()
                    .setSubject(userId)
                    .addClaims(claimsMap)
                    .setIssuedAt(startDateTime)
                    .setExpiration(expiryDateTime)
                    .signWith(SignatureAlgorithm.HS512, AppConstants.SECURITY_SECRET)
                    .compact();
        }
        logger.info("Token didn't create! User is null");
        return null;
    }

    /*
    Validation of token got from request
    */
    public boolean validateJWTToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(AppConstants.SECURITY_SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.error("validateToken: " + e);
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(AppConstants.SECURITY_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return String.valueOf(claims.get("userName"));
    }
}
