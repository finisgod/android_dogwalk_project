package com.example.dogwalk;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.dogwalk.Adapters.DogAdapter;
import com.example.dogwalk.Backend.Database.FireBaseCmd;
import com.example.dogwalk.Backend.Objects.DogObject;
import com.example.dogwalk.Fragments.AddDogFragment;
import com.example.dogwalk.Fragments.ChangeDogFragment;
import com.example.dogwalk.Fragments.MainMenuFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainMenu extends FragmentActivity {
    public List<DogObject> dogs = new ArrayList<>();
    FireBaseCmd cmd = new FireBaseCmd();

    private Handler mainHandler = new Handler();
    public static boolean pauseThread = true;
    private boolean stopThread = false;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startThread();
        //dogs = new ArrayList<DogObject>();
        //cmd.GetAllDogs(dogs);
        //for(int i = 0 ; i < 5 ; i++){
            //dogs.add(new DogObject("first" , "1", "test"," 1 "));
            //dogs.add(new DogObject("lada" , "3", "corgi"," 2 "));
        //}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainMenuFragment menuFragment = new MainMenuFragment();
        menuFragment.dogs = dogs;
        fragmentTransaction.replace(R.id.FragmentActivity,menuFragment );
        fragmentTransaction.commit();

        pauseThread = false;
    }
    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainMenuFragment menuFragment = new MainMenuFragment();
        menuFragment.dogs = dogs;
        fragmentTransaction.replace(R.id.FragmentActivity,menuFragment );
        fragmentTransaction.commit();

        pauseThread = false;
        //
    }

    private final int Pick_image = 1;

    public static boolean newDogImg = false;
    public void clickAddImg(View view) {

                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, Pick_image);
        //Обрабатываем результат выбора в галерее:
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case Pick_image:
                if(resultCode == RESULT_OK){
                    try {
                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = imageReturnedIntent.getData();
                        filePath = imageUri;
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ImageView img = findViewById(R.id.dogImage);
                        Glide.with(this).load(imageUri).placeholder(R.drawable.love).diskCacheStrategy(DiskCacheStrategy.ALL).apply(RequestOptions.circleCropTransform()).into(img);
                        String id = null;
                        if(!newDogImg){
                            ChangeDogFragment nowObj = (ChangeDogFragment) getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);
                            if (nowObj != null) {
                                id = nowObj.id;
                                if (id != null) {
                                    uploadImage(id);
                                }
                            }
                        }
                        else{
                            AddDogFragment nowObj = (AddDogFragment) getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);
                            if (nowObj != null) {
                                nowObj.uri = imageUri;
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }}
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }
    //Фоновая работа с базой
    public void startThread() {
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }
    class ExampleRunnable implements Runnable {
        ExampleRunnable() {
        }
        @Override
        public void run() {
            for (int i = 0; i > -1; i++) {
                Log.d(TAG, "uiDatabaseThreadRunning: " + isInternetAvailable());
                if (!stopThread) {
                    if (!pauseThread) {
                        int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //dogs.add(new DogObject("foo" , Integer.toString(finalI), "dolmatin"," 1 "));
                                UpdateList();
                            }
                        });
                        Log.d(TAG, "uiDatabaseThreadRunning: " + i);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "uiDatabaseThreadPaused: " + i);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{i=-10;}
            }
        }
    }
    ////


