package com.example.dogwalk.Adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dogwalk.Backend.Objects.DogObject;
import com.example.dogwalk.R;

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

    // кол-во элементов
    @Override
    public int getCount() {
        return dogs.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return dogs.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.dog_item, parent, false);
        }
        DogObject dog = getDog(position);
        ((TextView) view.findViewById(R.id.dogName)).setText(dog.getName());
        ((TextView) view.findViewById(R.id.dogAge)).setText(dog.getAge());
        ((TextView) view.findViewById(R.id.dogBreed)).setText(dog.getBreed());
        return view;
    }

    // товар по позиции
    DogObject getDog(int position) {
        return ((DogObject) getItem(position));
    }
}