package com.example.dogwalk.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dogwalk.Backend.Objects.DogObject;
import com.example.dogwalk.R;

public class ChangeDogFragment extends Fragment {

    public static ChangeDogFragment newInstance() {
        return new ChangeDogFragment();
    }
    public String name;
    public String breed;
    public String age;
    public String id;
    public EditText nameText;
    public EditText ageText;
    public EditText breedText;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.change_dog_fragment, container, false);

        nameText = root.findViewById(R.id.nameAdd);
        nameText.setText(name);

        ageText = root.findViewById(R.id.ageAdd);
        ageText.setText(age);

        breedText = root.findViewById(R.id.breedAdd);
        breedText.setText(breed);

        return root;
    }

}
