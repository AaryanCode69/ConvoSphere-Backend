package com.example.convospherebackend.services;

import com.example.convospherebackend.entities.User;
import com.example.convospherebackend.exception.InvalidAuthenticationPrincipalException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtils {

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (!(principal instanceof User creator)) {
            throw new InvalidAuthenticationPrincipalException("Invalid authentication principal");
        }
        return creator;
    }
}
