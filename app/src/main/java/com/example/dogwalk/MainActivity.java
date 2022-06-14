package com.example.dogwalk;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dogwalk.Backend.Database.FireBaseAuth;
import com.example.dogwalk.Backend.Database.FireBaseCmd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                "Loading. Please wait...", true);
        if (email.equals("") && password.equals("")) {
            dialog.dismiss();
            email_field.setError("Required E-mail more than 6 symbols");
            password_field.setError("Required password more than 6 symbols");
        } else if (email.equals("")) {
            dialog.dismiss();
            email_field.setError("Required E-mail more than 6 symbols");
        } else if (password.equals("")) {
            dialog.dismiss();
            password_field.setError("Required password more than 6 symbols");
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null)
                {
                    final Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(intent);
                }
                dialog.dismiss();
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(currentUser==null){
                        email_field.setError("Try Another Password or E-mail / Login is failed");
                        password_field.setError("Try Another Password or E-mail / Login is failed");
                    }
                    dialog.dismiss();
                }
            });

        }
    }

    public void onClickRegister(View view) {
        EditText email_field = findViewById(R.id.editText);
        EditText password_field = findViewById(R.id.editText2);
        String email = email_field.getText().toString();
        String password = password_field.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //FireBaseAuth User = new FireBaseAuth(email,password);

        if (email.equals("") && password.equals("")) {
            email_field.setError("Required E-mail more than 6 symbols");
            password_field.setError("Required password more than 6 symbols");
        } else if (email.equals("")) {
            email_field.setError("Required E-mail more than 6 symbols");
        } else if (password.equals("")) {
            password_field.setError("Required password more than 6 symbols");
        } else if (password.length() < 6) {
            password_field.setError("Required password more than 6 symbols");
        } else if (email.length() < 6) {
            email_field.setError("Required E-mail more than 6 symbols");
        } else {
            ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                    "Loading. Please wait...", true);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //Exception
                            //Cloud
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            Map<String, Object> Usid = new HashMap<>();
                            if(currentUser!=null) {
                                Usid.put("id", currentUser.getUid());
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("Users").document(Objects.requireNonNull(currentUser.getEmail()))
                                        .set(Usid).addOnSuccessListener(aVoid -> {
                                            dialog.dismiss();
                                            final Intent intent = new Intent(MainActivity.this, MainMenu.class);
                                            startActivity(intent);
                                        });//Exception
                                //
                            }
                        }
                        else {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if(currentUser==null){
                                email_field.setError("Try Another Email / Registration is failed");
                                password_field.setError("Try Another Email / Registration is failed");
                            }
                            dialog.dismiss();
                        }
                    });
        }
        //Status.setText("Status : " + User.Register());
    }

}