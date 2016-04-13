package gllc.ravore.app.Main;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.firebase.client.Firebase;
import com.localytics.android.Localytics;
import com.splunk.mint.Mint;

import gllc.ravore.app.Automation.AddBracelet;
import gllc.ravore.app.Automation.GetDateTimeInstance;
import gllc.ravore.app.Interfaces.GoToMainActivity;
import gllc.ravore.app.OrderRavore.OrderRavoreActivity;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.UserInfo;
import gllc.ravore.app.Pushy.RegisterPushy;
import gllc.ravore.app.R;
import me.pushy.sdk.Pushy;


public class LoginActivity extends FragmentActivity implements GoToMainActivity {

    EditText inputID;
    TextView HIW, getRavore;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    GoToMainActivity goToMainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginSetup();
        setUpHowItWorksAndOrderRavoreButton();

    }

    public void loginSetup(){
        HIW = (TextView)findViewById(R.id.how_it_works_button);
        getRavore = (TextView)findViewById(R.id.getRavore);
        goToMainActivity = this;

        if (MyApplication.allBracelets.size() < 2){MyApplication.beginDownload(goToMainActivity, getBaseContext());}
        else {startActivity(new Intent(getBaseContext(), MainActivity.class));}


    }

    public void receiver (View v){
        if (MyApplication.devStatus.equals("production")){
            Mint.logEvent("Received Bracelet");}
        inputID = (EditText)findViewById(R.id.input_id);
        String braceletId = inputID.getText().toString();
        new AddBracelet("receiver", braceletId, getBaseContext(), "Login", goToMainActivity);
    }

    public void giver (View v){
        if (MyApplication.devStatus.equals("production")){Mint.logEvent("Registered Bracelet");}
        inputID = (EditText)findViewById(R.id.input_id);
        String braceletId = inputID.getText().toString();
        new AddBracelet("giver", braceletId, getBaseContext(), "Login", goToMainActivity);
    }

    public void setUpHowItWorksAndOrderRavoreButton(){

        new HowItWorks(getBaseContext(), HIW, getRavore, LoginActivity.this);

        getRavore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.cameFromLogin = true;
                Intent intent = new Intent(getBaseContext(), OrderRavoreActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    public void GoToMain() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    public void addUserIntoDatabase(){

        boolean alreadyInUserDatabase = false;

        for (int j=0; j<MyApplication.allUsers.size(); j++){
            if (MyApplication.allUsers.get(j).getUserId().equals(MyApplication.android_id)){
                alreadyInUserDatabase = true;}}

        if (!alreadyInUserDatabase){
            UserInfo newUser = new UserInfo(MyApplication.android_id, GetDateTimeInstance.getRegDate());
            new Firebase (MyApplication.useFirebase+"Users/AllUsers").push().setValue(newUser);
        }
    }
}
