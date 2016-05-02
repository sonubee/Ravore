package gllc.ravore.app.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by bhangoo on 4/4/2016.
 */

@JsonIgnoreProperties({ "Bracelets" , "ProfilePics" })
public class User {

    String lastLogin;
    String os;
    String token;
    String deviceId;
    String ravorName;

    public User(String lastLogin, String os, String token, String deviceId, String ravorName) {
        this.lastLogin = lastLogin;
        this.os = os;
        this.token = token;
        this.deviceId = deviceId;
        this.ravorName = ravorName;
    }

    public User(){}

    public String getLastLogin() {return lastLogin;}

    public String getOs() {
        return os;
    }

    public String getToken() {
        return token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getRavorName() {return ravorName;}

}
