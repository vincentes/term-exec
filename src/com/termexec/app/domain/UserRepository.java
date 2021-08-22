package com.termexec.app.domain;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final List<User> users = new ArrayList<>();
    private static User currentUser;

    public static User add(String username) {
        User user = new User(username);
        user.setPassword("");
        users.add(user);
        return user;
    }

    public static boolean su(String username) {
        User user = find(username);
        if(user == null) {
            return false;
        }
        currentUser = user;
        return true;
    }

    public static User find(String username) {
        for(User user : users) {
            if(user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void setPassword(User user, String password) {
        for(User u : users) {
            if(u.equals(user)) {
                user.setPassword(password);
            }
        }
    }

    public static boolean exists(String username) {
        for(User u : users) {
            if(u.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static List<User> getUsers() {
        return users;
    }
}
