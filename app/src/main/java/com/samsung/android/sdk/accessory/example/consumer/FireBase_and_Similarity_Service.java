package com.samsung.android.sdk.accessory.example.consumer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class FireBase_and_Similarity_Service extends Activity {

    EditText Et1, Et2;
    Button  Btn;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_similarity);

        //give the reference to the ui elements
        Et1 = (EditText)findViewById(R.id.et1);
        Et2 = (EditText)findViewById(R.id.et2);
        Btn = (Button)findViewById(R.id.btn);
        tv = (TextView)findViewById(R.id.text_view);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("new_item");
        String  target = "coffee";
        DatabaseReference myRef = database.getReference(target);


        FirebaseStorage storage = FirebaseStorage.getInstance();;
        //StorageReference storageRef = storage.getReference();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://my-project-184b3.appspot.com/user1_test");
        //StorageReference questionnaire_Ref = storageRef.child("gs://my-project-184b3.appspot.com/user1_test");
        System.out.println("storage is ok.");

        //UploadTask uploadTask = questionnaire_Ref.putFile(Uri.fromFile(問卷.txt.file()));



        InputStream stream = null;
        try {
            System.out.println("stream");
            //stream = new FileInputStream(new File("C:/Users/88693/AndroidStudioProjects/my_RS/app/src/main/user_data/問卷.txt"));

            stream = new FileInputStream(String.valueOf(getAssets().open("問卷.txt")));

            //stream = new FileInputStream(問卷.txt);
            UploadTask uploadTask = storageRef.putStream(stream);
            System.out.println("stream is ok.");
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.e("問卷.txt", "unsuccessful upload...");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Log.v("問卷.txt", "successful upload!!");
                }
            });

        //} catch (FileNotFoundException e) {
        //    e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //myRef.setValue("health");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Log.d("TAG", "Value is: " + value);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.v(target, snapshot.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

/*
        try {
            Process process
                    = Runtime.getRuntime().exec("where java");

            StringBuilder output = new StringBuilder();

            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println(
                        "**************************** The Output is ******************************");
                System.out.println(output);
                System.exit(0);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
/*
        //execute python code in android studio
        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));

        Python py = Python.getInstance();
        final PyObject pyobj = py.getModule("similarity");  //here need to give name of python file



        Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //call function(function name, parameter1, parameter2)
                PyObject obj = pyobj.callAttr("main", Et1.getText().toString(), Et2.getText().toString());

                //set result to textview
                tv.setText(obj.toString());
                Log.v("similarity is " , obj.toString());
            }
        });

*/

        /*
        System.out.println("into csv block");
        try {
            System.out.println("1already into csv block1");
            CSVReader reader = new CSVReader(new FileReader("similarity_value.csv"));
            System.out.println("2already into csv block2");
            String[] nextLine;
            int i = 0;
            // reader.readNext()會讀取你csv一列的內容並轉換成字串陣列，例如
            // column1, column2, "column
            // 3"
            // 就會被拆成3個字串。這邊就看你想怎麼使用
            while ((nextLine = reader.readNext()) != null) {
                System.out.println("Reader read file content- Line " + i + ": " + nextLine[0] + "," + nextLine[1] + "etc...");
                i++;
            }
        } catch (IOException e) {
            // reader在初始化時可能遭遇問題。記得使用try/catch處理例外情形。
            e.printStackTrace();
        }
*/


        InputStreamReader the_csv = null;
        try {
            the_csv = new InputStreamReader(getAssets().open("similarity_value.csv"));
            String tag = null;
            String item = null;
            String similarity_val  = null;
            String max_val = null;
            String line;
            String max_item = null;
            float a;
            float max = 0;

            BufferedReader reader = new BufferedReader(the_csv);
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                //印出每個row
                Log.d("line -->",line);

                //將item1,item2,similarity分開
                String[] splitted = line.split(",");
                for (int i = 0; i < splitted.length; i++){
                    System.out.println(splitted[i]);
                }

                tag = splitted[0];
                item = splitted[1];
                similarity_val = splitted[2];
                a = Float.parseFloat(similarity_val);

                if(tag.equalsIgnoreCase("Insomnia")){
                    if (a > max){
                        max = a;
                        max_item = item;
                    }
                }
            }
            System.out.println("The highest similarity is : " + max_item + ". Value is : " + max);


        } catch (IOException e) {
            e.printStackTrace();
        }


        Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String target_1 = Et1.getText().toString();
                String target_2 = Et2.getText().toString();
                String item_1 = null;
                String item_2 = null;
                String similarity_val  = null;

                InputStreamReader is = null;
                System.out.println("into csv block");
                try {
                    is = new InputStreamReader(getAssets().open("similarity_value.csv"));
                    BufferedReader reader = new BufferedReader(is);
                    //讀取每行(title)
                    reader.readLine();
                    String line;
                    System.out.println("already into csv block");
                    while ((line = reader.readLine()) != null) {
                        //印出每個row
                        Log.d("line -->",line);

                        //將item1,item2,similarity分開
                        String[] splitted = line.split(",");
                        for (int i = 0; i < splitted.length; i++){
                            System.out.println(splitted[i]);
                        }

                        item_1 = splitted[0];
                        item_2 = splitted[1];
                        similarity_val = splitted[2];

                        if(item_1.equalsIgnoreCase(target_1) && item_2.equalsIgnoreCase(target_2) ||
                                item_2.equalsIgnoreCase(target_1) && item_1.equalsIgnoreCase(target_2)){
                            break;
                        }

                    }
                    //顯示在app上
                    tv.setText("The similarity is :" +similarity_val);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });




    }
}