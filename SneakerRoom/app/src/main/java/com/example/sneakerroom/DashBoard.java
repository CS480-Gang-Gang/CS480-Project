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
    private Button searchShoeButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        viewProfileButton = (Button) findViewById(R.id.view_profile_B);
        searchShoeButton = (Button) findViewById(R.id.search_shoe_B);

        viewProfileButton.setOnClickListener(this);
        searchShoeButton.setOnClickListener(this);

        user = (User)getIntent().getSerializableExtra("user");

    }

    //Dashboard buttons
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.view_profile_B:
                //opens the view profile activity
                Intent i1 = new Intent(this, ViewProfile.class);
                i1.putExtra("user", user);
                startActivity(i1);
                break;

            case R.id.search_shoe_B:
                //opens search shoe activity
                Intent i3 = new Intent(this, SearchShoe.class);
                i3.putExtra("user", user);
                startActivity(i3);
                break;
        }
    }
}