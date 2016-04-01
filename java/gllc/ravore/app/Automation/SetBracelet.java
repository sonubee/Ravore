package gllc.ravore.app.Automation;

import android.util.Log;

import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Bracelet;

/**
 * Created by bhangoo on 3/30/2016.
 */
public class SetBracelet {

    public SetBracelet(String braceletId){
        for (int i=0; i< MyApplication.allBracelets.size(); i++){
            if (MyApplication.allBracelets.get(i).getBraceletId().equals(braceletId)){

                MyApplication.selectedBracelet = MyApplication.allBracelets.get(i);
                Log.i("MyActivity", "Set Bracelet: " + MyApplication.allBracelets.get(i).getBraceletId());

                boolean alreadyInGRBracelets = false;
                for (int j=0; j<MyApplication.allGivenAndReceivedBraceletsObjects.size(); j++){
                    if (MyApplication.selectedBracelet.getBraceletId().equals(MyApplication.allGivenAndReceivedBraceletsObjects.get(j).getBraceletId())){
                        alreadyInGRBracelets=true;
                    }
                }

                if (!alreadyInGRBracelets){
                    MyApplication.allGivenAndReceivedBraceletsObjects.add(MyApplication.selectedBracelet);
                }
            }
        }
    }
}
