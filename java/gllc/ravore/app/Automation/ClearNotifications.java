package gllc.ravore.app.Automation;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by bhangoo on 4/3/2016.
 */
public class ClearNotifications {

    NotificationManager notificationManager;

    public ClearNotifications(Context context){
        notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
