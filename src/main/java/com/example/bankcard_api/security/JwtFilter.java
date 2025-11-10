package com.example.bankcard_api.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.bankcard_api.exception.InvalidJwtException;
import com.example.bankcard_api.service.UserDetailsServiceImpl;
import com.example.bankcard_api.utils.JwtUtill;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtFilter extends OncePerRequestFilter {
    

    private final JwtUtill jwtUtill;
    private final UserDetailsServiceImpl userDetailsService;

    JwtFilter(JwtUtill jwtUtill, UserDetailsServiceImpl userDetailsService){
        this.jwtUtill = jwtUtill;
        this.userDetailsService = userDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        try{
            if (authHeader != null && authHeader.startsWith("Bearer ")){
                token = authHeader.substring(7);
                
                    username = jwtUtill.exctractUsername(token);
                
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails ud = userDetailsService.loadUserByUsername(username);
                if (jwtUtill.validateToken(token)) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            
        } catch (ExpiredJwtException e) {
            throw new InvalidJwtException("JWT token expired", e);
        } catch (MalformedJwtException e) {
            throw new InvalidJwtException("JWT token malformed", e);
        } catch (UnsupportedJwtException e) {
            throw new InvalidJwtException("JWT token type unsupported", e);
        } catch (SignatureException e) {
            throw new InvalidJwtException("Invalid JWT signature", e);
        } catch (IllegalArgumentException e) {
            throw new InvalidJwtException("JWT token missing or empty", e);
        }

        filterChain.doFilter(request, response);
    }
}
