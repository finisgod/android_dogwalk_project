package com.example.dogwalk.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dogwalk.Adapters.DogAdapter;
import com.example.dogwalk.MainMenu;
import com.example.dogwalk.Backend.Objects.DogObject;
import com.example.dogwalk.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainMenuFragment extends Fragment {

    public static MainMenuFragment  newInstance() {
        return new MainMenuFragment();
    }
    public ListView list;
    
    public Button update;
    public List<DogObject> dogs = new ArrayList<>();
    public DogAdapter adapter;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.main_menu, container, false);
        list = root.findViewById(R.id.dogList);
        //update = root.findViewById(R.id.updateButton);
        adapter = new DogAdapter(this.getContext(), dogs);
        list.setAdapter(adapter);
        list.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
        list.setClickable(false);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainMenu.pauseThread = true;
                DogObject selectedDog = (DogObject)adapterView.getItemAtPosition(i);
                Toast toast = Toast.makeText(adapterView.getContext(),
                        "Name:"+selectedDog.getName().toString()+
                        " Age:"+selectedDog.getAge().toString()+
                        " Breed:"+selectedDog.getBreed().toString(),Toast.LENGTH_SHORT );
                toast.show();

                ChangeDogFragment fragment = new ChangeDogFragment();
                fragment.name = selectedDog.getName();
                fragment.age = selectedDog.getAge();
                fragment.breed = selectedDog.getBreed();
                fragment.id = selectedDog.getId();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.FragmentActivity, fragment);
                fragmentTransaction.commit();
            }
        });
        adapter.notifyDataSetChanged();


        return root;
    }

}

