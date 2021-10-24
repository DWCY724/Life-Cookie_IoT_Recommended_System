package com.samsung.android.sdk.accessory.example.consumer;

import static java.lang.Thread.sleep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends Activity {
    EditText account, password;
    Button login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        final int[] flag = {0};
        final String[] url_1 = new String[1];
        final String[] url_2 = new String[1];

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://user-data-password-account-name.firebaseio.com/");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference Ref = database.getReference(account.getText().toString());
                Ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        url_1[0] = snapshot.getValue().toString();
                        System.out.println("URL1:" + url_1[0]);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("gmail/Account", "There is no data.");
                    }
                });

                DatabaseReference Ref_2 = database.getReference(password.getText().toString());
                Ref_2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        url_2[0] = snapshot.getValue().toString();
                        System.out.println("URL2:" + url_2[0]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("gmail/Account", "There is no data.");
                    }
                });


                if(url_1[0] == url_2[0]){
                    flag[0]=1;
                    System.out.println("flag is 1   " + url_1[0]+"  " +url_2[0]);
                }

                /*
                if(url_1[0].equals(url_2[0]) && (url_2[0]!= null && url_1[0]!= null)){
                    flag[0] = 1;
                }
                else{
                    flag[0] = 0;
                }

                 */

                System.out.println("url_1:"+ url_1[0]);
                System.out.println("url_2:"+ url_2[0]);
                System.out.println("flag:" +flag[0]);

                if(flag[0] == 1){
                    //flag[0] = 0;
                    //url_2[0]= null;
                    //url_1[0]= null;
                    //change the page into main_page
                    Intent intent = new Intent();
                    intent.setClass(Login.this, Main_page.class);
                    startActivity(intent);
                }

            }
        });



    }
}
