package gllc.ravore.app.Automation;

import java.io.File;

/**
 * Created by bhangoo on 4/1/2016.
 */
public class CreateFile {

    File file;

    public CreateFile (String path){
        file = new File(path);
    }

    public File getFile(){
        return file;
    }
}
