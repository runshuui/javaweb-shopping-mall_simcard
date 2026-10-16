package com.chouwansim.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import sun.misc.BASE64Decoder;

public class PasswordUtil {

    public static boolean verifyPassword(
        String password,
        String saltBase64,
        int iterations,
        String expectedHashBase64) {

        if (password == null
            || saltBase64 == null
            || expectedHashBase64 == null
            || iterations <= 0) {

            return false;
        }

        try {

            BASE64Decoder decoder =
                new BASE64Decoder();

            byte[] salt =
                decoder.decodeBuffer(
                    saltBase64
                );

            byte[] expected =
                decoder.decodeBuffer(
                    expectedHashBase64
                );

            PBEKeySpec spec =
                new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    iterations,
                    expected.length * 8
                );

            SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                    "PBKDF2WithHmacSHA1"
                );

            byte[] actual =
                factory.generateSecret(
                    spec
                ).getEncoded();

            if (actual.length
                != expected.length) {

                return false;
            }

            int diff = 0;

            for (int i = 0;
                 i < actual.length;
                 i++) {

                diff |=
                    actual[i]
                    ^ expected[i];
            }

            return diff == 0;

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);

        } catch (InvalidKeySpecException e) {

            throw new RuntimeException(e);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}
