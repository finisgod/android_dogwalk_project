package com.example.dogwalk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dogwalk.Adapters.DogAdapter;
import com.example.dogwalk.Backend.Database.FireBaseCmd;
import com.example.dogwalk.Backend.Objects.DogObject;
import com.example.dogwalk.Fragments.AddDogFragment;
import com.example.dogwalk.Fragments.ChangeDogFragment;
import com.example.dogwalk.Fragments.MainMenuFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenu extends FragmentActivity {
    public List<DogObject> dogs = new ArrayList<>();
    FireBaseCmd cmd = new FireBaseCmd();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dogs = new ArrayList<DogObject>();
        //cmd.GetAllDogs(dogs);

        for(int i = 0 ; i < 5 ; i++){
            dogs.add(new DogObject("foo" , "23", "dolmatin"," 1 "));
            dogs.add(new DogObject("lada" , "3", "corgi"," 2 "));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainMenuFragment menuFragment = new MainMenuFragment();
        menuFragment.dogs = dogs;
        fragmentTransaction.replace(R.id.FragmentActivity,menuFragment );
        fragmentTransaction.commit();

    }

    public void OnDogItemClick(View view){

    }
    public void UpdateDogList(View view) {
        MainMenuFragment menuFragment = (MainMenuFragment)getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);
        if(menuFragment!=null) {
            Toast toast = Toast.makeText(MainMenu.this , "Updated!" , Toast.LENGTH_SHORT );
            ListView listView = (ListView) menuFragment.list;
            //listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            DogAdapter adapter = new DogAdapter(this, dogs);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            toast.show();
        }
    }

    public void AddDogClick(View view)
    {
        Fragment fragment = new AddDogFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentActivity, fragment);
        fragmentTransaction.commit();
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

    public void ChangeDogClick(View view) {

        ChangeDogFragment nowObj = (ChangeDogFragment)getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);

        Fragment fragment = new MainMenuFragment();
        if(nowObj!=null) {
            if ((nowObj.nameText.getText().toString().length() > 0)
                    && (nowObj.breedText.getText().toString().length() > 0)
                    && (nowObj.ageText.getText().toString().length() > 0)) {
                DogObject newDog = new DogObject(nowObj.nameText.getText().toString()
                        , nowObj.ageText.getText().toString(), nowObj.breedText.getText().toString(),nowObj.id);

                //Добавить работу с базой
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.FragmentActivity, fragment);
                fragmentTransaction.commit();

                Toast toast = Toast.makeText(MainMenu.this , "Changed!"+newDog.getName()
                        + "," + newDog.getAge()
                        + "," + newDog.getBreed() + newDog.getId(), Toast.LENGTH_SHORT );
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(MainMenu.this, "nullable", Toast.LENGTH_SHORT);
            toast.show();
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
                        ,nowObj.age.getText().toString(),nowObj.breed.getText().toString(),"newIdAdd");

                //Добавить классу собаки айди
                FireBaseCmd cmd = new FireBaseCmd();
                cmd.AddDog(newDog);
                dogs.add(newDog);
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