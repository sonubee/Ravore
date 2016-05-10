package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 5/9/2016.
 */
public class Party {

    String partyName;
    String location;
    String date;
    String time;
    String details;
    String imageUrl;

    public Party(){}

    public Party(String partyName, String location, String date, String time, String details, String imageUrl) {
        this.partyName = partyName;
        this.location = location;
        this.date = date;
        this.time = time;
        this.details = details;
        this.imageUrl = imageUrl;
    }

    public String getPartyName() {
        return partyName;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDetails() {
        return details;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
