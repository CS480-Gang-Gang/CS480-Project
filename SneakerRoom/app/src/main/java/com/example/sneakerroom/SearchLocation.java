package com.example.sneakerroom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

public class SearchLocation extends AppCompatActivity implements  View.OnClickListener {

    //Google Maps Activity Cloud -- need the API
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_location_layout);
    }

    @Override
    public void onClick(View v) {

    }
}
