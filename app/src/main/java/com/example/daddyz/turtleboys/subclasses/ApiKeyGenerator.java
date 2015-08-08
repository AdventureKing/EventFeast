package com.example.daddyz.turtleboys.subclasses;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by zachrdz on 7/18/15.
 */
public class ApiKeyGenerator {
    private String emailSalt;

    public ApiKeyGenerator(String emailSalt){
        this.emailSalt = emailSalt;
    }

    /*
     * Generate unique Api Key to be used for Turtleboys API requests
     * Uses custom md5 and uniqid functions
     * @params String emailSalt
     * @returns String
    */

    public String generateApiKey(){
        return md5(uniqid() + this.emailSalt);
    }

    private static final String uniqid(){
        return UUID.randomUUID().toString();
    }

    private static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
