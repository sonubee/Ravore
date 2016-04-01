package gllc.ravore.app.Messaging;

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

    public LoadProfilePhoto(ImageView giverImage, ImageView receiverImage, boolean amIGiver, Bracelet bracelet){

        if (amIGiver){
            loadLocalPath(giverImage, bracelet);
            loadOtherPerson(receiverImage, bracelet, "receiver");
        }
        else {
            loadLocalPath(receiverImage, bracelet);
            loadOtherPerson(giverImage, bracelet, "giver");
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

    public void loadOtherPerson(ImageView imageView, Bracelet bracelet, String giverReceiver){
        /*
        if (giverReceiver.equals("receiver")){
            String url = MyApplication.cloudinary.url().format("jpg")
                    .generate("v" + MyApplication.allAnon.get(i).getUrlVersion() + "/" + braceletForMessaging.getReceiverId());
            Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.anon).into(receiverImage);
        }
        */
    }

}
