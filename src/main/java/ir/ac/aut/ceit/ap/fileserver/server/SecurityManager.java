package ir.ac.aut.ceit.ap.fileserver.server;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class SecurityManager {
    static final String JWT_SECRET = "3RtvA{E8=n}5D<HH&^#SlNx$<(P(5n}5D<HH&^#Su";

    static public String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
        return Jwts.builder()
                .setSubject("users/TzMUocMF4p")
                .claim("username", username)
                .signWith(key)
                .compact();
    }
}
