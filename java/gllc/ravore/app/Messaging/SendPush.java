package gllc.ravore.app.Messaging;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import gllc.ravore.app.Main.LoginActivity;
import gllc.ravore.app.MyApplication;

/**
 * Created by bhangoo on 3/14/2016.
 */
public class SendPush {

    AsyncHttpClient client;

    public SendPush(String message, String receiver, String title, String type, String braceletId, String os) {

        client = new AsyncHttpClient();

        Log.i("MyActivity", "In SendPush");

        RequestParams params = new RequestParams();

        params.put("to", receiver);
        params.put("os", os);
        params.put("title",title);
        params.put("message", message);
        params.put("type", type);
        params.put("braceletId", braceletId);
        params.put("devProd", MyApplication.devStatus);

        client.post("https://sheltered-wave-14675.herokuapp.com/sendPush", params,
                new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.i("MyActivity", "In Failure");
                        Log.i("MyActivity", "Failure response: " + responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i("MyActivity", "Success Sending Push Notifications");
                        Log.i("MyActivity", "Result: " + responseString);
                    }
                });

    }



}
