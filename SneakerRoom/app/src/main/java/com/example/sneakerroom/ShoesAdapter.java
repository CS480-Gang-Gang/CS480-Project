package com.example.sneakerroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class ShoesAdapter extends ArrayAdapter {
    ArrayList<Shoes> arrayList;
    Context context;
    public ShoesAdapter(Context context, ArrayList<Shoes> arrayList) {
        super(context, 0, arrayList);
        this.arrayList=arrayList;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Shoes shoes = arrayList.get(position);
        if(convertView==null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.shoe_adapter, null);



        }
        return convertView;
    }
}
