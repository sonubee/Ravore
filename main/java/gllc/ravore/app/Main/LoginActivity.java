package gllc.ravore.app.Main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import gllc.ravore.app.Automation.AddBracelet;
import gllc.ravore.app.Automation.DownloadObjects;
import gllc.ravore.app.Automation.SetBracelet;
import gllc.ravore.app.GCM.QuickstartPreferences;
import gllc.ravore.app.GCM.RegistrationIntentService;
import gllc.ravore.app.Interfaces.GoToMainActivity;
import gllc.ravore.app.Kandi.OrderRavore;
import gllc.ravore.app.Messaging.ListAllMessagesAdapter;
import gllc.ravore.app.Messaging.MessagingActivity;
import gllc.ravore.app.Messaging.SendPush;
import gllc.ravore.app.Messaging.ShowAllMessages;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Anon;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.Objects.DJs;
import gllc.ravore.app.Objects.Orders;
import gllc.ravore.app.Objects.Token;
import gllc.ravore.app.Objects.UserInfo;
import gllc.ravore.app.R;
import me.pushy.sdk.Pushy;

public class LoginActivity extends FragmentActivity implements GoToMainActivity {
    //String dateString = new SimpleDateFormat("MM" + "/" + "dd" + "/" + "yyyy").format(new Date());


    public static ArrayList<Bracelet> allBracelets = new ArrayList<>();
    public static ArrayList<Bracelet> allGivenAndReceivedBraceletsObjects = new ArrayList<>();
    public static ArrayList<Anon> allAnon = new ArrayList<>();
    public static ArrayList<DJs> allDjsArray = new ArrayList<>();
    public static ArrayList<String> favDjs = new ArrayList<>();
    public static ArrayList<Orders> allOrders = new ArrayList<>();
    public static ArrayList<UserInfo> allUsers = new ArrayList<>();
    public static ArrayList<Token> allTokens = new ArrayList<>();

    public static Map<String, String> braceletKey = new HashMap<String, String>();

    public static String selectedId="";
    public static String android_id;
    public static String useFirebase = "";
    public static Bracelet selectedBracelet;
    ProgressDialog loadingDialog;
    public static boolean currentUserIsGiver;
    public static boolean isAlreadyUser;
    public static String registrationId;

    public final static String devStatus = "sandbox";

    EditText inputID;
    TextView HIW;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoToMainActivity goToMainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Pushy.listen(this);

        goToMainActivity = this;

        new registerForPushNotificationsAsync().execute();

        setUpVariablesAndContexts();
        //Localytics.tagEvent("Opened Ravore");

        loginOrNo();

        setChannelId();

        setUpHowItWorksButton();

        turnOnLoadingDialog();

        setUpGCM();

    }

    public void loginOrNo(){
        if (allBracelets.size() < 2){
            new DownloadObjects(getBaseContext(), goToMainActivity);
        }
        else {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public void setUpVariablesAndContexts(){

        Firebase.setAndroidContext(this);
        isAlreadyUser=false;
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        HIW = (TextView)findViewById(R.id.how_it_works_button);


        if (devStatus.equals("production")){
            useFirebase = "https://liveravore.firebaseio.com/";
        }

        if (devStatus.equals("sandbox")){
            useFirebase = "https://testravore.firebaseio.com/";
        }

    }

    public void turnOnLoadingDialog(){
        loadingDialog = new ProgressDialog(LoginActivity.this);
        loadingDialog.setMessage("Loading");
        //loadingDialog.show();
    }

    public void showHowToReceive(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("How To Receive");
        builder.setMessage("On your Bracelet there is an ID.\n\nEnter that into the Login Screen and click the \"I Got Kandi!\" button.\n\nYou can now chat with the person who gave you the bracelet!");
        builder.show();
    }

    public void showHowToGive(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("How To Give A Bracelet");
        builder.setMessage("On the Bracelet there is an ID.\n\nEnter that into the Login Screen and click the \"Register Kandi\" button.\n\nNow Give that bracelet to someone to chat with them!");
        builder.show();
    }

    public void showNoAccount(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("No Account Needed");
        builder.setMessage("We use the bracelet to ID so you won't need an account to chat!\n\nHowever, you'll be missing out on what our free account has to offer :) Check us out!");
        builder.show();
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

    public static void setSelectedBracelet(String selectedId){

        new SetBracelet(selectedId);

    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    public void buyRavore (View v){
        Intent intent = new Intent(getBaseContext(), OrderRavore.class);
        startActivity(intent);
    }

    public static void setChannelId(){
        Firebase setChannelId = new Firebase(useFirebase+"Users/ChannelIDs/" + android_id);
        setChannelId.setValue(MyApplication.getChannelId());
    }


    public void setUpHowItWorksButton(){
        HIW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Localytics.tagEvent("How It Works");
                final CharSequence[] items = {"I Received A Bracelet", "I Want To Give A Bracelet", "No Account Needed?"};
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("How It Works");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("I Received A Bracelet")) {
                            showHowToReceive();
                        } else if (items[item].equals("I Want To Give A Bracelet")) {
                            showHowToGive();
                        } else if (items[item].equals("No Account Needed?")) {
                            showNoAccount();
                        }

                    }
                });
                builder.show();
            }
        });
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
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
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

    private class registerForPushNotificationsAsync extends AsyncTask<Void, Void, Exception>
    {

        protected Exception doInBackground(Void... params)
        {
            Log.i("MyActivity", "Came to Async");
            try
            {
                // Acquire a unique registration ID for this device
                registrationId = Pushy.register(getApplicationContext());

                Log.i("MyActivity", "Reg ID: " + registrationId);

                // Send the registration ID to your backend server and store it for later
                sendRegistrationIdToBackendServer(registrationId);
            }
            catch( Exception exc )
            {
                // Return exc to onPostExecute
                return exc;
            }

            // We're good
            return null;
        }

        @Override
        protected void onPostExecute(Exception exc)
        {
            // Failed?
            if ( exc != null )
            {
                // Show error as toast message
                Toast.makeText(getApplicationContext(), exc.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            // Succeeded, do something to alert the user
        }

        // Example implementation
        void sendRegistrationIdToBackendServer(String registrationId) throws Exception
        {
            // The URL to the function in your backend API that stores registration IDs
            URL sendRegIdRequest = new URL("https://{YOUR_API_HOSTNAME}/register/device?registration_id=" + registrationId);

            // Send the registration ID by executing the GET request
            sendRegIdRequest.openConnection();
        }
    }

    public void addUserIntoDatabase(){

        String dateString = new SimpleDateFormat("MM" + "/" + "dd" + "/" + "yyyy").format(new Date());
        boolean alreadyInUserDatabase = false;

        for (int j=0; j<allUsers.size(); j++){
            if (allUsers.get(j).getUserId().equals(android_id)){
                alreadyInUserDatabase = true;
            }
        }

        if (!alreadyInUserDatabase){
            Firebase addUserIntoDatabase = new Firebase (useFirebase+"Users/AllUsers");
            UserInfo newUser = new UserInfo(android_id, dateString);
            addUserIntoDatabase.push().setValue(newUser);
        }
    }
}
