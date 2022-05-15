package com.example.dogwalk.Backend.Database;

import static com.google.android.gms.tasks.Tasks.await;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dogwalk.Backend.Objects.DogObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class FireBaseCmd {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void AddDog(DogObject dog){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Map<String, Object> dogMap = new HashMap<>();
        assert currentUser != null;
        dogMap.put("name", dog.getName());
        dogMap.put("breed", dog.getBreed());
        dogMap.put("age", dog.getAge());
        dogMap.put("id", dog.getId());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(Objects.requireNonNull(currentUser.getEmail())).collection("Dogs").document(dog.getId())
                .set(dogMap).addOnSuccessListener(aVoid -> {});//Exception
    }

    public void ChangeDog(DogObject dog){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Map<String, Object> dogMap = new HashMap<>();
        assert currentUser != null;
        dogMap.put("name", dog.getName());
        dogMap.put("breed", dog.getBreed());
        dogMap.put("age", dog.getAge());
        dogMap.put("id", dog.getId());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(Objects.requireNonNull(currentUser.getEmail())).collection("Dogs").document(dog.getId())
                .update(dogMap).addOnSuccessListener(aVoid -> {});//Exception
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
        DogObject dog = new DogObject(obj.get("name").toString(),obj.get("age").toString(),
                obj.get("breed").toString(),obj.get("id").toString());
        list.add(dog);
    }
    public void WriteList(List<DogObject> dogs){
        Log.d(" Dogs to return ", dogs.toString());
    }
}