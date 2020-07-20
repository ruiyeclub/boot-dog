package cn.ruiyeclub.common.util;

import java.security.MessageDigest;

public class MD5EncryptUtil {
    public MD5EncryptUtil() {
    }

    public static String encrypt(String encryptStr) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuilder hexValue = new StringBuilder();
            byte[] var4 = md5Bytes;
            int var5 = md5Bytes.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                byte md5Byte = var4[var6];
                int val = md5Byte & 255;
                if (val < 16) {
                    hexValue.append("0");
                }

                hexValue.append(Integer.toHexString(val));
            }

            encryptStr = hexValue.toString();
            return encryptStr;
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        }
    }
}
