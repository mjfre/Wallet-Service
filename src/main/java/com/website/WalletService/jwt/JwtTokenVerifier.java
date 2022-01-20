package com.website.WalletService.jwt;


import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import com.website.WalletService.exception.ApiRequestException;
import com.website.WalletService.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//extends OncePerRequestFilter so that is only executed once - sometimes filters can happen multiple times per request
@Component
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final ApplicationUserRepository applicationUserRepository;

    @Autowired
    public JwtTokenVerifier(SecretKey secretKey,
                            JwtConfig jwtConfig,
                            ApplicationUserRepository applicationUserRepository) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.applicationUserRepository = applicationUserRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        //if ... we have a problem, reject
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

        try {

            //get the token
            Jws<Claims> claimsJws =
                    Jwts.parserBuilder()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(token);

            //get the bod of the token
            Claims body = claimsJws.getBody();

            //get the username (i.e. subject) from the token
            String username = body.getSubject();

            //get the authorities from the token body
            List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

            //get the individual authorities
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect((Collectors.toSet()));

            //get authentication
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            );

            //This is to invalidate any users that have been removed from the database before their token expires
            //if user is enabled in application_user table
            if (applicationUserRepository.userExistsByUsername(username)) {
                //set authentication to true
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new ApiRequestException("User \"" + username + "\" is no longer valid.  If you believe this to be an error, please contact an administrator");
            }


        } catch (JwtException e) {
            //TODO: Add more detail about why token can't be trust in exception
            throw new IllegalStateException(String.format("Token %s cannot be verified", token));
        }

        //pass on request and response to next filter
        filterChain.doFilter(request, response);
    }
}
