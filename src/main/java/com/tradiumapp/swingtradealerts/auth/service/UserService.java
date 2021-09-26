package com.tradiumapp.swingtradealerts.auth.service;

import com.tradiumapp.swingtradealerts.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	User registerUser(String accessToken);

}
