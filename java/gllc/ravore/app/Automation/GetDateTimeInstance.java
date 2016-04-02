package gllc.ravore.app.Automation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by bhangoo on 4/1/2016.
 */
public class GetDateTimeInstance {

    public static String getRegDate(){
        return new SimpleDateFormat("MM" + "/" + "dd" + "/" + "yyyy").format(new Date());
    }

    public static String getTimeStamp(){
        SimpleDateFormat timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss");
        timestamp.setTimeZone(TimeZone.getTimeZone("PST"));
        String currentDateandTime = timestamp.format(new Date());
        long miliSeconds = System.currentTimeMillis();
        currentDateandTime = currentDateandTime + miliSeconds;

        return currentDateandTime;
    }
}
