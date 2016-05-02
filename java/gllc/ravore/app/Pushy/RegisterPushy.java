package gllc.ravore.app.Pushy;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

import gllc.ravore.app.Automation.GetDateTimeInstance;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Token;
import me.pushy.sdk.Pushy;

/**
 * Created by bhangoo on 3/31/2016.
 */
public class RegisterPushy extends AsyncTask<Void, Void, Exception>
{
    private Context applicationContext;
    Activity activity;

    public RegisterPushy(Context applicationContext, Activity activity){
        this.applicationContext = applicationContext;
        this.activity = activity;
    }

    protected Exception doInBackground(Void... params)
    {
        Log.i("--AllRegisterPushy", "Came to Async");
        try
        {
            // Acquire a unique registration ID for this device
            MyApplication.registrationId = Pushy.register(applicationContext);

            Log.i("--AllRegisterPushy", "Reg ID: " + MyApplication.registrationId);

            // Send the registration ID to your backend server and store it for later
            Log.i("--AllRegisterPushy", "Before Sending to Server!");
            sendRegistrationIdToBackendServer(MyApplication.registrationId);
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
            Log.i("--AllRegisterPushy", "Error: " + exc.getMessage());
            // Show error as toast message
            //Toast.makeText(applicationContext, exc.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        Log.i("--AllRegisterPushy", "Success");
        // Succeeded, do something to alert the user
    }

    // Example implementation
    void sendRegistrationIdToBackendServer(String registrationId) throws Exception
    {

        Log.i("--AllRegisterPushy", "Came to Method");

        Token setUpToken = new Token(MyApplication.registrationId, MyApplication.android_id, "android");
        boolean foundToken = false;

        Log.i("--AllRegisterPushy", "Before loop");

        for (int i = 0; i < MyApplication.allTokens.size(); i++){
            if (MyApplication.allTokens.get(i).getToken().equals(registrationId)){
                Log.i("--AllRegisterPushy", "Token: " + MyApplication.allTokens.get(i).getToken());
                foundToken = true;
            }
        }

        Log.i("--AllRegisterPushy", "After loop");

        //if (!foundToken){new Firebase(MyApplication.useFirebase+"Users/PushToken").push().setValue(setUpToken);}

        Log.i("--AllRegisterPushy", "Before Map");

        Context context = activity;

        SharedPreferences sharedPref = context.getSharedPreferences("gllc.ravore.app.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        String ravorName = sharedPref.getString("RavorName", "NA");

        Map<String, Object> putToken = new HashMap<String, Object>();
        putToken.put("token", MyApplication.registrationId);
        putToken.put("deviceId", MyApplication.android_id);
        putToken.put("os", "android");
        putToken.put("lastLogin", GetDateTimeInstance.getRegDate());
        putToken.put("ravorName", ravorName);

        Log.i("--AllRegisterPushy", "Pushing Token: " + putToken);

        new Firebase(MyApplication.useFirebase+"UserInfo").child(MyApplication.android_id).updateChildren(putToken);
    }
}