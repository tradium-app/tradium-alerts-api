package com.tradiumapp.swingtradealerts.auth;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.security.core.GrantedAuthority;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class BuiltInRoleDefinitions {
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String ROLE_USER = "ROLE_USER";
    static public final String ROLE_ADMIN = "ROLE_ADMIN";

    public enum BuiltInRole {
        ANONYMOUS(ROLE_ANONYMOUS, "Anonymous"),
        USER(ROLE_USER, "User"),
        ADMIN(ROLE_ADMIN, "Admin");

        public final String id;
        public final String label;

        BuiltInRole(String id, String label) {
            this.id = id;
            this.label = label;
        }
    }

    private static final Set<PermissionDefinition> ANONYMOUS_USER_PERMISSIONS = ImmutableSet.of(
            PermissionDefinition.STOCK_PROFILE_VIEW
    );

    private static final Set<PermissionDefinition> AUTHENTICATED_USER_PERMISSIONS = ImmutableSet.of(
            PermissionDefinition.ALERT,
            PermissionDefinition.STOCK_PROFILE_VIEW,
            PermissionDefinition.WATCHLIST
    );

    private static final Map<String, Set<PermissionDefinition>> ROLE_TO_PERMISSIONS = ImmutableMap.<String, Set<PermissionDefinition>>builder()
            .put(ROLE_ANONYMOUS, ANONYMOUS_USER_PERMISSIONS)
            .put(ROLE_USER, AUTHENTICATED_USER_PERMISSIONS)
            .build();

    @Nonnull
    public static Set<GrantedAuthority> getAuthoritiesForRole(String role) {
        final Set<PermissionDefinition> permissionDefinitions = ROLE_TO_PERMISSIONS.get(role);
        Set<GrantedAuthority> authorities = new HashSet<>();

        if(permissionDefinitions != null) {
            for (PermissionDefinition permission : permissionDefinitions) {
                authorities.add((GrantedAuthority) () -> permission.id);
            }
        }

        return authorities;
    }
}
