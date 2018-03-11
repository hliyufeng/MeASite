package com.mea.site.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * @author Michael Jou
 * @PACKAGE com.gladtrust.measite.common.utils
 * @program: measite-manage
 * @description: ${description}
 * @create: 2018-02-06 17:33
 */
@Slf4j
public class Encryptor {
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

    private static final String sKey = "532wWw.NtrUe.Net";

    /********************************************
     * 解密URL参数.<br>
     * 方法业务逻辑详细描述……<br>
     * @since v1.0.0
     * @return 返回类型 返回类型描述
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2014年3月3日::巫作坤::创建此方法<br>
     *********************************************/
    public static String decryptUrl(String sSrc) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Convert.decode(sSrc);// 先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
                return null;
            }
        } catch (Exception ex) {
            log.error(ExceptionUtils.getFullStackTrace(ex));
            return null;
        }
    }

    /********************************************
     * 加密URL参数.<br>
     * 方法业务逻辑详细描述……<br>
     * @since v1.0.0
     * @return 返回类型 返回类型描述
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2014年3月3日::巫作坤::创建此方法<br>
     *********************************************/
    public static String encryptUrl(String sSrc) throws Exception {
        if (sKey == null) {
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return Convert.encode(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    /********************************************
     * encryptBinary<br>
     * 方法业务逻辑详细描述……<br>
     * @since v1.0.0
     * @param sSrc
     * @param key 空的时候使用默认key
     * @return
     * @throws Exception
     * byte[]
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2016年10月21日::李智武::创建此方法<br>
     *********************************************/
    public static byte[] encryptBinary(byte[] sSrc, String key) throws Exception {
        String secretKey = StringUtils.isEmpty(key) ? sKey : key;
        if (secretKey == null) {
            return null;
        }
        // 判断Key是否为16位
        if (secretKey.length() != 16) {
            return null;
        }
        byte[] raw = secretKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc);
        return encrypted;// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    /********************************************
     * decryptBinary<br>
     * 方法业务逻辑详细描述……<br>
     * @since v1.0.0
     * @param sSrc
     * @param key 空的时候使用默认key
     * @return
     * @throws Exception
     * byte[]
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2016年10月21日::李智武::创建此方法<br>
     *********************************************/
    public static byte[] decryptBinary(byte[] sSrc, String key) throws Exception {
        String secretKey = StringUtils.isEmpty(key) ? sKey : key;
        try {
            // 判断Key是否正确
            if (secretKey == null) {
                return null;
            }
            // 判断Key是否为16位
            if (secretKey.length() != 16) {
                return null;
            }
            byte[] raw = secretKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            // byte[] encrypted1 = new Convert().decode(sSrc);// 先用base64解密
            try {
                byte[] original = cipher.doFinal(sSrc);
                return original;
            } catch (Exception e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
                return null;
            }
        } catch (Exception ex) {
            log.error(ExceptionUtils.getFullStackTrace(ex));
            return null;
        }
    }

    /********************************************
     * 生成字符串的MD5值.<br>
     * 方法业务逻辑详细描述……<br>
     * @since v1.0.0
     * @return 返回类型 返回类型描述
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2014年5月16日::巫作坤::创建此方法<br>
     *********************************************/
    public static String md5(String inputStr) {
        if (inputStr != null) {
            try {
                return md5(inputStr.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
                return null;
            }
        }
        return null;
    }

    /********************************************
     * 生成字节数组的MD5值.<br>
     * 方法业务逻辑详细描述……<br>
     * @since v1.0.0
     * @return 返回类型 返回类型描述
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2014年5月16日::巫作坤::创建此方法<br>
     *********************************************/
    public static String md5(byte[] data) {
        try {
            // 创建具有指定算法名称的信息摘要
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
            byte[] results = md5.digest(data);
            // 将得到的字节数组变成字符串返回
            String result = byteArrayToHexString(results).toLowerCase();
            return result;
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return null;
    }

    /********************************************
     * 字节数组转十六进制字符串.<br>
     * 方法业务逻辑详细描述……<br>
     * @since v1.0.0

     * @return 返回类型 返回类型描述
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2014年5月16日::巫作坤::创建此方法<br>
     *********************************************/
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /********************************************
     * 字节转十六进制字符串.<br>
     * 方法业务逻辑详细描述……<br>
     * @since v1.0.0
     * @return 返回类型 返回类型描述
     * <br>
     * --------------------------------------<br>
     * 编辑历史<br>
     * 2014年5月16日::巫作坤::创建此方法<br>
     *********************************************/
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
