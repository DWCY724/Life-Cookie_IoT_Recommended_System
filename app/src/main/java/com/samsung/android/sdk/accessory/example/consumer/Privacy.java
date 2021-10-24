package com.samsung.android.sdk.accessory.example.consumer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class Privacy extends Activity {
    Button next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        next = (Button) findViewById(R.id.next_page1);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change the page into main_page
                Intent intent = new Intent();
                intent.setClass(Privacy.this, Register.class);
                startActivity(intent);
            }
        });
    }
}
