package gruporas.dttabelatarifaagua.auth.core.ports;

import java.util.Map;

public interface TokenProvider {
    String generateToken(Map<String, Object> claims, String subject);
    String extractSubject(String token);
    Map<String, Object> extractClaims(String token);
}
