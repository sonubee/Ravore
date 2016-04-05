package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 4/4/2016.
 */
public class User {

    String lastLogin;
    String os;
    String token;
    String deviceId;

    public User(String lastLogin, String os, String token, String deviceId) {
        this.lastLogin = lastLogin;
        this.os = os;
        this.token = token;
        this.deviceId = deviceId;
    }

    public User(){}

    public String getLastLogin() {
        return lastLogin;
    }

    public String getOs() {
        return os;
    }

    public String getToken() {
        return token;
    }

    public String getDeviceId() {
        return deviceId;
    }

}
