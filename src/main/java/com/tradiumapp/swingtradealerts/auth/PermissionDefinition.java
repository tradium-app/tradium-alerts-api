package com.tradiumapp.swingtradealerts.auth;

import java.util.Objects;

public enum PermissionDefinition {
    WATCHLIST_ADMIN("permission.watchlist.admin", "Watchlist view/edit/delete"),
    ALERT_CREATE("permission.alert.create", "Alert create"),
    STOCK_PROFILE_VIEW("permission.stock_profile.view", "View stock profile");

    public final String id;
    public final String description;

    PermissionDefinition(String id, String description){
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.description = Objects.requireNonNull(description, "description cannot be null");
    }
}
