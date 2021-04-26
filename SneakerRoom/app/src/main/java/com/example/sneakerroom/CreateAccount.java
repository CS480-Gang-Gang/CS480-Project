package com.example.sneakerroom;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {
    private EditText usernameN;
    private EditText passwordN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_login);

        usernameN = (EditText) findViewById(R.id.username);
        passwordN = (EditText) findViewById(R.id.password);

        Button newAccountB = (Button) findViewById(R.id.update_button);
        newAccountB.setOnClickListener((View.OnClickListener) this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.create_button:
                //Start a try catch to check for errors on click
                String URL = "jdbc:mysql://frodo.bentley.edu:3306/test";
                String username = "harry";
                String password = "harry";

                try { //load driver into VM memory
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    Log.e("JDBC", "Did not load driver");

                }

                PreparedStatement newAcc = null;
                Statement stmt = null;

                try ( //create connection using try with resources
                      Connection con = DriverManager.getConnection (
                              URL,
                              username,
                              password)) {
                    stmt = con.createStatement();


                    Random numRandom = new Random();
                    int newID = 0;
                    int i = 0;
                    while (i == 0){
                        newID = numRandom.nextInt(1000);
                        i = 1;
                    }

                    String updateAccount = ("insert into accountinfo (?, ?, ?)");
                    newAcc = con.prepareStatement(updateAccount);
                    newAcc.setString(1, String.valueOf(newID));
                    newAcc.setString(2, String.valueOf(usernameN));
                    newAcc.setString(3, String.valueOf(passwordN));

                    newAcc.executeQuery();


                    //clean up
                    Thread t = null;

                } catch (SQLException e) {
                    Log.e("JDBC","problems with SQL sent to "+URL);
                }


                break;
        }
    }
}
