package ir.ac.aut.ceit.ap.fileserver.server;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ir.ac.aut.ceit.ap.fileserver.network.Request;

import java.io.UnsupportedEncodingException;

public class SecurityManager {
    Request request;
    static final String secret = "3RtvA{E8=lNx$<(P(5n}5D<HH&^#Su";

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(generateToken("admin"));
    }

    static String generateToken(String username) throws UnsupportedEncodingException {
        String token = Jwts.builder()
                .setSubject("users/TzMUocMF4p")
                .claim("username", username)
                .signWith(
                        SignatureAlgorithm.HS256,
                        secret.getBytes("UTF-8")
                )
                .compact();

        return token;
    }
}
