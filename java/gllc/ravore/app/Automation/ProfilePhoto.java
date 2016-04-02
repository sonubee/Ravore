package gllc.ravore.app.Automation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
public class ProfilePhoto {

    File file;

    public ProfilePhoto(String path){
        file = new File(path);
    }

    public File getFile(){
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

}
