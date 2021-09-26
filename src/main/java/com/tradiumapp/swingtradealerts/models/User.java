package com.tradiumapp.swingtradealerts.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Document("users")
public class User implements UserDetails {
    @Id
    public ObjectId id;

    @Indexed(unique = true)
    public String firebaseUid;

    public String name;
    public String password;
    public String email;
    public String role;
    public String imageUrl;
    public List<String> watchList;
    public String authProvider;
    public String fcmToken;
    public String countryCode;
    public String timeZone;
    public String ipAddress;

    public Date createdDate;
    public Date modifiedDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.firebaseUid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
