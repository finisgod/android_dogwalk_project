package com.example.dogwalk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dogwalk.Backend.Database.FireBaseAuth;
import com.example.dogwalk.Backend.Database.FireBaseCmd;
import com.example.dogwalk.Backend.Objects.DogObject;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    List<Map<String, Object>> dogs = new ArrayList<>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FireBaseCmd cmd = new FireBaseCmd();
        dogs = new ArrayList<>();
        cmd.GetAllDogs(dogs);

    }

    public void onClickLogin(View view) {
        EditText email_field = findViewById(R.id.editText);
        EditText password_field = findViewById(R.id.editText2);
        String email = email_field.getText().toString();
        String password = password_field.getText().toString();

        FireBaseAuth User = new FireBaseAuth(email,password);
        User.Login();

        //Status.setText("Status : " + );
    }

    public void onClickDog(View view)  {
        EditText email_field = findViewById(R.id.editText);
        EditText password_field = findViewById(R.id.editText2);
        String email = email_field.getText().toString();
        String password = password_field.getText().toString();

        Log.d("", dogs.toString());
    }

    public void onClickRegister(View view) {
        EditText email_field = findViewById(R.id.editText);
        EditText password_field = findViewById(R.id.editText2);
        String email = email_field.getText().toString();
        String password = password_field.getText().toString();

        FireBaseAuth User = new FireBaseAuth(email,password);

        User.Register();
        //Status.setText("Status : " + User.Register());
    }

}