package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AesHelper {
    private static final String TAG = "AesHelper";
    private static final String HEX_CHARS = "0123456789ABCDEF";
    private static final String TRANSFORMATION = "AES"; // Preserving original for compatibility

    @Nullable
    public static String encrypt(@NonNull String secretKey, @NonNull String originalString) {
        try {
            byte[] originalBytes = originalString.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), TRANSFORMATION);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return Base64.encodeToString(cipher.doFinal(originalBytes), Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "Encryption failed", e);
            return null;
        }
    }

    @Nullable
    public static String decrypt(@NonNull String secretKey, @NonNull String encryptedBase64) {
        try {
            byte[] decodedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), TRANSFORMATION);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(decodedBytes));
        } catch (Exception e) {
            Log.e(TAG, "Decryption failed", e);
            return null;
        }
    }

    @Nullable
    private static byte[] getRawKey(byte[] seed) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(TRANSFORMATION);
            @SuppressLint("DeletedMethod") 
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seed);
            keyGen.init(128, secureRandom);
            return keyGen.generateKey().getEncoded();
        } catch (Exception e) {
            Log.e(TAG, "Raw key generation failed", e);
            return null;
        }
    }

    @NonNull
    public static String toHex(@Nullable String text) {
        if (text == null) return "";
        return toHex(text.getBytes());
    }

    @NonNull
    public static String fromHex(@NonNull String hex) {
        return new String(hexToBytes(hex));
    }

    @NonNull
    public static byte[] hexToBytes(@NonNull String hex) {
        int length = hex.length() / 2;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = Integer.valueOf(hex.substring(i * 2, (i * 2) + 2), 16).byteValue();
        }
        return result;
    }

    @NonNull
    public static String toHex(@Nullable byte[] bytes) {
        if (bytes == null) return "";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(HEX_CHARS.charAt((b >> 4) & 0x0F));
            sb.append(HEX_CHARS.charAt(b & 0x0F));
        }
        return sb.toString();
    }

    @SuppressLint("PackageManagerGetSignatures")
    @Nullable
    public static String getHashKey(@NonNull Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting hash key", e);
        }
        return null;
    }
}
