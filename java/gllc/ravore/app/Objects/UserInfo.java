package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 2/26/2016.
 */
public class UserInfo {

    String userId;
    String dateCreated;

    public UserInfo() {}

    public UserInfo(String userId, String dateCreated) {
        this.userId = userId;
        this.dateCreated = dateCreated;}

    public String getUserId() {return userId;}

    public String getDateCreated() {return dateCreated;}

}
