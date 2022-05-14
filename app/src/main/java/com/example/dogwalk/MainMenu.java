package com.example.dogwalk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dogwalk.Backend.Database.FireBaseCmd;
import com.example.dogwalk.Backend.Objects.DogObject;
import com.example.dogwalk.Fragments.AddDogFragment;
import com.example.dogwalk.Fragments.MainMenuFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenu extends FragmentActivity {
    List<Map<String, Object>> dogs = new ArrayList<>();
    FireBaseCmd cmd = new FireBaseCmd();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dogs = new ArrayList<>();
        cmd.GetAllDogs(dogs);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentActivity, new MainMenuFragment());
        fragmentTransaction.commit();

    }

    public void AddDogClick(View view)
    {
        Fragment fragment = new AddDogFragment();
        //Toast toast = Toast.makeText(MainMenu.this , "Неверный Логин или Пароль!" , Toast.LENGTH_SHORT );
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentActivity, fragment);
        fragmentTransaction.commit();
        //toast.show();
    }

    public void LogoutClick(View view)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
        {
            final Intent intent = new Intent(MainMenu.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void CommitDogClick(View view)
    {
        AddDogFragment nowObj = (AddDogFragment)getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);

        Fragment fragment = new MainMenuFragment();
        if(nowObj!=null) {
            if((nowObj.name.getText().toString().length()>0)
                    &&(nowObj.breed.getText().toString().length()>0)
                    &&(nowObj.age.getText().toString().length()>0)) {
                DogObject newDog = new DogObject(nowObj.name.getText().toString()
                        ,nowObj.age.getText().toString(),nowObj.breed.getText().toString());

                FireBaseCmd cmd = new FireBaseCmd();
                cmd.AddDog(newDog);

                Toast toast = Toast.makeText(MainMenu.this, "added", Toast.LENGTH_SHORT);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.FragmentActivity, fragment);
                fragmentTransaction.commit();
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(MainMenu.this, "nullable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}