package com.example.sneakerroom;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_shoe_layout);

        shoeName = (EditText) findViewById(R.id.SearchShoe);
        searchShoebtn = (Button) findViewById(R.id.DoSearchButton);
        shoeList = (ListView)findViewById(R.id.listShoe);

        searchShoebtn.setOnClickListener(this);
    }

    Runnable findShoe = new Runnable() {
        public void run() {
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/sneakeroom";
            String username = "harry";
            String password = "harry";

            try { //load driver into VM memory
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Log.e("JDBC", "Did not load driver");

            }

            Statement stmt = null;

            //Note try with resources block
            try  //create connection to database
                    (Connection con = DriverManager.getConnection(
                            URL,
                            username,
                            password)) {

                String shoeQuery =("SELECT * FROM sneaker WHERE sneakerName LIKE ?");
                PreparedStatement updateShoes = con.prepareStatement(shoeQuery);
                updateShoes.setString(1, shoeN);
                ResultSet shoeResult = updateShoes.executeQuery();

                //Puts the data from the DB in the List
                //Here have to set the list to the parameters from the DB
                //Have to use a handler/something to send a message to the activity
                int count = 0;
                int sneakerID;
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

                        //Add variable for the blob
                        returnList.add(finalOut);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };
    // Need to have handler message that will send a message with the shoe information
    // Activity will set the list to the shoe information

    @Override
    //Search the DB for the shoe name
    public void onClick(View view) {
        findShoe.run();
        adapt = new ArrayAdapter<String>(this, R.layout.item, returnList);
        shoeList.setAdapter(adapt);
    }

    //Click listener for items in the shoe List
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

    }
}
