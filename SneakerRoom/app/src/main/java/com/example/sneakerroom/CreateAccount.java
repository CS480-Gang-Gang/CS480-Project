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
import java.util.ArrayList;

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
    private EditText addy;

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
    private String address = null;

    private ArrayList<Integer> u = new ArrayList<Integer>();

    Thread t = null;

    private Button createAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_login);

        usernameN = (EditText) findViewById(R.id.userName);
        passwordN = (EditText) findViewById(R.id.pass);
        passwordRep = (EditText) findViewById(R.id.passrep);
        firstN = (EditText) findViewById(R.id.first_name);
        lastN = (EditText) findViewById(R.id.last_name);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        zip = (EditText) findViewById(R.id.zip);
        phoneNum = (EditText) findViewById(R.id.phone_num);
        addy= (EditText) findViewById(R.id.address_info);

        //checkPass = (Button)findViewById(R.id.check_password);
        createAcc = (Button)findViewById(R.id.create_acc);
        createAcc.setOnClickListener(this);


    }

    public void makeToast(String message){
        Toast.makeText( this, message, Toast.LENGTH_LONG).show();
    }

    public void startLogIn(){
        Intent i = new Intent(this, LogIn.class);
        startActivity(i);
    }

    Handler handler = new Handler(Looper.getMainLooper());

    Runnable toUI = new Runnable() {
        public void run() {
            if (isGood == false){
                makeToast("Account information not complete");
            } else {
                makeToast("Account Creation Successful");
                startLogIn();
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

                String userCheckQuery = ("SELECT * FROM user WHERE username LIKE ?;");
                PreparedStatement findUser = con.prepareStatement(userCheckQuery);
                findUser.setString(1, userN);
                ResultSet userResult = findUser.executeQuery();

                String userCountQuery = ("SELECT * FROM user;");
                PreparedStatement cQ = con.prepareStatement(userCountQuery);
                ResultSet users = cQ.executeQuery();
                while(users.next()){
                    int i = users.getInt("idUser");
                    u.add(i);
                }
                int uID = u.size() + 1;

                if (userResult.next() == true){
                    throw new InterruptedException();
                } else {
                    String insertUserQ = "INSERT INTO user " +
                            "(idUser, firstName, lastName, address, city, state, zip, username, password, phonenum) VALUES (?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement addUser = con.prepareStatement(insertUserQ);
                    addUser.setInt(1, uID);
                    addUser.setString(2, fN);
                    addUser.setString(3, lN);
                    addUser.setString(4, address);
                    addUser.setString(5, c);
                    addUser.setString(6, s);
                    addUser.setString(7, z);
                    addUser.setString(8, userN);
                    addUser.setString(9, pass);
                    addUser.setString(10, phone);

                    addUser.executeUpdate();
                    Log.e("New User", "Added User " + userN + " to the system.");
                    isGood = true;
                    Thread.sleep(500);
                    handler.post(toUI);
                }

                Log.e("JDBC", "Ran query");
            } catch (SQLException e) {
                Log.e("JDBC", "Could not add the account");
            } catch (InterruptedException e) {
                Log.e("Interrupt", "Account User Name Exists");
            }

        }
    };

    public boolean checkPassMatch () {
        if((passwordN.getText().toString()).equals(passwordRep.getText().toString())){
            passMatch = true;
            Toast.makeText( this, "Passwords Match", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText( this, "Passwords Don't Match", Toast.LENGTH_LONG).show();
            passMatch = false;
            passwordRep.setText("");
            passwordN.setText("");
        }
        return passMatch;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.create_acc:
                if (checkPassMatch() == true){
                    userN = usernameN.getText().toString();
                    pass = passwordN.getText().toString();
                    fN = firstN.getText().toString();
                    lN = lastN.getText().toString();
                    address = addy.getText().toString();
                    c = city.getText().toString();
                    s = state.getText().toString();
                    z = zip.getText().toString();
                    phone = phoneNum.getText().toString();

                    t = new Thread(insertAndAdd);
                    t.start();
                }

                break;
        }
    }
}
