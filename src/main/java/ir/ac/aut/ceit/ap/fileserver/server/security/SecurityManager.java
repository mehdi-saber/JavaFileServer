package ir.ac.aut.ceit.ap.fileserver.server.security;

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

/**
 * Handles server restriction behaviors
 */
public class SecurityManager implements SaveAble {
    private SecretKey key;
    private List<User> userList;
    private long clientIdCounter;

    /**
     * Constructs a new security manager
     */
    public SecurityManager() {
        Long clientIdCounter = (Long) load();
        this.clientIdCounter = clientIdCounter != null ? clientIdCounter : 0;
        this.userList = new ArrayList<>();
        userList.add(new User("admin", BCrypt.hashpw("admin123", BCrypt.gensalt())));
        String JWT_SECRET = "3RtvA{E8=n}5D<HH&^#SlNx$<(P(5n}5D<HH&^#Su";
        key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    /**
     * Creates new ID for clients
     *
     * @return A new ID
     */
    private synchronized Long createClientId() {
        return clientIdCounter++;
    }

    /**
     * Authenticates user
     *
     * @param request The auth request
     * @return OK if successful else FORBIDDEN
     */
    public SendingMessage loginUser(ReceivingMessage request) {
        int listenPort = (int) request.getParameter("listenPort");
        String username = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");
        int space = (int) request.getParameter("space");
        for (User user : userList)
            if (user.getUsername().equals(username))
                if (BCrypt.checkpw(password, user.getPassHash())) {
                    SendingMessage tokenResponse = new SendingMessage(ResponseSubject.OK);
                    ClientInfo client = new ClientInfo(createClientId(), request.getSenderAddress(), listenPort, username, space);
                    tokenResponse.addParameter("client", client);
                    tokenResponse.addParameter("token", getUserToken(client));
                    return tokenResponse;
                }
        return new SendingMessage(ResponseSubject.FORBIDDEN);
    }

    /**
     * Create and sign a  Json Web Token
     *
     * @param client the client
     * @return The user token
     */
    private String getUserToken(ClientInfo client) {
        return Jwts.builder()
                .claim("username", client.getUsername())
                .signWith(key)
                .compact();
    }

    /**
     * Decodes a token
     *
     * @param token The token
     * @return The username
     */
    public String getUsername(String token) {
        return (String) Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .get("username");
    }

    /**
     * Finds user with specific username
     *
     * @param username The username
     * @return The User
     */
    public User getUserByUserName(String username) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * @return Saving data
     */
    @Override
    public Object getSaveObject() {
        return clientIdCounter;
    }

    /**
     * @return Save file name
     */
    @Override
    public String getSaveFileName() {
        return "securityManager";
    }
}
