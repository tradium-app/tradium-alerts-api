package com.tradiumapp.swingtradealerts.auth.service;

import com.tradiumapp.swingtradealerts.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
//import rs.pscode.pomodorofire.domain.model.UserEntity;
//import rs.pscode.pomodorofire.service.shared.RegisterUserInit;

public interface UserService extends UserDetailsService {

	User registerUser(String accessToken);

}
