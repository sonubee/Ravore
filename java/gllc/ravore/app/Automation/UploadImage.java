package gllc.ravore.app.Automation;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.util.Log;

import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.firebase.client.Firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import gllc.ravore.app.Messaging.MessagingActivity;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Anon;

/**
 * Created by bhangoo on 1/26/2016.
 */
public class UploadImage extends AsyncTask<String, String, String> {

    String newURL, newURL2, urlVersion, urlVersion2;
    int requestCode;
    Context context;

    public UploadImage (int requestCode, Context context){
        this.requestCode = requestCode;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        if (MyApplication.android_id!=null){

            ContextWrapper cw = new ContextWrapper(context);
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

            try {

                File f=new File(directory, "profile.jpg");

                if (requestCode == MyApplication.SELECT_FILE || requestCode == MyApplication.REQUEST_EXTERNAL_STORAGE_READ){
                    //Map uploadResult = MyApplication.cloudinary.uploader().upload("sdcard/ravore/profile_pic.jpg", ObjectUtils.asMap("public_id", MyApplication.android_id,
                    //        "transformation", new Transformation().crop("limit").width(70).height(70).crop("fill")));

                    Map uploadResult = MyApplication.cloudinary.uploader().upload(f, ObjectUtils.asMap("public_id", MyApplication.android_id,
                            "transformation", new Transformation().crop("limit").width(70).height(70).crop("fill")));
                    newURL = uploadResult.get("url").toString();
                    urlVersion = uploadResult.get("version").toString();

                    Map uploadResult2 = MyApplication.cloudinary.uploader().upload(f, ObjectUtils.asMap("public_id", MyApplication.android_id+"full"));

                    newURL2 = uploadResult2.get("url").toString();
                    urlVersion2 = uploadResult2.get("version").toString();
                }

                else if (requestCode==MyApplication.REQUEST_CAMERA){
                    Map uploadResult = MyApplication.cloudinary.uploader().upload(f, ObjectUtils.asMap("public_id", MyApplication.android_id,
                            "transformation", new Transformation().crop("limit").width(70).height(70).crop("fill")));
                    newURL = uploadResult.get("url").toString();
                    urlVersion = uploadResult.get("version").toString();

                    Map uploadResult2 = MyApplication.cloudinary.uploader().upload(f, ObjectUtils.asMap("public_id", MyApplication.android_id+"full"));
                    newURL2 = uploadResult2.get("url").toString();
                    urlVersion2 = uploadResult2.get("version").toString();
                }
            }

            catch (IOException ie) {
                Log.i("--AllUploadImage", "Reached Exception");
                Log.i("--AllUploadImage", "Exception is: " + ie.getMessage());}}

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Anon updateAnon;
        Log.i("--AllUploadImage", "On Post Execute");
        Log.i("--AllUploadImage", "url (upload) is: " + newURL);
        Log.i("--AllUploadImage", "url2 (upload) is: " + newURL2);

        if (requestCode==MyApplication.REQUEST_CAMERA){
            Log.i("--AllUploadImage", "Inside Request Camera");
            updateAnon = new Anon(MyApplication.android_id, newURL, urlVersion, newURL2, urlVersion2);

            if (MyApplication.android_id!=null){
                Log.i("--AllUploadImage", "Inside Past Null");
                Firebase uploadNewURL = new Firebase(MyApplication.useFirebase+"Users/ProfilePics/" + MyApplication.android_id);
                uploadNewURL.setValue(updateAnon);
                new Firebase(MyApplication.useFirebase+"Users").child(MyApplication.android_id).child("ProfilePics").setValue(updateAnon);
            }
        }

        if (requestCode==MyApplication.SELECT_FILE){

            updateAnon = new Anon(MyApplication.android_id, newURL, urlVersion, newURL2, urlVersion2);

            if (MyApplication.android_id!=null){
                Firebase uploadNewURL = new Firebase(MyApplication.useFirebase+"Users/ProfilePics/" + MyApplication.android_id);
                uploadNewURL.setValue(updateAnon);
                new Firebase(MyApplication.useFirebase+"Users").child(MyApplication.android_id).child("ProfilePics").setValue(updateAnon);
            }
        }
    }
}
