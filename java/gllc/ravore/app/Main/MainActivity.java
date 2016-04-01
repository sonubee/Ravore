package gllc.ravore.app.Main;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
//import com.localytics.android.Localytics;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;
import gllc.ravore.app.GCM.MyGcmListenerService;
import gllc.ravore.app.GCM.PushReceiver;
import gllc.ravore.app.GCM.QuickstartPreferences;
import gllc.ravore.app.GCM.RegistrationIntentService;
import gllc.ravore.app.Interfaces.GoToMainActivity;
import gllc.ravore.app.Kandi.OrderRavore;
import gllc.ravore.app.Kandi.OrderScreen;
//import gllc.ravore.app.Messaging.IntentReceiver;
import gllc.ravore.app.Messaging.MessagingActivity;
import gllc.ravore.app.Messaging.MessagingAdapter;
import gllc.ravore.app.Messaging.ShowAllMessages;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Token;
import gllc.ravore.app.Other.Feedback;
import gllc.ravore.app.R;

public class MainActivity extends NavigationLiveo implements OnItemClickListener{

    //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    private HelpLiveo mHelpLiveo;

    Button sendFeedback;
    EditText feedbackMessage;



    @Override
    public void onInt(Bundle savedInstanceState) {
        SDKinitializations();
        materialDesignSetup();
    }

    public void SDKinitializations() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Token setUpToken = new Token(MyApplication.registrationId, MyApplication.android_id, "android");
        boolean foundToken = false;

        for (int i = 0; i < MyApplication.allTokens.size(); i++){
            Log.i("MyActivity", "Token: " + MyApplication.allTokens.get(i).getToken());
            if (MyApplication.allTokens.get(i).getToken().equals(MyApplication.registrationId)){
                foundToken = true;
            }
        }

        if (!foundToken){
            Firebase sendTokenToServer = new Firebase(MyApplication.useFirebase+"Users/PushToken");
            sendTokenToServer.push().setValue(setUpToken);
        }

    }



    @Override
    public void onItemClick(int position) {
        Fragment mFragment;
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        switch (position) {

            case 0:
                mFragment = new ShowAllMessages();
                break;

            case 1:
                Intent intent = new Intent(getBaseContext(), OrderRavore.class);
                startActivity(intent);
                mFragment = new ShowAllMessages();
                break;

            case 2:
                mFragment = new Feedback();
                break;

            case 3:
                mFragment = new OrderScreen();
                break;

            default:
                mFragment = new ShowAllMessages();
                break;
        }

        if (mFragment != null) {
            try {
                MessagingActivity.messageArrayList.clear();
                MessagingActivity.adapter.clear();
                MessagingAdapter.pullMessages.removeEventListener(MessagingAdapter.listener1);
            } catch (Exception e) {
            }

            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.container, mFragment).commit();
        }

        setElevationToolBar(position != 2 ? 15 : 0);
    }

    public void sendFeedback(View v) {
        feedbackMessage = (EditText) findViewById(R.id.feedback_message);
        sendFeedback = (Button) findViewById(R.id.send_feedback_button);

        if (!feedbackMessage.getText().toString().equals("")) {
            sendFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SimpleDateFormat timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    timestamp.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String currentDateandTime = timestamp.format(new Date());

                    long miliSeconds = System.currentTimeMillis();
                    currentDateandTime = currentDateandTime + miliSeconds;

                    Firebase uploadFeedback = new Firebase(MyApplication.useFirebase+"Feedback/" + currentDateandTime);
                    uploadFeedback.child("timestamp").setValue(currentDateandTime);
                    uploadFeedback.child("userId").setValue(MyApplication.android_id);
                    uploadFeedback.child("feedback").setValue(feedbackMessage.getText().toString());

                    feedbackMessage.setText("");

                    Toast.makeText(getApplicationContext(), "Feedback Sent!", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MyActivity", "On Destroy");
        //System.exit(0);
    }


    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MyActivity", "On Pause");

        PushReceiver.displayNotifications=true;
        PushReceiver.unreadCount.clear();
        PushReceiver.chatIdKey.clear();

    }

    @Override
    protected void onResume() {
        super.onResume();
        PushReceiver.displayNotifications=false;
        PushReceiver.unreadCount.clear();
        PushReceiver.chatIdKey.clear();

        Log.i("MyActivity", "Do Not Display Notifications");
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
            Log.i("MyActivity", "Options Selected");

            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };

    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
        }
    };

    public void materialDesignSetup() {
        //this.userName.setText("Check Out Other Features Below!");
        //this.userEmail.setText("Or Keep Chatting!");

        this.userBackground.setImageResource(R.drawable.kandi);

        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(getString(R.string.inbox), R.mipmap.ic_drafts_black_24dp);
//        mHelpLiveo.addSubHeader(getString(R.string.categories)); //Item subHeader
        //mHelpLiveo.add(getString(R.string.starred), R.mipmap.ic_star_black_24dp);
        mHelpLiveo.add(getString(R.string.sent_mail), R.drawable.bracelet2);
        mHelpLiveo.add(getString(R.string.drafts), R.drawable.feedback);
//        mHelpLiveo.addSeparator(); // Item separator
        mHelpLiveo.add(getString(R.string.trash), R.mipmap.ic_add_white_24dp);
//        mHelpLiveo.add(getString(R.string.spam), R.mipmap.ic_report_black_24dp, 120);

        //with(this, Navigation.THEME_DARK). add theme dark
        //with(this, Navigation.THEME_LIGHT). add theme light

        with(this) // default theme is dark
                .startingPosition(0) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                        //.footerItem(R.string.settings, R.mipmap.ic_settings_black_24dp)
                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                .build();
    }

}