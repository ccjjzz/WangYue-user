package com.jiuyue.user.utils;

import java.security.MessageDigest;

public class Md5 {
    private static final char[] hexDigits = { 48, 49, 50, 51, 52, 53, 54, 55,
            56, 57, 97, 98, 99, 100, 101, 102 };

    public static String headiest(String paramString) {
        try {
            return headiest(paramString.getBytes());
        } catch (Exception ignored) {
        }
        return null;
    }

    public static String headiest(byte[] paramArrayOfByte) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            int i = 0;
            int j = 0;
            while (true) {
                if (i >= 16)
                    return new String(arrayOfChar);
                int k = arrayOfByte[i];
                int m = j + 1;
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                j = m + 1;
                arrayOfChar[m] = hexDigits[(k & 0xF)];
                i++;
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
