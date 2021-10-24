package com.samsung.android.sdk.accessory.example.consumer;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.app.Activity;


public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void mOnClickMain(View v) {
        switch (v.getId()) {
            // Watch
            case R.id.physiological_data: {
                //Intent: 從MainActivity切換過去ActivityConsumer的class
                Intent intent = new Intent(this, ActivityConsumer.class);
                startActivity(intent);
            }
            break;
            // Google Map
            case R.id.googlemap: {
                Intent intent = new Intent(this, MapsActivityCurrentPlace.class);
                startActivity(intent);
            }
            break;
            // Easy card
            case R.id.easy_wallet: {
                Intent intent = new Intent(this, Transaction.class);
                startActivity(intent);
            }
            break;
            // Similarity
            case R.id.similarity: {
                Intent intent = new Intent(this, FireBase_and_Similarity_Service.class);
                startActivity(intent);
            }
            break;
        }
    }
}
