package gllc.ravore.app.Messaging;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import gllc.ravore.app.MyApplication;
import gllc.ravore.app.Objects.Bracelet;
import gllc.ravore.app.R;

/**
 * Created by bhangoo on 3/31/2016.
 */
public class LoadProfilePhoto {

    public LoadProfilePhoto(ImageView giverImage, ImageView receiverImage, boolean amIGiver, Bracelet bracelet, Context context){

        if (amIGiver){
            loadLocalPath(giverImage, bracelet);
            loadOtherPerson(receiverImage, bracelet, "receiver", context);
        }
        else {
            loadLocalPath(receiverImage, bracelet);
            loadOtherPerson(giverImage, bracelet, "giver", context);
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
    }

    public void loadOtherPerson(ImageView imageView, Bracelet bracelet, String giverReceiver, Context context){


        if (giverReceiver.equals("receiver")){

            for (int i = 0; i < MyApplication.allAnon.size(); i++) {
                if (MyApplication.allAnon.get(i).getUserId().equals(bracelet.getReceiverId())) {
                    String url = MyApplication.cloudinary.url().format("jpg")
                            .generate("v" + MyApplication.allAnon.get(i).getUrlVersion() + "/" + bracelet.getReceiverId());
                    Picasso.with(context).load(url).placeholder(R.drawable.anon).into(imageView);
                }
            }
        }

        if (giverReceiver.equals("giver")){
            for (int i = 0; i < MyApplication.allAnon.size(); i++) {
                if (MyApplication.allAnon.get(i).getUserId().equals(bracelet.getGiverId())) {
                    String url = MyApplication.cloudinary.url().format("jpg")
                            .generate("v" + MyApplication.allAnon.get(i).getUrlVersion() + "/" + bracelet.getGiverId());
                    Picasso.with(context).load(url).placeholder(R.drawable.anon).into(imageView);
                }
            }
        }
    }

}
