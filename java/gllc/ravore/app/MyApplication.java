package gllc.ravore.app;

import android.app.Application;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.client.Firebase;
//import com.urbanairship.UAirship;
//import com.urbanairship.push.notifications.DefaultNotificationFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gllc.ravore.app.Automation.DownloadObjects;
import gllc.ravore.app.Interfaces.GoToMainActivity;
import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.Main.MainActivity;
import gllc.ravore.app.Objects.Anon;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.Objects.DJs;
import gllc.ravore.app.Objects.Orders;
import gllc.ravore.app.Objects.Token;
import gllc.ravore.app.Objects.UserInfo;

//import com.localytics.android.LocalyticsActivityLifecycleCallbacks;

/**
 * Created by bhangoo on 2/5/2016.
 */
public class MyApplication extends Application {

    public final static String devStatus = "sandbox";

    public static String channelId;
    public static String android_id;
    public static Map<String, String> braceletKey = new HashMap<String, String>();
    public static String selectedId="";
    public static Bracelet selectedBracelet;
    public static boolean currentUserIsGiver;
    public static boolean isAlreadyUser = false;
    public static String registrationId;

    public static ArrayList<Bracelet> allBracelets = new ArrayList<>();
    public static ArrayList<Bracelet> allGivenAndReceivedBraceletsObjects = new ArrayList<>();
    public static ArrayList<Anon> allAnon = new ArrayList<>();
    public static ArrayList<DJs> allDjsArray = new ArrayList<>();
    public static ArrayList<String> favDjs = new ArrayList<>();
    public static ArrayList<Orders> allOrders = new ArrayList<>();
    public static ArrayList<UserInfo> allUsers = new ArrayList<>();
    public static ArrayList<Token> allTokens = new ArrayList<>();

    public static String useFirebase = "";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;



    @Override
    public void onCreate()
    {
        super.onCreate();

        Firebase.setAndroidContext(this);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        if (devStatus.equals("production")){
            useFirebase = "https://liveravore.firebaseio.com/";
        }

        if (devStatus.equals("sandbox")){
            useFirebase = "https://testravore.firebaseio.com/";
        }
    }

    public static String getChannelId() {
/*
        channelId = UAirship.shared().getPushManager().getChannelId();
        Log.i("MyActivity", "My Application Channel ID: " + channelId);
*/
        return channelId;
    }
}
