package top.fusuccess.aidemo.demos.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
public class RsaUtil {
    @Value("${rsa.private}")
    private String PRIVATE_KEY;
    @Value("${rsa.public}")
    private String PUBLIC_KEY;

    public String getPublicKey() {
        return PUBLIC_KEY;
    }

    public String decrypt(String encryptedData) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(PRIVATE_KEY);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
