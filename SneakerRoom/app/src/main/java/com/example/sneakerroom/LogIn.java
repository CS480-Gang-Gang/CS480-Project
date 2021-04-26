package com.example.sneakerroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LogIn extends AppCompatActivity implements View.OnClickListener{
    private EditText usernameE;
    private EditText passwordE;

    private Button loginB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        usernameE = (EditText) findViewById(R.id.username);
        passwordE = (EditText) findViewById(R.id.password);

        loginB = (Button) findViewById(R.id.update_button);
        loginB.setOnClickListener((View.OnClickListener) this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_button:
                //Start a try catch to check for errors on click
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

                    ResultSet account = null;
                    String updateAccount = ("Select * from user WHERE username = ?, password = ?");
                    newAcc = con.prepareStatement(updateAccount);
                    newAcc.setString(1, String.valueOf(usernameE));
                    newAcc.setString(2, String.valueOf(passwordE));

                    account = newAcc.executeQuery();


                    //clean up
                    Thread t = null;

                } catch (SQLException e) {
                    Log.e("JDBC","problems with SQL sent to "+URL);
                }


                break;

            case R.id.create_account_button:
                //Intent to place a call
                Intent i3 = new Intent(this, CreateAccount.class);
                startActivity(i3);
                break;
        }
    }
}
