package gllc.ravore.app.Messaging;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import gllc.ravore.app.Interfaces.StartCamera;
import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 3/31/2016.
 */
public class LoadProfilePhoto {

    AlertDialog.Builder alertadd;
    StartCamera startCamera;

    public LoadProfilePhoto(ImageView giverImage, ImageView receiverImage, boolean amIGiver, Bracelet bracelet, Context context, Context alertDialogContext, StartCamera startCamera){

        alertadd = new AlertDialog.Builder(alertDialogContext);
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

    public void loadLocalPath(ImageView imageView, Bracelet bracelet){
        Bitmap myBitmap = BitmapFactory.decodeFile(MyApplication.f.getPath());
        try {
            ExifInterface exif = new ExifInterface(MyApplication.f.getPath());
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
            imageView.setImageBitmap(myBitmap);
        }
        catch (Exception e) {

        }
        imageView.setImageBitmap(myBitmap);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera.StartCamera();
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

                if (MyApplication.currentUserIsGiver){userId = bracelet.getReceiverId();}
                else {userId = bracelet.getGiverId();}


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
