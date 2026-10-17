package com.chouwansim.util;

import java.security.MessageDigest;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

public class PasswordUtil {


    public static boolean verifyPassword(
        String inputPassword,
        String storedHash,
        String storedSalt,
        int iterations) {

        try {

            byte[] expectedHash =
                decode(storedHash);

            byte[] salt =
                decode(storedSalt);


            PBEKeySpec spec =
                new PBEKeySpec(
                    inputPassword.toCharArray(),
                    salt,
                    iterations,
                    expectedHash.length * 8
                );


            SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                    "PBKDF2WithHmacSHA1"
                );


            byte[] actualHash =
                factory
                    .generateSecret(spec)
                    .getEncoded();


            spec.clearPassword();


            return MessageDigest.isEqual(
                expectedHash,
                actualHash
            );


        } catch (Exception e) {

            e.printStackTrace();

            return false;

        }

    }


    private static byte[] decode(
        String value) {

        /*
         * 如果全部由 0-9 A-F 组成，
         * 认为数据库保存的是 Hex。
         */

        if (value != null
            && value.length() % 2 == 0
            && value.matches(
                "(?i)^[0-9a-f]+$")) {

            return hexToBytes(value);

        }


        /*
         * 否则认为数据库保存的是 Base64。
         */

        return DatatypeConverter
            .parseBase64Binary(value);

    }


    private static byte[] hexToBytes(
        String hex) {

        int length = hex.length();

        byte[] data =
            new byte[length / 2];


        int i;

        for (i = 0; i < length; i += 2) {

            data[i / 2] =
                (byte) (
                    (Character.digit(
                        hex.charAt(i),
                        16
                    ) << 4)

                    +

                    Character.digit(
                        hex.charAt(i + 1),
                        16
                    )
                );

        }


        return data;

    }

}
