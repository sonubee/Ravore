package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 2/5/2016.
 */
public class Anon {

    String userId;
    String url;
    String urlVersion;
    String fullPhotoUrl;
    String fullPhotoVersion;

    public Anon(String userId, String url, String urlVersion, String fullPhotoUrl, String fullPhotoVersion) {
        this.userId = userId;
        this.url = url;
        this.urlVersion = urlVersion;
        this.fullPhotoUrl = fullPhotoUrl;
        this.fullPhotoVersion = fullPhotoVersion;
    }

    public Anon () {}

    public String getUserId() {return userId;}

    public String getUrl() {return url;}

    public String getUrlVersion() {return urlVersion;}

    public String getFullPhotoUrl() {return fullPhotoUrl;}

    public String getFullPhotoVersion() {return fullPhotoVersion;}
}
