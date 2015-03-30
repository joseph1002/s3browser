import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Created by Joseph on 3/30/2015.
 */
public class GeneralTest {
    private static final Logger logger = LoggerFactory.getLogger(GeneralTest.class);
    private static String password = "Hello S3Browser";
    private static String seed = "wallestar";
    @Test
    public void testAESCryption() {
        try {
            // 生成KEY
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            // 产生密钥
//            SecretKey secretKey = keyGenerator.generateKey();
            keyGenerator.init(128, new SecureRandom(seed.getBytes()));
            SecretKey secretKey = keyGenerator.generateKey();
            // 获取密钥
            byte[] keyBytes = secretKey.getEncoded();

            // KEY转换
            Key key = new SecretKeySpec(keyBytes, "AES");

            // 加密
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(password.getBytes());
            logger.info("jdk aes encrypt: {}", Hex.encodeHexString(result));

            // 解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            result = cipher.doFinal(result);
            logger.info("jdk aes decrypt: {}", new String(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMD5Cryption() {
        String hexString = DigestUtils.md5Hex(password.getBytes());
        logger.info("md5 password:{}, encrypted hex:{},length:{}", password, hexString, hexString.length());

//        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
//        md5.update(data);

    }
}
