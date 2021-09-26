package com.tradiumapp.swingtradealerts.auth.service.impl;

import com.tradiumapp.swingtradealerts.auth.BuiltInRoleDefinitions;
import com.tradiumapp.swingtradealerts.auth.SecurityConfig;
import com.tradiumapp.swingtradealerts.auth.service.UserService;
//import org.apache.log4j.Logger;
import com.tradiumapp.swingtradealerts.auth.Role;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.repositories.RoleRepository;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import rs.pscode.pomodorofire.config.SecurityConfig.Roles;
//import rs.pscode.pomodorofire.domain.dao.RoleRepository;
//import rs.pscode.pomodorofire.domain.dao.UserRepository;
//import rs.pscode.pomodorofire.domain.model.RoleEntity;
//import rs.pscode.pomodorofire.domain.model.UserEntity;
//import rs.pscode.pomodorofire.service.UserService;
//import rs.pscode.pomodorofire.service.shared.RegisterUserInit;

import javax.annotation.PostConstruct;
//import javax.transaction.Transactional;
import java.util.*;

@Service(value = UserServiceImpl.NAME)
public class UserServiceImpl implements UserService {

	public final static String NAME = "UserService";
	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		throw new UsernameNotFoundException("ss");
//		UserDetails userDetails = userRepository.findByUsername(username);
//		if (userDetails == null)
//			return null;
//
//		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
//		for (GrantedAuthority role : userDetails.getAuthorities()) {
//			grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
//		}
//
//		return new org.springframework.security.core.userdetails.User(userDetails.getUsername(),
//				userDetails.getPassword(), userDetails.getAuthorities());
	}

	@Override
	@Transactional
	@Secured(value = BuiltInRoleDefinitions.ROLE_ANONYMOUS)
	public User registerUser(final String accessToken) {
		return new User();
//		UserEntity userLoaded = userDao.findByUsername(init.getUserName());
//
//		if (userLoaded == null) {
//			UserEntity userEntity = new UserEntity();
//			userEntity.setUsername(init.getUserName());
//			userEntity.setEmail(init.getEmail());
//
//			userEntity.setAuthorities(getUserRoles());
//			// TODO firebase users should not be able to login via username and
//			// password so for now generation of password is OK
//			userEntity.setPassword(UUID.randomUUID().toString());
//			userDao.save(userEntity);
//			logger.info("registerUser -> user created");
//			return userEntity;
//		} else {
//			logger.info("registerUser -> user exists");
//			return userLoaded;
//		}
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
////		BuiltInRoleDefinitions.BuiltInRole.USER
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
