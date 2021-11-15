package com.programacion.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SaveImage {

    private Context TheThis;
    private String NameOfFolder = "/Mascotas";
    private String NameOfFile = "imagen";
    String file_path;
    String CurrentDateAndTime;
    String url;
    public void SaveImage(Context context, Bitmap ImageToSave) {

        TheThis = context;
        file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + NameOfFolder;
        CurrentDateAndTime = getCurrentDateAndTime();
        File dir = new File(file_path);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        url=file_path+"/"+NameOfFile + CurrentDateAndTime+ ".jpg";


        File file = new File(dir,NameOfFile + CurrentDateAndTime+ ".jpg");

        try {
            FileOutputStream fOut = new FileOutputStream(file);

            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            AbleToSave();
        }
        catch(FileNotFoundException e) {
            UnableToSave();
        }
        catch(IOException e) {
            UnableToSave();
        }

    }

    public String getURL(){
        return url;
    }
    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void UnableToSave() {
        Toast.makeText(TheThis, "¡No se ha podido guardar la imagen!", Toast.LENGTH_SHORT).show();
    }

    private void AbleToSave() {
        Toast.makeText(TheThis, "Imagen guardada en la galería.", Toast.LENGTH_SHORT).show();
    }
}
