package com.example.mpip.freeride;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.*;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class ClientRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.constrainLayout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
    EditText et1, et2, et3;
    FloatingActionButton fab;
    ConstraintLayout constraintLayout;
    Boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);


        constraintLayout = (ConstraintLayout) findViewById(R.id.constrainLayout);
        constraintLayout.setOnClickListener(this);
        et1 = (EditText) findViewById(R.id.reg_ime);
        et2 = (EditText) findViewById(R.id.reg_surname);
        et3 = (EditText) findViewById(R.id.reg_tel);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et1.getText().toString();
                String surname = et2.getText().toString();
                String tel = et3.getText().toString();

                if(name.equals("") || surname.equals("") || tel.equals(""))
                {
                    Toast.makeText(v.getContext(), "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!validateName() || !validateSurname() || !validatePhoneNo()){}
                    else {
                        Intent fromIntent = getIntent();
                        String email = fromIntent.getStringExtra("email");
                        String pass = fromIntent.getStringExtra("pass");
                        ParseObject object = new ParseObject("Users");
                        object.put("email", email);
                        object.put("name", name);
                        object.put("surname", surname);
                        object.put("password", pass);
                        object.put("telephone", Integer.parseInt(tel));
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getApplicationContext(), "Registration as client is successful!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Registration failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private Boolean validateName() {
        String val = et1.getText().toString();
        String namePattern="^[A-Z][a-z]{2,19}";
        if (!val.matches(namePattern)) {
            et1.setError("Enter a valid name!");
            return false;
        } else {
            et1.setError(null);
            return true;
        }
    }

    private Boolean validateSurname() {
        String val = et2.getText().toString();
        String phonePattern="^[A-Z][a-z]{2,19}";
        if (!val.matches(phonePattern)) {
            et2.setError("Enter a valid sururname!");
            return false;
        } else {
            et2.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = et3.getText().toString();
        String phonePattern="^07[0-9]{7}";
        if (!val.matches(phonePattern)) {
            et3.setError("Incorrect phone number!");
            return false;
        } else {
            et3.setError(null);
            return true;
        }
    }
}
