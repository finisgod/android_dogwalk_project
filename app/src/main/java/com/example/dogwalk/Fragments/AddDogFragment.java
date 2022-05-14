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

public class AddDogFragment extends Fragment {

    public static AddDogFragment newInstance() {
        return new AddDogFragment();
    }
    public EditText name;
    public EditText breed;
    public EditText age;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_dog_fragment, container, false);
        name = root.findViewById(R.id.nameAdd);
        breed = root.findViewById(R.id.breedAdd);
        age = root.findViewById(R.id.ageAdd);
        return root;
    }

}
