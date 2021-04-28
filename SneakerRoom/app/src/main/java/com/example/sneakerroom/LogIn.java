package com.example.sneakerroom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;

public class LogIn extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener{
    private EditText usernameE;
    private EditText passwordE;
    private Button loginB;
    private Button createB;
    private String uN;
    private String pass;
    private String message;
    private ImageView img;
    private TextToSpeech speaker;
    private static final String tag = "";

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

        img = (ImageView) findViewById(R.id.simple_anim);
        //set ImageView background to AnimationDrawable XML resource.
        img.setBackgroundResource(R.drawable.sneakerroom_animation);

        //instantiate inner classes
        AnimationStart start = new AnimationStart();
        AnimationStop stop = new AnimationStop();

        Timer t = new Timer();
        t.schedule(start, 1000);
        //Timer t2 = new Timer();
        //t2.schedule(stop, 9950);

        speaker = new TextToSpeech(this, this);

    }

    public void speak(String output){
        speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null, "Id 0");
    }

    //text to speech initializer
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // If a language is not be available, the result will indicate it.
            int result = speaker.setLanguage(Locale.US);

            //int result = speaker.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language data is missing or the language is not supported.
                Log.e(tag, "Language is not available.");
            } else {
                // The TTS engine has been successfully initialized
                speak("Please enter task");
                Log.i(tag, "TTS Initialization successful.");
            }
        } else {
            // Initialization failed.
            Log.e(tag, "Could not initialize TextToSpeech.");
        }
    }

    class AnimationStart extends TimerTask {

        @Override
        public void run() {
            // Get the background, which has been compiled to an AnimationDrawable object.
            AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
            frameAnimation.start();
        }
    }

    class AnimationStop extends TimerTask {

        @Override
        public void run() {
            // Get the background, which has been compiled to an AnimationDrawable object.
            AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
            frameAnimation.stop();
        }
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

    //posts to UI and logs in if criteria is accepted
    Runnable toUI = new Runnable() {
        @Override
        public void run() {
            makeToast(message);
            if (isGood == true) {
                speak(message);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                speaker.stop();
                startDash();
                finish();
            }
        }
    };

    //runnable to find the account if username and password entered
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
                        con.close();
                    } else {
                        message = "Password Not Recognized, try again";
                        handler.post(toUI);
                        con.close();
                    }
                } else {
                    message = "No User Name Found";
                    handler.post(toUI);
                    con.close();
                }


            } catch (SQLException e) {
                Log.e("JDBC","problems with SQL sent to "+URL);
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //button to login
            case R.id.login_button:
                uN = usernameE.getText().toString();
                pass = passwordE.getText().toString();
                t = new Thread(findAcc);
                t.start();
                break;
            //button that starts create account activity
            case R.id.create_account_button:
                Intent i = new Intent(this, CreateAccount.class);
                startActivity(i);
                break;
        }
    }

    public void onDestroy(){

        // shut down TTS engine
        if(speaker != null){
            speaker.stop();
            speaker.shutdown();
        }
        super.onDestroy();
    }
}
