package com.example.giftcardmaster.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

import java.util.UUID;

public class EncryptionUtil {

    private static StandardPBEStringEncryptor encryptor;

    private static EnvironmentStringPBEConfig config;

    static {
        encryptor = new StandardPBEStringEncryptor();
        config = new EnvironmentStringPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("san_jose");
        encryptor.setConfig(config);
    }

    public static String encryptString(String plainText) {
        return encryptor.encrypt(plainText);
    }

    public static String decryptString(String cypherText) {
        return encryptor.decrypt(cypherText);
    }

    public static String getUserEncryptedPassword(String password) {
        return new SimpleHash("md5", password, "GroceryOutlet", 7).toString();
    }

    public static String getOrderIdToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
