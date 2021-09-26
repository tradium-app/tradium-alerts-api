package com.tradiumapp.swingtradealerts.auth.service.impl;

import com.tradiumapp.swingtradealerts.auth.BuiltInRoleDefinitions;
import com.tradiumapp.swingtradealerts.auth.firebase.FirebaseTokenHolder;
import com.tradiumapp.swingtradealerts.auth.service.FirebaseService;
import com.tradiumapp.swingtradealerts.auth.service.UserService;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.repositories.RoleRepository;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.UUID;

@Service(value = UserServiceImpl.NAME)
public class UserServiceImpl implements UserService {

    public final static String NAME = "UserService";
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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
//    @Secured(value = BuiltInRoleDefinitions.ROLE_ANONYMOUS)
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

    @PostConstruct
    public void init() {

//		if (userDao.count() == 0) {
//			UserEntity adminEntity = new UserEntity();
//			adminEntity.setUsername("admin");
//			adminEntity.setPassword("admin");
//			adminEntity.setEmail("savic.prvoslav@gmail.com");
//
//			adminEntity.setAuthorities(getAdminRoles());
//			userDao.save(adminEntity);
//
//			UserEntity userEntity = new UserEntity();
//			userEntity.setUsername("user1");
//			userEntity.setPassword("user1");
//			userEntity.setEmail("savic.prvoslav@gmail.com");
//			userEntity.setAuthorities(getUserRoles());
//
//			userDao.save(userEntity);
//		}
    }

//	private List<Role> getAdminRoles() {
//		return Collections.singletonList(getRole(SecurityConfig.Roles.ROLE_ADMIN));
//	}
//
//	private List<Role> getUserRoles() {
//		BuiltInRoleDefinitions.BuiltInRole.get
//		return Collections.singletonList(getRole(SecurityConfig.Roles.ROLE_USER));
//	}

    /**
     * Get or create role
     * @param authority
     * @return
     */
//	private Role getRole(String authority) {
//		Role adminRole = roleRepository.findByAuthority(authority);
//		if (adminRole == null) {
//			return new Role(authority);
//		} else {
//			return adminRole;
//		}
//	}

}
