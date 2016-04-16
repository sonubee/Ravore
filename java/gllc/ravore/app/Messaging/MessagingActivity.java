package gllc.ravore.app.Messaging;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.localytics.android.Localytics;
import com.loopj.android.http.AsyncHttpClient;
import com.splunk.mint.Mint;

import java.util.ArrayList;

import gllc.ravore.app.Automation.AddBracelet;
import gllc.ravore.app.Automation.GetBracelet;
import gllc.ravore.app.Automation.GetDateTimeInstance;
import gllc.ravore.app.Automation.RotateBitmap;
import gllc.ravore.app.Automation.SendPush;
import gllc.ravore.app.Automation.UploadImage;
import gllc.ravore.app.Interfaces.StartCamera;
import gllc.ravore.app.Main.Feedback;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Anon;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.Objects.Message;
import gllc.ravore.app.Objects.Token;
import gllc.ravore.app.OrderRavore.PurchaseScreenFragment;
import gllc.ravore.app.R;

public class MessagingActivity extends AppCompatActivity {

    MessagingFragment messagingFragment;
    public static Context context;
    FragmentTransaction transaction;
    public static String whichFragment = "ShoppingCartFragment";
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_kandi_container);


        messagingFragment = new MessagingFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_about_kandi, messagingFragment).commit();

        if (MyApplication.devStatus.equals("production")){
            Mint.logEvent("MessagingActivity");}


        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);}

        context = getApplicationContext();

        transaction = getSupportFragmentManager().beginTransaction();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("--AllMessagingActivity", "Request Code: " + requestCode + " : " + MyApplication.REQUEST_CAMERA + " : " + MyApplication.SELECT_FILE);
            Log.i("--AllMessagingActivity", "Result Code: " + resultCode + " Result Ok : " + RESULT_OK);

        if (resultCode == RESULT_OK) {
            ImageView imageView;

            if (MyApplication.currentUserIsGiver){imageView = (ImageView)findViewById(R.id.giver_image);}
            //if (MyApplication.currentUserIsGiver){imageView = MessagingFragment.giverImage;}
            else {imageView = (ImageView)findViewById(R.id.receiver_image);}
            //else {imageView = MessagingFragment.receiverImage;}

            if (requestCode == MyApplication.REQUEST_CAMERA) {
                Log.i("--AllMessagingActivity", "Before LoadProfile");

                //Bundle extras = data.getExtras();
                //Bitmap imageBitmap = (Bitmap) extras.get("data");
                //imageView.setImageBitmap(imageBitmap);

                //MyApplication.file.saveToInternalStorage(imageBitmap, this);
                //MyApplication.file.storeImage(imageBitmap);

                new LoadProfilePhoto(imageView, this);
            }

            else if (requestCode == MyApplication.SELECT_FILE) {new LoadProfilePhoto(data.getData(), imageView, this);}

            Log.i("--AllMessagingActivity", "Before Uploading Image");
            new UploadImage(requestCode, this).execute();
        }
        else {Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("AllMessagingActivity", "Reached Destroy from Messaging");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_messaging, menu);
        this.menu = menu;
        return true;

        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("AllMessagingActivity", "Reached onPause from Messaging");

        MessagingFragment.messageArrayList.clear();
        MessagingFragment.adapter.clear();
        //Killing Firebase listener otherwise the text messages double since a new listener gets created each time the activity opens
        MessagingAdapter.pullMessages.removeEventListener(MessagingAdapter.firebaseChildListenerMessages);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AboutKandi aboutKandi;

        switch (item.getItemId()) {

            case android.R.id.home:

                if (whichFragment.equals("MessagingFragment")){
                    finish();
                }

                else {
                    whichFragment = "MessagingFragment";
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_about_kandi, messagingFragment);
                    transaction.commit();

                    MenuItem menuAboutKandi = menu.findItem(R.id.tell_story);
                    menuAboutKandi.setVisible(true);
                }
                return true;

            case R.id.tell_story:
                aboutKandi = new AboutKandi();
                whichFragment = "AboutKandi";
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_about_kandi, aboutKandi);
                transaction.commit();

                item.setVisible(false);

                break;
                //return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
