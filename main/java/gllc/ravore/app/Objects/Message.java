package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 1/22/2016.
 */
public class Message {

    String message;
    String sender;
    String date;
    String braceletId;
    String timestamp;

    public Message() {}

    public Message(String message, String sender, String date, String braceletId, String timestamp) {
        this.message = message;
        this.sender = sender;
        this.date = date;
        this.braceletId = braceletId;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }

    public String getBraceletId () {return braceletId;}

    public String getTimestamp() {return timestamp;}

}
