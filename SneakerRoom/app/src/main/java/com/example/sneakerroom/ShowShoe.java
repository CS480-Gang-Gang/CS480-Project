package com.example.sneakerroom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowShoe extends AppCompatActivity implements View.OnClickListener {
    private TextView name;
    private TextView price;
    private TextView colorway;
    private TextView condition;

    private TextView userTitle;
    private TextView user;
    private Button maps;
    private Button sms;
    private Button dial;

    private TextView userN;
    private Connection con;
    private String URL;
    private int userID;
    private User u;
    Thread t = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_shoe_layout);

        name = (TextView)findViewById(R.id.sneaker_name);
        price = (TextView)findViewById(R.id.sneaker_price);
        colorway = (TextView)findViewById(R.id.sneaker_colorway);
        condition = (TextView)findViewById(R.id.sneaker_condition);

        userTitle = (TextView)findViewById(R.id.textViewUser);
        user = (TextView)findViewById(R.id.shoe_user_name);
        maps = (Button)findViewById(R.id.maps);
        maps.setOnClickListener(this);
        sms = (Button)findViewById(R.id.sms);
        sms.setOnClickListener(this);
        dial = (Button)findViewById(R.id.dial);
        dial.setOnClickListener(this);

        Shoes shoe = (Shoes)getIntent().getSerializableExtra("shoe");
        Bundle bundle = getIntent().getExtras();
        boolean bool = bundle.getBoolean("bool");

        name.setText(shoe.getSneakerName());
        price.setText(shoe.getPrice() + "");
        colorway.setText(shoe.getColorway());
        condition.setText(shoe.getCondition());
        userID = shoe.getUserID();

        if (bool == false) {
            userTitle.setVisibility(View.INVISIBLE);
            user.setVisibility(View.INVISIBLE);
            maps.setVisibility(View.INVISIBLE);
            sms.setVisibility(View.INVISIBLE);
            dial.setVisibility(View.INVISIBLE);
        } else {
            t = new Thread(findUser);
            t.start();
        }
    }

    Handler handler = new Handler(Looper.getMainLooper());

    public void makeConnection() {
        URL = "jdbc:mysql://frodo.bentley.edu:3306/sneakerroom";
        String username = "harry";
        String password = "harry";

        try { //load driver into VM memory
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Log.e("JDBC", "Did not load driver");
            finish();
        }

        try {
            con = DriverManager.getConnection(
                    URL,
                    username,
                    password);
        } catch (SQLException e) {
            Log.e("JDBC", "Could not make connection" + e.getMessage());
        }
    }

    private Runnable toUI = new Runnable() {
        public void run() {
            user.setText(u.getUserName());
        }
    };

    private Runnable findUser = new Runnable() {
        public void run() {
            makeConnection();

            try {
                String userCheckQuery = ("SELECT * FROM user WHERE idUser = ?;");
                PreparedStatement findUser = con.prepareStatement(userCheckQuery);
                findUser.setInt(1, userID);
                ResultSet userResult = findUser.executeQuery();

                int uID = userResult.getInt("idUser");
                String fName = userResult.getString("firstName");
                String lName = userResult.getString("lastName");
                String addy = userResult.getString("address");
                String uName = userResult.getString("address");
                String pass = userResult.getString("address");
                String city = userResult.getString("city");
                String state = userResult.getString("state");
                String zip = userResult.getString("zip");
                String phone = userResult.getString("phonenum");
                u = new User(uID, fName, lName, addy, uName, pass, city, state, zip, phone);
                con.close();
            } catch (SQLException e){

            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sms:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "12125551212");
                smsIntent.putExtra("sms_body","Body of Message");
                startActivity(smsIntent);
                break;

            //implicit intent, call GoogleMaps
            case R.id.maps:
                Uri uri2 = Uri.parse("geo:0,0?q=175+forest+street+waltham+ma");
                Intent i2 = new Intent(Intent.ACTION_VIEW,uri2);
                startActivity(i2);
                break;


            //implicit intent, open dialer and make call
            case R.id.dial:
                Uri uri3 = Uri.parse("tel:7818912000");
                Intent i3 = new Intent(Intent.ACTION_CALL,uri3);
                startActivity(i3);
                break;
        }
    }
}
