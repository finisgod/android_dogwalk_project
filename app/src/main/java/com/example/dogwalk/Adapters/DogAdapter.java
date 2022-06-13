package com.example.dogwalk.Adapters;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

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

public class DogAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<DogObject> dogs;

    public DogAdapter(Context ctext, List<DogObject> list_dog) {
        context = ctext;
        dogs = list_dog;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dogs.size();
    }

    @Override
    public Object getItem(int position) {
        return dogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference= storage.getReference();

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.dog_item, parent, false);
        }

        DogObject dog = getDog(position);

        if(dog.getFoodCounter()==0||dog.getWalkCounter()==0) {
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.item_no));
        }
        else
        if(dog.getFoodCounter()>1||dog.getWalkCounter()>1) {
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.item_yes));
        }

        ImageView img = view.findViewById(R.id.imageView2);
        ImageView img_food = view.findViewById(R.id.food);
        ImageView img_walk = view.findViewById(R.id.walk);
        if(dog.getFoodCounter()==0){
            Glide.with(view).load(R.drawable.food_no).placeholder(R.drawable.food_no).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_food);
        }
        if(dog.getWalkCounter()==0){
            Glide.with(view).load(R.drawable.walk_no).placeholder(R.drawable.walk_no).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_walk);
        }
        if(dog.getFoodCounter()>1){
            Glide.with(view).load(R.drawable.food_yes).placeholder(R.drawable.food_no).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_food);
        }
        if(dog.getWalkCounter()>1){
            Glide.with(view).load(R.drawable.walk_yes).placeholder(R.drawable.walk_no).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_walk);
        }
        ((TextView) view.findViewById(R.id.dogName)).setText(dog.getName());
        ((TextView) view.findViewById(R.id.walkCount)).setText(Integer.toString(dog.getWalkCounter()));
        ((TextView) view.findViewById(R.id.foodCount)).setText(Integer.toString(dog.getFoodCounter()));
        //((TextView) view.findViewById(R.id.dogBreed)).setText(dog.getBreed());
        if(dog.getUri()!=null) {
            Glide.with(view).load(dog.getUri()).placeholder(R.drawable.love).diskCacheStrategy(DiskCacheStrategy.ALL).apply(RequestOptions.circleCropTransform()).into(img);
        }
        /*
        StorageReference ref = storageReference.child("images/"+ dog.getId());
        View finalView = view;
        ref.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(finalView).load(uri).placeholder(R.drawable.love).diskCacheStrategy(DiskCacheStrategy.ALL).apply(RequestOptions.circleCropTransform()).into(img);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        MainMenu.uploadImage(dog.getId());
                        Glide.with(finalView).load(R.drawable.love).placeholder(R.drawable.love).diskCacheStrategy(DiskCacheStrategy.ALL).apply(RequestOptions.circleCropTransform()).into(img);
                    }
                });
                */

        return view;
    }

    DogObject getDog(int position) {
        return ((DogObject) getItem(position));
    }
}