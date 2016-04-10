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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.loopj.android.http.AsyncHttpClient;
import java.util.ArrayList;
import gllc.ravore.app.Automation.GetBracelet;
import gllc.ravore.app.Automation.GetDateTimeInstance;
import gllc.ravore.app.Automation.RotateBitmap;
import gllc.ravore.app.Automation.SendPush;
import gllc.ravore.app.Automation.UploadImage;
import gllc.ravore.app.Interfaces.StartCamera;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Anon;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.Objects.Message;
import gllc.ravore.app.Objects.Token;
import gllc.ravore.app.R;

public class MessagingActivity extends AppCompatActivity implements StartCamera {

    public static ArrayList<Message> messageArrayList = new ArrayList<>();
    public static MessagingAdapter adapter;
    public static ListView listView;
    public static Bracelet braceletForMessaging;
    public static Context context;
    AlertDialog.Builder alertadd;
    AsyncHttpClient client;
    StartCamera startCamera;

    String selectedId = MyApplication.selectedId;
    public static String messageSender = "", messageReceiver = "", messageReceiverToken = "", messageReceiverOs = "";

    public static ImageView giverImage, receiverImage;
    public static TextView giverName, receiverName, braceletNum;
    EditText sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);

        setup();
        amIgiver();
        setupSenderReceiver();
        setupAdapter();
        setupImages();
        setupKeyboardSendButton();
    }

    public void setup() {

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);}

        alertadd = new AlertDialog.Builder(this);

        startCamera = this;

        braceletForMessaging = GetBracelet.getBracelet(selectedId);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sendMessage = (EditText)findViewById(R.id.message_to_send);

        braceletNum = (TextView)findViewById(R.id.braceletNumber);
        braceletNum.setText("Kandi# " + braceletForMessaging.getBraceletId());

        context=getApplicationContext();

        client = new AsyncHttpClient();
    }

    public void setupKeyboardSendButton(){
        sendMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    send(v);
                    return true;
                }
                return false;
            }
        });
    }

    public void send (View v) {
        if (!sendMessage.getText().toString().equals("")){

            Message message = new Message(sendMessage.getText().toString(), MyApplication.android_id, GetDateTimeInstance.getRegDate(), MyApplication.selectedId, GetDateTimeInstance.getTimeStamp());
            new Firebase(MyApplication.useFirebase+"Messages/"+ MyApplication.selectedId).push().setValue(message);
            new SendPush(sendMessage.getText().toString(), messageReceiverToken, braceletForMessaging.getBraceletId(), "message", braceletForMessaging.getBraceletId(), messageReceiverOs);
        }
        else {Toast.makeText(getApplicationContext(), "Please Enter Something to Send", Toast.LENGTH_SHORT).show();}

        sendMessage.setText("");
    }

    public void setupSenderReceiver() {
        if (MyApplication.currentUserIsGiver) {
            messageSender = braceletForMessaging.getGiverId();
            messageReceiver = braceletForMessaging.getReceiverId();}
        else {
            messageSender = braceletForMessaging.getReceiverId();
            messageReceiver = braceletForMessaging.getGiverId();}

        for (int i= 0 ; i < MyApplication.allTokens.size(); i++) {
            if (messageReceiver.equals(MyApplication.allTokens.get(i).getUserId())) {
                messageReceiverToken = MyApplication.allTokens.get(i).getToken();
                messageReceiverOs = MyApplication.allTokens.get(i).getOs();
            }
        }
    }

    public void setupImages(){
        giverImage = (ImageView)findViewById(R.id.giver_image);
        receiverImage = (ImageView)findViewById(R.id.receiver_image);
        giverImage.setImageResource(R.drawable.anon);
        receiverImage.setImageResource(R.drawable.anon);

        new LoadProfilePhoto(giverImage, receiverImage, MyApplication.currentUserIsGiver, braceletForMessaging, context, MessagingActivity.this, startCamera);
    }

    public void setupAdapter (){

        adapter = new MessagingAdapter(getApplicationContext(), R.id.listViewMessaging, messageArrayList);
        listView = (ListView) findViewById(R.id.listViewMessaging);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            }
        });

        giverName = (TextView) findViewById(R.id.giver_name);
        giverName.setTextColor(Color.GREEN);
        if (MyApplication.currentUserIsGiver){giverName.setText("You");}

        receiverName = (TextView) findViewById(R.id.receiver_name);
        receiverName.setTextColor(Color.CYAN);
        if (!MyApplication.currentUserIsGiver){receiverName.setText("You");}

        giverName.setTextColor(Color.GREEN);
        receiverName.setTextColor(Color.CYAN);
    }

    public void amIgiver(){
        if (braceletForMessaging.getGiverId().equals(MyApplication.android_id)){MyApplication.currentUserIsGiver = true;}
        else {MyApplication.currentUserIsGiver = false;}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MyApplication.REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Log.i("--AllMessagingActivity", "Permission Version");

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    //        Uri.fromFile(MyApplication.file.getFile()));
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, MyApplication.REQUEST_CAMERA);
                    }

                    else {
                        Toast.makeText(this, "Error Opening Camera", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MyApplication.SELECT_FILE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(MyApplication.file.getFile()));
                    startActivityForResult(Intent.createChooser(intent, "Select File"),
                            MyApplication.SELECT_FILE);
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void StartCamera(String itemSelected) {

        if (itemSelected.equals("Take Photo")) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                int cameraPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA);
                int writeExternalStorage = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalStorage == PackageManager.PERMISSION_GRANTED){
                    Log.i("--AllMessagingActivity", "Got Camera Permission");
                    //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    //        Uri.fromFile(MyApplication.file.getFile()));
                    startActivityForResult(takePictureIntent, MyApplication.REQUEST_CAMERA);
                }

                else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MyApplication.REQUEST_CAMERA);
                }
                Log.i("MessagingActivity", "File Exists: " + MyApplication.file.getFile().exists());
            }

        } else if (itemSelected.equals("Choose from Library")) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");

            if (intent.resolveActivity(getPackageManager()) != null) {

                int readExternalStorage = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                int writeExternalStorage = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (readExternalStorage == PackageManager.PERMISSION_GRANTED && writeExternalStorage == PackageManager.PERMISSION_GRANTED){

                    Log.i("--AllMessagingActivity", "Permission Granted for Reading Gallery");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(MyApplication.file.getFile()));
                    startActivityForResult(Intent.createChooser(intent, "Select File"),
                            MyApplication.SELECT_FILE);
                }

                else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MyApplication.SELECT_FILE);
                }
            }

        } else if (itemSelected.equals("View Photo")) {
            LayoutInflater factory = LayoutInflater.from(getApplicationContext());
            View view = factory.inflate(R.layout.full_photo, null);
            ImageView fullImageView = (ImageView) view.findViewById(R.id.fullPhotoImageview);

            MyApplication.file.loadImageFromStorage(fullImageView, getBaseContext());
            //Bitmap myBitmap = BitmapFactory.decodeFile(MyApplication.file.getPath());
            //fullImageView.setImageBitmap(RotateBitmap.RotateBitmap(myBitmap));

            alertadd.setView(view);
            alertadd.setNeutralButton("OK!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dlg, int sumthin) {

                }
            });

            alertadd.show();

        } else if (itemSelected.equals("Delete Photo")) {

            MyApplication.file.getFile().delete();

            if (MyApplication.currentUserIsGiver) {
                MessagingActivity.giverImage.setImageResource(R.drawable.anon);
                Firebase removeProfilePhoto = new Firebase(MyApplication.useFirebase+"Users/ProfilePics/" + MyApplication.android_id);

                Anon removeAnon = new Anon(MyApplication.android_id, "NA", "NA", "NA", "NA");
                removeProfilePhoto.setValue(removeAnon);
            } else {
                MessagingActivity.receiverImage.setImageResource(R.drawable.anon);
                Firebase removeProfilePhoto = new Firebase(MyApplication.useFirebase+"Users/ProfilePics/" + MyApplication.android_id);

                Anon removeAnon = new Anon(MyApplication.android_id, "NA", "NA", "NA", "NA");
                removeProfilePhoto.setValue(removeAnon);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("--AllMessagingActivity", "Request Code: " + requestCode);
            Log.i("--AllMessagingActivity", "Result Code: " + resultCode + " Result Ok : " + RESULT_OK);

        if (resultCode == RESULT_OK) {
            ImageView imageView;

            if (MyApplication.currentUserIsGiver){imageView = (ImageView)this.findViewById(R.id.giver_image);}
            else {imageView = (ImageView)this.findViewById(R.id.receiver_image);}

            if (requestCode == MyApplication.REQUEST_CAMERA) {
                Log.i("--AllMessagingActivity", "Before LoadProfile");

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);

                MyApplication.file.saveToInternalStorage(imageBitmap, this);
                MyApplication.file.storeImage(imageBitmap);


                //new LoadProfilePhoto(imageView, this);
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
    protected void onPause() {
        super.onPause();
        Log.i("AllMessagingActivity", "Reached onPause from Messaging");

        messageArrayList.clear();
        adapter.clear();
        //Killing Firebase listener otherwise the text messages double since a new listener gets created each time the activity opens
        MessagingAdapter.pullMessages.removeEventListener(MessagingAdapter.firebaseChildListenerMessages);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
