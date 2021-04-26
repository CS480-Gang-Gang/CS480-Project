package com.example.sneakerroom;

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
import java.sql.Statement;
import java.util.Random;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {
    private EditText usernameN;
    private EditText passwordN;
    private EditText passwordRep;
    private EditText firstN;
    private EditText lastN;
    private EditText city;
    private EditText state;
    private EditText zip;
    private EditText phoneNum;

    private boolean passMatch = false;
    private boolean isGood = false;

    private String userN = null;
    private String pass = null;
    private String fN = null;
    private String lN = null;
    private String c = null;
    private String s = null;
    private String z = null;
    private String phone = null;

    Thread t = null;

    private Button checkPass;
    private Button createAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_login);

        usernameN = (EditText) findViewById(R.id.username);
        passwordN = (EditText) findViewById(R.id.password);
        passwordRep = (EditText) findViewById(R.id.passwordrep);
        firstN = (EditText) findViewById(R.id.first_name);
        lastN = (EditText) findViewById(R.id.last_name);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        zip = (EditText) findViewById(R.id.zip);
        phoneNum = (EditText) findViewById(R.id.phone_num);

        checkPass = (Button)findViewById(R.id.check_password);
        createAcc = (Button)findViewById(R.id.create_account_button);

        checkPass.setOnClickListener((View.OnClickListener) this);
        createAcc.setOnClickListener((View.OnClickListener) this);


    }

    public void makeToast(String message){
        Toast.makeText( this, message, Toast.LENGTH_LONG).show();
    }

    Handler handler = new Handler(Looper.getMainLooper());

    Runnable toUI = new Runnable() {
        public void run() {
            if (isGood == false){

            }
        }
    };


    Runnable insertAndAdd = new Runnable() {
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

                String shoeQuery = ("SELECT * FROM user WHERE username LIKE ?;");
                PreparedStatement updateShoes = con.prepareStatement(shoeQuery);
                updateShoes.setString(1, userN);
                ResultSet shoeResult = updateShoes.executeQuery();
                if (shoeResult.next() == true){
                    throw new InterruptedException();
                }
                Log.e("JDBC", "Ran query");
            } catch (SQLException e) {
                Log.e("JDBC", "Could not add the account");
            } catch (InterruptedException e) {
                Log.e("Interrupt", "Account User Name Exists");
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.create_button:
                userN = usernameN.getText().toString();
                pass = passwordN.getText().toString();
                fN = firstN.getText().toString();
                lN = lastN.getText().toString();
                c = city.getText().toString();
                s = state.getText().toString();
                userN = usernameN.getText().toString();
                userN = usernameN.getText().toString();

                t = new Thread(insertAndAdd);
                t.start();
                break;

            case R.id.check_password:
                if((passwordN.getText().toString()).equals(passwordRep.getText().toString())){
                    passMatch = true;
                    Toast.makeText( this, "Passwords Match", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText( this, "Passwords Don't Match", Toast.LENGTH_LONG).show();
                    passwordRep.setText("");
                    passwordN.setText("");
                }
                break;
        }
    }
}
