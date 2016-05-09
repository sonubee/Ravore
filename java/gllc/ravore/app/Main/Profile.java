package gllc.ravore.app.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.braintreepayments.api.threedsecure.ThreeDSecureWebChromeClient;
import com.firebase.client.Firebase;

import gllc.ravore.app.Automation.AddBracelet;
import gllc.ravore.app.Automation.RotateBitmap;
import gllc.ravore.app.Interfaces.StartCamera;
import gllc.ravore.app.Messaging.LoadProfilePhoto;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Anon;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 5/1/2016.
 */

public class Profile extends Fragment implements StartCamera {

    TextView ravorName, gender, help;
    ImageView ravorImage;
    StartCamera startCamera;
    AlertDialog.Builder alertaddProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile, container, false);

        ravorName = (TextView)view.findViewById(R.id.ravorName);
        gender = (TextView)view.findViewById(R.id.gender);
        help = (TextView)view.findViewById(R.id.help);
        ravorImage = (ImageView)view.findViewById(R.id.ravorImage);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        startCamera = (StartCamera)this;

        alertaddProfile = new AlertDialog.Builder(getContext());

        SharedPreferences sharedPref = getActivity().getSharedPreferences("gllc.ravore.app.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        String ravorNameString = sharedPref.getString("RavorName", "Ravor Name (Click Here)");
        String genderString = sharedPref.getString("Gender", "Gender (Click Here)");

        ravorName.setText(ravorNameString);
        gender.setText(genderString.substring(0,1).toUpperCase() + genderString.substring(1).toLowerCase());

        new LoadProfilePhoto(ravorImage, getContext(), startCamera);

        ravorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                input.setHint("Enter Ravor Name Here");
                builder.setTitle("Ravor Name");
                builder.setMessage("Enter the Ravore Name Below!");
                builder.setView(input);
                builder.setPositiveButton("Save Name!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Firebase(MyApplication.useFirebase).child("UserInfo").child(MyApplication.android_id).child("ravorName").setValue(input.getText().toString());

                        Context context = getActivity();
                        SharedPreferences sharedPref = context.getSharedPreferences("gllc.ravore.app.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("RavorName", input.getText().toString());
                        editor.commit();

                        ravorName.setText(input.getText().toString());
                    }
                });

                builder.show();
            }
        });

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Male", "Female"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Your Gender");

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        new Firebase(MyApplication.useFirebase).child("UserInfo").child(MyApplication.android_id).child("gender").setValue(items[item].toString().toLowerCase());
                        gender.setText(items[item].toString());
                    }
                });
                builder.show();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Your Gender");
                builder.setMessage("Please enter a Ravor Name with 2 Words. This will not be used for login but will be displayed to others. Random characters and letters will be deleted. Examples: Happy Feet and Misty Eyes");
                builder.show();
            }
        });

        //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putString("Ravor Name", "NA");
        //editor.apply();
    }

    @Override
    public void StartCamera(String itemSelected) {

        if (itemSelected.equals("Take Photo")) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                int cameraPermission = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA);
                int writeExternalStorage = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalStorage == PackageManager.PERMISSION_GRANTED){
                    Log.i("--AllMessagingActivity", "Got Camera Permission");
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(MyApplication.file.getFile()));
                    getActivity().startActivityForResult(takePictureIntent, MyApplication.REQUEST_CAMERA);
                }

                else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MyApplication.REQUEST_CAMERA);
                }
                Log.i("MessagingActivity", "File Exists: " + MyApplication.file.getFile().exists());
            }

        } else if (itemSelected.equals("Choose from Library")) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");

            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {

                int readExternalStorage = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                int writeExternalStorage = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (readExternalStorage == PackageManager.PERMISSION_GRANTED && writeExternalStorage == PackageManager.PERMISSION_GRANTED){

                    Log.i("--AllMessagingActivity", "Permission Granted for Reading Gallery");


                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(MyApplication.file.getFile()));
                    getActivity().startActivityForResult(Intent.createChooser(intent, "Select File"),
                            MyApplication.SELECT_FILE);
                }

                else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MyApplication.SELECT_FILE);
                }
            }

        } else if (itemSelected.equals("View Photo")) {
            LayoutInflater factory = LayoutInflater.from(getContext());
            View view = factory.inflate(R.layout.full_photo, null);
            ImageView fullImageView = (ImageView) view.findViewById(R.id.fullPhotoImageview);

            //MyApplication.file.loadImageFromStorage(fullImageView, getBaseContext());
            Bitmap myBitmap = BitmapFactory.decodeFile(MyApplication.file.getPath());
            fullImageView.setImageBitmap(RotateBitmap.RotateBitmap(myBitmap));

            alertaddProfile.setView(view);
            alertaddProfile.setNeutralButton("OK!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dlg, int sumthin) {

                }
            });

            alertaddProfile.show();

        } else if (itemSelected.equals("Delete Photo")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete Photo");
            builder.setMessage("Are You Sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyApplication.file.getFile().delete();

                    //if (MyApplication.currentUserIsGiver) {giverImage.setImageResource(R.drawable.anon);}
                    //else {receiverImage.setImageResource(R.drawable.anon);}
                    ravorImage.setImageResource(R.drawable.anon);

                    Anon removeAnon = new Anon(MyApplication.android_id, "NA", "NA", "NA", "NA");
                    new Firebase(MyApplication.useFirebase + "Users/ProfilePics/" + MyApplication.android_id).setValue(removeAnon);
                    new Firebase(MyApplication.useFirebase + "UserInfo").child(MyApplication.android_id).child("ProfilePics").setValue(removeAnon);
                }
            });
            builder.show();
        }
    }
}
