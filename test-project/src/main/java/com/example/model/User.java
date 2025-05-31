package com.example.model;

/**
 * ユーザー情報を表すモデルクラス
 */
public class User {
    private String id;
    private String name;
    private String email;
    private boolean active;
    private int loginCount;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.active = true;
        this.loginCount = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void incrementLoginCount() {
        this.loginCount++;
    }

    public void resetLoginCount() {
        this.loginCount = 0;
    }

    public boolean isFrequentUser() {
        return loginCount >= 10;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", loginCount=" + loginCount +
                '}';
    }
}