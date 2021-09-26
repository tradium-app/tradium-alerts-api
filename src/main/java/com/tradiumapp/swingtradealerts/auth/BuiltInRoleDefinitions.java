package com.tradiumapp.swingtradealerts.auth;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import java.util.Collections;
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
            PermissionDefinition.ALERT_CREATE,
            PermissionDefinition.STOCK_PROFILE_VIEW,
            PermissionDefinition.WATCHLIST_ADMIN
    );


    private static final Map<String, Set<PermissionDefinition>> ROLE_TO_PERMISSIONS = ImmutableMap.<String, Set<PermissionDefinition>>builder()
            .put(ROLE_ANONYMOUS, ANONYMOUS_USER_PERMISSIONS)
            .put(ROLE_USER, AUTHENTICATED_USER_PERMISSIONS)
            .build();

    @Nonnull
    public static Set<PermissionDefinition> permissionsForRole(String role) {
        final Set<PermissionDefinition> permissionDefinitions = ROLE_TO_PERMISSIONS.get(role);

        if (permissionDefinitions == null) {
            return Collections.emptySet();
        }
        return permissionDefinitions;
    }

//    public static Role getRoleById(String role){
//    }
}
