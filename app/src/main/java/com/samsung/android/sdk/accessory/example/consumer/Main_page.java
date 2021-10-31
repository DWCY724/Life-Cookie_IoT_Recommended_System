package com.samsung.android.sdk.accessory.example.consumer;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.os.StrictMode;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.samsung.android.sdk.accessory.SAAgentV2;
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
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.GeneralSecurityException;


public class Main_page extends AppCompatActivity
        implements OnMapReadyCallback {
    TextView tv;
    ImageView imageView;
    Button Btn1, Btn2, Bt_analysis, Bt_personal_center, Bt_more;
    int total_recommend = 1;
    int sim_total_recommend = 1;
    int random_total_recommend = 0;

    String tag_analysis;
    StringBuffer rc1_analysis = new StringBuffer("");  //classification
    StringBuffer rc2_analysis = new StringBuffer("");  //item
    StringBuffer rv1_analysis = new StringBuffer("");  //classification number(random)
    StringBuffer rv2_analysis = new StringBuffer("");  //item number(random)



    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    /*
    int TIME_INTERVAL = 86400000; //24 hours
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    public static final String TEST_ACTION = "XXX.XXX.XXX" + "_TEST_ACTION";
    */

    // Watch
    private ConsumerService mConsumerService = null;
    private static final String TAG = "ConsumerService";
    private static ToggleButton buttonConnect;
    private static Main_page.MessageAdapter mMessageAdapter;
    private ListView mMessageListView;
    private static TextView mTextView;//id: Terminal
    private static final String TAG1 = MapsActivityCurrentPlace.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;
    //Google Maps
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;
    private List[] likelyPlaceTypes;
    //Easy Card
    final String FILE_NAME = "/AndroMoney/AndroMoney.csv";
    //Google Calendar
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

        tv = (TextView) findViewById(R.id.item_name);
        Btn1 = (Button) findViewById(R.id.item_Like);
        Btn2 = (Button) findViewById(R.id.item_Dislike);
        imageView = (ImageView)findViewById(R.id.item_pic);
        Bt_analysis = (Button)findViewById(R.id.analysis);
        Bt_personal_center = (Button) findViewById(R.id.personal_center);
        Bt_more =(Button) findViewById(R.id.more);
        //tool---------------------------------------------------------------------------------------------------------------------------
        //Watch Initialize
        InitializeAccessory();
        //Google Maps
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        //Easy Card
        verifyStoragePermissions(this);
        read();


        //Google Calendar
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


        //tool end---------------------------------------------------------------------------------------------------------------------------

        /*
        IntentFilter intentFilter = new IntentFilter(TEST_ACTION);
        registerReceiver(receiver, intentFilter);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(TEST_ACTION);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0低电量模式需要使用该方法触发定时任务
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4以上 需要使用该方法精确执行时间
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
        } else {//4。4一下 使用老方法
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), TIME_INTERVAL, pendingIntent);
        }
    */
        recommend_item();



        //change the page into analysis
        Bt_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Main_page.this, Analysis.class);
                //put the data into analysis class
                Bundle bundle = new Bundle();
                bundle.putString("tag_val", tag_analysis);
                bundle.putString("RC1_val", String.valueOf(rc1_analysis));
                bundle.putString("RC2_val", String.valueOf(rc2_analysis));
                bundle.putString("RV1_val", String.valueOf(rv1_analysis));
                bundle.putString("RV2_val", String.valueOf(rv2_analysis));
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        //change the page into personal center
        Bt_personal_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Main_page.this, person.class);
                startActivity(intent);
            }
        });


    }

    /*
    @Nullable
    //@Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("action is :"+ action);
            if (TEST_ACTION.equals(action)) {
                recommend_item();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + TIME_INTERVAL, pendingIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + TIME_INTERVAL, pendingIntent);
                }
            }
        }
    };

     */

    public void recommend_item(){
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("new_item");
        String target = "Sleep";
        //in order to UI
        tag_analysis = target;

        DatabaseReference myRef = database.getReference(target);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Log.d("TAG", "Value is: " + value);
                String key_value = dataSnapshot.toString();
                String val = dataSnapshot.getValue().toString();  //val:狀態tag
                System.out.println("value is : "+val);
                Log.v(target, key_value);


                InputStreamReader the_csv = null;
                try {
                    the_csv = new InputStreamReader(getAssets().open("similarity_value.csv"));
                    String tag = null;
                    String classification = null;
                    String similarity_val = null;
                    String max_val = null;
                    String line;
                    String max_item = null;
                    float a;
                    float max = 0;
                    String []random_classification = new String[100];
                    float []random_value = new float[100];
                    float total_val = 0;
                    int cnt = 0;

                    BufferedReader reader = new BufferedReader(the_csv);
                    reader.readLine();
                    while ((line = reader.readLine()) != null) {
                        //將item1,item2,similarity分開
                        String[] splitted = line.split(",");
                        for (int i = 0; i < splitted.length; i++) {
                            System.out.println(splitted[i]);
                        }

                        tag = splitted[0];
                        classification = splitted[1];
                        similarity_val = splitted[2];
                        a = Float.parseFloat(similarity_val);

                        if (tag.equalsIgnoreCase(val)) {    //find the same 狀態tag
                            if(a > 0.15){
                                random_classification[cnt] = classification;
                                random_value[cnt] = a;
                                total_val += a;
                                cnt++;
                            }
                        }
                    }
                    System.out.println("The total_val is :" + total_val);

                    float random_num = (float) (Math.random()*total_val);
                    System.out.println("The random number is：" + random_num);
                    float goal_num = 0;
                    int val_cnt = 0;
                    while(goal_num < random_num){
                        goal_num += random_value[val_cnt];
                        val_cnt++;
                    }
                    System.out.println("The target classification is :" + random_classification[--val_cnt] + ". Value is :" + random_value[val_cnt]);

                    //in order to UI
                    rc1_analysis.append(random_classification[val_cnt]);
                    rv1_analysis.append(String.valueOf(random_num));

                    //random_classification[--val_cnt] is the target classification
                    //connect the firebase and find the classification
                    //random number to get item

                    FirebaseDatabase db = null;
                    int total_item_number = 0;
                    if(random_classification[val_cnt].equalsIgnoreCase("accessory")){
                        db = FirebaseDatabase.getInstance("https://accessory.firebaseio.com/");
                        total_item_number = 25;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("art")){
                        db = FirebaseDatabase.getInstance("https://art-d9cb7.firebaseio.com/");
                        total_item_number = 18;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("automotive")){
                        db = FirebaseDatabase.getInstance("https://automotive.firebaseio.com/");
                        total_item_number = 25;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("baby")){
                        db = FirebaseDatabase.getInstance("https://baby-c8550.firebaseio.com/");
                        total_item_number = 9;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("bag")){
                        db = FirebaseDatabase.getInstance("https://bag-3dda0.firebaseio.com/");
                        total_item_number = 19;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("beauty")){
                        db = FirebaseDatabase.getInstance("https://beauty-5fb16.firebaseio.com/");
                        total_item_number = 24;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("computer")){
                        db = FirebaseDatabase.getInstance("https://computer-8011f.firebaseio.com/");
                        total_item_number = 1;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("craft")){
                        db = FirebaseDatabase.getInstance("https://craft-f2f46.firebaseio.com/");
                        total_item_number = 13;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("electronic")){
                        db = FirebaseDatabase.getInstance("https://electronic-d3568.firebaseio.com/");
                        total_item_number = 24;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("furnish")){
                        db = FirebaseDatabase.getInstance("https://furnish.firebaseio.com/");
                        total_item_number = 19;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("game")){
                        db = FirebaseDatabase.getInstance("https://game-ba611.firebaseio.com/");
                        total_item_number = 13;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("health")){
                        db = FirebaseDatabase.getInstance("https://health-59042.firebaseio.com/");
                        total_item_number = 28;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("home")){
                        db = FirebaseDatabase.getInstance("https://home-705b0.firebaseio.com/");
                        total_item_number = 22;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("household")){
                        db = FirebaseDatabase.getInstance("https://household-2ed0d.firebaseio.com/");
                        total_item_number = 21;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("industrial")) {
                        db = FirebaseDatabase.getInstance("https://industrial-ec9a4.firebaseio.com/");
                        total_item_number = 9;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("kitchen")){
                        db = FirebaseDatabase.getInstance("https://kitchen-8d5d5.firebaseio.com/");
                        total_item_number = 16;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("luggage")){
                        db = FirebaseDatabase.getInstance("https://luggage.firebaseio.com/");
                        total_item_number = 16;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("movie")){
                        db = FirebaseDatabase.getInstance("https://movie-e106c.firebaseio.com/");
                        total_item_number = 16;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("outdoor")) {
                        db = FirebaseDatabase.getInstance("https://outdoor.firebaseio.com/");
                        total_item_number = 20;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("pet")){
                        db = FirebaseDatabase.getInstance("https://pet-2427c.firebaseio.com/");
                        total_item_number = 20;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("scientific")){
                        db = FirebaseDatabase.getInstance("https://scientific.firebaseio.com/");
                        total_item_number = 13;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("short")){
                        db = FirebaseDatabase.getInstance("https://short-54df7.firebaseio.com/");
                        total_item_number = 16;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("sleep")){
                        db = FirebaseDatabase.getInstance("https://sleep-da956.firebaseio.com/");
                        total_item_number = 16;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("software")){
                        db = FirebaseDatabase.getInstance("https://software-568e9.firebaseio.com/");
                        total_item_number = 17;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("sport")) {
                        db = FirebaseDatabase.getInstance("https://sport-dc5d0.firebaseio.com/");
                        total_item_number = 26;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("suit")){
                        db = FirebaseDatabase.getInstance("https://suit-eed70.firebaseio.com/");
                        total_item_number = 21;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("swimsuit")){
                        db = FirebaseDatabase.getInstance("https://swimsuit.firebaseio.com/");
                        total_item_number = 19;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("television")){
                        db = FirebaseDatabase.getInstance("https://television-71fb4.firebaseio.com/");
                        total_item_number = 12;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("tool")) {
                        db = FirebaseDatabase.getInstance("https://tool-3ce85.firebaseio.com/");
                        total_item_number = 18;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("toy")){
                        db = FirebaseDatabase.getInstance("https://toy-19e9f.firebaseio.com/");
                        total_item_number = 14;
                    }
                    else if(random_classification[val_cnt].equalsIgnoreCase("watch")){
                        db = FirebaseDatabase.getInstance("https://watch-8a774.firebaseio.com/");
                        total_item_number = 4;
                    }

                    //FirebaseDatabase db = FirebaseDatabase.getInstance("https://sleep-da956.firebaseio.com/");
                    System.out.println("total_item_number is :"+ total_item_number);
                    int random_item_val = (int) (Math.random()*total_item_number);
                    System.out.println("The random_item_val is:" + random_item_val);
                    String key_num = Integer.toString(random_item_val);
                    System.out.println("The key_num is:" + key_num);
                    if(db != null){
                        System.out.println("The db is not null");
                        DatabaseReference Ref = db.getReference(key_num);
                        Ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot ds) {
                                //String key_value = ds.toString();

                                String final_item = ds.getValue().toString();
                                System.out.println("The recommend item is:" + final_item);

                                tv.setText(final_item);

                                //in order to UI
                                rc2_analysis.append(final_item);
                                rv2_analysis.append(key_num);

                                //show picture
                                downloadwithbyte(final_item);

                                //check more item -> to amazon
                                Bt_more.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        FirebaseDatabase more_item = FirebaseDatabase.getInstance("https://more-information-item-urls.firebaseio.com/");
                                        DatabaseReference more_ref = more_item.getReference(final_item);
                                        more_ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String more_url = snapshot.getValue().toString();
                                                Uri uri = Uri.parse(more_url);//要跳轉的網址
                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                });
                                /*
                                    // Reference to an image file in Cloud Storage
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference RS_storageRef = storage.getReference("845797.jpg");
                                    //StorageReference isRef = RS_storageRef.child("845797.jpg");

                                    // ImageView in your Activity
                                    ImageView imageView = findViewById(R.id.item_pic);

                                    //String url = "https://media.istockphoto.com/photos/business-man-pushing-large-stone-up-to-hill-business-heavy-tasks-and-picture-id825383494?k=20&m=825383494&s=612x612&w=0&h=tEqZ5HFZcM3lmDm_cmI7hOeceiqy9gYrkyLTTkrXdY4=";

                                    // Download directly from StorageReference using Glide
                                    // (See MyAppGlideModule for Loader registration)
                                    Glide.with(imageView.getContext())
                                        .load(RS_storageRef)
                                        .into(imageView);

                                    */


                            }
                            @Override
                            public void onCancelled(DatabaseError error_data) {
                                // Failed to read value
                                Log.w("TAG", "Failed to read value.", error_data.toException());
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        Btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                total_recommend ++;
                if(total_recommend <=6){

                    sim_total_recommend ++;
                    random_total_recommend ++;

                    if(sim_total_recommend <=3){

                        random_total_recommend --;
                        //myRef.setValue("health");

                        // Read from the database
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                //String value = dataSnapshot.getValue(String.class);
                                //Log.d("TAG", "Value is: " + value);
                                String key_value = dataSnapshot.toString();
                                String val = dataSnapshot.getValue().toString();  //val:狀態tag
                                System.out.println("value is : "+val);
                                Log.v(target, key_value);


                                InputStreamReader the_csv = null;
                                try {
                                    the_csv = new InputStreamReader(getAssets().open("similarity_value.csv"));
                                    String tag = null;
                                    String classification = null;
                                    String similarity_val = null;
                                    String max_val = null;
                                    String line;
                                    String max_item = null;
                                    float a;
                                    float max = 0;
                                    String []random_classification = new String[100];
                                    float []random_value = new float[100];
                                    float total_val = 0;
                                    int cnt = 0;

                                    BufferedReader reader = new BufferedReader(the_csv);
                                    reader.readLine();
                                    while ((line = reader.readLine()) != null) {
                                        //將item1,item2,similarity分開
                                        String[] splitted = line.split(",");
                                        for (int i = 0; i < splitted.length; i++) {
                                            System.out.println(splitted[i]);
                                        }

                                        tag = splitted[0];
                                        classification = splitted[1];
                                        similarity_val = splitted[2];
                                        a = Float.parseFloat(similarity_val);

                                        if (tag.equalsIgnoreCase(val)) {    //find the same 狀態tag
                                            if(a > 0.15){
                                                random_classification[cnt] = classification;
                                                random_value[cnt] = a;
                                                total_val += a;
                                                cnt++;
                                            }
                                        }
                                    }
                                    System.out.println("The total_val is :" + total_val);

                                    float random_num = (float) (Math.random()*total_val);
                                    System.out.println("The random number is：" + random_num);
                                    float goal_num = 0;
                                    int val_cnt = 0;
                                    while(goal_num < random_num){
                                        goal_num += random_value[val_cnt];
                                        val_cnt++;
                                    }
                                    System.out.println("The target classification is :" + random_classification[--val_cnt] + ". Value is :" + random_value[val_cnt]);

                                    //in order to UI
                                    rc1_analysis.append(", ");
                                    rc1_analysis.append(random_classification[val_cnt]);
                                    rv1_analysis.append(", ");
                                    rv1_analysis.append(String.valueOf(random_num));

                                    //random_classification[--val_cnt] is the target classification
                                    //connect the firebase and find the classification
                                    //random number to get item

                                    FirebaseDatabase db = null;
                                    int total_item_number = 0;
                                    if(random_classification[val_cnt].equalsIgnoreCase("accessory")){
                                        db = FirebaseDatabase.getInstance("https://accessory.firebaseio.com/");
                                        total_item_number = 25;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("art")){
                                        db = FirebaseDatabase.getInstance("https://art-d9cb7.firebaseio.com/");
                                        total_item_number = 18;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("automotive")){
                                        db = FirebaseDatabase.getInstance("https://automotive.firebaseio.com/");
                                        total_item_number = 25;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("baby")){
                                        db = FirebaseDatabase.getInstance("https://baby-c8550.firebaseio.com/");
                                        total_item_number = 9;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("bag")){
                                        db = FirebaseDatabase.getInstance("https://bag-3dda0.firebaseio.com/");
                                        total_item_number = 19;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("beauty")){
                                        db = FirebaseDatabase.getInstance("https://beauty-5fb16.firebaseio.com/");
                                        total_item_number = 24;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("computer")){
                                        db = FirebaseDatabase.getInstance("https://computer-8011f.firebaseio.com/");
                                        total_item_number = 1;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("craft")){
                                        db = FirebaseDatabase.getInstance("https://craft-f2f46.firebaseio.com/");
                                        total_item_number = 13;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("electronic")){
                                        db = FirebaseDatabase.getInstance("https://electronic-d3568.firebaseio.com/");
                                        total_item_number = 24;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("furnish")){
                                        db = FirebaseDatabase.getInstance("https://furnish.firebaseio.com/");
                                        total_item_number = 19;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("game")){
                                        db = FirebaseDatabase.getInstance("https://game-ba611.firebaseio.com/");
                                        total_item_number = 13;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("health")){
                                        db = FirebaseDatabase.getInstance("https://health-59042.firebaseio.com/");
                                        total_item_number = 28;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("home")){
                                        db = FirebaseDatabase.getInstance("https://home-705b0.firebaseio.com/");
                                        total_item_number = 22;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("household")){
                                        db = FirebaseDatabase.getInstance("https://household-2ed0d.firebaseio.com/");
                                        total_item_number = 21;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("industrial")) {
                                        db = FirebaseDatabase.getInstance("https://industrial-ec9a4.firebaseio.com/");
                                        total_item_number = 9;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("kitchen")){
                                        db = FirebaseDatabase.getInstance("https://kitchen-8d5d5.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("luggage")){
                                        db = FirebaseDatabase.getInstance("https://luggage.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("movie")){
                                        db = FirebaseDatabase.getInstance("https://movie-e106c.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("outdoor")) {
                                        db = FirebaseDatabase.getInstance("https://outdoor.firebaseio.com/");
                                        total_item_number = 20;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("pet")){
                                        db = FirebaseDatabase.getInstance("https://pet-2427c.firebaseio.com/");
                                        total_item_number = 20;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("scientific")){
                                        db = FirebaseDatabase.getInstance("https://scientific.firebaseio.com/");
                                        total_item_number = 13;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("short")){
                                        db = FirebaseDatabase.getInstance("https://short-54df7.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("sleep")){
                                        db = FirebaseDatabase.getInstance("https://sleep-da956.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("software")){
                                        db = FirebaseDatabase.getInstance("https://software-568e9.firebaseio.com/");
                                        total_item_number = 17;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("sport")) {
                                        db = FirebaseDatabase.getInstance("https://sport-dc5d0.firebaseio.com/");
                                        total_item_number = 26;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("suit")){
                                        db = FirebaseDatabase.getInstance("https://suit-eed70.firebaseio.com/");
                                        total_item_number = 21;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("swimsuit")){
                                        db = FirebaseDatabase.getInstance("https://swimsuit.firebaseio.com/");
                                        total_item_number = 19;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("television")){
                                        db = FirebaseDatabase.getInstance("https://television-71fb4.firebaseio.com/");
                                        total_item_number = 12;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("tool")) {
                                        db = FirebaseDatabase.getInstance("https://tool-3ce85.firebaseio.com/");
                                        total_item_number = 18;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("toy")){
                                        db = FirebaseDatabase.getInstance("https://toy-19e9f.firebaseio.com/");
                                        total_item_number = 14;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("watch")){
                                        db = FirebaseDatabase.getInstance("https://watch-8a774.firebaseio.com/");
                                        total_item_number = 4;
                                    }

                                    //FirebaseDatabase db = FirebaseDatabase.getInstance("https://sleep-da956.firebaseio.com/");
                                    System.out.println("total_item_number is :"+ total_item_number);
                                    int random_item_val = (int) (Math.random()*total_item_number);
                                    System.out.println("The random_item_val is:" + random_item_val);
                                    String key_num = Integer.toString(random_item_val);
                                    System.out.println("The key_num is:" + key_num);
                                    if(db != null){
                                        System.out.println("The db is not null");
                                        DatabaseReference Ref = db.getReference(key_num);
                                        Ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot ds) {
                                                //String key_value = ds.toString();

                                                String final_item = ds.getValue().toString();
                                                System.out.println("The recommend item is:" + final_item);

                                                tv.setText(final_item);

                                                //in order to UI
                                                rc2_analysis.append(", ");
                                                rc2_analysis.append(final_item);
                                                rv2_analysis.append(", ");
                                                rv2_analysis.append(key_num);

                                                //show picture
                                                downloadwithbyte(final_item);

                                                //check more item -> to amazon
                                                Bt_more.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        FirebaseDatabase more_item = FirebaseDatabase.getInstance("https://more-information-item-urls.firebaseio.com/");
                                                        DatabaseReference more_ref = more_item.getReference(final_item);
                                                        more_ref.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                String more_url = snapshot.getValue().toString();
                                                                Uri uri = Uri.parse(more_url);//要跳轉的網址
                                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                                startActivity(intent);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                            @Override
                                            public void onCancelled(DatabaseError error_data) {
                                                // Failed to read value
                                                Log.w("TAG", "Failed to read value.", error_data.toException());
                                            }
                                        });
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w("TAG", "Failed to read value.", error.toException());
                            }
                        });
                    }
                    else if(random_total_recommend <= 3){

                        sim_total_recommend --;

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                //String value = dataSnapshot.getValue(String.class);
                                //Log.d("TAG", "Value is: " + value);
                                String key_value = dataSnapshot.toString();
                                String val = dataSnapshot.getValue().toString();  //val:狀態tag
                                System.out.println("value is : " + val);
                                Log.v(target, key_value);

                                InputStreamReader the_csv = null;
                                try {
                                    the_csv = new InputStreamReader(getAssets().open("similarity_value.csv"));
                                    String tag = null;
                                    String classification = null;
                                    String similarity_val = null;
                                    String max_val = null;
                                    String line;
                                    String max_item = null;
                                    float a;
                                    float max = 0;
                                    float total_val = 0;
                                    int cnt = 0;
                                    int without_sim_cnt = 0;
                                    String[] without_sim_random_classification = new String[100];
                                    float[] without_sim_random_value = new float[100];

                                    BufferedReader reader = new BufferedReader(the_csv);
                                    reader.readLine();
                                    while ((line = reader.readLine()) != null) {

                                        //將item1,item2,similarity分開
                                        String[] splitted = line.split(",");
                                        for (int i = 0; i < splitted.length; i++) {
                                            System.out.println(splitted[i]);
                                        }

                                        tag = splitted[0];
                                        classification = splitted[1];
                                        similarity_val = splitted[2];
                                        a = Float.parseFloat(similarity_val);

                                        if (tag.equalsIgnoreCase(val)) {    //find the same 狀態tag
                                            without_sim_random_classification[without_sim_cnt] = classification;
                                            without_sim_random_value[without_sim_cnt] = a;
                                            without_sim_cnt++;
                                        }
                                    }

                                    //without similarity random
                                    int without_sim_random_num = (int) (Math.random()*(without_sim_cnt-1));

                                    FirebaseDatabase db = null;
                                    int total_item_number = 0;

                                    //in order to UI
                                    rc1_analysis.append(", ");
                                    rc1_analysis.append(without_sim_random_classification[without_sim_random_num]);
                                    rv1_analysis.append(", ");
                                    rv1_analysis.append(String.valueOf(without_sim_random_num));

                                    if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("accessory")){
                                        db = FirebaseDatabase.getInstance("https://accessory.firebaseio.com/");
                                        total_item_number = 25;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("art")){
                                        db = FirebaseDatabase.getInstance("https://art-d9cb7.firebaseio.com/");
                                        total_item_number = 18;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("automotive")){
                                        db = FirebaseDatabase.getInstance("https://automotive.firebaseio.com/");
                                        total_item_number = 25;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("baby")){
                                        db = FirebaseDatabase.getInstance("https://baby-c8550.firebaseio.com/");
                                        total_item_number = 9;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("bag")){
                                        db = FirebaseDatabase.getInstance("https://bag-3dda0.firebaseio.com/");
                                        total_item_number = 19;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("beauty")){
                                        db = FirebaseDatabase.getInstance("https://beauty-5fb16.firebaseio.com/");
                                        total_item_number = 24;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("computer")){
                                        db = FirebaseDatabase.getInstance("https://computer-8011f.firebaseio.com/");
                                        total_item_number = 1;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("craft")){
                                        db = FirebaseDatabase.getInstance("https://craft-f2f46.firebaseio.com/");
                                        total_item_number = 13;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("electronic")){
                                        db = FirebaseDatabase.getInstance("https://electronic-d3568.firebaseio.com/");
                                        total_item_number = 24;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("furnish")){
                                        db = FirebaseDatabase.getInstance("https://furnish.firebaseio.com/");
                                        total_item_number = 19;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("game")){
                                        db = FirebaseDatabase.getInstance("https://game-ba611.firebaseio.com/");
                                        total_item_number = 13;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("health")){
                                        db = FirebaseDatabase.getInstance("https://health-59042.firebaseio.com/");
                                        total_item_number = 28;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("home")){
                                        db = FirebaseDatabase.getInstance("https://home-705b0.firebaseio.com/");
                                        total_item_number = 22;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("household")){
                                        db = FirebaseDatabase.getInstance("https://household-2ed0d.firebaseio.com/");
                                        total_item_number = 21;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("industrial")) {
                                        db = FirebaseDatabase.getInstance("https://industrial-ec9a4.firebaseio.com/");
                                        total_item_number = 9;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("kitchen")){
                                        db = FirebaseDatabase.getInstance("https://kitchen-8d5d5.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("luggage")){
                                        db = FirebaseDatabase.getInstance("https://luggage.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("movie")){
                                        db = FirebaseDatabase.getInstance("https://movie-e106c.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("outdoor")) {
                                        db = FirebaseDatabase.getInstance("https://outdoor.firebaseio.com/");
                                        total_item_number = 20;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("pet")){
                                        db = FirebaseDatabase.getInstance("https://pet-2427c.firebaseio.com/");
                                        total_item_number = 20;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("scientific")){
                                        db = FirebaseDatabase.getInstance("https://scientific.firebaseio.com/");
                                        total_item_number = 13;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("short")){
                                        db = FirebaseDatabase.getInstance("https://short-54df7.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("sleep")){
                                        db = FirebaseDatabase.getInstance("https://sleep-da956.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("software")){
                                        db = FirebaseDatabase.getInstance("https://software-568e9.firebaseio.com/");
                                        total_item_number = 17;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("sport")) {
                                        db = FirebaseDatabase.getInstance("https://sport-dc5d0.firebaseio.com/");
                                        total_item_number = 26;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("suit")){
                                        db = FirebaseDatabase.getInstance("https://suit-eed70.firebaseio.com/");
                                        total_item_number = 21;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("swimsuit")){
                                        db = FirebaseDatabase.getInstance("https://swimsuit.firebaseio.com/");
                                        total_item_number = 19;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("television")){
                                        db = FirebaseDatabase.getInstance("https://television-71fb4.firebaseio.com/");
                                        total_item_number = 12;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("tool")) {
                                        db = FirebaseDatabase.getInstance("https://tool-3ce85.firebaseio.com/");
                                        total_item_number = 18;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("toy")){
                                        db = FirebaseDatabase.getInstance("https://toy-19e9f.firebaseio.com/");
                                        total_item_number = 14;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("watch")){
                                        db = FirebaseDatabase.getInstance("https://watch-8a774.firebaseio.com/");
                                        total_item_number = 4;
                                    }
                                    //FirebaseDatabase db = FirebaseDatabase.getInstance("https://sleep-da956.firebaseio.com/");
                                    int no_sim_random_item_val = (int) (Math.random()*total_item_number);
                                    while(no_sim_random_item_val == 0){
                                        no_sim_random_item_val = (int) (Math.random()*total_item_number);
                                    }
                                    System.out.println("The no_sim_random_item_val is:" + no_sim_random_item_val);
                                    String no_sim_key_num = Integer.toString(no_sim_random_item_val);
                                    System.out.println("The no_sim_key_num is:" + no_sim_key_num);
                                    DatabaseReference no_sim_Ref = db.getReference(no_sim_key_num);
                                    no_sim_Ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot no_sim_ds) {
                                            //String key_value = ds.toString();

                                            String final_item = no_sim_ds.getValue().toString();
                                            System.out.println("The no sim recommend item is:" + final_item);

                                            tv.setText(final_item);

                                            //in order to UI
                                            rc2_analysis.append(", ");
                                            rc2_analysis.append(final_item);
                                            rv2_analysis.append(", ");
                                            rv2_analysis.append(no_sim_key_num);

                                            //show picture
                                            downloadwithbyte(final_item);

                                            //check more item -> to amazon
                                            Bt_more.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    FirebaseDatabase more_item = FirebaseDatabase.getInstance("https://more-information-item-urls.firebaseio.com/");
                                                    DatabaseReference more_ref = more_item.getReference(final_item);
                                                    more_ref.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String more_url = snapshot.getValue().toString();
                                                            Uri uri = Uri.parse(more_url);//要跳轉的網址
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                            startActivity(intent);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            });

                                        }
                                        @Override
                                        public void onCancelled(DatabaseError no_sim_error_data) {
                                            // Failed to read value
                                            Log.w("TAG", "Failed to read value.", no_sim_error_data.toException());
                                        }
                                    });
                                }catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w("TAG", "Failed to read value.", error.toException());
                            }
                        });

                    }
                }


            }
        });

        Btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                total_recommend ++;
                if(total_recommend <=6){

                    sim_total_recommend ++;
                    random_total_recommend ++;

                    if(sim_total_recommend <=3){

                        random_total_recommend --;
                        //myRef.setValue("health");

                        // Read from the database
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                //String value = dataSnapshot.getValue(String.class);
                                //Log.d("TAG", "Value is: " + value);
                                String key_value = dataSnapshot.toString();
                                String val = dataSnapshot.getValue().toString();  //val:狀態tag
                                System.out.println("value is : "+val);
                                Log.v(target, key_value);


                                InputStreamReader the_csv = null;
                                try {
                                    the_csv = new InputStreamReader(getAssets().open("similarity_value.csv"));
                                    String tag = null;
                                    String classification = null;
                                    String similarity_val = null;
                                    String max_val = null;
                                    String line;
                                    String max_item = null;
                                    float a;
                                    float max = 0;
                                    String []random_classification = new String[100];
                                    float []random_value = new float[100];
                                    float total_val = 0;
                                    int cnt = 0;

                                    BufferedReader reader = new BufferedReader(the_csv);
                                    reader.readLine();
                                    while ((line = reader.readLine()) != null) {
                                        //將item1,item2,similarity分開
                                        String[] splitted = line.split(",");
                                        for (int i = 0; i < splitted.length; i++) {
                                            System.out.println(splitted[i]);
                                        }

                                        tag = splitted[0];
                                        classification = splitted[1];
                                        similarity_val = splitted[2];
                                        a = Float.parseFloat(similarity_val);

                                        if (tag.equalsIgnoreCase(val)) {    //find the same 狀態tag
                                            if(a > 0.15){
                                                random_classification[cnt] = classification;
                                                random_value[cnt] = a;
                                                total_val += a;
                                                cnt++;
                                            }
                                        }
                                    }
                                    System.out.println("The total_val is :" + total_val);

                                    float random_num = (float) (Math.random()*total_val);
                                    System.out.println("The random number is：" + random_num);
                                    float goal_num = 0;
                                    int val_cnt = 0;
                                    while(goal_num < random_num){
                                        goal_num += random_value[val_cnt];
                                        val_cnt++;
                                    }
                                    System.out.println("The target classification is :" + random_classification[--val_cnt] + ". Value is :" + random_value[val_cnt]);

                                    //in order to UI
                                    rc1_analysis.append(", ");
                                    rc1_analysis.append(random_classification[val_cnt]);
                                    rv1_analysis.append(", ");
                                    rv1_analysis.append(String.valueOf(random_value[val_cnt]));

                                    //random_classification[--val_cnt] is the target classification
                                    //connect the firebase and find the classification
                                    //random number to get item

                                    FirebaseDatabase db = null;
                                    int total_item_number = 0;
                                    if(random_classification[val_cnt].equalsIgnoreCase("accessory")){
                                        db = FirebaseDatabase.getInstance("https://accessory.firebaseio.com/");
                                        total_item_number = 25;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("art")){
                                        db = FirebaseDatabase.getInstance("https://art-d9cb7.firebaseio.com/");
                                        total_item_number = 18;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("automotive")){
                                        db = FirebaseDatabase.getInstance("https://automotive.firebaseio.com/");
                                        total_item_number = 25;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("baby")){
                                        db = FirebaseDatabase.getInstance("https://baby-c8550.firebaseio.com/");
                                        total_item_number = 9;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("bag")){
                                        db = FirebaseDatabase.getInstance("https://bag-3dda0.firebaseio.com/");
                                        total_item_number = 19;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("beauty")){
                                        db = FirebaseDatabase.getInstance("https://beauty-5fb16.firebaseio.com/");
                                        total_item_number = 24;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("computer")){
                                        db = FirebaseDatabase.getInstance("https://computer-8011f.firebaseio.com/");
                                        total_item_number = 1;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("craft")){
                                        db = FirebaseDatabase.getInstance("https://craft-f2f46.firebaseio.com/");
                                        total_item_number = 13;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("electronic")){
                                        db = FirebaseDatabase.getInstance("https://electronic-d3568.firebaseio.com/");
                                        total_item_number = 24;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("furnish")){
                                        db = FirebaseDatabase.getInstance("https://furnish.firebaseio.com/");
                                        total_item_number = 19;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("game")){
                                        db = FirebaseDatabase.getInstance("https://game-ba611.firebaseio.com/");
                                        total_item_number = 13;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("health")){
                                        db = FirebaseDatabase.getInstance("https://health-59042.firebaseio.com/");
                                        total_item_number = 28;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("home")){
                                        db = FirebaseDatabase.getInstance("https://home-705b0.firebaseio.com/");
                                        total_item_number = 22;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("household")){
                                        db = FirebaseDatabase.getInstance("https://household-2ed0d.firebaseio.com/");
                                        total_item_number = 21;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("industrial")) {
                                        db = FirebaseDatabase.getInstance("https://industrial-ec9a4.firebaseio.com/");
                                        total_item_number = 9;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("kitchen")){
                                        db = FirebaseDatabase.getInstance("https://kitchen-8d5d5.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("luggage")){
                                        db = FirebaseDatabase.getInstance("https://luggage.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("movie")){
                                        db = FirebaseDatabase.getInstance("https://movie-e106c.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("outdoor")) {
                                        db = FirebaseDatabase.getInstance("https://outdoor.firebaseio.com/");
                                        total_item_number = 20;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("pet")){
                                        db = FirebaseDatabase.getInstance("https://pet-2427c.firebaseio.com/");
                                        total_item_number = 20;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("scientific")){
                                        db = FirebaseDatabase.getInstance("https://scientific.firebaseio.com/");
                                        total_item_number = 13;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("short")){
                                        db = FirebaseDatabase.getInstance("https://short-54df7.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("sleep")){
                                        db = FirebaseDatabase.getInstance("https://sleep-da956.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("software")){
                                        db = FirebaseDatabase.getInstance("https://software-568e9.firebaseio.com/");
                                        total_item_number = 17;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("sport")) {
                                        db = FirebaseDatabase.getInstance("https://sport-dc5d0.firebaseio.com/");
                                        total_item_number = 26;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("suit")){
                                        db = FirebaseDatabase.getInstance("https://suit-eed70.firebaseio.com/");
                                        total_item_number = 21;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("swimsuit")){
                                        db = FirebaseDatabase.getInstance("https://swimsuit.firebaseio.com/");
                                        total_item_number = 19;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("television")){
                                        db = FirebaseDatabase.getInstance("https://television-71fb4.firebaseio.com/");
                                        total_item_number = 12;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("tool")) {
                                        db = FirebaseDatabase.getInstance("https://tool-3ce85.firebaseio.com/");
                                        total_item_number = 18;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("toy")){
                                        db = FirebaseDatabase.getInstance("https://toy-19e9f.firebaseio.com/");
                                        total_item_number = 14;
                                    }
                                    else if(random_classification[val_cnt].equalsIgnoreCase("watch")){
                                        db = FirebaseDatabase.getInstance("https://watch-8a774.firebaseio.com/");
                                        total_item_number = 4;
                                    }

                                    //FirebaseDatabase db = FirebaseDatabase.getInstance("https://sleep-da956.firebaseio.com/");
                                    System.out.println("total_item_number is :"+ total_item_number);
                                    int random_item_val = (int) (Math.random()*total_item_number);
                                    System.out.println("The random_item_val is:" + random_item_val);
                                    String key_num = Integer.toString(random_item_val);
                                    System.out.println("The key_num is:" + key_num);
                                    if(db != null){
                                        System.out.println("The db is not null");
                                        DatabaseReference Ref = db.getReference(key_num);
                                        Ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot ds) {
                                                //String key_value = ds.toString();

                                                String final_item = ds.getValue().toString();
                                                System.out.println("The recommend item is:" + final_item);

                                                tv.setText(final_item);

                                                //in order to UI
                                                rc2_analysis.append(", ");
                                                rc2_analysis.append(final_item);
                                                rv2_analysis.append(", ");
                                                rv2_analysis.append(key_num);

                                                //show picture
                                                downloadwithbyte(final_item);

                                                //check more item -> to amazon
                                                Bt_more.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        FirebaseDatabase more_item = FirebaseDatabase.getInstance("https://more-information-item-urls.firebaseio.com/");
                                                        DatabaseReference more_ref = more_item.getReference(final_item);
                                                        more_ref.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                String more_url = snapshot.getValue().toString();
                                                                Uri uri = Uri.parse(more_url);//要跳轉的網址
                                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                                startActivity(intent);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                            @Override
                                            public void onCancelled(DatabaseError error_data) {
                                                // Failed to read value
                                                Log.w("TAG", "Failed to read value.", error_data.toException());
                                            }
                                        });
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w("TAG", "Failed to read value.", error.toException());
                            }
                        });
                    }
                    else if(random_total_recommend <= 3){

                        sim_total_recommend --;

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                //String value = dataSnapshot.getValue(String.class);
                                //Log.d("TAG", "Value is: " + value);
                                String key_value = dataSnapshot.toString();
                                String val = dataSnapshot.getValue().toString();  //val:狀態tag
                                System.out.println("value is : " + val);
                                Log.v(target, key_value);

                                InputStreamReader the_csv = null;
                                try {
                                    the_csv = new InputStreamReader(getAssets().open("similarity_value.csv"));
                                    String tag = null;
                                    String classification = null;
                                    String similarity_val = null;
                                    String max_val = null;
                                    String line;
                                    String max_item = null;
                                    float a;
                                    float max = 0;
                                    float total_val = 0;
                                    int cnt = 0;
                                    int without_sim_cnt = 0;
                                    String[] without_sim_random_classification = new String[100];
                                    float[] without_sim_random_value = new float[100];

                                    BufferedReader reader = new BufferedReader(the_csv);
                                    reader.readLine();
                                    while ((line = reader.readLine()) != null) {

                                        //將item1,item2,similarity分開
                                        String[] splitted = line.split(",");
                                        for (int i = 0; i < splitted.length; i++) {
                                            System.out.println(splitted[i]);
                                        }

                                        tag = splitted[0];
                                        classification = splitted[1];
                                        similarity_val = splitted[2];
                                        a = Float.parseFloat(similarity_val);

                                        if (tag.equalsIgnoreCase(val)) {    //find the same 狀態tag
                                            without_sim_random_classification[without_sim_cnt] = classification;
                                            without_sim_random_value[without_sim_cnt] = a;
                                            without_sim_cnt++;
                                        }
                                    }

                                    //without similarity random
                                    int without_sim_random_num = (int) (Math.random()*(without_sim_cnt-1));

                                    //In order to UI
                                    rc1_analysis.append(", ");
                                    rc1_analysis.append(without_sim_random_classification[without_sim_random_num]);
                                    rv1_analysis.append(", ");
                                    rv1_analysis.append(String.valueOf(without_sim_random_num));

                                    FirebaseDatabase db = null;
                                    int total_item_number = 0;

                                    if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("accessory")){
                                        db = FirebaseDatabase.getInstance("https://accessory.firebaseio.com/");
                                        total_item_number = 25;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("art")){
                                        db = FirebaseDatabase.getInstance("https://art-d9cb7.firebaseio.com/");
                                        total_item_number = 18;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("automotive")){
                                        db = FirebaseDatabase.getInstance("https://automotive.firebaseio.com/");
                                        total_item_number = 25;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("baby")){
                                        db = FirebaseDatabase.getInstance("https://baby-c8550.firebaseio.com/");
                                        total_item_number = 9;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("bag")){
                                        db = FirebaseDatabase.getInstance("https://bag-3dda0.firebaseio.com/");
                                        total_item_number = 19;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("beauty")){
                                        db = FirebaseDatabase.getInstance("https://beauty-5fb16.firebaseio.com/");
                                        total_item_number = 24;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("computer")){
                                        db = FirebaseDatabase.getInstance("https://computer-8011f.firebaseio.com/");
                                        total_item_number = 1;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("craft")){
                                        db = FirebaseDatabase.getInstance("https://craft-f2f46.firebaseio.com/");
                                        total_item_number = 13;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("electronic")){
                                        db = FirebaseDatabase.getInstance("https://electronic-d3568.firebaseio.com/");
                                        total_item_number = 24;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("furnish")){
                                        db = FirebaseDatabase.getInstance("https://furnish.firebaseio.com/");
                                        total_item_number = 19;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("game")){
                                        db = FirebaseDatabase.getInstance("https://game-ba611.firebaseio.com/");
                                        total_item_number = 13;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("health")){
                                        db = FirebaseDatabase.getInstance("https://health-59042.firebaseio.com/");
                                        total_item_number = 28;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("home")){
                                        db = FirebaseDatabase.getInstance("https://home-705b0.firebaseio.com/");
                                        total_item_number = 22;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("household")){
                                        db = FirebaseDatabase.getInstance("https://household-2ed0d.firebaseio.com/");
                                        total_item_number = 21;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("industrial")) {
                                        db = FirebaseDatabase.getInstance("https://industrial-ec9a4.firebaseio.com/");
                                        total_item_number = 9;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("kitchen")){
                                        db = FirebaseDatabase.getInstance("https://kitchen-8d5d5.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("luggage")){
                                        db = FirebaseDatabase.getInstance("https://luggage.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("movie")){
                                        db = FirebaseDatabase.getInstance("https://movie-e106c.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("outdoor")) {
                                        db = FirebaseDatabase.getInstance("https://outdoor.firebaseio.com/");
                                        total_item_number = 20;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("pet")){
                                        db = FirebaseDatabase.getInstance("https://pet-2427c.firebaseio.com/");
                                        total_item_number = 20;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("scientific")){
                                        db = FirebaseDatabase.getInstance("https://scientific.firebaseio.com/");
                                        total_item_number = 13;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("short")){
                                        db = FirebaseDatabase.getInstance("https://short-54df7.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("sleep")){
                                        db = FirebaseDatabase.getInstance("https://sleep-da956.firebaseio.com/");
                                        total_item_number = 16;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("software")){
                                        db = FirebaseDatabase.getInstance("https://software-568e9.firebaseio.com/");
                                        total_item_number = 17;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("sport")) {
                                        db = FirebaseDatabase.getInstance("https://sport-dc5d0.firebaseio.com/");
                                        total_item_number = 26;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("suit")){
                                        db = FirebaseDatabase.getInstance("https://suit-eed70.firebaseio.com/");
                                        total_item_number = 21;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("swimsuit")){
                                        db = FirebaseDatabase.getInstance("https://swimsuit.firebaseio.com/");
                                        total_item_number = 19;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("television")){
                                        db = FirebaseDatabase.getInstance("https://television-71fb4.firebaseio.com/");
                                        total_item_number = 12;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("tool")) {
                                        db = FirebaseDatabase.getInstance("https://tool-3ce85.firebaseio.com/");
                                        total_item_number = 18;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("toy")){
                                        db = FirebaseDatabase.getInstance("https://toy-19e9f.firebaseio.com/");
                                        total_item_number = 14;
                                    }
                                    else if(without_sim_random_classification[without_sim_random_num].equalsIgnoreCase("watch")){
                                        db = FirebaseDatabase.getInstance("https://watch-8a774.firebaseio.com/");
                                        total_item_number = 4;
                                    }

                                    int no_sim_random_item_val = (int) (Math.random()*total_item_number);
                                    System.out.println("The no_sim_random_item_val is:" + no_sim_random_item_val);
                                    String no_sim_key_num = Integer.toString(no_sim_random_item_val);
                                    System.out.println("The no_sim_key_num is:" + no_sim_key_num);
                                    DatabaseReference no_sim_Ref = db.getReference(no_sim_key_num);
                                    no_sim_Ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot no_sim_ds) {
                                            //String key_value = ds.toString();

                                            String final_item = no_sim_ds.getValue().toString();
                                            System.out.println("The no sim recommend item is:" + final_item);

                                            tv.setText(final_item);

                                            //in order to UI
                                            rc2_analysis.append(", ");
                                            rc2_analysis.append(final_item);
                                            rv2_analysis.append(", ");
                                            rv2_analysis.append(no_sim_key_num);

                                            //show picture
                                            downloadwithbyte(final_item);

                                            //check more item -> to amazon
                                            Bt_more.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    FirebaseDatabase more_item = FirebaseDatabase.getInstance("https://more-information-item-urls.firebaseio.com/");
                                                    DatabaseReference more_ref = more_item.getReference(final_item);
                                                    more_ref.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            String more_url = snapshot.getValue().toString();
                                                            Uri uri = Uri.parse(more_url);//要跳轉的網址
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                            startActivity(intent);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            });

                                        }
                                        @Override
                                        public void onCancelled(DatabaseError no_sim_error_data) {
                                            // Failed to read value
                                            Log.w("TAG", "Failed to read value.", no_sim_error_data.toException());
                                        }
                                    });
                                }catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w("TAG", "Failed to read value.", error.toException());
                            }
                        });

                    }
                }


            }
        });
    }

    public void downloadwithbyte(String item){

        StringBuffer url = new StringBuffer("picture/");
        url.append(item);
        url.append(".jpg");
        StorageReference imgRef = storageReference.child(String.valueOf(url));

        long MaxByte = 1024*1024;

        imgRef.getBytes(MaxByte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //convert byte[] to bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        System.out.println("TAG~: "+tag_analysis);
        System.out.println("RC1~: "+rc1_analysis);
        System.out.println("RC2~: "+rc2_analysis);
        System.out.println("RV1~: "+rv1_analysis);
        System.out.println("RV2~: "+rv2_analysis);


    }
    //Watch---------------------------------------------------------------------------------------------------------------------
    //Click button
    public void OnClickWatch(View v){
        switch (v.getId()){
            case R.id.buttonConnect:{
                if(mConsumerService != null){
                    //當按鈕狀態為選取時
                    if(buttonConnect.isChecked()){
                        buttonConnect.setChecked(false);
                        Log.e("mConsumerService", String.valueOf(mConsumerService));
                        Log.e("","Connect1?");
                        mConsumerService.findPeers();//找Tizen
                        Log.e("mConsumerService", String.valueOf(mConsumerService));
                        Log.e("","Connect1?");
                    }
                    //當按鈕狀態為未選取時，並且沒有關掉連線
                    else{
                        if(mConsumerService.closeConnection() == false){
                            updateTextView("Disconnected");
                            Toast.makeText(getApplicationContext(), R.string.ConnectionAlreadyDisconnected, Toast.LENGTH_LONG).show();
                            mMessageAdapter.clear();
                            Log.e("","Disonnect?");
                        }
                    }
                }
                break;
            }
            default:
        }
    }
    // (SAAgentV2) Accessory Initialize success or not
    private SAAgentV2.RequestAgentCallback mAgentCallback1 = new SAAgentV2.RequestAgentCallback() {
        @Override
        public void onAgentAvailable(SAAgentV2 saAgentV2) {
            mConsumerService = (ConsumerService) saAgentV2;
            Log.e(TAG,"Accessory Initialize success");
        }

        @Override
        public void onError(int errorCode, String massage) {
            Log.e(TAG, "Agent initialization error: " + errorCode + ". ErrorMsg: " + massage);
        }
    };
    //Initialize
    private void InitializeAccessory(){
        mMessageListView = (ListView) findViewById(R.id.lvMessage);
        mMessageAdapter = new Main_page.MessageAdapter();
        mMessageListView.setAdapter(mMessageAdapter);
        buttonConnect = ((ToggleButton) findViewById(R.id.buttonConnect));
        mTextView = (TextView) findViewById(R.id.tvStatus);
        updateTextView("Disconnected");
        SAAgentV2.requestAgent(getApplicationContext(), ConsumerService.class.getName(), mAgentCallback1);
    }
    //Destroy Accesssory
    @Override
    protected void onDestroy() {
        // Clean up connections
        //unregisterReceiver(receiver);
        destroyAccessory();
        super.onDestroy();
    }
    private void destroyAccessory() {
        if (mConsumerService != null) {
            if (!mConsumerService.closeConnection()) {
                updateTextView("Disconnected");
                mMessageAdapter.clear();
            }
            mConsumerService.releaseAgent();
            mConsumerService = null;
        }
    }
    // addMessage: add Msg to viewList
    public static void addMessage(String data){
        mMessageAdapter.addMessage(new Main_page.Message(data));
        Log.e("data: ", data);
    }
    //輔助ConsumerService
    public static void updateToggleButton(boolean enable) {
        buttonConnect.setChecked(enable);
    }
    public static void updateTextView(final String str) {
        mTextView.setText(str);
    }
    // Message Adapter
    private class MessageAdapter extends BaseAdapter {
        private static final int MAX_MESSAGES_TO_DISPLAY = 20;
        private List<Main_page.Message> mMessages;//mailbox??

        public MessageAdapter() {
            mMessages = Collections.synchronizedList(new ArrayList<Main_page.Message>());
        }

        void addMessage(final Main_page.Message msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mMessages.size() == MAX_MESSAGES_TO_DISPLAY) {
                        mMessages.remove(0);
                        mMessages.add(msg);
                        //eggShow.setText(msg.data);//++ //Received: 15:47:57:636 -> Heart Rate: -3
                    } else {
                        mMessages.add(msg);//msg: com.samsung.android.sdk.accessory.example.consumer.AccessoryActivity$Message@4392b9a//msg: ...@c6e6766
                        //eggShow.setText(msg.data);//++//資料量超過手機頁面大小時才有顯示新增的最後一筆資料
                    }
                    notifyDataSetChanged();
                    mMessageListView.setSelection(getCount() - 1);//add to the last one of viewList
                }
            });
        }

        void clear() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMessages.clear();
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getCount() {
            return mMessages.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View messageRecordView = null;
            if (inflator != null) {
                messageRecordView = inflator.inflate(R.layout.message, null);
                TextView tvData = (TextView) messageRecordView.findViewById(R.id.tvData);
                Main_page.Message message = (Main_page.Message) getItem(position);
                tvData.setText(message.data);
            }
            return messageRecordView;
        }
    }

    private static final class Message {
        String data;

        public Message(String data) {
            super();
            this.data = data;//Received: 15:47:57:636 -> Heart Rate: -3
            String a;
        }
    }
    //Watch_End-------------------------------------------------------------------------------------------------------------------------
    //Google Maps Start-----------------------------------------------------------------------------------------------------------------
    /**
     * Saves the state of the map when the activity is paused.
     */
    // [START maps_current_place_on_save_instance_state]
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }
    // [END maps_current_place_on_save_instance_state]

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    /**Get Place會跳出可選擇地點的頁面*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("My: ","onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    // [START maps_current_place_on_options_item_selected]
    /** 點選Item會跳入此function */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("My: ","點選Item: onOptionsItemSelected");
        if (item.getItemId() == R.id.option_get_place) {
            Log.e("Now: ", String.valueOf(R.id.option_get_place));
            Log.e("Now: ", "Call showCurrentPlace()");
            showCurrentPlace();
        }
        else if(item.getItemId() == R.id.Restart) {
            //Google Calendar & Easy Card
            try {
                verifyStoragePermissions(this);
                main();
                read();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            //推薦
            recommend_item();
        }
        return true;
    }
    // [END maps_current_place_on_options_item_selected]

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    // [START maps_current_place_on_map_ready]
    /** 1. 最一開始
     因為呼叫OnMapReadyCallback，並覆寫OnMapReady功能 */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        Log.e("My:","In Map Ready");
        // [START_EXCLUDE]
        // [START map_current_place_set_info_window_adapter]
        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });
        // [END map_current_place_set_info_window_adapter]

        // Prompt the user for permission.
        getLocationPermission();
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        // onOptionsItemSelected();
    }
    // [END maps_current_place_on_map_ready]

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    // [START maps_current_place_get_device_location]
    // Geolocation API
    private void getDeviceLocation() {
        Log.e("My:","In getDeviceLocation()");
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Log.e("","Geolocation: ");
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                Log.e("緯度", String.valueOf(lastKnownLocation.getLatitude()));
                                Log.e("經度", String.valueOf(lastKnownLocation.getLongitude()));
                            }
                        } else {
                            Log.d(TAG1, "Current location is null. Using defaults.");
                            Log.e(TAG1, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    // [END maps_current_place_get_device_location]

    /**
     * Prompts the user for permission to use the device location.
     */
    // [START maps_current_place_location_permission]
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            // getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    // [END maps_current_place_location_permission]
    /**
     * Handles the result of the request for location permissions.
     */
    // [START maps_current_place_on_request_permissions_result]
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        getDeviceLocation();
    }
    // [END maps_current_place_on_request_permissions_result]

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    // [START maps_current_place_show_current_place]
    /** 找到經緯度附近的地標 */
    private void showCurrentPlace() {
        Log.e("My","In showCurrentPlace");
        if (map == null) {
            return;
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.TYPES);
            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<FindCurrentPlaceResponse> placeResult = placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();
                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        likelyPlaceNames = new String[count];
                        likelyPlaceAddresses = new String[count];
                        likelyPlaceAttributions = new List[count];
                        likelyPlaceLatLngs = new LatLng[count];
                        likelyPlaceTypes = new List[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            likelyPlaceAttributions[i] = placeLikelihood.getPlace().getAttributions();
                            likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
                            likelyPlaceTypes[i] = placeLikelihood.getPlace().getTypes();
                            Log.e("Name",placeLikelihood.getPlace().getName());
                            Log.e("Address",placeLikelihood.getPlace().getAddress());
                            //Log.e("Attributions", String.valueOf(placeLikelihood.getPlace().getAttributions()));
                            Log.e("LatLngs", String.valueOf(placeLikelihood.getPlace().getLatLng()));
                            Log.e("Types", String.valueOf(placeLikelihood.getPlace().getTypes()));
                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        Main_page.this.openPlacesDialog();
                    }
                    else {
                        Log.e(TAG1, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG1, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            map.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }
    // [END maps_current_place_show_current_place]

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    // [START maps_current_place_open_places_dialog]
    private void openPlacesDialog() {
        Log.e("My","In openPlacesDialog");
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = likelyPlaceLatLngs[which];
                String markerSnippet = likelyPlaceAddresses[which];
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
                }
                Log.e("Final",likelyPlaceNames[which]);
                Log.e("Final",likelyPlaceAddresses[which]);
                Log.e("Final", String.valueOf(likelyPlaceLatLngs[which]));
                Log.e("Final", String.valueOf(likelyPlaceTypes[which]));
                // Add a marker for the selected place, with an info window
                // showing information about that place.
                map.addMarker(new MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(likelyPlaceNames, listener)
                .show();
    }
    // [END maps_current_place_open_places_dialog]

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    // [START maps_current_place_update_location_ui]
    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        Log.e("My","In updateLocationUI");
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    // [END maps_current_place_update_location_ui]
    //Google Maps End--------------------------------------------------------------------------------------------------------------
    //Easy Card Start--------------------------------------------------------------------------------------------------------------
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
    //Easy Card End--------------------------------------------------------------------------------------------------------------
    //Google Calendar Start--------------------------------------------------------------------------------------------------------------
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
    //Google Calendar End--------------------------------------------------------------------------------------------------------------

}