package gllc.ravore.app.Automation;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import gllc.ravore.app.MyApplication;

/**
 * Created by bhangoo on 4/1/2016.
 */
public class RotateBitmap {

    public static Bitmap RotateBitmap(Bitmap bitmap) {
        try {
            ExifInterface exif = new ExifInterface(MyApplication.file.getPath());
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
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
            //imageView.setImageBitmap(myBitmap);
        }
        catch (Exception e) {
            Log.i("MyActivity", "Exception trying to load bitmap: " + e.getMessage());
        }

        return bitmap;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);}


}
