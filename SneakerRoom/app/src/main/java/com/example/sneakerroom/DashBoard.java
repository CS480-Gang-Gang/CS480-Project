package com.example.sneakerroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {

    private Button viewProfileButton;
    private Button searchProfileButton;
    private Button searchShoeButton;
    private Button searchLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        viewProfileButton = (Button) findViewById(R.id.view_profile_B);
        searchProfileButton = (Button) findViewById(R.id.search_profile_B);
        searchShoeButton = (Button) findViewById(R.id.search_shoe_B);
        searchLocationButton = (Button) findViewById(R.id.search_location_B);

        viewProfileButton.setOnClickListener(this);
        searchProfileButton.setOnClickListener(this);
        searchShoeButton.setOnClickListener(this);
        searchLocationButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.view_profile_B:
                //Start a try catch to check for errors on click
                Intent i1 = new Intent(this, ViewProfile.class);
                startActivity(i1);
                break;

            case R.id.search_profile_B:
                //Intent to pull up new class
                Intent i2 = new Intent(this, SearchProfile.class);
                startActivity(i2);
                break;

            case R.id.search_shoe_B:
                //Intent to place a call
                Intent i3 = new Intent(this, SearchShoe.class);
                startActivity(i3);
                break;

            case R.id.search_location_B:
                //Intent to open Map showing Bentley University
                Intent i4 = new Intent(this, SearchLocation.class);
                startActivity(i4);
                break;
        }
    }
}