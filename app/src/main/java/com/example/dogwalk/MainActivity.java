package com.example.dogwalk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dogwalk.Backend.Database.FireBaseAuth;
import com.example.dogwalk.Backend.Database.FireBaseCmd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public List<Map<String, Object>> dogs = new ArrayList<>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //FireBaseCmd cmd = new FireBaseCmd();
        //dogs = new ArrayList<>();
        //cmd.GetAllDogs(dogs);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null)
        {
            final Intent intent = new Intent(MainActivity.this, MainMenu.class);
            startActivity(intent);
        }
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        //FireBaseCmd cmd = new FireBaseCmd();
        //dogs = new ArrayList<>();
        //cmd.GetAllDogs(dogs);

        super.onResume();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            final Intent intent = new Intent(MainActivity.this, MainMenu.class);
            startActivity(intent);
        }
    }

    public void onClickLogin(View view) {
        EditText email_field = findViewById(R.id.editText);
        EditText password_field = findViewById(R.id.editText2);
        String email = email_field.getText().toString();
        String password = password_field.getText().toString();

        //FireBaseAuth User = new FireBaseAuth(email,password);
        //User.Login();
        
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (email.equals("") && password.equals("")) {
            //Exception
        } else if (email.equals("")) {
            //Exception
        } else if (password.equals("")) {
            //Exception
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null)
                {
                    final Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Exception
                }
            });
        }
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