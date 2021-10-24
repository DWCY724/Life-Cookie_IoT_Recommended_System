package com.samsung.android.sdk.accessory.example.consumer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class person extends Activity {
    TextView username, gmail, gender, occupation, exercising_time, sleep_interval_A, exercise_first, exercise_second, exercise_third, heart_rate, sleep_interval_R, update_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        username = (TextView) findViewById(R.id.username);
        gmail = (TextView) findViewById(R.id.GMAIL);
        gender = (TextView) findViewById(R.id.gender);
        occupation = (TextView) findViewById(R.id.work);
        exercising_time = (TextView) findViewById(R.id.exercise_time);
        sleep_interval_A = (TextView) findViewById(R.id.sleep_interval);
        exercise_first = (TextView) findViewById(R.id.Exercise1);
        exercise_second = (TextView) findViewById(R.id.Exercise2);
        exercise_third = (TextView) findViewById(R.id.Exercise3);
        heart_rate = (TextView) findViewById(R.id.Heart_rate);
        sleep_interval_R = (TextView) findViewById(R.id.Sleep_interval);
        update_time = (TextView) findViewById(R.id.time);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://recommendedsystemuser1-62c23-a64d6.firebaseio.com/");

        DatabaseReference username_Ref = database.getReference("Username");
        username_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = snapshot.getValue().toString();
                System.out.println("val_username:"+val);
                username.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("username", "There is no data.");
            }
        });

        DatabaseReference gmail_Ref = database.getReference("Account");
        gmail_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = snapshot.getValue().toString();
                gmail.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("gmail/Account", "There is no data.");
            }
        });

        DatabaseReference gender_Ref = database.getReference("Gender");
        gender_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = snapshot.getValue().toString();
                gender.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("gender", "There is no data.");
            }
        });

        DatabaseReference occupation_Ref = database.getReference("occupation");
        occupation_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = snapshot.getValue().toString();
                occupation.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("occupation", "There is no data.");
            }
        });

        DatabaseReference exercise_time_Ref = database.getReference("Average exercising per day");
        exercise_time_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = snapshot.getValue().toString();
                exercising_time.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("exercise_time", "There is no data.");
            }
        });

        DatabaseReference sleep_interval_A_Ref = database.getReference("Average sleep per day");
        sleep_interval_A_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = snapshot.getValue().toString();
                sleep_interval_A.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("sleep_interval_A", "There is no data.");
            }
        });

        DatabaseReference exercise_first_Ref = database.getReference("Favorite sport first");
        exercise_first_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = snapshot.getValue().toString();
                exercise_first.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Favorite sport first:", "There is no data.");
            }
        });

        DatabaseReference exercise_second_Ref = database.getReference("Favorite sport second");
        exercise_second_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = snapshot.getValue().toString();
                exercise_second.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Favorite sport second:", "There is no data.");
            }
        });

        DatabaseReference exercise_third_Ref = database.getReference("Favorite sport third");
        exercise_third_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = snapshot.getValue().toString();
                exercise_third.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Favorite sport third:", "There is no data.");
            }
        });

        DatabaseReference heart_rate_Ref = database.getReference("HRM");
        heart_rate_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = snapshot.getValue().toString();
                heart_rate.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HRM:", "There is no data.");
            }
        });

        DatabaseReference sleep_interval_R_Ref = database.getReference("SleepInterval");
        sleep_interval_R_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = snapshot.getValue().toString();
                sleep_interval_R.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SleepInterval:", "There is no data.");
            }
        });

        getTime1();
    }

    void getTime1(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String t=format.format(new Date());
        Log.e("msg", t);
        update_time.setText(t);

    }
}
