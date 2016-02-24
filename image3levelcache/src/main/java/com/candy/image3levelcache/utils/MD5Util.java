package com.candy.image3levelcache.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 获取MD5值的工具类
 * <p/>
 * 用于生成DiskLruCache缓存文件的文件名
 * <p/>
 *
 * Created by 帅阳 on 2016/2/24.
 */
public class MD5Util {

    /**
     * 获取MD5值
     * @param plainText
     * @return
     */
    public static String getMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("no md5！");
        }
        //这个应该是转换成十六进制字符串
        String md5code = new BigInteger(1, secretBytes).toString(16);
        //不够32个长度前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
}
