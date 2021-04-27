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
    private EditText name;
    private EditText colorway;
    private EditText price;
    private EditText condition;
    private Button add;


    private String URL;
    private Date date;
    private String sName;
    private String sColorway;
    private Double sPrice;
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

    public void startProfile(){
        Intent i = new Intent(this, ViewProfile.class);
        makeToast(message);
        i.putExtra("user", user);
        startActivity(i);
    }

    Runnable toUI = new Runnable() {
        @Override
        public void run() {
            if (isGood = true) {
                startProfile();
            }
        }
    };

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
                String shoeCountQuery = ("SELECT * FROM sneakers;");
                Log.e("NumberFormatException", "Here");
                PreparedStatement cQ = con.prepareStatement(shoeCountQuery);
                Log.e("NumberFormatException", "Here");
                ResultSet shoes = cQ.executeQuery();
                Log.e("NumberFormatException", "Here");
                Thread.sleep(1000);
                while(shoes.next()){
                    int i = shoes.getInt("idSneakers");
                    u.add(i);
                }
                sID = u.size()+1;
                Log.e("NumberFormatException", "Here");
                //u = new ArrayList<Integer>();

                String insertUserQ = "INSERT INTO sneakers " +
                        "(idSneakers, sneakerName, colorway, price, condition, idUser) VALUES (?,?,?,?,?,?)";
                PreparedStatement addShoe = con.prepareStatement(insertUserQ);
                addShoe.setInt(1, sID);
                addShoe.setString(2, sName);
                addShoe.setString(3, sColorway);
                addShoe.setDouble(4, sPrice);
                addShoe.setString(5, sCondition);
                addShoe.setInt(6, uID);
                addShoe.executeUpdate();
                message = "Added " + sName + " to the system";
                Log.e("New Shoe", "Added Shoe " + sName + " to the system.");
                isGood = true;
                handler.post(toUI);
                //Log.e("JDBC", "Ran query");
            } catch (SQLException e) {
                message = "Could not add the shoe";
                Log.e("JDBC", message);
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
        sPrice = Double.parseDouble(price.getText().toString());
        sCondition = condition.getText().toString();
        uID = user.getIdUser();

        t = new Thread(addShoe);
        t.start();
    }
}
