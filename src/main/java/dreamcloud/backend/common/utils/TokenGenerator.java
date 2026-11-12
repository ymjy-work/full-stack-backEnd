package dreamcloud.backend.common.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HexFormat;

@Component
public class TokenGenerator {
    private static final int TOKEN_BYTES = 32;
    private final SecureRandom random = new SecureRandom();

    public String generate(){
        byte[] bytes = new byte[TOKEN_BYTES];
        random.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }
}
