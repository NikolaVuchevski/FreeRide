package com.example.mpip.freeride;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.example.mpip.freeride.domain.Bike;
import com.example.mpip.freeride.domain.Location;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.*;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AddBicycleActivity extends AppCompatActivity {

    public static final int GET_FROM_GALLERY = 3;
    private ImageView image;
    private ImageButton imageButton;
    private FloatingActionButton addBike;
    private FloatingActionButton deleteBike;
    private AutoCompleteTextView actv;
    private TextView perHour;
    private Button changePic;
    private Context mContext;
    private EditText et;
    private ProgressBar progressBar;
    private Timer timer;
    private EditText et2;
    Bitmap bitmap = null;
    RelativeLayout rlAdd;
    ConstraintLayout constraintAddBicycle;
    Uri uri = null;
    String bikeId;
    int count;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bicycle);
//        getSupportActionBar().hide();
        image = (ImageView) findViewById(R.id.imageView);
        changePic = (Button) findViewById(R.id.changePic);
        addBike = (FloatingActionButton) findViewById(R.id.btn_add_bike);
        deleteBike = (FloatingActionButton) findViewById(R.id.deleteBike);
        deleteBike.setVisibility(View.INVISIBLE);
        actv = (AutoCompleteTextView) findViewById(R.id.category);
        perHour = (TextView) findViewById(R.id.perHour);
        perHour.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        changePic.setVisibility(View.INVISIBLE);
        rlAdd = (RelativeLayout) findViewById(R.id.rlAdd);
        constraintAddBicycle = (ConstraintLayout) findViewById(R.id.constraintAddBicycle);
        rlAdd.setVisibility(View.INVISIBLE);
        et = (EditText) findViewById(R.id.price);
        et2 = (EditText) findViewById(R.id.model_name);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_view);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
        final String email = getIntent().getStringExtra("email");
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_bikes:
                        Intent intent0 = new Intent(AddBicycleActivity.this, RenterMainActivity.class);
                        intent0.putExtra("email", email);
                        startActivity(intent0);
                        break;
                    case R.id.ic_rented_bikes:
                        Intent intent1 = new Intent(AddBicycleActivity.this, RenterRentedBikesActivity.class);
                        intent1.putExtra("email", email);
                        startActivity(intent1);
                        break;
                    case R.id.ic_exit:
                        Intent intent2 = new Intent(AddBicycleActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
        final ArrayList<String> list_categories = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.autocomplete_textview, list_categories);
        Intent intent = getIntent();
        bikeId = intent.getStringExtra("id");
        final ParseQuery<ParseObject> queryCat = new ParseQuery<ParseObject>("Categories");
        queryCat.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            list_categories.add(object.getString("name"));
                        }
                        actv.setAdapter(adapter);
                        actv.setThreshold(1);
                        actv.setTextColor(Color.parseColor("#000000"));
                    } else {
                        e.printStackTrace();
                    }
                } else {
                    e.printStackTrace();
                }
            }

        });

        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Bike");
        query.whereEqualTo("objectId", bikeId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (final ParseObject object : objects) {
                            if (object.getObjectId().equals(bikeId)) {
                                deleteBike.setVisibility(View.VISIBLE);
                                queryCat.whereEqualTo("objectId", object.getString("category_id"));
                                queryCat.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> list, ParseException e) {
                                        if (e == null) {
                                            ParseObject category = list.get(0);
                                            String nameCat = category.getString("name");
                                            actv.setText(nameCat);
                                            imageButton.setVisibility(View.INVISIBLE);
                                            changePic.setVisibility(View.VISIBLE);
                                            et2.setText(object.getString("name"));
                                            int br = object.getInt("price");
                                            et.setText(String.valueOf(br));
                                            ParseFile imageFile = (ParseFile) object.get("image");
                                            try {
                                                assert imageFile != null;
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(imageFile.getData(), 0, imageFile.getData().length);
                                                image.setImageBitmap(bitmap);
                                                image.setVisibility(View.VISIBLE);
//                                                imageButton.setVisibility(View.INVISIBLE);
                                            } catch (ParseException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    }
                                });

                            }
                        }
                    }
                }
            }
        });

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(AddBicycleActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddBicycleActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

                    return;
                }
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                i.setType("image/*");
                startActivityForResult(i, 1);
            }
        });
        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                i.setType("image/*");
                startActivityForResult(i, 1);
            }
        });
        deleteBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Bike");
                query1.whereEqualTo("objectId", bikeId);
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        ParseObject object_for_delete = objects.get(0);
                        object_for_delete.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    Toast.makeText(getApplicationContext(),"Successfully removed bike!", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(getApplicationContext(), RenterMainActivity.class);
                                    startActivity(intent1);
                                }else {
                                    Toast.makeText(getApplicationContext(),"The Bike cant be removed at the moment!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
        addBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                rlAdd.setVisibility(View.VISIBLE);
                constraintAddBicycle.setVisibility(View.INVISIBLE);


                timer = new Timer();
                final int[] count = {0};
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        count[0]++;
                        progressBar.setProgress(count[0]);
                        if (count[0] == 15) {
                            timer.cancel();
                        }
                    }
                };
                timer.schedule(timerTask, 0, 100);
                final int price = Integer.parseInt(et.getText().toString());
                final String modelName = et2.getText().toString();
                Intent i = getIntent();
                final String email = i.getStringExtra("email");
                final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Renters");
                query.whereEqualTo("email", getIntent().getStringExtra("email"));
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, com.parse.ParseException e) {
                        if (e == null) {
                            final String id = objects.get(0).getObjectId();
                            final double lat = objects.get(0).getDouble("latitude");
                            final double longi = objects.get(0).getDouble("longitude");
                            final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Categories");
                            query.whereEqualTo("name", actv.getText().toString());
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, com.parse.ParseException e) {
                                    if (e == null) {
                                        final String category_id = objects.get(0).getObjectId();
                                        if (getIntent().getStringExtra("bikeExists") != null) {
                                            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Bike");
                                            query2.whereEqualTo("objectId", bikeId);
                                            query2.getFirstInBackground(new GetCallback<ParseObject>() {
                                                @Override
                                                public void done(ParseObject parseObject, ParseException e) {
                                                    parseObject.put("price", price);
                                                    parseObject.put("name", modelName);
                                                    parseObject.put("category_id", category_id);
                                                    Bitmap bitmap = null;
                                                    try {
                                                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                                    } catch (IOException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                    final byte[] byteArray = stream.toByteArray();
                                                    final ParseFile file = new ParseFile("image", byteArray);
                                                    parseObject.put("image", file);
                                                    parseObject.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e == null) {
                                                                Toast.makeText(getApplicationContext(), "You edited this bike successfully!", Toast.LENGTH_SHORT).show();
                                                                Intent intent1 = new Intent(AddBicycleActivity.this, RenterMainActivity.class);
                                                                intent1.putExtra("email", email);
                                                                startActivity(intent1);

                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        } else {
                                            final ParseObject object = new ParseObject("Bike");
                                            object.put("price", price);
                                            object.put("name", modelName);
                                            object.put("renter_id", id);
                                            object.put("category_id", category_id);
                                            object.put("rented", false);
                                            object.put("latitude", lat);
                                            object.put("longitude", longi);
                                            ParseGeoPoint parseGeoPoint = new ParseGeoPoint(lat, longi);
                                            object.put("location", parseGeoPoint);
                                            try {
                                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                final byte[] byteArray = stream.toByteArray();
                                                final ParseFile file = new ParseFile("image", byteArray);
                                                object.put("image", file);
                                                object.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        if (e == null) {
                                                            Toast.makeText(getApplicationContext(), "You added the bike successfully!", Toast.LENGTH_SHORT).show();
                                                            Intent intent1 = new Intent(AddBicycleActivity.this, RenterMainActivity.class);
                                                            intent1.putExtra("email", email);
                                                            startActivity(intent1);
                                                            rlAdd.setVisibility(View.INVISIBLE);
                                                            constraintAddBicycle.setVisibility(View.VISIBLE);
                                                        }
                                                    }

                                                });

                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    } else{
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                    perHour.setVisibility(View.INVISIBLE);
                else
                    perHour.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                image.setImageBitmap(bitmap);
                image.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.INVISIBLE);
                changePic.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
