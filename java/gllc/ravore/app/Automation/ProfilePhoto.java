package gllc.ravore.app.Automation;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfilePhoto {

    File file;

    public ProfilePhoto(String path){

        file = new File(path);
    }

    public File getFile() {
        return file;
    }

    public String getPath () {return file.getPath();}

    public void storeImage(Bitmap bitmap){
        try {
            FileOutputStream out = new FileOutputStream(file.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String saveToInternalStorage(Bitmap bitmapImage, Context context){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e){
                Log.i("MyActivity", "Error Saving to Internal Storage: " + e.getMessage());
            }
        }
        return directory.getAbsolutePath();
    }

    public void loadImageFromStorage(ImageView imageView, Context context)
    {

        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        try {
            File f=new File(directory, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //ImageView img=(ImageView)findViewById(R.id.imgPicker);
            imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}
