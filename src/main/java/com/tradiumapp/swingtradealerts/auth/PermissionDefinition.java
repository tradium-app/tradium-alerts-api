package com.tradiumapp.swingtradealerts.auth;

import java.util.Objects;

public enum PermissionDefinition {
    WATCHLIST("permission.watchlist.admin", "Watchlist view/edit/delete"),
    ALERT("permission.alert.admin", "Alert create/edit/delete"),
    STOCK_PROFILE_VIEW("permission.stock_profile.view", "View stock profile");

    public final String id;
    public final String description;

    PermissionDefinition(String id, String description){
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.description = Objects.requireNonNull(description, "description cannot be null");
    }
}
