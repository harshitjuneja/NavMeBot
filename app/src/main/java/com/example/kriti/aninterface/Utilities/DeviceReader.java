package com.example.kriti.aninterface.Utilities;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by harshit on 19/2/17.
 */

public class DeviceReader {

    private String path;
    private String file;

    public DeviceReader(String filename) throws IOException {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        this.path = dir.getAbsolutePath();
        Log.w("TAGU", this.path);
        this.file = filename;
    }

    public boolean is_ExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        Log.w("TAGU", "DeviceReader: " + state);
        if (state.equals(Environment.MEDIA_MOUNTED)) return true;
        else return false;
    }

    public String initialize() throws IOException {
        String line = "";
        String content = "";
        File file = new File(new String(this.path + File.separator + this.file));
        if (!file.exists()) {
            file.mkdirs();
            Log.w("TAGU", "new file created in document");
        } else {
            Log.w("TAGU", "file exists");
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                content += line;
            }
            Log.w("TAGU", content);
            return content;
        }

        Log.w("TAGU", file.getCanonicalPath());
        return null;
    }

    public void setFileName(String fileName) {
        this.file = fileName;
    }

    public String getPath() {
        return this.path;
    }

    public String getFileName() {
        return this.file;
    }
}
