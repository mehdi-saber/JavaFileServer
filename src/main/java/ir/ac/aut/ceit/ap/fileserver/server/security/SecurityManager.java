package ir.ac.aut.ceit.ap.fileserver.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ir.ac.aut.ceit.ap.fileserver.file.SaveAble;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.ResponseSubject;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;
import ir.ac.aut.ceit.ap.fileserver.server.ClientInfo;
import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

public class SecurityManager implements SaveAble {
    private SecretKey key;
    private List<User> userList;
    private long clientIdCounter;

    private synchronized Long createClientId() {
        return clientIdCounter++;
    }

    public SecurityManager() {
        Long clientIdCounter = (Long) load();
        this.clientIdCounter = clientIdCounter != null ? clientIdCounter : 0;
        this.userList = new ArrayList<>();
        userList.add(new User("admin", BCrypt.hashpw("admin123", BCrypt.gensalt())));
        String JWT_SECRET = "3RtvA{E8=n}5D<HH&^#SlNx$<(P(5n}5D<HH&^#Su";
        key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    public SendingMessage loginUser(ReceivingMessage request) {
        int listenPort = (int) request.getParameter("listenPort");
        String username = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");
        for (User user : userList)
            if (user.getUsername().equals(username))
                if (BCrypt.checkpw(password, user.getPassHash())) {
                    SendingMessage tokenResponse = new SendingMessage(ResponseSubject.OK);
                    ClientInfo client = new ClientInfo(createClientId(), request.getSenderAddress(), listenPort, username);
                    tokenResponse.addParameter("client", client);
                    tokenResponse.addParameter("token", getUserToken(client));
                    return tokenResponse;
                }
        return new SendingMessage(ResponseSubject.FORBIDDEN);
    }

    private String getUserToken(ClientInfo client) {
        return Jwts.builder()
                .claim("username", client.getUsername())
                .signWith(key)
                .compact();
    }

    public String getUsername(String token) {
        return (String) Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .get("username");
    }

    public User getUserByUserName(String username) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    private boolean checkUserPassValidation(String username, String password) {
        return username.matches("/^[a-z][^\\W_]{3,14}$/i") &&
                password.matches("/^(?=[^a-z]*[a-z])(?=\\D*\\d)[^:&.~\\s]{5,20}$/");
    }

    @Override
    public Object getSaveObject() {
        return clientIdCounter;
    }

    @Override
    public String getSaveFileName() {
        return "securityManager";
    }
}
