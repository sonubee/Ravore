package gllc.ravore.app;

import android.app.Application;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.client.Firebase;
//import com.urbanairship.UAirship;
//import com.urbanairship.push.notifications.DefaultNotificationFactory;

import gllc.ravore.app.Main.LoginActivity;

//import com.localytics.android.LocalyticsActivityLifecycleCallbacks;

/**
 * Created by bhangoo on 2/5/2016.
 */
public class MyApplication extends Application {

    public static String channelId;

    @Override
    public void onCreate()
    {
        super.onCreate();
/*
        UAirship.takeOff(this, new UAirship.OnReadyCallback() {

            @Override
            public void onAirshipReady(UAirship airship) {
                airship.getPushManager().setUserNotificationsEnabled(false);
                // Create a custom DefaultNotificationFactory
                DefaultNotificationFactory factory = new DefaultNotificationFactory(getApplicationContext());

                // Set the icon background color
                factory.setColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_pressed));

                // Set the notification icon
                factory.setSmallIconId(R.drawable.bg_small);

                // Use the new custom factory
                airship.getPushManager().setNotificationFactory(factory);
            }

        });
*/
        //registerActivityLifecycleCallbacks(
        //new LocalyticsActivityLifecycleCallbacks(this));

    }

    public static String getChannelId() {
/*
        channelId = UAirship.shared().getPushManager().getChannelId();
        Log.i("MyActivity", "My Application Channel ID: " + channelId);
*/
        return channelId;
    }
}
