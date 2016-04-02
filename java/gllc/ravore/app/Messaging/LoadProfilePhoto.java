package gllc.ravore.app.Messaging;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import gllc.ravore.app.Automation.RotateBitmap;
import gllc.ravore.app.Interfaces.StartCamera;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 3/31/2016.
 */
public class LoadProfilePhoto {

    AlertDialog.Builder alertadd;
    AlertDialog.Builder alertadd2;
    StartCamera startCamera;

    public LoadProfilePhoto(ImageView giverImage, ImageView receiverImage, boolean amIGiver, Bracelet bracelet, Context context, Context alertDialogContext, StartCamera startCamera){

        alertadd = new AlertDialog.Builder(alertDialogContext);
        alertadd2 = new AlertDialog.Builder(alertDialogContext);
        this.startCamera = startCamera;

        if (amIGiver){
            loadLocalPath(giverImage, bracelet);
            loadOtherPersonAndSetListener(receiverImage, bracelet, "receiver", context);
        }
        else {
            loadLocalPath(receiverImage, bracelet);
            loadOtherPersonAndSetListener(giverImage, bracelet, "giver", context);
        }
    }

    public LoadProfilePhoto(ImageView imageView, Activity activity){
        if (MyApplication.currentUserIsGiver){imageView = (ImageView)activity.findViewById(R.id.giver_image);}
        else {imageView = (ImageView)activity.findViewById(R.id.receiver_image);}

        imageView.setVisibility(View.VISIBLE);

        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(MyApplication.file.getPath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(MyApplication.file.getPath(), bmOptions);
        bitmap = RotateBitmap.RotateBitmap(bitmap, 270);

        imageView.setImageBitmap(bitmap);
    }

    public LoadProfilePhoto(Uri uri, ImageView imageView, Activity activity){
        Uri selectedImage = uri;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = activity.getContentResolver().query(
                selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String galleryImageFilePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap myBitmap = BitmapFactory.decodeFile(galleryImageFilePath);

        imageView.setImageBitmap(myBitmap);
        MyApplication.file.storeImage(myBitmap);
    }

    public void loadLocalPath(ImageView imageView, Bracelet bracelet){

        if (MyApplication.file.getFile().exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(MyApplication.file.getPath());
            imageView.setImageBitmap(RotateBitmap.RotateBitmap(myBitmap));
        }

        else {imageView.setImageResource(R.drawable.anon);}

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = { "View Photo","Take Photo", "Choose from Library", "Delete Photo", "Cancel" };
                alertadd2.setTitle(" Photo Options!");
                alertadd2.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        startCamera.StartCamera(items[item].toString());
                    }
                });
                alertadd2.show();
            }
        });
    }

    public void loadOtherPersonAndSetListener(ImageView imageView, final Bracelet bracelet, String giverReceiver, final Context context){
        String userId;
        if (giverReceiver.equals("receiver")){userId = bracelet.getReceiverId();}
        else {userId = bracelet.getGiverId();}

        for (int i = 0; i < MyApplication.allAnon.size(); i++) {
            if (MyApplication.allAnon.get(i).getUserId().equals(userId)) {
                String url = MyApplication.cloudinary.url().format("jpg")
                        .generate("v" + MyApplication.allAnon.get(i).getUrlVersion() + "/" + userId);
                Picasso.with(context).load(url).placeholder(R.drawable.anon).into(imageView);
            }
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(context);
                View view = factory.inflate(R.layout.full_photo, null);
                ImageView fullImageView = (ImageView) view.findViewById(R.id.fullPhotoImageview);
                alertadd.setView(view);

                String userId;

                if (MyApplication.currentUserIsGiver) {
                    userId = bracelet.getReceiverId();
                } else {
                    userId = bracelet.getGiverId();
                }


                for (int i = 0; i < MyApplication.allAnon.size(); i++) {
                    if (MyApplication.allAnon.get(i).getUserId().equals(userId)) {
                        Picasso.with(context).load(MyApplication.allAnon.get(i).getFullPhotoUrl()).placeholder(R.drawable.placeholder).into(fullImageView);
                    }
                }

                alertadd.setNeutralButton("OK!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {

                    }
                });

                alertadd.show();
            }
        });
    }


}
