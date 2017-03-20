package com.example.kriti.aninterface.Utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by harshit on 14/2/17.
 */


public class DeviceWriter {

    private String path;
    private String fileName;
    private FileOutputStream fos;
    private OutputStreamWriter osw;
    private Context c;
    File f;

    public DeviceWriter(Context c){
        Log.w("TAGU", "DeviceWriter: abc");
        this.c = c;

        f = getFileDirectory(c.getApplicationContext(),"navmebot");
        path = f.getAbsolutePath();
        Log.w("patholo",path);
        fileName = new String("samples.txt");
        initialize();
    }


    public DeviceWriter(String s){

            f = getFileDirectory(c.getApplicationContext(),"navmebot");
            this.path = f.getAbsolutePath();
            this.fileName ="samples.txt";
            initialize();

    }

    public File getFileDirectory(Context context,String dirname){
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),dirname);
        return file;
    }


    public void write(String s) {
        try {
            this.osw.write(s);
            this.osw.flush();
        }
        catch (IOException e) {
        }
    }


    public void close() {
        try {
            this.osw.flush();
            this.osw.close();
            this.fos.close();
        }
        catch (IOException e) {
        }
    }


    public String toString() {
        return new String(this.path + this.fileName);
        }

    /*
    * Initialize : writes to specified files
    *
    */


    private void initialize() {
        File f = new File(new String(this.path +"/"+ this.fileName));
        try {
            this.fos = new FileOutputStream(f);
        }
        catch (FileNotFoundException e) {
        }
    //    this.osw = new OutputStreamWriter(this.fos);
    }


    public void setPath(String path) {
        this.path = path;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return this.path;
    }

    public String getFileName() {
        return this.fileName;
    }

}