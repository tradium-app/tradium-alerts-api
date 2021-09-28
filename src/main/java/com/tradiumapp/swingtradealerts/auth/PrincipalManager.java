package com.tradiumapp.swingtradealerts.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class PrincipalManager {
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User){
            String userId = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
            return userId;
        } else {
            return null;
        }
    }
}
