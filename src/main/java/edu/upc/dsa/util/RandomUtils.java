package edu.upc.dsa.util;

import net.moznion.random.string.RandomStringGenerator;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomUtils {


    public static String getId() {
        RandomStringGenerator generator = new RandomStringGenerator();
        String randomString = generator.generateByRegex("\\w+\\d*[0-9]{0,8}");

        return randomString;
    }

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64encoder = Base64.getUrlEncoder();

    public static String generateNewToken(){
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64encoder.encodeToString(randomBytes);
    }
}
