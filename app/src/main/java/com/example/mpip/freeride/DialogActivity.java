package com.example.mpip.freeride;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DialogActivity extends AppCompatActivity {
    Button bmx, beachCruiser, commutingBike, roadBike, comfortBike, mountainBike;
    String id1="Sbi8xvMOTB";
    String id2="OmHHww0vDq";
    String id3="kyimtWwlTq";
    String id4="SupXrlr0Mv";
    String id5="aoKHGh6oF5";
    String id6="PuoiQal9ed";
    Intent i;
    private ImageButton clearAll;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);
        getSupportActionBar().hide();
        bmx=(Button)findViewById(R.id.button);
        beachCruiser=(Button)findViewById(R.id.button3);
        commutingBike=(Button)findViewById(R.id.button4);
        roadBike=(Button)findViewById(R.id.button5);
        comfortBike=(Button)findViewById(R.id.button6);
        mountainBike=(Button)findViewById(R.id.button7);
        clearAll = (ImageButton) findViewById(R.id.clearAll);
        final String clientId = getIntent().getStringExtra("client_id");

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(DialogActivity.this, ClientMainActivity.class);
                i.putExtra("id", clientId);
                startActivity(i);
            }
        });
        bmx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(DialogActivity.this, ClientMainActivity.class);
                i.putExtra("categoryId", id1);
                i.putExtra("id", clientId);
                startActivity(i);
            }
        });

        beachCruiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(DialogActivity.this, ClientMainActivity.class);
                i.putExtra("categoryId", id2);
                i.putExtra("id", clientId);
                startActivity(i);
            }
        });

        commutingBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(DialogActivity.this, ClientMainActivity.class);
                i.putExtra("categoryId", id3);
                i.putExtra("id", clientId);
                startActivity(i);
            }
        });

        roadBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(DialogActivity.this, ClientMainActivity.class);
                i.putExtra("categoryId", id4);
                i.putExtra("id", clientId);
                startActivity(i);
            }
        });

        comfortBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(DialogActivity.this, ClientMainActivity.class);
                i.putExtra("categoryId", id5);
                i.putExtra("id", clientId);
                startActivity(i);
            }
        });

        mountainBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(DialogActivity.this, ClientMainActivity.class);
                i.putExtra("categoryId", id6);
                i.putExtra("id", clientId);
                startActivity(i);
            }
        });
    }
}
