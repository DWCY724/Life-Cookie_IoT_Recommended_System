package com.samsung.android.sdk.accessory.example.consumer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends Activity {
    EditText account, password, username;
    Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.username);
        register = (Button) findViewById(R.id.button14);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upload to firebase
                String url = "https://recommendedsystemuser1-62c23-a64d6.firebaseio.com/";
                String url_2 = "https://user-data-password-account-name.firebaseio.com/";
                String user_account = account.getText().toString();
                String user_password = password.getText().toString();
                String user_name = username.getText().toString();

                FirebaseDatabase database_account_password = FirebaseDatabase.getInstance(url_2);
                DatabaseReference Ref = database_account_password.getReference(user_name);
                Ref.setValue(url);
                Ref = database_account_password.getReference(user_password);
                Ref.setValue(url);

                FirebaseDatabase database = FirebaseDatabase.getInstance(url);
                DatabaseReference myRef = database.getReference("Account");
                myRef.setValue(user_account);
                myRef = database.getReference("Password");
                myRef.setValue(user_password);
                myRef = database.getReference("Username");
                myRef.setValue(user_name);








                //change the page into main_page
                Intent intent = new Intent();
                intent.setClass(Register.this, Survey1.class);
                startActivity(intent);
            }
        });


    }
}
