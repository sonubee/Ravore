/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gllc.ravore.app.GCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.R;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    public static int notificationsId=0;

    public static Map<String, String> chatIdKey = new HashMap<String, String>();
    public static Map<String, String> unreadCount = new HashMap<String, String>();
    public static Map<String, String> braceletUnreadCount = new HashMap<String, String>();

    public static boolean displayNotifications = false;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {

        if (displayNotifications){
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

                Log.i("MyActivity", "messageDecoded: " + messageDecoded);

            } catch (Exception e){Log.i("MyActivity", "Error from BackGround Push");}

            if (messageType.equals("addition")){
                notificationsId++;
                showNotification(messageDecoded, messageTitle, notificationsId);
            }

            else if (chatIdKey.containsKey(messageBracelet)){
                Log.i("MyActivity", "Found Key");
                String count = unreadCount.get(messageBracelet);
                int intCount = Integer.parseInt(count);
                intCount++;
                messageToDisplay = "(" +intCount+") " + messageDecoded;
                unreadCount.put(messageBracelet, Integer.toString(intCount));
                showNotification(messageToDisplay, messageBracelet, notificationsId);
            }

            else {
                Log.i("MyActivity", "Did Not Find Key");
                chatIdKey.put(messageBracelet,Integer.toString(notificationsId) );
                notificationsId++;
                unreadCount.put(messageBracelet, "1");
                messageToDisplay = messageDecoded;
                showNotification(messageToDisplay, messageBracelet, notificationsId);
            }
        }
    }

    private void showNotification(String message, String title, int notificationId){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.bg_small)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }


}
