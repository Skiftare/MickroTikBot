package edu;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Утилитарный класс для шифрования и дешифрования данных с использованием алгоритма AES.
 */
public final class EncryptionUtil {
    /** Алгоритм шифрования. */
    private static final String ALGORITHM = "AES";
    /** Секретный ключ для шифрования/дешифрования. */
    private static final byte[] SECRET_KEY = System.getenv("DB_ENCRYPTION_KEY").getBytes();

    private EncryptionUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Шифрует строковые данные с использованием AES.
     *
     * @param data данные для шифрования
     * @return зашифрованная строка в формате Base64
     * @throws Exception при ошибках шифрования
     */
    public static String encrypt(final String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * Дешифрует зашифрованные данные.
     *
     * @param encryptedData зашифрованные данные в формате Base64
     * @return расшифрованная строка
     * @throws Exception при ошибках дешифрования
     */
    public static String decrypt(final String encryptedData) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }
}
