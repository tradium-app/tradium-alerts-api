package com.tradiumapp.swingtradealerts.auth;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public class Role implements GrantedAuthority {
	private Long id;

	private String authority;

	public Set<PermissionDefinition> permissions;

	@Override
	public String getAuthority() {
		return authority;
	}
}
