package com.samsung.android.sdk.accessory.example.consumer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class Front_page extends Activity {
    Button login, register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);

        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change the page into main_page
                Intent intent = new Intent();
                intent.setClass(Front_page.this, Privacy.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change the page into main_page
                Intent intent = new Intent();
                intent.setClass(Front_page.this, Login.class);
                startActivity(intent);
            }
        });


    }


    /*
    public void mOnClickMain(View v) {
        switch (v.getId()) {
            // Watch
            case R.id.login: {
                //Intent: 從MainActivity切換過去ActivityConsumer的class
                Intent intent = new Intent(this, Main_page.class);
                startActivity(intent);
            }
            break;
            // Google Map
            case R.id.register: {
                Intent intent = new Intent(this, Register.class);
                startActivity(intent);
            }
            break;
        }
    }

     */

}
