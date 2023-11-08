package dev.ngdangkiet.encoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * @author ngdangkiet
 * @since 11/8/2023
 */

@Component
public class PBKDF2Encoder implements PasswordEncoder {

    @Value("${springbootwebfluxjwt.password.encoder.secret}")
    private String secret;

    @Value("${springbootwebfluxjwt.password.encoder.iteration}")
    private Integer iteration;

    @Value("${springbootwebfluxjwt.password.encoder.keyLength}")
    private Integer keyLength;

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(), secret.getBytes(), iteration, keyLength))
                    .getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
