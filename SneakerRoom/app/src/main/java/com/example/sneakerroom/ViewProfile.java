package com.example.sneakerroom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewProfile extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView name;
    private Button add;
    private Button delete;
    private Button edit;
    private ListView shoeList;
    private ArrayAdapter<Shoes> adapt;

    final int SearchShoeI = Menu.FIRST ;
    final int Exit = Menu.FIRST + 1;

    private String shoeN = null;
    ArrayList<Shoes> returnList = new ArrayList<Shoes>();
    private Thread t;
    private User user;
    private int uID;
    private Connection con = null;
    private String URL;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);
        user = (User)getIntent().getSerializableExtra("user");
        name = (TextView)findViewById(R.id.show_name);
        name.setText("Welcome User: " + user.getUserName());
        add = (Button)findViewById(R.id.addbutton);
        delete = (Button)findViewById(R.id.dashbutton);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        shoeList = (ListView) findViewById(R.id.listShoe);
        uID = user.getIdUser();

        adapt = new ShoesAdapter(this, returnList);
        shoeList.setAdapter(adapt);
        shoeList.setOnItemClickListener(this);

        t = new Thread(findShoe);
        t.start();
    }

    // makes the connection
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

    Handler handler = new Handler(Looper.getMainLooper());

    Runnable findShoe = new Runnable() {
        public void run() {
            makeConnection();
            try {

                //Created prepared statement and finds the shoes matching the user's id
                String shoeQuery = ("SELECT * FROM sneaker WHERE userid LIKE ?;");
                PreparedStatement updateShoes = con.prepareStatement(shoeQuery);
                updateShoes.setInt(1, uID);
                ResultSet shoeResult = updateShoes.executeQuery();
                Log.e("JDBC", "Ran query");

                Shoes shoes = null;
                int shoeid;
                String sneakerName;
                String colorway;
                int price;
                String condition;
                int userid;

                //gets shoe info from the results
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        MenuItem item1 = menu.add(0, SearchShoeI, Menu.NONE, "SearchShoe");
        MenuItem item2 = menu.add(0, Exit, Menu.NONE, "Dashboard");


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemID = item.getItemId();  //get id of menu item picked

        switch (itemID) {
            case SearchShoeI :
                Intent i = new Intent(this, SearchShoe.class);
                i.putExtra("user", user);
                startActivity(i);
                return true;

            case Exit :
                Intent i2 = new Intent(this, DashBoard.class);
                i2.putExtra("user", user);
                startActivity(i2);

            default: super.onOptionsItemSelected(item);
        }

        return false;
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            //opens the add shoe activity
            case R.id.addbutton:
                Intent i = new Intent(this, AddShoe.class);
                i.putExtra("user", user);
                startActivity(i);
                break;
                //goes back to the dashboard
            case R.id.dashbutton:
                Intent i2 = new Intent(this, DashBoard.class);
                i2.putExtra("user", user);
                startActivity(i2);
                break;
        }
    }

    //Posts the info to the UI
    private Runnable toUI = new Runnable() {
        public void run() {
            try {
                Thread.sleep(1000);
                adapt.notifyDataSetChanged();
                Log.e("Post", "Updated");
            } catch (InterruptedException e) {

            }

        }
    };


    //Runs showshoe intent when list item is clicked
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Shoes shoe = returnList.get(position);
        Intent i = new Intent(this, ShowShoe.class);
        i.putExtra("shoe", shoe);
        i.putExtra("bool", false);
        startActivity(i);
    }
}
