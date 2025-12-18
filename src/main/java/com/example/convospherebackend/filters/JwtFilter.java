package com.example.convospherebackend.filters;

import com.example.convospherebackend.entities.User;
import com.example.convospherebackend.services.JwtService;
import com.example.convospherebackend.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    private final UserService userService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException , IOException {
        try{
            final String authorizationHeader = request.getHeader("Authorization");
            if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
                String token = authorizationHeader.substring(7);;
                String id = jwtService.getUserIdFromToken(token);
                User user = userService.getUserById(id);
                if(user != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request,response);
        }catch (Exception e){
            exceptionResolver.resolveException(request,response,null,e);
        }
    }
}
