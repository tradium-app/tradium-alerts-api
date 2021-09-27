package com.tradiumapp.swingtradealerts.auth.service.impl;

import com.tradiumapp.swingtradealerts.auth.BuiltInRoleDefinitions;
import com.tradiumapp.swingtradealerts.auth.firebase.FirebaseTokenHolder;
import com.tradiumapp.swingtradealerts.auth.service.FirebaseService;
import com.tradiumapp.swingtradealerts.auth.service.UserService;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service(value = UserServiceImpl.NAME)
public class UserServiceImpl implements UserService {

    public final static String NAME = "UserService";
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByFirebaseUid(username);
        if (user == null) return null;

        Set<GrantedAuthority> grantedAuthorities = BuiltInRoleDefinitions.getAuthoritiesForRole(user.role);

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), grantedAuthorities);
    }

    @Autowired
    FirebaseService firebaseService;

    @Override
    @Transactional
    public User registerUser(final String accessToken) {
        FirebaseTokenHolder tokenHolder = firebaseService.parseToken(accessToken);
        User userLoaded = userRepository.findByFirebaseUid(tokenHolder.getUid());

        if (userLoaded == null) {
            User user = new User();
            user.firebaseUid = tokenHolder.getUid();
            user.name = tokenHolder.getName();
            user.password = UUID.randomUUID().toString();
            user.email = tokenHolder.getEmail();
            user.role = BuiltInRoleDefinitions.ROLE_USER;
            user.imageUrl = tokenHolder.getPicture();

            userRepository.save(user);
            logger.info("registerUser -> user created");
            return user;
        } else {
            return userLoaded;
        }
    }
}
