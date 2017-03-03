package com.liulei.retrofitrxdemoo.model;


import java.io.Serializable;

public class UserModel implements Serializable {
    public String username;
    public String password;

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
