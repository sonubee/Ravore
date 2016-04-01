/*
 * Copyright 2015 Rudson Lima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gllc.ravore.app.Messaging;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import gllc.ravore.app.Automation.SendPush;
import gllc.ravore.app.GCM.MyGcmListenerService;
import gllc.ravore.app.Automation.UploadImage;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Anon;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.Objects.Message;
import gllc.ravore.app.R;

public class MessagingActivity extends AppCompatActivity {

    public static ArrayList<Message> messageArrayList = new ArrayList<>();
    public static MessagingAdapter adapter;
    public static ListView listView;
    public static ImageView giverImage, receiverImage;
    public static TextView giverName, receiverName, braceletNum;
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    EditText sendMessage;
    AlertDialog.Builder alertadd;

    String selectedId = MyApplication.selectedId;
    public static Bracelet selectedBraceletFromLogin = new Bracelet();
    String fileName;

    public static Context context;
    //public static Activity messagingActivity = this;

    public static String messageSender = "";
    public static String messageReceiver = "";

    File f;

    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);

        //IntentReceiver.displayNotifications=false;

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);}

        alertadd = new AlertDialog.Builder(this);

        MyGcmListenerService.displayNotifications=false;

        client = new AsyncHttpClient();

        giverImage = (ImageView)findViewById(R.id.giver_image);
        receiverImage = (ImageView)findViewById(R.id.receiver_image);

        sendMessage = (EditText)findViewById(R.id.message_to_send);

        braceletNum = (TextView)findViewById(R.id.braceletNumber);

        context=getApplicationContext();

        giverImage.setImageResource(R.drawable.anon);
        receiverImage.setImageResource(R.drawable.anon);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setupAdapter();

        f = new File("sdcard/ravore/profile_pic.jpg");

        for (int i=0; i<MyApplication.allBracelets.size(); i++){
            if (selectedId.equals(MyApplication.allBracelets.get(i).getBraceletId())){
                selectedBraceletFromLogin = MyApplication.allBracelets.get(i);}}

        braceletNum.setText("Kandi# " + selectedBraceletFromLogin.getBraceletId());

        if (MyApplication.currentUserIsGiver) {

            messageSender = selectedBraceletFromLogin.getGiverId();
            messageReceiver = selectedBraceletFromLogin.getReceiverId();

            if (f.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile("sdcard/ravore/profile_pic.jpg");

                try {
                    ExifInterface exif = new ExifInterface("sdcard/ravore/profile_pic.jpg");
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    Log.d("EXIF", "Exif: " + orientation);
                    Matrix matrix = new Matrix();
                    if (orientation == 6) {
                        matrix.postRotate(90);
                    }
                    else if (orientation == 3) {
                        matrix.postRotate(180);
                    }
                    else if (orientation == 8) {
                        matrix.postRotate(270);
                    }
                    myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
                    giverImage.setImageBitmap(myBitmap);
                }
                catch (Exception e) {

                }
                giverImage.setImageBitmap(myBitmap);


            }

            else {giverImage.setImageResource(R.drawable.anon);}

            for (int i = 0; i < MyApplication.allAnon.size(); i++) {

                Log.i("MyActivity", "Anon UserID: " + MyApplication.allAnon.get(i).getUserId());
                Log.i("MyActivity", "Selected Bracelet Receiver ID: " + selectedBraceletFromLogin.getReceiverId());


                if (MyApplication.allAnon.get(i).getUserId().equals(selectedBraceletFromLogin.getReceiverId())) {

                    String url = MyApplication.cloudinary.url().format("jpg")
                            .generate("v" + MyApplication.allAnon.get(i).getUrlVersion() + "/" + selectedBraceletFromLogin.getReceiverId());
                    Log.i("MyActivity", "URL is: " + url);

                    Log.i("MyActivity", "Found Receiver ID and loading from Cloud");
                    Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.anon).into(receiverImage);}}
        }

        else {

            messageSender = selectedBraceletFromLogin.getReceiverId();
            messageReceiver = selectedBraceletFromLogin.getGiverId();

            if (f.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile("sdcard/ravore/profile_pic.jpg");

                try {
                    ExifInterface exif = new ExifInterface("sdcard/ravore/profile_pic.jpg");
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    Log.d("EXIF", "Exif: " + orientation);
                    Matrix matrix = new Matrix();
                    if (orientation == 6) {
                        matrix.postRotate(90);
                    }
                    else if (orientation == 3) {
                        matrix.postRotate(180);
                    }
                    else if (orientation == 8) {
                        matrix.postRotate(270);
                    }
                    myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
                    receiverImage.setImageBitmap(myBitmap);
                }
                catch (Exception e) {
                    Log.i("MyActivity", "In Exception of Loading Receiver Image through Matrix");
                }

                receiverImage.setImageBitmap(myBitmap);

            }
            else {receiverImage.setImageResource(R.drawable.anon);}


            for (int i = 0; i < MyApplication.allAnon.size(); i++) {

                if (MyApplication.allAnon.get(i).getUserId().equals(selectedBraceletFromLogin.getGiverId())) {


                    String url = MyApplication.cloudinary.url().format("jpg")
                            .generate("v" + MyApplication.allAnon.get(i).getUrlVersion() + "/" + selectedBraceletFromLogin.getGiverId());
                    Log.i("MyActivity", "URL is: " + url);
                    Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.anon).into(giverImage);}}

        }

        giverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.currentUserIsGiver) {
                    startCamera();
                } else {

                    LayoutInflater factory = LayoutInflater.from(getApplicationContext());
                    View view = factory.inflate(R.layout.full_photo, null);
                    ImageView fullImageView = (ImageView) view.findViewById(R.id.fullPhotoImageview);
                    alertadd.setView(view);


                    for (int i = 0; i < MyApplication.allAnon.size(); i++) {
                        if (MyApplication.allAnon.get(i).getUserId().equals(selectedBraceletFromLogin.getGiverId())) {
                            Picasso.with(getApplicationContext()).load(MyApplication.allAnon.get(i).getFullPhotoUrl()).placeholder(R.drawable.placeholder).into(fullImageView);}}


                    alertadd.setNeutralButton("OK!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {

                        }
                    });

                    alertadd.show();


                }
            }
        });

        receiverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.currentUserIsGiver) {
                    startCamera();

                } else {
                    LayoutInflater factory = LayoutInflater.from(getApplicationContext());
                    View view = factory.inflate(R.layout.full_photo, null);
                    alertadd.setView(view);
                    for (int i = 0; i < MyApplication.allAnon.size(); i++) {
                        if (MyApplication.allAnon.get(i).getUserId().equals(selectedBraceletFromLogin.getReceiverId())) {

                            ImageView fullImageView = (ImageView) view.findViewById(R.id.fullPhotoImageview);
                            Picasso.with(getApplicationContext()).load(MyApplication.allAnon.get(i).getFullPhotoUrl()).placeholder(R.drawable.placeholder).into(fullImageView);

                        }
                    }

                    alertadd.setNeutralButton("OK!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {

                        }
                    });

                    alertadd.show();
                }
            }
        });

        sendMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    Log.i("MyActivity", "in send");

                    if (!sendMessage.getText().toString().equals("")) {
                        Log.i("MyActivity", "selectedId is: " + MyApplication.selectedId);

                        Firebase uploadMessage = new Firebase(MyApplication.useFirebase+"Messages/" + MyApplication.selectedId);

                        SimpleDateFormat date = new SimpleDateFormat("MM" + "/" + "dd" + "/" + "yyyy");
                        date.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String dateString = date.format(new Date());

                        SimpleDateFormat timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        timestamp.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String currentDateandTime = timestamp.format(new Date());
                        long miliSeconds = System.currentTimeMillis();
                        currentDateandTime = currentDateandTime + miliSeconds;

                        Message message = new Message(sendMessage.getText().toString(), MyApplication.android_id, dateString, MyApplication.selectedId, currentDateandTime);

                        Log.i("MyActivity", "Before Set Value");
                        uploadMessage.push().setValue(message);
                        Log.i("MyActivity", "After Set Value");


                        JSONObject theMessage = new JSONObject();
                        try {
                            theMessage.put("alert", sendMessage.getText().toString());

                            if (MyApplication.selectedBracelet.getGiverId().equals(MyApplication.android_id)) {
                                theMessage.put("target", MyApplication.selectedBracelet.getReceiverId());
                            } else {
                                theMessage.put("target", MyApplication.selectedBracelet.getGiverId());
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        JSONArray messages = new JSONArray();
                        messages.put(theMessage);

                        JSONObject notifyReceiver = new JSONObject();
                        try {
                            notifyReceiver.put("request_id", MyApplication.android_id);
                            notifyReceiver.put("campaign_key", "tests");
                            notifyReceiver.put("target_type", "customer_id");
                            notifyReceiver.put("messages", messages);


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        Log.i("MyActivity", "JSON String: " + notifyReceiver);

                        AsyncHttpClient sendPush = new AsyncHttpClient();
                        //sendPush.addHeader(
                        //        "Authorization",
                        //        "Basic " + Base64.encodeToString(
                        //                ("0e2e9abc48788a8773f3f7e-85466b6e-8433-11e5-1467-00deb82fd81f" + ":" + "b6b19ea56ddba195c2ea26d-85466e3e-8433-11e5-1467-00deb82fd81f").getBytes(), Base64.NO_WRAP)
                        //);
                        //sendPush.setBasicAuth("0e2e9abc48788a8773f3f7e-85466b6e-8433-11e5-1467-00deb82fd81f","b6b19ea56ddba195c2ea26d-85466e3e-8433-11e5-1467-00deb82fd81f");
                        sendPush.setBasicAuth("0e2e9abc48788a8773f3f7e-85466b6e-8433-11e5-1467-00deb82fd81f", "b6b19ea56ddba195c2ea26d-85466e3e-8433-11e5-1467-00deb82fd81f");

                        StringEntity entity = null;

                        try {
                            entity = new StringEntity(notifyReceiver.toString());
                        } catch (Exception e) {
                            Log.i("MyActivity", "Exception from StringEntity");
                        }


                        sendPush.post(getApplicationContext(), "https://messaging.localytics.com/v2/push/ddd119ad20d7eecd8dd2545-dd4aa8ca-c6e8-11e5-63af-002dea3c3994", entity, "application/json",
                                //new AsyncHttpResponseHandler() {
                                new TextHttpResponseHandler() {

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        Log.i("MyActivity", "Failure sending message");
                                        Log.i("MyActivity", "Response: " + responseString);
                                        Log.i("MyActivity", "Headers: " + headers);
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                        Log.i("MyActivity", "Success sending message");
                                        Log.i("MyActivity", "Response: " + responseString);
                                        Log.i("MyActivity", "Headers: " + headers);
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter Something to Send", Toast.LENGTH_SHORT).show();
                    }

                    sendMessage.setText("");
                    //sendMessage.requestFocus();
                    //InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
                    //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
                return false;
            }
        });

        createJSON();

    }

    public void createJSON (){
        JSONObject message = new JSONObject();
        try {
            message.put("target","bbe483679580dc16");
            message.put("alert", "You Have a Message!");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject fullMessage = new JSONObject();
        try {
            fullMessage.put("request_id", "bbe483679580dc16");
            fullMessage.put("campaign_key", "null");
            fullMessage.put("target_type", "customer_id");
            fullMessage.put("messages", message);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.i("MyActivity", "JSON: " + fullMessage);
    }

    public void send (View v) {

        Log.i("MyActivity", "in send");

        if (!sendMessage.getText().toString().equals("")){
            Log.i("MyActivity", "selectedId is: " + MyApplication.selectedId);

            Firebase uploadMessage = new Firebase(MyApplication.useFirebase+"Messages/"+ MyApplication.selectedId);

            Log.i("MyActivity", "Message Count (before adding message) is: " + MessagingAdapter.messageArrayList.size());

            SimpleDateFormat date = new SimpleDateFormat("MM" + "/" + "dd" + "/" + "yyyy");
            date.setTimeZone(TimeZone.getTimeZone("GMT"));
            String dateString = date.format(new Date());

            SimpleDateFormat timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss");
            timestamp.setTimeZone(TimeZone.getTimeZone("GMT"));
            String currentDateandTime = timestamp.format(new Date());
            long miliSeconds = System.currentTimeMillis();
            currentDateandTime = currentDateandTime + miliSeconds;

            Message message = new Message(sendMessage.getText().toString(), MyApplication.android_id, dateString, MyApplication.selectedId, currentDateandTime);

            Log.i("MyActivity", "Before Set Value");
            uploadMessage.push().setValue(message);
            Log.i("MyActivity", "After Set Value");

            sendPush(sendMessage.getText().toString());
        }

        else {
            Toast.makeText(getApplicationContext(), "Please Enter Something to Send", Toast.LENGTH_SHORT).show();}

        sendMessage.setText("");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startCamera() {
        final CharSequence[] items = { "View Photo","Take Photo", "Choose from Library", "Delete Photo", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MessagingActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                        }
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                            startActivityForResult(Intent.createChooser(intent, "Select File"),
                                    SELECT_FILE);
                        }
                    }
                } else if (items[item].equals("View Photo")) {
                    LayoutInflater factory = LayoutInflater.from(getApplicationContext());
                    View view = factory.inflate(R.layout.full_photo, null);
                    ImageView fullImageView = (ImageView) view.findViewById(R.id.fullPhotoImageview);

                    if (f.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile("sdcard/ravore/profile_pic.jpg");

                        try {
                            ExifInterface exif = new ExifInterface("sdcard/ravore/profile_pic.jpg");
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                            Log.d("EXIF", "Exif: " + orientation);
                            Matrix matrix = new Matrix();
                            if (orientation == 6) {
                                matrix.postRotate(90);
                            } else if (orientation == 3) {
                                matrix.postRotate(180);
                            } else if (orientation == 8) {
                                matrix.postRotate(270);
                            }
                            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
                            giverImage.setImageBitmap(myBitmap);
                        } catch (Exception e) {

                        }
                        fullImageView.setImageBitmap(myBitmap);
                    }


                    alertadd.setView(view);
                    alertadd.setNeutralButton("OK!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {

                        }
                    });

                    alertadd.show();

                } else if (items[item].equals("Delete Photo")) {
                    File file = new File("sdcard/ravore/profile_pic.jpg");

                    if (file.exists()) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
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
                        } else {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No Profile Photo Exists", Toast.LENGTH_SHORT).show();
                    }


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private File createImageFile() throws IOException {
        File folder = new File("sdcard/ravore");
        File destination = new File (folder, "profile_pic.jpg");
        fileName =  destination.getAbsolutePath();
        return destination;}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MyActivity", "In Activity Results");

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                ImageView view;
                if (MyApplication.currentUserIsGiver){view = (ImageView)this.findViewById(R.id.giver_image);}
                else {view = (ImageView)this.findViewById(R.id.receiver_image);}

                view.setVisibility(View.VISIBLE);

                int targetW = view.getWidth();
                int targetH = view.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(fileName, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(fileName, bmOptions);
                bitmap = RotateBitmap(bitmap, 270);
                //storeImage(bitmap);

                view.setImageBitmap(bitmap);
                new UploadImage(requestCode).execute();
            }

            else if (requestCode == SELECT_FILE) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap myBitmap = BitmapFactory.decodeFile(filePath);

                try {
                    ExifInterface exif = new ExifInterface(filePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    Log.d("EXIF", "Exif: " + orientation);
                    Matrix matrix = new Matrix();
                    if (orientation == 6) {
                        matrix.postRotate(90);
                    }
                    else if (orientation == 3) {
                        matrix.postRotate(180);
                    }
                    else if (orientation == 8) {
                        matrix.postRotate(270);
                    }
                    myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
                    //receiverImage.setImageBitmap(myBitmap);
                }
                catch (Exception e) {

                }

                if (MyApplication.currentUserIsGiver){MessagingActivity.giverImage.setImageBitmap(myBitmap);}
                else {MessagingActivity.receiverImage.setImageBitmap(myBitmap);}

                storeImage(myBitmap);

                new UploadImage(requestCode).execute();}}
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);}

    public static void storeImage(Bitmap image) {
        File myDir = new File("sdcard/ravore");
        myDir.mkdirs();
        File file = new File(myDir, "profile_pic.jpg");
        Log.i("MyActivity", "File: " + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MyActivity", "Reached Destroy");
        messageArrayList.clear();
        adapter.clear();
        MessagingAdapter.pullMessages.removeEventListener(MessagingAdapter.listener1);
    }

    public void sendPush(String message){

        for (int i= 0 ; i < MyApplication.allTokens.size(); i++){
            if (messageReceiver.equals(MyApplication.allTokens.get(i).getUserId())){

                new SendPush(message, MyApplication.allTokens.get(i).getToken(), selectedBraceletFromLogin.getBraceletId(), "message", selectedBraceletFromLogin.getBraceletId(), MyApplication.allTokens.get(i).getOs());
            }
        }
    }
}
