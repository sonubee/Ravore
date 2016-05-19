package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 5/17/2016.
 */
public class MatchMessage {

    public MatchMessage(String message, String sender, String date, String event, String timestamp, String receiver) {
        this.message = message;
        this.sender = sender;
        this.date = date;
        this.event = event;
        this.timestamp = timestamp;
        this.receiver = receiver;
    }

    String message;
    String sender;
    String date;
    String event;
    String timestamp;
    String receiver;

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }

    public String getEvent() {
        return event;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getReceiver() {
        return receiver;
    }
}
