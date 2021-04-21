package com.example.sneakerroom;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchShoe extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private EditText shoeName;
    private Button searchShoebtn;
    private ListView shoeList;
    private ArrayAdapter<String> adapt;
    private String shoeN = null;
    ArrayList<String> returnList = new ArrayList<String>();
    private Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_shoe_layout);
        shoeName = (EditText) findViewById(R.id.SearchShoe);
        searchShoebtn = (Button) findViewById(R.id.DoSearchButton);
        searchShoebtn.setOnClickListener(this);

        shoeList = (ListView)findViewById(R.id.listShoe);
        adapt = new ArrayAdapter<String>(this, R.layout.item, returnList);
        shoeList.setAdapter(adapt);
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
           returnList.add((String)msg.obj);
           adapt.notifyDataSetChanged();
           Log.e("To List", (String)msg.obj);

        }
    };

    Runnable findShoe = new Runnable() {
        public void run() {
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/sneakerroom";
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
                PreparedStatement updateShoes = null;
                ResultSet shoeResult = null;
                String shoeQuery = ("SELECT * FROM sneakers WHERE sneakerName = 'Jordan 1';");
                updateShoes = con.prepareStatement(shoeQuery);
                //updateShoes.setString(1, shoeN);
                shoeResult = updateShoes.executeQuery();
                Log.e("JDBC", "Ran query");
                int count = 0;
                String sneakerName;
                String colorway;
                double price;
                String condition;
                String finalOut;

                String s = "";
                while (shoeResult.next()) {
                    sneakerName = shoeResult.getString("sneakerName");
                    colorway = shoeResult.getString("colorway");
                    price = shoeResult.getDouble("price");
                    condition = shoeResult.getString("condition");
                    finalOut = sneakerName + " " + colorway + " " + price + " " + condition;
                    Log.e("JDBC", "Found shoe" + " " + sneakerName);
                    //Add variable for the blob
                    Message msg = handler.obtainMessage(count, finalOut);
                    handler.sendMessage(msg);

                }

                con.close();
            } catch (SQLException e) {
                Log.e("JDBC", "problems with SQL sent to " + URL +
                        ": " + e.getMessage());
            }

        }};
    // Need to have handler message that will send a message with the shoe information
    // Activity will set the list to the shoe information

    @Override
    //Search the DB for the shoe name
    public void onClick(View view) {
        t = new Thread(findShoe);
        t.start();

        //adapt = new ArrayAdapter<String>(this, R.layout.item, returnList);
        //shoeList.setAdapter(adapt);
    }

    //Click listener for items in the shoe List
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

    }
}
