package com.tradiumapp.swingtradealerts.auth.firebase;

import com.tradiumapp.swingtradealerts.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class FirebaseAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;

	public boolean supports(Class<?> authentication) {
		return (FirebaseAuthenticationToken.class.isAssignableFrom(authentication));
	}

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}

		FirebaseAuthenticationToken authenticationToken = (FirebaseAuthenticationToken) authentication;
		UserDetails details = userService.loadUserByUsername(authenticationToken.getName());
		if (details != null) {
			authenticationToken = new FirebaseAuthenticationToken(details, authentication.getCredentials(),
					details.getAuthorities());
		}

		return authenticationToken;
	}

}
