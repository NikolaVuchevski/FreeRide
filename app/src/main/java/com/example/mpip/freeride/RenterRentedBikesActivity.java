package com.example.mpip.freeride;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.mpip.freeride.domain.Bike;
import com.example.mpip.freeride.domain.Location;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RenterRentedBikesActivity extends AppCompatActivity {

    ArrayList<Bike> bikes = new ArrayList<Bike>();
    GridView gridView;
    FloatingActionButton fab;
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;
    private RelativeLayout relativeLayout;
    TextView available;
    Timer timer;
    BikeAdapter bikeAdapter = null;
    int count;
    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        constraintLayout.setVisibility(View.INVISIBLE);
        count = 0;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                count++;
                progressBar.setProgress(count);
                if(count == 15){
                    timer.cancel();
                    if(bikeAdapter != null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handdlee();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                constraintLayout.setVisibility(View.VISIBLE);
                                if(bikes.size()==0)
                                    available.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                relativeLayout.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
            }
        };
        timer.schedule(timerTask, 0, 100);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_renter_rented_bikes);
        gridView = (GridView) findViewById(R.id.gridview_renter);
        fab = (FloatingActionButton) findViewById(R.id.fab1);
        available = (TextView) findViewById(R.id.available);
        available.setVisibility(View.INVISIBLE);
        SpannableString spannableString = new SpannableString(available.getText());
        ImageSpan imageSpan = new ImageSpan(getApplicationContext(), R.drawable.ic_cancel_red);
        int start = 0;
        int end = 1;
        int flag = 0;
        spannableString.setSpan(imageSpan, start, end, flag);
        available.setText(spannableString);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintRenter);
        progressBar = (ProgressBar) findViewById(R.id.progressbar1);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl1);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_view);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_bikes:
                        Intent intent1 = new Intent(RenterRentedBikesActivity.this, RenterMainActivity.class);
                        String email = getIntent().getStringExtra("email");
                        intent1.putExtra("email", email);
                        startActivity(intent1);
                        break;
                    case R.id.ic_exit:
                        Intent intent2 = new Intent(RenterRentedBikesActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Renters");
        query.whereEqualTo("email", getIntent().getStringExtra("email"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    final String renter_id = objects.get(0).getObjectId();
                    final ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Bike");
                    query1.whereEqualTo("renter_id", renter_id);
                    query1.whereEqualTo("rented", true);
                    query1.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                if (objects.size() > 0) {
                                    for (ParseObject o : objects) {
                                        final String id = o.getObjectId();
                                        final String name = o.getString("name");
                                        final int price = o.getInt("price");
                                        final String category_id = o.getString("category_id");
                                        double latitude = o.getDouble("latitude");
                                        final boolean rented = o.getBoolean("rented");
                                        double longitude = o.getDouble("longitude");
                                        final Location location = new Location(latitude, longitude);
                                        ParseFile img = (ParseFile) o.get("image");
                                        Bitmap bmp = null;
                                        if (img != null) {
                                            try {
                                                bmp = BitmapFactory.decodeStream(img.getDataStream());
                                            } catch (ParseException ex) {
                                                ex.printStackTrace();
                                            }
                                            Bike bike = new Bike(id, name, price, bmp, rented, location, renter_id, category_id);
                                            bikes.add(bike);
                                        }};
                                    handdlee();
                                }
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RenterRentedBikesActivity.this, AddBicycleActivity.class);
                Intent intent = getIntent();
                String email = intent.getStringExtra("email");
                i.putExtra("email", email);
                startActivity(i);
            }
        });
    }

    public void handdlee() {
        bikeAdapter = new BikeAdapter(RenterRentedBikesActivity.this, bikes.toArray(new Bike[0]), getIntent().getStringExtra("email"));
        gridView.setAdapter(bikeAdapter);
        relativeLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(View.VISIBLE);
        if(bikes.size() == 0)
            available.setVisibility(View.VISIBLE);
    }
}
