package gift.auth;

import gift.common.exception.PasswordEncryptionException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordUtil {

    private static final int ITERATIONS = 100000;
    private static final int KEY_LENGTH = 256;

    public static String encryptPassword(String password, byte[] salt) {
        if (password == null || salt == null) {
            throw new IllegalArgumentException("비밀번호 저장 실패");
        }

        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = keyFactory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new PasswordEncryptionException("암호화 실패");
        }
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

}
