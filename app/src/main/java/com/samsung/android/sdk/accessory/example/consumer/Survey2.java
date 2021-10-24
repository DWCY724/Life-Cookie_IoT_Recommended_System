package com.samsung.android.sdk.accessory.example.consumer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.view.View;
import android.widget.Toast;



import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Survey2 extends Activity {
    Spinner sport1, sport2, sport3;
    Button next;
    String[] sports = {"Please Choose","Volleyball","Baseball","Jogging","Fitness","Swimming","Football","Badminton","Yoga","Cardio"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_2);

        sport1 = (Spinner) findViewById(R.id.sport1);
        sport2 = (Spinner) findViewById(R.id.sport2);
        sport3 = (Spinner) findViewById(R.id.sport3);

        next = (Button) findViewById(R.id.next_page1);

        ArrayAdapter<String> sport_list = new ArrayAdapter<>(Survey2.this, android.R.layout.simple_spinner_dropdown_item,sports);
        sport1.setAdapter(sport_list);
        sport2.setAdapter(sport_list);
        sport3.setAdapter(sport_list);

        //設定項目被選取之後的動作

        sport1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                Toast.makeText(Survey2.this, "你選的是"+sport1.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override

            public void onNothingSelected(AdapterView arg0) {

                Toast.makeText(Survey2.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();

            }

        });
        sport2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                Toast.makeText(Survey2.this, "你選的是"+sport1.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override

            public void onNothingSelected(AdapterView arg0) {

                Toast.makeText(Survey2.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();

            }

        });
        sport3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                Toast.makeText(Survey2.this, "你選的是"+sport1.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override

            public void onNothingSelected(AdapterView arg0) {

                Toast.makeText(Survey2.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();

            }

        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://recommendedsystemuser1-62c23-a64d6.firebaseio.com/");
                DatabaseReference myRef = database.getReference("Favorite sport first");
                myRef.setValue(sport1.getSelectedItem().toString());
                myRef = database.getReference("Favorite sport second");
                myRef.setValue(sport2.getSelectedItem().toString());
                myRef = database.getReference("Favorite sport third");
                myRef.setValue(sport3.getSelectedItem().toString());

                //change the page into main_page
                Intent intent = new Intent();
                intent.setClass(Survey2.this, Front_page.class);
                startActivity(intent);
            }
        });
    }

}