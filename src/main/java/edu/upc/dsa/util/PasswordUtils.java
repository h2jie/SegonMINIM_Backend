package edu.upc.dsa.util;

import javax.validation.constraints.NotNull;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    @NotNull
    public static String getPasswordHash(String password){
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            return Base64.getEncoder().encodeToString(messageDigest.digest());
        }catch(NoSuchAlgorithmException e){
            return null;
        }
    }
}
