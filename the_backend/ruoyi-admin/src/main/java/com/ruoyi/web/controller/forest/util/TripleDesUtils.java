package com.ruoyi.web.controller.forest.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.util.Base64;

public class TripleDesUtils {

    private static final String encoding = "utf-8";

    /**
     * 3DES加密
     * @param plainText 明文
     * @param secretKey 密钥（长度应为24字节）
     * @param ivPara 8位偏移向量
     * @return 加密后的Base64字符串
     * @throws Exception 异常
     */
    public static String encode(String plainText, String secretKey, String ivPara) throws Exception {
        Key deskey;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes(encoding));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(ivPara.getBytes(encoding));
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));

        return Base64.getEncoder().encodeToString(encryptData);
    }

    /**
     * 3DES解密
     * @param encryptText 密文（Base64编码）
     * @param secretKey 密钥（长度应为24字节）
     * @param ivPara 8位偏移向量
     * @return 解密后的明文
     * @throws Exception 异常
     */
    public static String decode(String encryptText, String secretKey, String ivPara) throws Exception {
        Key deskey;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes(encoding));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(ivPara.getBytes(encoding));
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        String decodedEncryptText = java.net.URLDecoder.decode(encryptText, encoding);

        byte[] decryptData = cipher.doFinal(Base64.getDecoder().decode(decodedEncryptText));
        return new String(decryptData, encoding);
    }

    public static void main(String[] args) {
        try {
            decode("EQ5bErsAKhyy64rqjIHP2qAyOT34JWzs3uvtpMYineHxy8VSIFn44ng%2BLXK0SAv7onscPejbgh7F1mz8feLylwQjkQZoTFVYYFLJIdFM3WGHQ0EyZngWd43wYYjPYzCxnTFFfFIjCT8exa%2BnJsMDHKZSzYUXA8exF8jOju25HTrIvDeEaC5F5U5ECt4w%2BqKOTZcRHF4ncOirualwHg0EHRgYkvpvzeWgfSIg3c8P9M4/msTpJUhiFXjOET5ul3p8ew5FWIoTAZZfKoc3AUayJb67/ok450J0Htb4ZCGujTjGO8H6uTQJTw==",
                    "0653216b6a36434db824b272","76990968");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}