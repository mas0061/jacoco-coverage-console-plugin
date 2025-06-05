package com.example.service;

import com.example.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

/**
 * ユーザー管理サービスクラス
 */
public class UserService {
    private Map<String, User> users;

    public UserService() {
        this.users = new HashMap<>();
    }

    public User createUser(String id, String name, String email) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        
        if (users.containsKey(id)) {
            throw new IllegalStateException("User with ID " + id + " already exists");
        }

        User user = new User(id, name, email);
        users.put(id, user);
        return user;
    }

    public Optional<User> findUserById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public List<User> getActiveUsers() {
        List<User> activeUsers = new ArrayList<>();
        for (User user : users.values()) {
            if (user.isActive()) {
                activeUsers.add(user);
            }
        }
        return activeUsers;
    }

    public boolean deleteUser(String id) {
        return users.remove(id) != null;
    }

    public boolean activateUser(String id) {
        User user = users.get(id);
        if (user != null) {
            user.setActive(true);
            return true;
        }
        return false;
    }

    public boolean deactivateUser(String id) {
        User user = users.get(id);
        if (user != null) {
            user.setActive(false);
            return true;
        }
        return false;
    }

    public void recordLogin(String id) {
        User user = users.get(id);
        if (user != null && user.isActive()) {
            user.incrementLoginCount();
        }
    }

    public List<User> getFrequentUsers() {
        List<User> frequentUsers = new ArrayList<>();
        for (User user : users.values()) {
            if (user.isFrequentUser()) {
                frequentUsers.add(user);
            }
        }
        return frequentUsers;
    }

    public int getTotalUserCount() {
        return users.size();
    }

    public int getActiveUserCount() {
        int count = 0;
        for (User user : users.values()) {
            if (user.isActive()) {
                count++;
            }
        }
        return count;
    }

    /**
     * ユーザーの統計情報を取得する（テストされていないメソッド）
     */
    public UserStatistics getUserStatistics() {
        int totalUsers = users.size();
        int activeUsers = 0;
        int frequentUsers = 0;
        int totalLogins = 0;
        
        for (User user : users.values()) {
            if (user.isActive()) {
                activeUsers++;
            }
            if (user.isFrequentUser()) {
                frequentUsers++;
            }
            totalLogins += user.getLoginCount();
        }
        
        return new UserStatistics(totalUsers, activeUsers, frequentUsers, totalLogins);
    }
    
    /**
     * メールドメインでユーザーを検索する（テストされていないメソッド）
     */
    public List<User> findUsersByEmailDomain(String domain) {
        List<User> result = new ArrayList<>();
        
        if (domain == null || domain.trim().isEmpty()) {
            return result;
        }
        
        for (User user : users.values()) {
            String email = user.getEmail();
            if (email != null && email.toLowerCase().endsWith("@" + domain.toLowerCase())) {
                result.add(user);
            }
        }
        
        return result;
    }
    
    /**
     * ユーザーをバッチで作成する（一部テストされていないメソッド）
     */
    public int createUsersFromCsv(String csvData) {
        if (csvData == null || csvData.trim().isEmpty()) {
            return 0;
        }
        
        String[] lines = csvData.split("\n");
        int created = 0;
        
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                try {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String email = parts[2].trim();
                    
                    // このエラーハンドリングはテストされない
                    if (!users.containsKey(id)) {
                        createUser(id, name, email);
                        created++;
                    }
                } catch (Exception e) {
                    // エラーは無視（このブランチはテストされない）
                    continue;
                }
            }
        }
        
        return created;
    }
}