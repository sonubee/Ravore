package gllc.ravore.app.Automation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gllc.ravore.app.Interfaces.GoToMainActivity;
import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.Messaging.SendPush;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Bracelet;

/**
 * Created by bhangoo on 3/30/2016.
 */
public class AddBracelet {

    GoToMainActivity goToMainActivity;

    public AddBracelet(String giverOrReceiver, String braceletId, Context context, String fromLoginOrMain, GoToMainActivity goToMainActivity) {

        boolean found = false;

        this.goToMainActivity = goToMainActivity;

        if (MyApplication.allBracelets.size() > 2){
            if (braceletId.equals("")) {
                Toast.makeText(context, "Please enter the ID", Toast.LENGTH_SHORT).show();
            }

            else {

                Bracelet bracelet = new Bracelet();

                for (int i = 0; i < MyApplication.allBracelets.size(); i++) {
                    if (MyApplication.allBracelets.get(i).getBraceletId().equals(braceletId)) {
                        bracelet = MyApplication.allBracelets.get(i);
                        found = true;
                    }
                }

                String dateString = new SimpleDateFormat("MM" + "/" + "dd" + "/" + "yyyy").format(new Date());
                String key = MyApplication.braceletKey.get(bracelet.getBraceletId());
                Firebase updateBracelet = new Firebase(MyApplication.useFirebase + "Bracelets/" + key);

                Log.i("MyActivity", "Before Found");

                if (found) {
                    if (giverOrReceiver.equals("giver")) {
                        if (bracelet.getGiverId().equals("NA")) {

                            Map<String, Object> newGiverMap = new HashMap<>();
                            newGiverMap.put("giverId", MyApplication.android_id);
                            newGiverMap.put("dateRegistered", dateString);
                            updateBracelet.updateChildren(newGiverMap);

                            Toast.makeText(context, "Added!", Toast.LENGTH_SHORT).show();

                            if (fromLoginOrMain.equals("Login")){
                                goToMainActivity.GoToMain();
                            }
                            //LoginActivity.allGivenAndReceivedBraceletsObjects.add(LoginActivity.allBracelets.get(i));

                        } else if (bracelet.getGiverId().equals(MyApplication.android_id)) {
                            Toast.makeText(context, "You Are Already Registered for this ID", Toast.LENGTH_SHORT).show();
                            //LoginActivity.allGivenAndReceivedBraceletsObjects.add(LoginActivity.allBracelets.get(i));

                        } else {
                            Toast.makeText(context, "Already Taken!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else if (giverOrReceiver.equals("receiver")) {

                        boolean taken = false;
                        boolean noGiver = false;

                        if (bracelet.getGiverId().equals(MyApplication.android_id)) {
                            Toast.makeText(context, "Cannot Add Your Own Bracelet!\nThis already belong to you.", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            if (bracelet.getReceiverId().equals("NA") && !bracelet.getGiverId().equals("NA")) {

                                Map<String, Object> newGiverMap = new HashMap<>();
                                newGiverMap.put("receiverId", MyApplication.android_id);
                                newGiverMap.put("dateReceived", dateString);
                                updateBracelet.updateChildren(newGiverMap);

                                for (int j = 0; j < MyApplication.allTokens.size(); j++) {
                                    if (bracelet.getGiverId().equals(MyApplication.allTokens.get(j).getUserId())) {
                                        new SendPush("Bracelet " + bracelet.getBraceletId() + " has been added!", MyApplication.allTokens.get(j).getToken(), "Bracelet Added!", "addition", bracelet.getBraceletId(), MyApplication.allTokens.get(j).getOs());
                                    }
                                }

                                //LoginActivity.allGivenAndReceivedBraceletsObjects.add(bracelet);

                                if (fromLoginOrMain.equals("Login")){
                                    goToMainActivity.GoToMain();
                                }

                            }

                            if (bracelet.getReceiverId().equals("NA") && bracelet.getGiverId().equals("NA")) {
                                noGiver = true;
                            }

                            if (!bracelet.getReceiverId().equals("NA")) {
                                taken = true;
                            }
                        }

                        if (noGiver) {
                            Toast.makeText(context, "No Giver Registered.\nIs That You?", Toast.LENGTH_SHORT).show();
                        }

                        if (taken) {
                            Toast.makeText(context, "Already Taken!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                else {
                    Toast.makeText(context, "Not Found!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        else {
            Toast.makeText(context, "Try Again In a Few Seconds", Toast.LENGTH_SHORT).show();
        }
    }
}
