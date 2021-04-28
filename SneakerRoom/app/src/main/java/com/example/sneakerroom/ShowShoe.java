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
    private TextView userTitleInfo;

    private Button maps;
    private Button sms;
    private Button dial;

    private TextView userN;
    private String URL;
    private int userID;
    private User u;
    private User usermain;
    Thread t = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_shoe_layout);
        usermain = (User)getIntent().getSerializableExtra("user");

        name = (TextView)findViewById(R.id.sneaker_name);
        price = (TextView)findViewById(R.id.sneaker_price);
        colorway = (TextView)findViewById(R.id.sneaker_colorway);
        condition = (TextView)findViewById(R.id.sneaker_condition);
        userTitleInfo = (TextView)findViewById(R.id.user_title_info);
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

        userID = shoe.getUserID();

        name.setText(shoe.getSneakerName());
        price.setText(shoe.getPrice() + "");
        colorway.setText(shoe.getColorway());
        condition.setText(shoe.getCondition());


        if (bool == false) {
            userTitleInfo.setVisibility(View.INVISIBLE);
            userTitle.setVisibility(View.INVISIBLE);
            user.setVisibility(View.INVISIBLE);
            maps.setVisibility(View.INVISIBLE);
            sms.setVisibility(View.INVISIBLE);
            dial.setVisibility(View.INVISIBLE);
        }

        t = new Thread(findUser);
        t.start();

    }

    Handler handler = new Handler(Looper.getMainLooper());


    private Runnable toUI = new Runnable() {
        public void run() {
            user.setText(u.getUserName());
        }
    };

    public void makeToast(String message){
        Toast.makeText( this, message, Toast.LENGTH_LONG).show();
    }

    private Runnable findUser = new Runnable() {
        public void run() {
            Log.e("Start Thread", "Start");
            URL = "jdbc:mysql://frodo.bentley.edu:3306/sneakerroom";
            String username = "harry";
            String password = "harry";

            Log.e("Load Driver", "Load");

            try { //load driver into VM memory
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Log.e("JDBC", "Did not load driver");
                finish();
            }

            Connection con = null;
            Log.e("Create Connection", "Connect");
            try {
                con = DriverManager.getConnection(
                        URL,
                        username,
                        password);
            } catch (SQLException e) {
                Log.e("JDBC", "Could not make connection" + e.getMessage());
            }

            Log.e("Load User", "User");
            try {
                String userCheckQuery = ("SELECT * FROM user WHERE idUser = ?;");
                PreparedStatement findUser = con.prepareStatement(userCheckQuery);
                findUser.setInt(1, userID);
                ResultSet userResult = findUser.executeQuery();
                if (userResult.next() == true) {
                    int uID = userResult.getInt("idUser");
                    String fName = userResult.getString("firstName");
                    String lName = userResult.getString("lastName");
                    String addy = userResult.getString("address");
                    String uName = userResult.getString("username");
                    String pass = userResult.getString("address");
                    String city = userResult.getString("city");
                    String state = userResult.getString("state");
                    String zip = userResult.getString("zip");
                    String phone = userResult.getString("phonenum");
                    u = new User(uID, fName, lName, addy, uName, pass, city, state, zip, phone);
                }
                Log.e("Found User", "Found user name " + u.getUserName());
                handler.post(toUI);
                con.close();
            } catch (SQLException e){
                Log.e("JDBC", "Could not find user " + e.getMessage());
            }
        }
    };

    @Override
    public void onClick(View v) {
        String pNum = u.getPhoneNum();
        switch (v.getId()) {
            case R.id.sms:
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + pNum));
                startActivity(sendIntent);
                break;

            case R.id.maps:
                Uri uri2 = Uri.parse("geo:0,0?q=175+forest+street+waltham+ma");
                Intent i2 = new Intent(Intent.ACTION_VIEW,uri2);
                startActivity(i2);
                break;

            case R.id.dial:
                try{
                    Uri uri3 = Uri.parse("tel:" + pNum);
                    Intent i3 = new Intent(Intent.ACTION_CALL,uri3);
                    startActivity(i3);
                } catch (SecurityException e) {
                    makeToast("Cannot Open Dialer, Go to Settings and Enable Permissions");
                }

                break;
        }
    }
}
