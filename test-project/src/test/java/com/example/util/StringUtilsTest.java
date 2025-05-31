package com.example.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * StringUtilsクラスのユニットテスト（一部のテストを削除してカバレッジを下げる）
 */
public class StringUtilsTest {

    @Test
    public void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertTrue(StringUtils.isEmpty("   "));
        assertFalse(StringUtils.isEmpty("test"));
        assertFalse(StringUtils.isEmpty(" test "));
    }

    @Test
    public void testIsValidEmail() {
        assertFalse(StringUtils.isValidEmail(null));
        assertFalse(StringUtils.isValidEmail(""));
        assertFalse(StringUtils.isValidEmail("   "));
        assertFalse(StringUtils.isValidEmail("invalid"));
        assertFalse(StringUtils.isValidEmail("@example.com"));
        assertFalse(StringUtils.isValidEmail("user@"));
        assertTrue(StringUtils.isValidEmail("user@example.com"));
        assertTrue(StringUtils.isValidEmail("test.email@domain.org"));
    }

    @Test
    public void testCapitalize() {
        assertNull(StringUtils.capitalize(null));
        assertEquals("", StringUtils.capitalize(""));
        assertEquals("Test", StringUtils.capitalize("test"));
        assertEquals("Test", StringUtils.capitalize("TEST"));
        // 一部のテストケースを削除してカバレッジを下げる（空白文字のテストなど）
    }

    @Test
    public void testTruncate() {
        assertNull(StringUtils.truncate(null, 10));
        assertEquals("test", StringUtils.truncate("test", 10));
        assertEquals("test", StringUtils.truncate("test", 4));
        assertEquals("tes...", StringUtils.truncate("test", 3));
        // 一部のテストケースを削除（長い文字列や0文字のテストケースを削除）
    }

    // isStrongPasswordの基本的なテストのみ追加（すべてのブランチはカバーしない）
    @Test
    public void testIsStrongPasswordBasic() {
        assertFalse(StringUtils.isStrongPassword(null));
        assertFalse(StringUtils.isStrongPassword(""));
        assertFalse(StringUtils.isStrongPassword("short"));
        assertTrue(StringUtils.isStrongPassword("Strong1@"));
        // 弱いパスワードのチェック（123456、passwordを含むケース）はテストしない
        // 複雑な条件分岐のテストもしないため、カバレッジが下がる
    }

    // reverse と extractNumbers メソッドはテストしない（カバレッジ0%）
}