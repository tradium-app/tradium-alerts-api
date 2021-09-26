package com.tradiumapp.swingtradealerts.auth.firebase;

import com.tradiumapp.swingtradealerts.auth.BuiltInRoleDefinitions;
import com.tradiumapp.swingtradealerts.auth.service.FirebaseService;
import com.tradiumapp.swingtradealerts.auth.service.exception.FirebaseTokenInvalidException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FirebaseFilter extends OncePerRequestFilter {

	private static String HEADER_NAME = "X-Authorization-Tradium";

	private FirebaseService firebaseService;

	public FirebaseFilter(FirebaseService firebaseService) {
		this.firebaseService = firebaseService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String xAuth = request.getHeader(HEADER_NAME);
		if (StringUtils.isBlank(xAuth)) {
			List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(BuiltInRoleDefinitions.ROLE_ANONYMOUS);
			Authentication auth = new AnonymousAuthenticationToken(BuiltInRoleDefinitions.ROLE_ANONYMOUS,BuiltInRoleDefinitions.ROLE_ANONYMOUS, authorities);
			SecurityContextHolder.getContext().setAuthentication(auth);
			filterChain.doFilter(request, response);
		} else {
			try {
				FirebaseTokenHolder holder = firebaseService.parseToken(xAuth);

				String userName = holder.getUid();

				Authentication auth = new FirebaseAuthenticationToken(userName, holder);
				SecurityContextHolder.getContext().setAuthentication(auth);

				filterChain.doFilter(request, response);
			} catch (FirebaseTokenInvalidException e) {
				throw new SecurityException(e);
			}
		}
	}

}