/*
    public void UpdateDogList(View view) {
        cmd.GetAllDogs(dogs);
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

        if(!pauseThread) {
            Toast toast = Toast.makeText(MainMenu.this, "PauseThread!", Toast.LENGTH_SHORT);
            toast.show();
            pauseThread = true;
        }
        else{
            Toast toast = Toast.makeText(MainMenu.this, "ResumeThread!", Toast.LENGTH_SHORT);
            toast.show();
            pauseThread = false;
        }
        stopThread = true;

    }
    */

    public void UpdateList() {
        cmd.GetAllDogs(dogs);
        MainMenuFragment menuFragment = (MainMenuFragment)getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);
        if(menuFragment!=null) {
            //Toast toast = Toast.makeText(MainMenu.this , "Updated!" , Toast.LENGTH_SHORT );
            //ListView listView = (ListView)menuFragment.list;
            //listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
            //DogAdapter adapter = new DogAdapter(this, dogs);
            //listView.setAdapter(adapter);
            menuFragment.adapter.notifyDataSetChanged();
            //toast.show();
        }
    }

    public void SettingsClick(View view) {
        Button addDog = findViewById(R.id.addDogButton);
        Button logOut = findViewById(R.id.logoutButton);
        if(addDog.getVisibility()==View.VISIBLE) {
            addDog.setVisibility(View.INVISIBLE);
            logOut.setVisibility(View.INVISIBLE);
        }
        else{
            addDog.setVisibility(View.VISIBLE);
            logOut.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    public void PlusFoodClick(View view) {
        ChangeDogFragment nowObj = (ChangeDogFragment)getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);
        int foodCounter = nowObj.foodCounter;
        foodCounter++;
        nowObj.foodCounter++;
        TextView label;
        label = nowObj.foodCounterLabel;
        label.setText(Integer.toString(foodCounter));
    }
    @SuppressLint("SetTextI18n")
    public void MinusFoodClick(View view) {
        ChangeDogFragment nowObj = (ChangeDogFragment)getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);
        int foodCounter = nowObj.foodCounter;
        if(foodCounter>0) {
            foodCounter--;
            nowObj.foodCounter--;
            TextView label;
            label = nowObj.foodCounterLabel;
            label.setText(Integer.toString(foodCounter));
        }
        else{
            foodCounter=0;
            nowObj.foodCounter=0;
            TextView label;
            label = nowObj.foodCounterLabel;
            label.setText(Integer.toString(foodCounter));
        }
    }
    @SuppressLint("SetTextI18n")
    public void PlusWalkClick(View view) {
        ChangeDogFragment nowObj = (ChangeDogFragment)getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);
        int walkCounter = nowObj.walkCounter;
        walkCounter++;
        nowObj.walkCounter++;
        TextView label;
        label = nowObj.walkCounterLabel;
        label.setText(Integer.toString(walkCounter));
    }
    @SuppressLint("SetTextI18n")
    public void MinusWalkClick(View view) {
        ChangeDogFragment nowObj = (ChangeDogFragment)getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);
        int walkCounter = nowObj.walkCounter;
        if(walkCounter>0) {
            walkCounter--;
            nowObj.walkCounter--;
            TextView label;
            label = nowObj.walkCounterLabel;
            label.setText(Integer.toString(walkCounter));
        }
        else{
            walkCounter=0;
            nowObj.walkCounter=0;
            TextView label;
            label = nowObj.walkCounterLabel;
            label.setText(Integer.toString(walkCounter));
        }
    }


    public void AddDogClick(View view)
    {
        pauseThread = true;
        Fragment fragment = new AddDogFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentActivity, fragment);
        fragmentTransaction.commit();

    }

    public void LogoutClick(View view)
    {
        stopThread = true;
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
        ProgressDialog dialog = ProgressDialog.show(MainMenu.this, "",
                "Loading. Please wait...", true);

        ChangeDogFragment nowObj = (ChangeDogFragment)getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);
        MainMenuFragment fragment = new MainMenuFragment();
        fragment.dogs = dogs;
        if(nowObj!=null) {
            if ((nowObj.nameText.getText().toString().length() > 0)
                    && (nowObj.breedText.getText().toString().length() > 0)
                    && (nowObj.ageText.getText().toString().length() > 0)) {
                DogObject newDog = new DogObject(nowObj.nameText.getText().toString()
                        , nowObj.ageText.getText().toString(), nowObj.breedText.getText().toString(), nowObj.id);

                newDog.setUri(nowObj.uri);
                newDog.setFoodCounter(nowObj.foodCounter);
                newDog.setWalkCounter(nowObj.walkCounter);

                //Добавить работу с базой
                FireBaseCmd cmd = new FireBaseCmd();
                cmd.ChangeDog(newDog);

                for (int i = 0; i < dogs.size(); i++) {
                    if (dogs.get(i).getId().equals(newDog.getId())) {
                        dogs.set(i, newDog);
                    }
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.FragmentActivity, fragment);
                fragmentTransaction.commit();
                dialog.dismiss();
                pauseThread = false;
            } else {
                dialog.dismiss();
                if (nowObj.nameText.getText().toString().length() == 0)
                    nowObj.nameText.setError("Empty name");
                if (nowObj.ageText.getText().toString().length() == 0)
                    nowObj.ageText.setError("Empty age");
                if (nowObj.breedText.getText().toString().length() == 0)
                    nowObj.breedText.setError("Empty Breed");
            }
        }
    }

    public void DeleteDogClick(View view) {

        new AlertDialog.Builder(MainMenu.this)
                .setTitle("Delete dog")
                .setMessage("Are you sure you want to delete this dog?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ChangeDogFragment nowObj = (ChangeDogFragment)getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);
                        MainMenuFragment fragment = new MainMenuFragment();
                        fragment.dogs = dogs;
                        if(nowObj!=null) {
                            //Добавить работу с базой
                            FireBaseCmd cmd = new FireBaseCmd();
                            cmd.DeleteDog(nowObj.id);

                            for (int i = 0; i < dogs.size(); i++) {
                                if (dogs.get(i).getId().equals(nowObj.id)) {
                                    dogs.remove(i);
                                }
                            }
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.FragmentActivity, fragment);
                            fragmentTransaction.commit();
                            pauseThread = false;
                        }
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void CommitDogClick(View view)
    {
        ProgressDialog dialog = ProgressDialog.show(MainMenu.this, "",
                "Loading. Please wait...", true);
        AddDogFragment nowObj = (AddDogFragment)getSupportFragmentManager().findFragmentById(R.id.FragmentActivity);
        MainMenuFragment fragment = new MainMenuFragment();
        fragment.dogs = dogs;
        if(nowObj!=null) {
            if ((nowObj.name.getText().toString().length() > 0)
                    && (nowObj.breed.getText().toString().length() > 0)
                    && (nowObj.age.getText().toString().length() > 0)) {
                String id = UUID.randomUUID().toString();
                DogObject newDog = new DogObject(nowObj.name.getText().toString()
                        , nowObj.age.getText().toString(), nowObj.breed.getText().toString(), id);

                newDog.setUri(nowObj.uri);
                uploadImage(id);

                //Добавить классу собаки айди
                FireBaseCmd cmd = new FireBaseCmd();
                cmd.AddDog(newDog);
                dogs.add(newDog);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.FragmentActivity, fragment);
                fragmentTransaction.commit();
                dialog.dismiss();
                pauseThread = false;
            } else {
                dialog.dismiss();
                if (nowObj.name.getText().toString().length() == 0)
                    nowObj.name.setError("Empty name");
                if (nowObj.age.getText().toString().length() == 0)
                    nowObj.age.setError("Empty age");
                if (nowObj.breed.getText().toString().length() == 0)
                    nowObj.breed.setError("Empty Breed");
            }
        }

    }

    private static Uri filePath = null;
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static StorageReference storageReference= storage.getReference();

    public static void uploadImage(String id) {

        if(filePath == null) {
            filePath = Uri.parse("android.resource://"+ R.class.getPackage().getName()+"/"+R.drawable.love);
        }
            StorageReference ref = storageReference.child("images/"+ id);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        filePath = null;
    }


}