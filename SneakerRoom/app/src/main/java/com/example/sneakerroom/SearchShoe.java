package com.example.sneakerroom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchShoe extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, Serializable {
    //Instantiate Variables used in activity
    private EditText shoeName;
    private Button searchShoebtn;
    private ListView shoeList;
    private ArrayAdapter<Shoes> adapt;
    private String shoeN = null;
    ArrayList<Shoes> returnList = new ArrayList<Shoes>();
    private Thread t;
    private User user;

    final int DashboardI = Menu.FIRST ;
    final int Exit = Menu.FIRST + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        user = (User)getIntent().getSerializableExtra("user");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_shoe_layout);

        //Set variables to layout widgets
        shoeName = (EditText) findViewById(R.id.SearchShoe);
        searchShoebtn = (Button) findViewById(R.id.DoSearchButton);
        searchShoebtn.setOnClickListener(this);

        shoeList = (ListView)findViewById(R.id.listShoe);
        adapt = new ShoesAdapter(this, returnList);
        shoeList.setAdapter(adapt);
        shoeList.setOnItemClickListener(this);
    }

    Handler handler = new Handler(Looper.getMainLooper());

    //thread to find the shoe based off of sneakername
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


            //created prepared statement based off the sneakername
            try {
                String shoeQuery = ("SELECT * FROM sneaker WHERE sneakername LIKE ?;");
                PreparedStatement updateShoes = con.prepareStatement(shoeQuery);
                updateShoes.setString(1, shoeN);
                ResultSet shoeResult = updateShoes.executeQuery();
                Log.e("JDBC", "Ran query");

                Shoes shoes = null;
                int shoeid;
                String sneakerName;
                String colorway;
                int price;
                String condition;
                int userid;


                while (shoeResult.next()) {
                    shoeid = shoeResult.getInt("idsneaker");
                    sneakerName = shoeResult.getString("sneakername").toUpperCase();
                    colorway = shoeResult.getString("colorway").toUpperCase();
                    price = shoeResult.getInt("price");
                    condition = shoeResult.getString("cond").toUpperCase();
                    userid = shoeResult.getInt("userid");
                    shoes = new Shoes(shoeid, sneakerName, colorway, price, condition, userid);
                    returnList.add(shoes);

                    Log.e("JDBC", "Found shoe" + " " + sneakerName);
                    //Add variable for the blob
                    handler.post(toUI);
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
        for (int i = 0; i < returnList.size(); i++) {
            returnList.remove(i);
        }
        shoeN = "%" + shoeName.getText().toString() + "%";
        t = new Thread(findShoe);
        t.start();
    }

    //posts the information to the UI
    private Runnable toUI = new Runnable() {
        public void run() {
            adapt.notifyDataSetChanged();
            Log.e("Post", "Updated");
        }
    };

    //Click listener for items in the shoe List

    //Starts showshoe when an item in the listview is clicked
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Shoes shoe = returnList.get(position);
        Intent i = new Intent(this, ShowShoe.class);
        i.putExtra("shoe", shoe);
        i.putExtra("bool", true);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item1 = menu.add(0, DashboardI, Menu.NONE, "Dashboard");
        MenuItem item2 = menu.add(0, Exit, Menu.NONE, "View Profile");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemID = item.getItemId();  //get id of menu item picked

        switch (itemID) {
            case DashboardI :
                Intent i = new Intent(this, DashBoard.class);
                i.putExtra("user", user);
                startActivity(i);

            case Exit :
                Intent i2 = new Intent(this, ViewProfile.class);
                i2.putExtra("user", user);
                startActivity(i2);

            default: super.onOptionsItemSelected(item);
        }

        return false;
    }
}
