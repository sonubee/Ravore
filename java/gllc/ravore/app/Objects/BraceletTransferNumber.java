package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 4/22/2016.
 */
public class BraceletTransferNumber {

    String braceletId;
    int transferNumber;

    public BraceletTransferNumber(String braceletId, int transferNumber) {
        this.braceletId = braceletId;
        this.transferNumber = transferNumber;
    }

    public String getBraceletId() {
        return braceletId;
    }

    public int getTransferNumber() {
        return transferNumber;
    }
}
