package com.example.dogwalk.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.dogwalk.Backend.Objects.DogObject;
import com.example.dogwalk.MainMenu;
import com.example.dogwalk.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

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

    private Uri filePath;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference= storage.getReference();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.change_dog_fragment, container, false);

        ImageView imageView=root.findViewById(R.id.dogImage);

        StorageReference ref = storageReference.child("images/"+ id);
        ref.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(root).load(uri).into(imageView);
                        Toast.makeText(root.getContext(), "Downloaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Glide.with(root).load(R.drawable.love).into(imageView);
                        Toast.makeText(root.getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        nameText = root.findViewById(R.id.nameAdd);
        nameText.setText(name);

        ageText = root.findViewById(R.id.ageAdd);
        ageText.setText(age);

        breedText = root.findViewById(R.id.breedAdd);
        breedText.setText(breed);

        return root;
    }

}
