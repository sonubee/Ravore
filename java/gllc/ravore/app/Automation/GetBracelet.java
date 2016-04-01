package gllc.ravore.app.Automation;

import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Bracelet;

/**
 * Created by bhangoo on 3/31/2016.
 */
public class GetBracelet {

    public static Bracelet tempBracelet;

    public static Bracelet getBracelet(String braceletId){

        tempBracelet = null;

        for (int i=0; i< MyApplication.allBracelets.size(); i++){
            if (braceletId.equals(MyApplication.allBracelets.get(i).getBraceletId())){
                tempBracelet = MyApplication.allBracelets.get(i);}}

        return tempBracelet;
    }
}
