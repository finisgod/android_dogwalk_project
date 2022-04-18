package com.example.dogwalk;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dogwalk.Backend.Database.FireBaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("SetTextI18n")
    public void onClickLogin(View view) {
        EditText email_field = findViewById(R.id.editText);
        EditText password_field = findViewById(R.id.editText2);
        String email = email_field.getText().toString();
        String password = password_field.getText().toString();

        FireBaseAuth User = new FireBaseAuth(email,password);
        User.Login();

        TextView Status = findViewById(R.id.textView2);
        Status.setText("Status : " + User.Status);
    }

    @SuppressLint("SetTextI18n")
    public void onClickRegister(View view) {
        EditText email_field = findViewById(R.id.editText);
        EditText password_field = findViewById(R.id.editText2);
        String email = email_field.getText().toString();
        String password = password_field.getText().toString();

        FireBaseAuth User = new FireBaseAuth(email,password);
        User.Register();

        TextView Status = findViewById(R.id.textView2);
        Status.setText("Status : " + User.Status);
    }

}