package com.example.vulnspring.model;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private Long   userId;
    private String email;


    private transient String password;
    private transient String sessionToken;

    public UserProfile() { }

    public UserProfile(String username, Long userId, String email,
                       String password, String sessionToken) {
        this.username     = username;
        this.userId       = userId;
        this.email        = email;
        this.password     = password;
        this.sessionToken = sessionToken;
    }



    @Override
    public String toString() {
        return "UserProfile{username='" + username +
                "', userId=" + userId +
                ", email='" + email +
                "', password=" + password +
                ", sessionToken=" + sessionToken + "}";
    }
}
