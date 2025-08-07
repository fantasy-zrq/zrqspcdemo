package com.example.model.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author zrq
 * @time 2025/8/7 10:56
 * @description
 */
public final class MD5EncryptUtil {

    // 十六进制下数字到字符的映射数组
    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private final static String SALT = "7db10e7d-ce86-44cf-ab2b-1a51930d2010";

    /**
     * 生成MD5加密后的字符串
     *
     * @param origin 原始字符串
     * @return 加密后的字符串
     */
    public static String md5Encode(String origin) {
        if (origin == null) {
            return null;
        }
        try {
            // 创建具有指定算法名称的信息摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节数组对摘要进行最后更新
            byte[] digest = md.digest(origin.getBytes());
            // 将得到的字节数组变成字符串返回
            return byteArrayToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 带盐值的MD5加密
     *
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encryptWithSalt(String password) {
        // 密码加盐，格式：password{salt}
        return md5Encode(password + "{" + SALT + "}");
    }

    /**
     * 生成随机盐值
     *
     * @param length 盐值长度
     * @return 随机盐值
     */
    public static String generateSalt(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("盐值长度必须大于0");
        }

        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 验证密码是否正确
     *
     * @param inputPassword  输入的密码
     * @param storedPassword 存储的加密密码
     * @return 是否匹配
     */
    public static boolean verifyPassword(String inputPassword, String storedPassword) {
        String encryptedInput = encryptWithSalt(inputPassword);
        return encryptedInput.equals(storedPassword);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param b 字节
     * @return 十六进制字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }
}

