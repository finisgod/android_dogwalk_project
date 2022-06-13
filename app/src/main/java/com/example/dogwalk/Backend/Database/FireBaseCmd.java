package com.example.dogwalk.Backend.Database;

import static com.google.android.gms.tasks.Tasks.await;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.dogwalk.Backend.Objects.DogObject;
import com.example.dogwalk.Fragments.ChangeDogFragment;
import com.example.dogwalk.MainMenu;
import com.example.dogwalk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class FireBaseCmd {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void AddDog(DogObject dog){
        Date currentTime = Calendar.getInstance().getTime();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Map<String, Object> dogMap = new HashMap<>();
        assert currentUser != null;
        dogMap.put("name", dog.getName());
        dogMap.put("breed", dog.getBreed());
        dogMap.put("age", dog.getAge());
        dogMap.put("id", dog.getId());
        dogMap.put("food", Integer.toString(0));
        dogMap.put("walk", Integer.toString(0));
        dogMap.put("date", currentTime.toString());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(Objects.requireNonNull(currentUser.getEmail())).collection("Dogs").document(dog.getId())
                .set(dogMap).addOnSuccessListener(aVoid -> {});//Exception
    }

    public void ChangeDog(DogObject dog){
        Date currentTime = Calendar.getInstance().getTime();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Map<String, Object> dogMap = new HashMap<>();
        assert currentUser != null;
        dogMap.put("name", dog.getName());
        dogMap.put("breed", dog.getBreed());
        dogMap.put("age", dog.getAge());
        dogMap.put("id", dog.getId());
        dogMap.put("food", dog.getFoodCounter());
        dogMap.put("walk", dog.getWalkCounter());
        dogMap.put("date", currentTime.toString());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(Objects.requireNonNull(currentUser.getEmail())).collection("Dogs").document(dog.getId())
                .update(dogMap).addOnSuccessListener(aVoid -> {});//Exception
    }
    public void DeleteDog(String id){

        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(Objects.requireNonNull(currentUser.getEmail())).collection("Dogs").document(id)
                .delete().addOnSuccessListener(aVoid -> {});//Exception
    }

    public void GetAllDogs(List<DogObject> dogs){
        try {

            FirebaseUser currentUser = mAuth.getCurrentUser();
            assert currentUser != null;
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(Objects.requireNonNull(currentUser.getEmail())).collection("Dogs")
                    .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //List<Map<String, Object>> dogsToReturn = new ArrayList<>();
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        UpdateList(dogs, document.getData());
                    }
                    WriteList(dogs);
                } else {
                    Log.w("", "Error getting documents.", task.getException());
                }
            });
        }
        catch(Exception ignored){
        }
    }

    public void UpdateList(List<DogObject> list ,Map<String, Object> obj){
        boolean update = true;
        Date currentTime = Calendar.getInstance().getTime();
        String[] timeString = currentTime.toString().split(" ");

        DogObject dog = new DogObject(obj.get("name").toString(),obj.get("age").toString(),
                obj.get("breed").toString(),obj.get("id").toString());

        //Ежедневное обнуление прогулок/кормежки
        if(!timeString[2].equals(obj.get("date").toString().split(" ")[2])||(!timeString[1].equals(obj.get("date").toString().split(" ")[1]))){
            Log.d("Change time ",  timeString[2] + " / " + obj.get("date").toString().split(" ")[2]);
            Log.d("Change date ",  timeString[1] + " / " + obj.get("date").toString().split(" ")[1]);
            dog.setWalkCounter(Integer.parseInt(Integer.toString(0)));
            dog.setFoodCounter(Integer.parseInt(Integer.toString(0)));
            ChangeDog(dog);
        }
        else {
            dog.setWalkCounter(Integer.parseInt(obj.get("walk").toString()));
            dog.setFoodCounter(Integer.parseInt(obj.get("food").toString()));
        }

        for(int i = 0;i < list.size();i++)
            if (list.get(i).getId().equals(dog.getId())) {
                list.get(i).setName(dog.getName());
                list.get(i).setAge(dog.getAge());
                list.get(i).setBreed(dog.getBreed());
                list.get(i).setFoodCounter(dog.getFoodCounter());
                list.get(i).setWalkCounter(dog.getWalkCounter());
                update = false;
            }
        if(update){list.add(dog);}

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference= storage.getReference();
        StorageReference ref = storageReference.child("images/"+ obj.get("id").toString());
        ref.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        for(int i = 0;i < list.size();i++)
                            if (list.get(i).getId().equals(dog.getId())) {
                                dog.setUri(uri);
                                list.get(i).setUri(dog.getUri());
                            }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public void WriteList(List<DogObject> dogs){
        Log.d(" Dogs to return ", dogs.toString());
    }
}