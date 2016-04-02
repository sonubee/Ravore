package gllc.ravore.app.GCM;

/**
 * Created by bhangoo on 3/17/2016.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.R;



public class PushReceiver extends BroadcastReceiver
{
    public static int notificationsId=0;

    public static Map<String, String> chatIdKey = new HashMap<String, String>();
    public static Map<String, String> unreadCount = new HashMap<String, String>();
    public static Map<String, String> braceletUnreadCount = new HashMap<String, String>();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //-----------------------------
        // Create a test notification
        //
        // (Use deprecated notification
        // API for demonstration purposes,
        // to avoid having to import
        // the Android Support Library)
        //-----------------------------

        String notificationTitle = "Pushy";
        String notificationDesc = "Test notification";

        //-----------------------------
        // Attempt to grab the message
        // property from the payload
        //
        // We will be sending the following
        // test push notification:
        //
        // {"message":"Hello World!"}
        //-----------------------------

        if ( intent.getStringExtra("message") != null )
        {
            notificationDesc = intent.getStringExtra("message");
            Log.i("MyActivity", "Message: " + notificationDesc);
        }

        //-----------------------------
        // Create a test notification
        //-----------------------------

        Notification notification = new Notification(android.R.drawable.ic_dialog_info, notificationDesc, System.currentTimeMillis());

        //-----------------------------
        // Sound + vibrate + light
        //-----------------------------

        notification.defaults = Notification.DEFAULT_ALL;

        //-----------------------------
        // Dismisses when pressed
        //-----------------------------

        notification.flags = Notification.FLAG_AUTO_CANCEL;

        //-----------------------------
        // Create pending intent
        // without a real intent
        //-----------------------------

        //notification.setLatestEventInfo(context, notificationTitle, notificationDesc, null);

        //-----------------------------
        // Get notification manager
        //-----------------------------

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //showNotification(context, notificationDesc, "Title", 0);
        sendNotification(notificationDesc, context);

        //-----------------------------
        // Issue the notification
        //-----------------------------

        //mNotificationManager.notify(0, notification);
    }

    private void sendNotification(String message, Context context) {

        if (MyApplication.displayNotifications){
            JSONObject messageJSON = null;
            String messageDecoded = "";
            String messageTitle = "";
            String messageType = "";
            String messageBracelet = "";
            String messageToDisplay = "";

            try {
                messageJSON = new JSONObject(message);

                messageDecoded = messageJSON.getString("message");
                messageTitle = messageJSON.getString("title");
                messageType = messageJSON.getString("type");
                messageBracelet = messageJSON.getString("braceletId");

                Log.i("MyActivity", "messageDecoded1: " + messageDecoded);

            } catch (Exception e){Log.i("MyActivity", "Error from BackGround Push");}

            if (messageType.equals("addition")){
                notificationsId++;
                showNotification(context, messageDecoded, messageTitle, notificationsId);
            }

            else if (chatIdKey.containsKey(messageBracelet)){
                Log.i("MyActivity", "Found Key");
                String count = unreadCount.get(messageBracelet);
                int intCount = Integer.parseInt(count);
                intCount++;
                messageToDisplay = "(" +intCount+") " + messageDecoded;
                unreadCount.put(messageBracelet, Integer.toString(intCount));
                showNotification(context, messageToDisplay, messageBracelet, notificationsId);
            }

            else {
                Log.i("MyActivity", "Did Not Find Key");
                chatIdKey.put(messageBracelet,Integer.toString(notificationsId) );
                notificationsId++;
                unreadCount.put(messageBracelet, "1");
                messageToDisplay = messageDecoded;
                showNotification(context, messageToDisplay, messageBracelet, notificationsId);
            }
        }
    }

    private void showNotification(Context context, String message, String title, int notificationId){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.bg_small)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }
}