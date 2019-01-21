package ir.ac.aut.ceit.ap.fileserver.server.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ir.ac.aut.ceit.ap.fileserver.network.Message;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.ResponseSubject;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;
import ir.ac.aut.ceit.ap.fileserver.server.ClientInfo;
import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

public class SecurityManager {
    private SecretKey key;
    private List<User> userList;

    public SecurityManager() {
        this.userList = new ArrayList<>();
        userList.add(new User("admin", BCrypt.hashpw("admin123", BCrypt.gensalt())));
        String JWT_SECRET = "3RtvA{E8=n}5D<HH&^#SlNx$<(P(5n}5D<HH&^#Su";
        key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    public SendingMessage loginUser(Message request, ClientInfo client) {
        String username = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");
        for (User user : userList)
            if (user.getUsername().equals(username))
                if (BCrypt.checkpw(password, user.getPassHash())) {
                    SendingMessage tokenData = new SendingMessage(ResponseSubject.OK);
                    tokenData.addParameter("token", getUserToken(client));
                    return tokenData;
                }
        return new SendingMessage(ResponseSubject.FAILED);
    }

    private String getUserToken(ClientInfo client) {
        return Jwts.builder()
                .claim("username", client.getUsername())
                .claim("address", client.getAddress())
                .claim("port", client.getListenPort())
                .signWith(key)
                .compact();
    }

    private boolean checkUserPassValidation(String username, String password) {
        return username.matches("/^[a-z][^\\W_]{3,14}$/i") &&
                password.matches("/^(?=[^a-z]*[a-z])(?=\\D*\\d)[^:&.~\\s]{5,20}$/");
    }

    public Message authUser(Message request, List<ClientInfo> clientList) {
//        String username = (String) request.getParameter("username");
//        String password = (String) request.getParameter("password");
//
//        for (ClientInfo client : clientList)
//            if (client.username.equals(username))
//                return new Message(Subject.REGISTER_USER_REPEATED_USERNAME);
//
//        Message tokenData = new Message(Subject.LOGIN_OK);
//        String token = generateToken(username);
//        tokenData.addParameter("token", token);
//        return tokenData;
        return null;
    }

}
