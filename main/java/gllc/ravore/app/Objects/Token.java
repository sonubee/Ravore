package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 3/25/2016.
 */
public class Token {

    String token;
    String userId;
    String os;

    public Token(String token, String userId, String os) {
        this.token = token;
        this.userId = userId;
        this.os = os;
    }

    public Token(){}

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public String getOs() {
        return os;
    }
}
