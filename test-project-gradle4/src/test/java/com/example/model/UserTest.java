package com.example.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Userクラスのユニットテスト
 */
public class UserTest {

    @Test
    public void testUserCreation() {
        User user = new User("user1", "John Doe", "john@example.com");
        
        assertEquals("user1", user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertTrue(user.isActive());
        assertEquals(0, user.getLoginCount());
        assertFalse(user.isFrequentUser());
    }

    @Test
    public void testSetValidName() {
        User user = new User("user1", "John", "john@example.com");
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullName() {
        User user = new User("user1", "John", "john@example.com");
        user.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyName() {
        User user = new User("user1", "John", "john@example.com");
        user.setName("   ");
    }

    @Test
    public void testSetValidEmail() {
        User user = new User("user1", "John", "john@example.com");
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullEmail() {
        User user = new User("user1", "John", "john@example.com");
        user.setEmail(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidEmail() {
        User user = new User("user1", "John", "john@example.com");
        user.setEmail("invalid-email");
    }

    @Test
    public void testActivateDeactivateUser() {
        User user = new User("user1", "John", "john@example.com");
        
        assertTrue(user.isActive());
        
        user.setActive(false);
        assertFalse(user.isActive());
        
        user.setActive(true);
        assertTrue(user.isActive());
    }

    @Test
    public void testLoginCount() {
        User user = new User("user1", "John", "john@example.com");
        
        assertEquals(0, user.getLoginCount());
        assertFalse(user.isFrequentUser());
        
        // 5回ログイン
        for (int i = 0; i < 5; i++) {
            user.incrementLoginCount();
        }
        assertEquals(5, user.getLoginCount());
        assertFalse(user.isFrequentUser());
        
        // さらに6回ログイン（合計11回）
        for (int i = 0; i < 6; i++) {
            user.incrementLoginCount();
        }
        assertEquals(11, user.getLoginCount());
        assertTrue(user.isFrequentUser());
        
        // カウントリセット
        user.resetLoginCount();
        assertEquals(0, user.getLoginCount());
        assertFalse(user.isFrequentUser());
    }

    @Test
    public void testToString() {
        User user = new User("user1", "John Doe", "john@example.com");
        String result = user.toString();
        
        assertTrue(result.contains("user1"));
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("john@example.com"));
        assertTrue(result.contains("active=true"));
        assertTrue(result.contains("loginCount=0"));
    }
}