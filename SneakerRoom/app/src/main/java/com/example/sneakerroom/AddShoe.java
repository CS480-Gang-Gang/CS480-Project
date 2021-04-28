package com.example.sneakerroom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddShoe extends AppCompatActivity implements View.OnClickListener {
    //Instantiate Variables used in activity
    private EditText name;
    private EditText colorway;
    private EditText price;
    private EditText condition;
    private Button add;


    private String URL;
    private Date date;
    private String sName;
    private String sColorway;
    private int sPrice;
    private String sCondition;
    private String message;
    private User user;
    private int uID;
    private int sID;
    private boolean isGood = false;
    Thread t = null;

    private ArrayList<Integer> u = new ArrayList<Integer>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_shoe_layout);

        //Set variables to layout widgets
        user = (User)getIntent().getSerializableExtra("user");
        name = (EditText)findViewById(R.id.shoename);
        colorway = (EditText)findViewById(R.id.colorway);
        price = (EditText)findViewById(R.id.price);
        condition = (EditText)findViewById(R.id.condition);

        add = (Button)findViewById(R.id.create_sneaker);
        add.setOnClickListener(this);
    }

    Handler handler = new Handler(Looper.getMainLooper());

    public void makeConnection() {

    }

    public void makeToast(String message){
        Toast.makeText( this, message, Toast.LENGTH_LONG).show();
    }

    //starts viewprofile activity after shoe is added
    public void startProfile(){
        Intent i = new Intent(this, ViewProfile.class);
        makeToast(message);
        i.putExtra("user", user);
        startActivity(i);
    }

    //posts to the UI
    Runnable toUI = new Runnable() {
        @Override
        public void run() {
            if (isGood = true) {
                startProfile();
                finish();
            }
        }
    };

    //Adds the shoe to the db and creates a unique id
    Runnable addShoe = new Runnable() {
        @Override
        public void run() {
            //makeConnection();
            URL = "jdbc:mysql://frodo.bentley.edu:3306/sneakerroom";
            String username = "harry";
            String password = "harry";

            try { //load driver into VM memory
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Log.e("JDBC", "Did not load driver");
                finish();
            }

            Connection con = null;
            try {
                con = DriverManager.getConnection(
                        URL,
                        username,
                        password);
            } catch (SQLException e) {
                Log.e("JDBC", "Could not make connection" + e.getMessage());
            }

            try {
                String shoeCountQuery = ("SELECT * FROM sneaker;");

                PreparedStatement cQ = con.prepareStatement(shoeCountQuery);

                ResultSet shoes = cQ.executeQuery();

                Thread.sleep(1000);
                while(shoes.next()){
                    int i = shoes.getInt("idsneaker");
                    u.add(i);
                }
                sID = u.size()+1;

                u = new ArrayList<Integer>();


                String insertUserQ = "INSERT INTO sneaker " +
                        "(idsneaker, sneakername, colorway, price, cond, userid) VALUES (?,?,?,?,?,?)";
                Log.e("Yeet", "Here");
                PreparedStatement addShoe = con.prepareStatement(insertUserQ);
                Log.e("Yeet", "Here");
                addShoe.setInt(1, sID);
                addShoe.setString(2, sName);
                addShoe.setString(3, sColorway);
                addShoe.setInt(4, sPrice);
                addShoe.setString(5, sCondition);
                addShoe.setInt(6, uID);

                //Add shoe to database
                addShoe.executeUpdate();
                message = "Added " + sName + " to the system";
                Log.e("New Shoe", "Added Shoe " + sName + " to the system.");

                isGood = true;
                handler.post(toUI);
                //Log.e("JDBC", "Ran query");
            } catch (SQLException e) {
                message = "Could not add the shoe ";
                Log.e("JDBC", message + e.getMessage());
            } catch (NumberFormatException e) {
                message = "Price is not the right format";
                Log.e("NumberFormatException", message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View v) {
        sName = name.getText().toString();
        sColorway = colorway.getText().toString();
        sPrice = Integer.parseInt(price.getText().toString());
        sCondition = condition.getText().toString();
        uID = user.getIdUser();
        Log.e("Yes", sName + "");
        Log.e("Yes", sColorway + "");
        Log.e("Yes", sPrice + "");
        Log.e("Yes", sCondition + "");
        Log.e("Yes", uID + "");



        t = new Thread(addShoe);
        t.start();
    }
}
