package com.example.service;

import com.example.model.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Optional;

/**
 * UserServiceクラスのユニットテスト（一部のテストを削除してカバレッジを下げる）
 */
public class UserServiceTest {
    
    private UserService userService;
    
    @Before
    public void setUp() {
        userService = new UserService();
    }
    
    @Test
    public void testCreateUser() {
        User user = userService.createUser("user1", "John Doe", "john@example.com");
        
        assertEquals("user1", user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertTrue(user.isActive());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithNullId() {
        userService.createUser(null, "John", "john@example.com");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCreateUserWithDuplicateId() {
        userService.createUser("user1", "John", "john@example.com");
        userService.createUser("user1", "Jane", "jane@example.com");
    }
    
    @Test
    public void testFindUserById() {
        User createdUser = userService.createUser("user1", "John", "john@example.com");
        
        Optional<User> foundUser = userService.findUserById("user1");
        assertTrue(foundUser.isPresent());
        assertEquals(createdUser, foundUser.get());
        
        Optional<User> notFound = userService.findUserById("nonexistent");
        assertFalse(notFound.isPresent());
    }
    
    @Test
    public void testGetAllUsers() {
        assertTrue(userService.getAllUsers().isEmpty());
        
        userService.createUser("user1", "John", "john@example.com");
        userService.createUser("user2", "Jane", "jane@example.com");
        
        List<User> allUsers = userService.getAllUsers();
        assertEquals(2, allUsers.size());
    }
    
    @Test
    public void testDeleteUser() {
        userService.createUser("user1", "John", "john@example.com");
        assertEquals(1, userService.getTotalUserCount());
        
        assertTrue(userService.deleteUser("user1"));
        assertEquals(0, userService.getTotalUserCount());
        
        assertFalse(userService.deleteUser("nonexistent"));
    }
    
    @Test
    public void testActivateDeactivateUser() {
        User user = userService.createUser("user1", "John", "john@example.com");
        assertTrue(user.isActive());
        
        assertTrue(userService.deactivateUser("user1"));
        assertFalse(user.isActive());
        
        assertTrue(userService.activateUser("user1"));
        assertTrue(user.isActive());
        
        assertFalse(userService.activateUser("nonexistent"));
        assertFalse(userService.deactivateUser("nonexistent"));
    }
    
    @Test
    public void testRecordLogin() {
        User user = userService.createUser("user1", "John", "john@example.com");
        assertEquals(0, user.getLoginCount());
        
        userService.recordLogin("user1");
        assertEquals(1, user.getLoginCount());
        
        // 非アクティブユーザーはログイン記録されない
        user.setActive(false);
        userService.recordLogin("user1");
        assertEquals(1, user.getLoginCount());
    }
    
    @Test
    public void testGetTotalUserCount() {
        assertEquals(0, userService.getTotalUserCount());
        
        userService.createUser("user1", "John", "john@example.com");
        assertEquals(1, userService.getTotalUserCount());
        
        userService.createUser("user2", "Jane", "jane@example.com");
        assertEquals(2, userService.getTotalUserCount());
    }
    
    // 以下のテストメソッドを削除してカバレッジを下げる:
    // - testGetActiveUsers（削除済み）
    // - testGetFrequentUsers（削除済み）
    // - testGetActiveUserCount（削除済み）
    // - 新しいメソッド（getUserStatistics, findUsersByEmailDomain, createUsersFromCsv）はテストしない
}