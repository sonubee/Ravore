package gllc.ravore.app.Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import java.text.SimpleDateFormat;
import java.util.Date;
import gllc.ravore.app.Automation.AddBracelet;
import gllc.ravore.app.GCM.QuickstartPreferences;
import gllc.ravore.app.GCM.RegistrationIntentService;
import gllc.ravore.app.Interfaces.GoToMainActivity;
import gllc.ravore.app.OrderRavore.OrderRavoreActivity;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.UserInfo;
import gllc.ravore.app.R;


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

        setUpGCM();

        //Localytics.tagEvent("Opened Ravore");

    }

    public void loginSetup(){
        HIW = (TextView)findViewById(R.id.how_it_works_button);
        getRavore = (TextView)findViewById(R.id.getRavore);
        goToMainActivity = this;

        if (MyApplication.allBracelets.size() < 2){
            MyApplication.beginDownload(goToMainActivity, getBaseContext());
        }
    }

    public void receiver (View v){
        //Localytics.tagEvent("Receive Successful");
        inputID = (EditText)findViewById(R.id.input_id);
        String braceletId = inputID.getText().toString();
        new AddBracelet("receiver", braceletId, getBaseContext(), "Login", goToMainActivity);
    }

    public void giver (View v){
        //Localytics.tagEvent("Registered Bracelet");
        inputID = (EditText)findViewById(R.id.input_id);
        String braceletId = inputID.getText().toString();

        //NEED TO ADD CHECK FOR MAKING SURE THERE ARE BRACELETS BEFORE SEEING IF UDID IS GIVER. DO if (allBracelets.size() > 2) //BUT NO NEED TO CHECK MAYBE SINCE USER IS MANUALLY ENTERING?

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



    public void setUpGCM(){
        //mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        Log.i("GCM", "setUpGCM");
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                Log.i("GCM", "inReceive");
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));

                } else {
                    //mInformationTextView.setText(getString(R.string.token_error_message));

                }
            }
        };
        Log.i("GCM", "Before Register Receiver");

        //mInformationTextView = (TextView) findViewById(R.id.informationTextView);

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            Log.i("GCM", "Passed Play Services Check");
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, MyApplication.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("GCM", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void GoToMain() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    public void addUserIntoDatabase(){

        String dateString = new SimpleDateFormat("MM" + "/" + "dd" + "/" + "yyyy").format(new Date());
        boolean alreadyInUserDatabase = false;

        for (int j=0; j<MyApplication.allUsers.size(); j++){
            if (MyApplication.allUsers.get(j).getUserId().equals(MyApplication.android_id)){
                alreadyInUserDatabase = true;
            }
        }

        if (!alreadyInUserDatabase){
            Firebase addUserIntoDatabase = new Firebase (MyApplication.useFirebase+"Users/AllUsers");
            UserInfo newUser = new UserInfo(MyApplication.android_id, dateString);
            addUserIntoDatabase.push().setValue(newUser);
        }
    }
}
