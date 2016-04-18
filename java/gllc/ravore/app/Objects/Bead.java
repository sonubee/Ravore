package gllc.ravore.app.Objects;

/**
 * Created by bhangoo on 4/16/2016.
 */
public class Bead {

    String beadName;
    String drawableName;

    public Bead (String beadName, String drawableName){
        this.beadName = beadName;
        this.drawableName = drawableName;
    }

    public Bead(){}

    public String getBeadName() {return beadName;}

    public String getDrawableName() {return drawableName;}

}
