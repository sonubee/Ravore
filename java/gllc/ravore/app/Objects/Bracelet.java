package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 2/3/2016.
 */
public class Bracelet {

    String receiverId;
    String giverId;
    String dateCreated;
    String dateReceived;
    String dateRegistered;
    String braceletId;

    public Bracelet(){}

    public Bracelet(String receiverId, String giverId, String braceletId, String dateCreated, String dateReceived, String dateRegistered) {
        this.receiverId = receiverId;
        this.giverId = giverId;
        this.dateCreated = dateCreated;
        this.dateReceived = dateReceived;
        this.dateRegistered = dateRegistered;
        this.braceletId = braceletId;}



    public String getReceiverId() {return receiverId;}

    public String getGiverId() {return giverId;}

    public String getBraceletId() {return braceletId;}

    public String getDateCreated() {return dateCreated;}

    public String getDateReceived() {return dateReceived;}

    public String getDateRegistered() {return dateRegistered;}

}
