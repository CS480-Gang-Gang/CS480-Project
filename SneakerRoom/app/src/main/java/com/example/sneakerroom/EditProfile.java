package com.example.sneakerroom;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class EditProfile extends AppCompatActivity {

    private TextView enter_f_name;
    private TextView enter_l_name;
    private TextView enter_num_shoes;
    private TextView enter_address;

    private EditText f_name;
    private EditText l_name;
    private EditText num_shoes;
    private EditText address;

    private Button update;

    private Thread t = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        f_name = (EditText) findViewById(R.id.f_name);
        l_name = (EditText) findViewById(R.id.l_name);
        num_shoes = (EditText) findViewById(R.id.num_shoes);
        address = (EditText) findViewById(R.id.address);

        update = (Button) findViewById(R.id.update_button);
        update.setOnClickListener((View.OnClickListener) this);

    }

    public void onClick(View v) {
        try {
            Log.e("JDBC", "On click invoked");

            t = new Thread(background);
            t.start();

            Log.e("JDBC", "On Click Complete");

        } catch (Exception e) {
            Log.e("JDBC", "Failed to Update Profile");
        }

    }

    private Runnable background = new Runnable() {
        public void run(){
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/test";
            String username = "harry";
            String password = "harry";

            try { //load driver into VM memory
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Log.e("JDBC", "Did not load driver");

            }

            PreparedStatement updateAcc = null;
            Statement stmt = null;

            try ( //create connection using try with resources
                  Connection con = DriverManager.getConnection (
                          URL,
                          username,
                          password)) {
                stmt = con.createStatement();

                String updateAccount = ("update user set firstName = ?, lastName = ?, numShoes = ?, address = ? where idUser = ?");
                updateAcc = con.prepareStatement(updateAccount);
                updateAcc.setString(1, String.valueOf(f_name));
                updateAcc.setString(2, String.valueOf(l_name));
                updateAcc.setString(3, String.valueOf(num_shoes));
                updateAcc.setString(4, String.valueOf(address));

                updateAcc.executeQuery();


                //clean up
                t = null;

            } catch (SQLException e) {
                Log.e("JDBC","problems with SQL sent to "+URL);
            }

        }
    };

}