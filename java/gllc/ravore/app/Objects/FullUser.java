package gllc.ravore.app.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by bhangoo on 5/7/2016.
 */

@JsonIgnoreProperties({ "Bracelets" , "GoingEvents", "Matching" , "ProfilePics"})
public class FullUser{


    String lastLogin;
    String os;
    String token;
    String deviceId;
    String ravorName;
    String gender;
    ProfilePics ProfilePics;

    public FullUser(String lastLogin, String os, String token, String deviceId, String ravorName, String gender) {
        this.lastLogin = lastLogin;
        this.os = os;
        this.token = token;
        this.deviceId = deviceId;
        this.ravorName = ravorName;
        this.gender = gender;
    }

    public FullUser(){}
    public String getLastLogin() {return lastLogin;}
    public String getOs() {return os;}
    public String getToken() {return token;}
    public String getDeviceId() {return deviceId;}
    public String getRavorName() {return ravorName;}
    public String getGender() {return gender;}
    public ProfilePics getProfilePics() {return ProfilePics;}


    public static class ProfilePics {

        String userId;
        String url;
        String urlVersion;
        String fullPhotoUrl;
        String fullPhotoVersion;

        public ProfilePics(String userId, String url, String urlVersion, String fullPhotoUrl, String fullPhotoVersion) {
            this.userId = userId;
            this.url = url;
            this.urlVersion = urlVersion;
            this.fullPhotoUrl = fullPhotoUrl;
            this.fullPhotoVersion = fullPhotoVersion;
        }

        public ProfilePics () {}
        public String getUserId() {return userId;}
        public String getUrl() {return url;}
        public String getUrlVersion() {return urlVersion;}
        public String getFullPhotoUrl() {return fullPhotoUrl;}
        public String getFullPhotoVersion() {return fullPhotoVersion;}


    }

}


