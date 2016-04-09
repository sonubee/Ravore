package gllc.ravore.app.Pushy;

import android.content.Context;
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

    public RegisterPushy(Context applicationContext){
        this.applicationContext = applicationContext;
    }

    protected Exception doInBackground(Void... params)
    {
        Log.i("MyActivity", "Came to Async");
        try
        {
            // Acquire a unique registration ID for this device
            MyApplication.registrationId = Pushy.register(applicationContext);

            Log.i("MyActivity", "Reg ID: " + MyApplication.registrationId);

            // Send the registration ID to your backend server and store it for later
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
            // Show error as toast message
            Toast.makeText(applicationContext, exc.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // Succeeded, do something to alert the user
    }

    // Example implementation
    void sendRegistrationIdToBackendServer(String registrationId) throws Exception
    {
        Token setUpToken = new Token(MyApplication.registrationId, MyApplication.android_id, "android");
        boolean foundToken = false;

        for (int i = 0; i < MyApplication.allTokens.size(); i++){
            if (MyApplication.allTokens.get(i).getToken().equals(MyApplication.registrationId)){
                foundToken = true;
            }
        }

        if (!foundToken){
            Firebase sendTokenToServer = new Firebase(MyApplication.useFirebase+"Users/PushToken");
            sendTokenToServer.push().setValue(setUpToken);
        }

        Map<String, String> putToken = new HashMap<String, String>();
        putToken.put("token", MyApplication.registrationId);
        putToken.put("deviceId", MyApplication.android_id);
        putToken.put("os", "android");
        putToken.put("lastLogin", GetDateTimeInstance.getRegDate());

        //new Firebase(MyApplication.useFirebase+"Users").child(MyApplication.android_id).child("token").setValue(MyApplication.registrationId);
        //new Firebase(MyApplication.useFirebase+"Users").child(MyApplication.android_id).child("deviceId").setValue(MyApplication.android_id);
        //new Firebase(MyApplication.useFirebase+"Users").child(MyApplication.android_id).child("os").setValue("android");
        //new Firebase(MyApplication.useFirebase+"Users").child(MyApplication.android_id).child("lastLogin").setValue(GetDateTimeInstance.getRegDate());
        new Firebase(MyApplication.useFirebase+"Users").child(MyApplication.android_id).setValue(putToken);
    }
}