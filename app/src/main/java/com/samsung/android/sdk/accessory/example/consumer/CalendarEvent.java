package com.samsung.android.sdk.accessory.example.consumer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.os.Environment;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.util.logging.Level;

public class CalendarEvent extends Activity{
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_main_page);
        mContext=this;
        // Internet Tread
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            verifyStoragePermissions(this);
            main();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
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
            Log.e("Permission:","添加權限完成");
        }
        else
            Log.e("Permission","有權限");
    }
    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = CalendarEvent.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        Log.e("SCOPE:", String.valueOf(SCOPES));
        Log.e("in:", String.valueOf(in));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        Log.e("JSON_FACTORY", String.valueOf(JSON_FACTORY));
        Log.e("client secret", String.valueOf(clientSecrets));
        Log.e("TOKENS_DIRECTORY_PATH",TOKENS_DIRECTORY_PATH);
        // token Folder path
        File tokenFolder = new File(Environment.getExternalStorageDirectory() +
                File.separator + TOKENS_DIRECTORY_PATH);

        if (!tokenFolder.exists()) {
            boolean mkdir = tokenFolder.mkdirs();
            Log.e("mkdir", String.valueOf(mkdir));
        }

        Log.e("Path", String.valueOf(tokenFolder));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(tokenFolder))
                .setAccessType("offline")
                .build();
        Log.e("flow", String.valueOf(flow));
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Log.e("receiver", String.valueOf(receiver));
        // localhost: OAuth驗證未過關
        AuthorizationCodeInstalledApp credential = new AuthorizationCodeInstalledApp(flow,new LocalServerReceiver()){
            protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException{
                Log.e("Now","In onAuthorization");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(authorizationUrl.build()));
                mContext.startActivity(intent);
            }
        };
        //Credential result = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //Log.e("result", String.valueOf(result));
        return credential.authorize("user");
    }
    public static void main() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        //final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final NetHttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        Log.e("Calendar","in");
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(20)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            Log.e("Now","No upcoming events found.");
        } else {
            Log.e("Now","Upcoming events");
            int day=0;
            DateTime compare = null;
            for (Event event : items) {
                //判斷三天
                if(day == 3)
                    break;
                else if(day == 0)
                {
                    compare = event.getStart().getDate();
                    day=1;
                }
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                if(!String.valueOf(compare).equals(String.valueOf(start)))
                {
                    day++;
                    compare = start;
                }
                Log.e( String.valueOf(start),event.getSummary());
            }
        }
    }

}
