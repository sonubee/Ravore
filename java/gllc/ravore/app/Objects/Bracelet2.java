package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 4/22/2016.
 */
public class Bracelet2 {

    String receiverId;
    String giverId;
    String dateCreated;
    String dateReceived;
    String dateRegistered;
    String braceletId;

    int transferNumber;

    public Bracelet2(){}

    public Bracelet2(String receiverId, String giverId, String braceletId, String dateCreated, String dateReceived, String dateRegistered, int transferNumber) {
        this.receiverId = receiverId;
        this.giverId = giverId;
        this.dateCreated = dateCreated;
        this.dateReceived = dateReceived;
        this.dateRegistered = dateRegistered;
        this.braceletId = braceletId;
        this.transferNumber = transferNumber;}



    public String getReceiverId() {return receiverId;}

    public String getGiverId() {return giverId;}

    public String getBraceletId() {return braceletId;}

    public String getDateCreated() {return dateCreated;}

    public String getDateReceived() {return dateReceived;}

    public String getDateRegistered() {return dateRegistered;}

    public int getTransferNumber() {return transferNumber;}
}
