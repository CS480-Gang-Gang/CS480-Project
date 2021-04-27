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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogIn extends AppCompatActivity implements View.OnClickListener{
    private EditText usernameE;
    private EditText passwordE;
    private Button loginB;
    private Button createB;
    private String uN;
    private String pass;
    private String message;

    private User user;

    Thread t = null;

    private boolean isGood = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        usernameE = (EditText) findViewById(R.id.uName);
        passwordE = (EditText) findViewById(R.id.pass);

        loginB = (Button) findViewById(R.id.login_button);
        loginB.setOnClickListener(this);

        createB = (Button) findViewById(R.id.create_account_button);
        createB.setOnClickListener(this);

    }

    public void makeToast(String message){
        Toast.makeText( this, message, Toast.LENGTH_LONG).show();
    }

    public void startDash(){
        Intent i = new Intent(this, DashBoard.class);
        i.putExtra("user", user);
        startActivity(i);
    }

    Handler handler = new Handler(Looper.getMainLooper());

    Runnable toUI = new Runnable() {
        @Override
        public void run() {
            makeToast(message);
            if (isGood == true) {
                startDash();
                finish();
            }
        }
    };

    Runnable findAcc = new Runnable() {
        @Override
        public void run() {

            String URL = "jdbc:mysql://frodo.bentley.edu:3306/sneakerroom";
            String username = "harry";
            String password = "harry";

            try { //load driver into VM memory
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Log.e("JDBC", "Did not load driver");

            }

            try ( //create connection using try with resources
                  Connection con = DriverManager.getConnection (
                          URL,
                          username,
                          password)) {

                String updateAccount = ("Select * from user WHERE username = ?;");
                PreparedStatement newAcc = con.prepareStatement(updateAccount);
                newAcc.setString(1, uN);
                ResultSet account = newAcc.executeQuery();
                if (account.next() == true) {
                    String p = account.getString("password");
                    if(p.equals(pass)) {
                        isGood = true;
                        message = "Account Logged in";
                        int uID = account.getInt("idUser");
                        String fName = account.getString("firstName");
                        String lName = account.getString("lastName");
                        String addy = account.getString("address");
                        String city = account.getString("city");
                        String state = account.getString("state");
                        String zip = account.getString("zip");
                        String phone = account.getString("phonenum");
                        user = new User(uID, fName, lName, addy, uN, pass, city, state, zip, phone);

                        handler.post(toUI);
                    } else {
                        message = "Password Not Recognized, try again";
                        handler.post(toUI);
                    }
                } else {
                    message = "No User Name Found";
                    handler.post(toUI);
                }


            } catch (SQLException e) {
                Log.e("JDBC","problems with SQL sent to "+URL);
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_button:
                uN = usernameE.getText().toString();
                pass = passwordE.getText().toString();
                t = new Thread(findAcc);
                t.start();
                break;

            case R.id.create_account_button:
                Intent i = new Intent(this, CreateAccount.class);
                startActivity(i);
                break;
        }
    }
}
