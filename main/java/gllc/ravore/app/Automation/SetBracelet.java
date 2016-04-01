package gllc.ravore.app.Automation;

import android.util.Log;

import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.Objects.Bracelet;

/**
 * Created by bhangoo on 3/30/2016.
 */
public class SetBracelet {

    public SetBracelet(String braceletId){
        for (int i=0; i< LoginActivity.allBracelets.size(); i++){
            if (LoginActivity.allBracelets.get(i).getBraceletId().equals(braceletId)){

                LoginActivity.selectedBracelet = LoginActivity.allBracelets.get(i);
                Log.i("MyActivity", "Set Bracelet: " + LoginActivity.allBracelets.get(i).getBraceletId());

                boolean alreadyInGRBracelets = false;
                for (int j=0; j<LoginActivity.allGivenAndReceivedBraceletsObjects.size(); j++){
                    if (LoginActivity.selectedBracelet.getBraceletId().equals(LoginActivity.allGivenAndReceivedBraceletsObjects.get(j).getBraceletId())){
                        alreadyInGRBracelets=true;
                    }
                }

                if (!alreadyInGRBracelets){
                    LoginActivity.allGivenAndReceivedBraceletsObjects.add(LoginActivity.selectedBracelet);
                }
            }
        }
    }
}
