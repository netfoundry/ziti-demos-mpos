package com.nf.flash.preferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurePreferences implements SharedPreferences {

    private static SharedPreferences sharedPreferences;
    private static byte[] sharedPreferenceKey;

    public SecurePreferences(Context context) {
        if (SecurePreferences.sharedPreferences == null) {
            SecurePreferences.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        try {
            final String key = SecurePreferences.generateAesKeyName(context);
            String value = SecurePreferences.sharedPreferences.getString(key, null);
            if (value == null) {
                value = SecurePreferences.generateAesKeyValue();
                SecurePreferences.sharedPreferences.edit().putString(key, value).commit();
            }
            SecurePreferences.sharedPreferenceKey = SecurePreferences.decode(value);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static String encode(byte[] input){
        return Base64.encodeToString(input, Base64.NO_PADDING | Base64.NO_WRAP);
    }

    private static byte[] decode(String input){
        return Base64.decode(input, Base64.NO_PADDING | Base64.NO_WRAP);
    }

    private static String generateAesKeyName(Context context) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final char[] password = context.getPackageName().toCharArray();
        final byte[] salt = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID).getBytes();
        final int iterations = 1000;
        final int keyLength = 256;
        final KeySpec keySpec = new PBEKeySpec(password, salt, iterations, keyLength);
        return SecurePreferences.encode(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(keySpec).getEncoded());
    }

    private static String generateAesKeyValue() throws NoSuchAlgorithmException {
        final SecureRandom random = new SecureRandom();
        final KeyGenerator generator = KeyGenerator.getInstance("AES");
        try {
            generator.init(256, random);
        } catch (Exception e) {
            try {
                generator.init(192, random);
            } catch (Exception e1) {
                generator.init(128, random);
            }
        }
        return SecurePreferences.encode(generator.generateKey().getEncoded());
    }

    private static String encrypt(String cleartext) {
        if (cleartext == null || cleartext.length() == 0) {
            return cleartext;
        }
        try {
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SecurePreferences.sharedPreferenceKey, "AES"));
            return SecurePreferences.encode(cipher.doFinal(cleartext.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            return null;
        }
    }

    private static String decrypt(String ciphertext) {
        if (ciphertext == null || ciphertext.length() == 0) {
            return ciphertext;
        }
        try {
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SecurePreferences.sharedPreferenceKey, "AES"));
            return new String(cipher.doFinal(SecurePreferences.decode(ciphertext)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<String, String> getAll() {
        final Map<String, ?> encryptedMap = SecurePreferences.sharedPreferences.getAll();
        final Map<String, String> decryptedMap = new HashMap<String, String>(encryptedMap.size());
        for (Entry<String, ?> entry : encryptedMap.entrySet()) {
            try {
                decryptedMap.put(SecurePreferences.decrypt(entry.getKey()),
                        SecurePreferences.decrypt(entry.getValue().toString()));
            } catch (Exception e) {
            }
        }
        return decryptedMap;
    }

    @Override
    public String getString(String key, String defaultValue) {
        final String encryptedValue =
                SecurePreferences.sharedPreferences.getString(SecurePreferences.encrypt(key), null);
        return (encryptedValue != null) ? SecurePreferences.decrypt(encryptedValue) : defaultValue;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key, Set<String> defaultValues) {
        final Set<String> encryptedSet =
                SecurePreferences.sharedPreferences.getStringSet(SecurePreferences.encrypt(key), null);
        if (encryptedSet == null) {
            return defaultValues;
        }
        final Set<String> decryptedSet = new HashSet<String>(encryptedSet.size());
        for (String encryptedValue : encryptedSet) {
            decryptedSet.add(SecurePreferences.decrypt(encryptedValue));
        }
        return decryptedSet;
    }

    @Override
    public int getInt(String key, int defaultValue) {
        final String encryptedValue =
                SecurePreferences.sharedPreferences.getString(SecurePreferences.encrypt(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(SecurePreferences.decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public long getLong(String key, long defaultValue) {
        final String encryptedValue =
                SecurePreferences.sharedPreferences.getString(SecurePreferences.encrypt(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(SecurePreferences.decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        final String encryptedValue =
                SecurePreferences.sharedPreferences.getString(SecurePreferences.encrypt(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(SecurePreferences.decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        final String encryptedValue =
                SecurePreferences.sharedPreferences.getString(SecurePreferences.encrypt(key), null);
        if (encryptedValue == null) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(SecurePreferences.decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public boolean contains(String key) {
        return SecurePreferences.sharedPreferences.contains(SecurePreferences.encrypt(key));
    }

    @Override
    public Editor edit() {
        return new Editor();
    }

    public static class Editor implements SharedPreferences.Editor {
        private SharedPreferences.Editor mEditor;

        private Editor() {
            mEditor = SecurePreferences.sharedPreferences.edit();
        }

        @Override
        public SharedPreferences.Editor putString(String key, String value) {
            mEditor.putString(SecurePreferences.encrypt(key), SecurePreferences.encrypt(value));
            return this;
        }

        @Override
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public SharedPreferences.Editor putStringSet(String key, Set<String> values) {
            final Set<String> encryptedValues = new HashSet<String>(values.size());
            for (String value : values) {
                encryptedValues.add(SecurePreferences.encrypt(value));
            }
            mEditor.putStringSet(SecurePreferences.encrypt(key), encryptedValues);
            return this;
        }

        @Override
        public SharedPreferences.Editor putInt(String key, int value) {
            mEditor.putString(SecurePreferences.encrypt(key),
                    SecurePreferences.encrypt(Integer.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putLong(String key, long value) {
            mEditor.putString(SecurePreferences.encrypt(key),
                    SecurePreferences.encrypt(Long.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putFloat(String key, float value) {
            mEditor.putString(SecurePreferences.encrypt(key),
                    SecurePreferences.encrypt(Float.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            mEditor.putString(SecurePreferences.encrypt(key),
                    SecurePreferences.encrypt(Boolean.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor remove(String key) {
            mEditor.remove(SecurePreferences.encrypt(key));
            return this;
        }

        @Override
        public SharedPreferences.Editor clear() {
            mEditor.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return mEditor.commit();
        }

        @Override
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        public void apply() {
            mEditor.apply();
        }
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        SecurePreferences.sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        SecurePreferences.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

}

