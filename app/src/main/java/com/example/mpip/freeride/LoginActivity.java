package com.example.mpip.freeride;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends Activity implements View.OnClickListener, View.OnKeyListener{

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(i == KeyEvent.KEYCODE_ENTER &&  keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                funkcija();

        }

        return false;
    }

    InputMethodManager inputMethodManager;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {


        if(getCurrentFocus() != null) {
            if (view.getId() == R.id.constrainLayout || view.getId() == R.id.Logo) {
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            }
        }

        }


    private static final int REQUEST_CODE = 101 ;
    Button register;
    Button sign;
    private String id;


    EditText e1, e2;
    ConstraintLayout constraintLayout;
    ImageView Logo;


    Handler handler = new Handler();
    String s1 = "";
    String s2 = "";
    int check;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


 inputMethodManager =(InputMethodManager)  getSystemService(INPUT_METHOD_SERVICE);

        Activity acc = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (Button) findViewById(R.id.login_reg);
        sign = (Button) findViewById(R.id.login_sign);


        e1 = (EditText) findViewById(R.id.login_email);
        e2 = (EditText) findViewById(R.id.login_pass);
        e1.setOnClickListener(this);
        e2.setOnClickListener(this);
        e2.setOnKeyListener(this);

        constraintLayout = (ConstraintLayout) findViewById(R.id.constrainLayout);
        Logo = (ImageView) findViewById(R.id.Logo);

        constraintLayout.setOnClickListener(this);
        Logo.setOnClickListener(this);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });


        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funkcija();
                }
        });

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void funkcija() {
        s1 = e1.getText().toString();
        s2 = e2.getText().toString();
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Renters");
        query.whereEqualTo("email",e1.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size()>0){
                        for(ParseObject object : objects){
                            if(object.getString("password").matches(e2.getText().toString())){
                                id = object.getObjectId();
                                goToNextActivity(2);
                            }else {
                                Toast.makeText(getApplicationContext(),"Incorrect password or username", Toast.LENGTH_SHORT).show();

                            }

                        }

                    }else {
                        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Users");
                        query.whereEqualTo("email", e1.getText().toString());
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e == null){
                                    if(objects.size() > 0 ){
                                        for(ParseObject object : objects){
                                            if(object.getString("password").matches(e2.getText().toString())){
                                                id = object.getObjectId();
                                                goToNextActivity(1);
                                            }else {
                                                Toast.makeText(getApplicationContext(),"Incorrect password or username", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Incorrect password or username", Toast.LENGTH_SHORT).show();

                                    }
                                }else {
                                    Toast.makeText(getApplicationContext(),"Incorrect password or username", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }else {




                }
            }
        });
    }


    public void goToNextActivity(int c) {
        Bundle extras = new Bundle();
        Intent i = new Intent(LoginActivity.this, ClientMainActivity.class);
         if(c==2){
             extras.putString("email", s1);
             i = new Intent(LoginActivity.this, RenterMainActivity.class);
             extras.putString("renter", "true");
         }
        extras.putString("id", id);
        extras.putString("password", s2);
        i.putExtras(extras);
        startActivity(i);
    }


}
