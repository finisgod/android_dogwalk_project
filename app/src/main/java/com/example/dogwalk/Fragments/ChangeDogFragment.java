package com.example.dogwalk.Fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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
    public Uri uri;
    public String id;
    public EditText nameText;
    public EditText ageText;
    public EditText breedText;

    public TextView foodCounterLabel;
    public TextView walkCounterLabel;

    public int foodCounter;
    public int walkCounter;

    private Uri filePath;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference= storage.getReference();

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.change_dog_fragment, container, false);
        MainMenu.newDogImg = false;
        ImageView imageView=root.findViewById(R.id.dogImage);

        if(uri!=null) {
            Glide.with(root).load(uri).placeholder(R.drawable.love).diskCacheStrategy(DiskCacheStrategy.ALL).apply(RequestOptions.circleCropTransform()).into(imageView);
        }
        else{
            Glide.with(root).load(R.drawable.love).placeholder(R.drawable.love).diskCacheStrategy(DiskCacheStrategy.ALL).apply(RequestOptions.circleCropTransform()).into(imageView);
        }

        nameText = root.findViewById(R.id.nameAdd);
        nameText.setText(name);


        foodCounterLabel = root.findViewById(R.id.foodCounterLabel);
        foodCounterLabel.setText(Integer.toString(foodCounter));

        walkCounterLabel = root.findViewById(R.id.walkCounterLabel);
        walkCounterLabel.setText(Integer.toString(walkCounter));


        ageText = root.findViewById(R.id.ageAdd);
        ageText.setText(age);

        breedText = root.findViewById(R.id.breedAdd);
        breedText.setText(breed);

        return root;
    }

}
