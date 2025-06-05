package com.example.service;

/**
 * ユーザー統計情報を保持するクラス（テストされていない）
 */
public class UserStatistics {
    private final int totalUsers;
    private final int activeUsers;
    private final int frequentUsers;
    private final int totalLogins;

    public UserStatistics(int totalUsers, int activeUsers, int frequentUsers, int totalLogins) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.frequentUsers = frequentUsers;
        this.totalLogins = totalLogins;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public int getFrequentUsers() {
        return frequentUsers;
    }

    public int getTotalLogins() {
        return totalLogins;
    }

    public double getActiveUserRatio() {
        if (totalUsers == 0) {
            return 0.0;
        }
        return (double) activeUsers / totalUsers;
    }

    public double getAverageLoginsPerUser() {
        if (totalUsers == 0) {
            return 0.0;
        }
        return (double) totalLogins / totalUsers;
    }

    @Override
    public String toString() {
        return String.format(
            "UserStatistics{totalUsers=%d, activeUsers=%d, frequentUsers=%d, totalLogins=%d, activeRatio=%.2f}",
            totalUsers, activeUsers, frequentUsers, totalLogins, getActiveUserRatio()
        );
    }
}