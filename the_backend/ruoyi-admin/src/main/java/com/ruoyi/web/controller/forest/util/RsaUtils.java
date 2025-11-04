package com.ruoyi.web.controller.forest.util;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaUtils {

    private static final String RSA_ALGORITHM = "RSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 使用公钥加密
     */
    public static String encryptByPublicKey(String plainText, String base64PublicKey) throws Exception {
        byte[] data = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return Base64.getEncoder().encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data));
    }

    /**
     * 使用私钥解密
     */
    public static String decryptByPrivateKey(String base64CipherText, String base64PrivateKey) throws Exception {
        byte[] encryptedData = Base64.getDecoder().decode(base64CipherText);
        byte[] keyBytes = Base64.getDecoder().decode(base64PrivateKey);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decryptedData = rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, encryptedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * RSA分段加解密
     */
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas) throws Exception {
        int inputLen = datas.length;
        int maxBlock = (opmode == Cipher.ENCRYPT_MODE) ? MAX_ENCRYPT_BLOCK : MAX_DECRYPT_BLOCK;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buffer;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxBlock) {
                buffer = cipher.doFinal(datas, offSet, maxBlock);
            } else {
                buffer = cipher.doFinal(datas, offSet, inputLen - offSet);
            }
            out.write(buffer, 0, buffer.length);
            offSet += maxBlock;
        }
        return out.toByteArray();
    }
}