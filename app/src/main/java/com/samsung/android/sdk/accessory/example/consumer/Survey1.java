package com.samsung.android.sdk.accessory.example.consumer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Survey1 extends Activity {
    Button next;
    RadioButton boy, girl, work, student, half_hour, half_one_hour, one_hour, six_hours, six_to_eight, eight_hours;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_1);

        next = (Button) findViewById(R.id.next_page1);
        boy = (RadioButton) findViewById(R.id.boy);
        girl = (RadioButton) findViewById(R.id.girl);
        work = (RadioButton) findViewById(R.id.work);
        student = (RadioButton) findViewById(R.id.student);
        half_hour = (RadioButton) findViewById(R.id.half_hour);
        half_one_hour = (RadioButton) findViewById(R.id.half_one_hour);
        one_hour = (RadioButton) findViewById(R.id.one_hour);
        six_hours = (RadioButton) findViewById(R.id.sixhours);
        six_to_eight = (RadioButton) findViewById(R.id.sixtoeight);
        eight_hours = (RadioButton) findViewById(R.id.eighthour);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://recommendedsystemuser1-62c23-a64d6.firebaseio.com/");

                //upload to realtime database
                if (boy.isChecked()){
                    String upload = boy.getText().toString();
                    //upload to firebase
                    DatabaseReference myRef = database.getReference("Gender");
                    myRef.setValue(upload);
                }
                if (girl.isChecked()){
                    String upload = girl.getText().toString();
                    //upload to firebase
                    DatabaseReference myRef = database.getReference("Gender");
                    myRef.setValue(upload);
                }
                if (work.isChecked()){
                    String upload = work.getText().toString();
                    //upload to firebase occupation
                    DatabaseReference myRef = database.getReference("occupation");
                    myRef.setValue(upload);
                }
                if (student.isChecked()){
                    String upload = student.getText().toString();
                    //upload to firebase
                    DatabaseReference myRef = database.getReference("occupation");
                    myRef.setValue(upload);
                }
                if (half_hour.isChecked()){
                    String upload = half_hour.getText().toString();
                    //upload to firebase
                    DatabaseReference myRef = database.getReference("Average exercising per day");
                    myRef.setValue(upload);
                }
                if (half_one_hour.isChecked()){
                    String upload = half_one_hour.getText().toString();
                    //upload to firebase
                    DatabaseReference myRef = database.getReference("Average exercising per day");
                    myRef.setValue(upload);
                }
                if (one_hour.isChecked()){
                    String upload = one_hour.getText().toString();
                    //upload to firebase
                    DatabaseReference myRef = database.getReference("Average exercising per day");
                    myRef.setValue(upload);
                }
                if (six_hours.isChecked()){
                    String upload = six_hours.getText().toString();
                    //upload to firebase
                    DatabaseReference myRef = database.getReference("Average sleep per day");
                    myRef.setValue(upload);
                }
                if (six_to_eight.isChecked()){
                    String upload = six_to_eight.getText().toString();
                    //upload to firebase
                    DatabaseReference myRef = database.getReference("Average sleep per day");
                    myRef.setValue(upload);
                }
                if (eight_hours.isChecked()){
                    String upload = eight_hours.getText().toString();
                    //upload to firebase
                    DatabaseReference myRef = database.getReference("Average sleep per day");
                    myRef.setValue(upload);
                }

                //change the page into main_page
                Intent intent = new Intent();
                intent.setClass(Survey1.this, Survey2.class);
                startActivity(intent);
            }
        });



    }


}
