package gllc.ravore.app.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by bhangoo on 5/7/2016.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class ProfilePics {

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
