package com.samsung.android.sdk.accessory.example.consumer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class Transaction extends Activity{
    final String FILE_NAME = "/AndroMoney/AndroMoney.csv";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        read();
    }

    private String read(){
        try {
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                //獲得SD卡對應的儲存目錄
                File sdCardDir = Environment.getExternalStorageDirectory();
                //獲取指定檔案對應的輸入流
                String Path = sdCardDir.getCanonicalPath()+FILE_NAME;
                Log.e("Path",sdCardDir.getCanonicalPath()+FILE_NAME);
                File fis = new File(sdCardDir.getCanonicalPath()+FILE_NAME);

                // IS File Exist
                if(isFileExist(Path))
                    Log.e("File","Exist");
                else
                    Log.e("File","Empty");
                //將指定輸入流包裝成BufferReader
                BufferedReader br = new BufferedReader(new FileReader(fis));
                StringBuilder sb = new StringBuilder("");
                String line = null;
                //迴圈讀取檔案內容
                while((line = br.readLine()) != null){
                    sb.append(line);
                    Log.e("Output",line);
                }
                br.close();
                //return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isFileExist(String fileName){
        File file = new File(fileName);
        return file.exists();
    }
    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //沒有權限會跳進來
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission","沒有權限");
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
        else
            Log.e("Permission","有權限");
    }
}
