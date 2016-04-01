package gllc.ravore.app.Main;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.URL;

import gllc.ravore.app.MyApplication;
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
        // The URL to the function in your backend API that stores registration IDs
        URL sendRegIdRequest = new URL("https://{YOUR_API_HOSTNAME}/register/device?registration_id=" + registrationId);

        // Send the registration ID by executing the GET request
        sendRegIdRequest.openConnection();
    }
}