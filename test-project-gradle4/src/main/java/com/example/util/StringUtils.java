package com.example.util;

/**
 * 文字列操作のユーティリティクラス
 */
public class StringUtils {
    
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        
        // 簡単なメール形式チェック
        return email.contains("@") && 
               email.indexOf("@") > 0 && 
               email.indexOf("@") < email.length() - 1;
    }
    
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        
        if (str.length() <= maxLength) {
            return str;
        }
        
        return str.substring(0, maxLength) + "...";
    }
    
    /**
     * 文字列を逆順にする（テストされていないメソッド）
     */
    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }
    
    /**
     * 文字列内の数字のみを抽出する（テストされていないメソッド）
     */
    public static String extractNumbers(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("[^0-9]", "");
    }
    
    /**
     * パスワード強度をチェックする（一部テストされていないメソッド）
     */
    public static boolean isStrongPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }
        
        // 8文字以上
        if (password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        
        // このブランチはテストされない
        if (password.contains("123456") || password.contains("password")) {
            return false;
        }
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}