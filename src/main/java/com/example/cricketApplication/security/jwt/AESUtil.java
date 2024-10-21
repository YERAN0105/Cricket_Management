//package com.example.cricketApplication.security.jwt;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Base64;
//
//public class AESUtil {
//
//    private static final String ALGORITHM = "AES";
//    private static final byte[] keyValue = "======================Pamitha=Spring===========================".getBytes(); // Replace with your secret key
//
//    public static String encrypt(String valueToEnc) throws Exception {
//        SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//        byte[] encValue = cipher.doFinal(valueToEnc.getBytes());
//        return Base64.getEncoder().encodeToString(encValue);
//    }
//
//    public static String decrypt(String encryptedValue) throws Exception {
//        SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.DECRYPT_MODE, key);
//        byte[] decValue = Base64.getDecoder().decode(encryptedValue);
//        byte[] decryptedValue = cipher.doFinal(decValue);
//        return new String(decryptedValue);
//    }
//}
//
