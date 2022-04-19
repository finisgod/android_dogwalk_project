package com.example.dogwalk.Backend.Database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

public class FireBaseAuth {

    public FireBaseAuth(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String email = "";
    private String password = "";

    public void Login() {
        if (email.equals("") && password.equals("")) {
            //Exception
        } else if (email.equals("")) {
            //Exception
        } else if (password.equals("")) {
            //Exception
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                //Exception
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Exception
                }
            });
        }
    }

    public void Register() {
        if (email.equals("") && password.equals("")) {
            //Exception
        } else if (email.equals("")) {
            //Exception
        } else if (password.equals("")) {
            //Exception
        } else if (password.length() < 6) {
            //Exception
        } else if (email.length() < 6) {
            //Exception
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //Exception
                            //Cloud
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            Map<String, Object> Usid = new HashMap<>();
                            assert currentUser != null;
                            Usid.put("id", currentUser.getUid());
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("Users").document(Objects.requireNonNull(currentUser.getEmail()))
                                    .set(Usid).addOnSuccessListener(aVoid -> {});//Exception
                            //
                        }
                        else {
                                //Exception
                             }
                    });
        }
    }
}