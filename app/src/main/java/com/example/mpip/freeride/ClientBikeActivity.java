package com.example.mpip.freeride;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.*;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mpip.freeride.domain.Location;
import com.example.mpip.freeride.service.CancelRentReceiver;
import com.example.mpip.freeride.service.ReminderLeaveReceiver;
import com.example.mpip.freeride.service.ReminderReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.*;

import java.util.*;

import static java.util.Calendar.MONTH;

public class ClientBikeActivity extends AppCompatActivity {

    LocationManager locationManager;

    LocationListener locationListener;

    private ImageView imageViewBike;
    FloatingActionButton next, viewMap;
    private TextView pickTimeFrom, pickTimeTo, pickDate, pickDateTo, bikeName, totalPrice, billingInfo, titleFrom, titleTo;
    Button rentHourly, rentDaily;
    private Context mContext = this;
    private int startMonth, endMonth, startDay, endDay, startHour, startMinute, endHour, endMinute;
    private double bLatitude;
    private double bLongitude;
    private int bPrice;
    ConstraintLayout cl1, chooseDateTime, constraint3;
    private String id, clientId, rentId;
    private Calendar date1, date2, startAlarm, endAlarm, beforeEndAlarm;
    private String startMonthName, endMonthName;
    int total;
    int hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_client_bike);
        createNotificationChannel();
        createNotificationChannelLeave();
        pickTimeFrom = (TextView) findViewById(R.id.pickTimeFrom);
        pickTimeTo = (TextView) findViewById(R.id.pickTimeTo);
        pickDate = (TextView) findViewById(R.id.pickDate);
        pickDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_date, 0, 0, 0);
        rentHourly = (Button) findViewById(R.id.rentHourly);
        rentDaily = (Button) findViewById(R.id.rentDaily);
        clientId = getIntent().getStringExtra("client_id");
        cl1 = (ConstraintLayout) findViewById(R.id.constraint1);
        chooseDateTime = (ConstraintLayout) findViewById(R.id.chooseDateTime);
        pickDateTo = (TextView) findViewById(R.id.pickDateTo);
        pickDateTo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_date, 0, 0, 0);
        bikeName = (TextView) findViewById(R.id.bikeName);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        titleFrom = (TextView) findViewById(R.id.titleTimeFrom);
        titleTo = (TextView) findViewById(R.id.titleTimeTo);
        constraint3 = (ConstraintLayout) findViewById(R.id.constraint3);
        next = (FloatingActionButton) findViewById(R.id.next);
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int mMonth = calendar.get(MONTH);
        final int mYear = calendar.get(Calendar.YEAR);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_view3);
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intent1 = new Intent(ClientBikeActivity.this, ClientMainActivity.class);
                        intent1.putExtra("id", clientId);
                        startActivity(intent1);
                        break;
                   case R.id.ic_bikes:
                       Intent intent2 = new Intent(ClientBikeActivity.this, RentedBikesActivity.class);
                       intent2.putExtra("client_id", clientId);
                       startActivity(intent2);
                       break;
                    case R.id.ic_exit:
                        Intent intent3 = new Intent(ClientBikeActivity.this, LoginActivity.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });
        imageViewBike = (ImageView) findViewById(R.id.imageViewBike);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        billingInfo = (TextView) findViewById(R.id.billingInfo);
        viewMap = (FloatingActionButton) findViewById(R.id.view_map);
        id = getIntent().getStringExtra("bikeId");
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Bike");
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<ParseObject>() {
                                   @Override
                                   public void done(List<ParseObject> objects, ParseException e) {
                                       ParseObject o = objects.get(0);
                                       final String name = o.getString("name");
                                       bPrice = o.getInt("price");
                                       final String category_id = o.getString("category_id");
                                       bLatitude = o.getDouble("latitude");
                                       bLongitude = o.getDouble("longitude");
                                       final boolean rented = o.getBoolean("category_id");
                                       final Location location = new Location(bLatitude, bLatitude);
                                       final String renter_id = o.getString("renter_id");
                                       ParseFile image = (ParseFile) o
                                               .get("image");
                                       Bitmap bmp = null;
                                       if (image != null) {
                                           try {
                                               bmp = BitmapFactory.decodeStream(image.getDataStream());
                                           } catch (ParseException ex) {
                                               ex.printStackTrace();
                                           }
                                           imageViewBike.setImageBitmap(bmp);
                                           bikeName.setText(name);
                                       }

                                   }
            });
        rentHourly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cl1.setVisibility(View.INVISIBLE);
                chooseDateTime.setVisibility(View.VISIBLE);
            }
        });
        rentDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cl1.setVisibility(View.INVISIBLE);
                pickTimeFrom.setVisibility(View.INVISIBLE);
                pickTimeTo.setVisibility(View.INVISIBLE);
                pickDate.setVisibility(View.VISIBLE);
                pickDateTo.setVisibility(View.VISIBLE);
                chooseDateTime.setVisibility(View.VISIBLE);
            }
        });

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(MONTH, month);
                        startMonth = month + 1;
                        startDay = dayOfMonth;
                        startMonthName = calendar.getDisplayName(MONTH, Calendar.LONG, Locale.US);
                        int dayB  = dayOfMonth;
                        int monthB = month;
                        int yearB = year;
                        date1 = Calendar.getInstance();
                        startAlarm = Calendar.getInstance();
                        endAlarm = Calendar.getInstance();
                        date1.set(Calendar.YEAR, yearB);
                        date1.set(MONTH, monthB);
                        date1.set(Calendar.DAY_OF_MONTH, dayB);
                        date1.set(Calendar.HOUR_OF_DAY, 9);
                        date1.set(Calendar.MINUTE, 0);
                        date1.set(Calendar.SECOND, 0);
                        date1.set(Calendar.MILLISECOND, 0);
                        if(pickTimeTo.getVisibility()==View.INVISIBLE) {
                            startAlarm.set(Calendar.DAY_OF_MONTH, dayB);
                            startAlarm.set(MONTH, monthB);
                            startAlarm.set(Calendar.YEAR, yearB);
                            endAlarm.set(Calendar.DAY_OF_MONTH, dayB);
                            endAlarm.set(MONTH, monthB);
                            endAlarm.set(Calendar.YEAR, yearB);
                            beforeEndAlarm = endAlarm;
                        } else {
                            startAlarm = date1;
                            startHour = 9;
                            startMinute = 0;
                        }
                        pickDate.setText(dayOfMonth + ". " + startMonthName);
                    }
                }, mYear, mMonth, mDay);
                Calendar currentDay = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMinDate(currentDay.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        pickDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickDate.getText().toString().equals("Pick start date"))
                    Toast.makeText(getApplicationContext(), "Enter start date first!", Toast.LENGTH_SHORT).show();
                else {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(MONTH, month);
                            endMonth = month + 1;
                            endDay = dayOfMonth;
                            endMonthName = calendar.getDisplayName(MONTH, Calendar.LONG, Locale.US);
                            if (!pickDate.getText().toString().equals("Pick start date")) {
                                if (startDay > endDay || startMonth > endMonth) {
                                    Toast.makeText(getApplicationContext(), "Invalid values of start date and end date.\nTry again!", Toast.LENGTH_SHORT).show();
                                } else {
                                    pickDateTo.setText(dayOfMonth + ". " + endMonthName);
                                    int total = estimatePriceDaily(startDay, startMonth, endDay, endMonth);
                                    billingInfo.setText("Billing info ");
                                    totalPrice.setText("Total price: " + total + " denars");
                                    constraint3.setVisibility(View.VISIBLE);
                                    int dayB = view.getDayOfMonth();
                                    int monthB = view.getMonth();
                                    int yearB = view.getYear();
                                    date2 = Calendar.getInstance();
                                    date2.set(Calendar.YEAR, yearB);
                                    date2.set(MONTH, monthB);
                                    date2.set(Calendar.DAY_OF_MONTH, dayB);
                                    date2.set(Calendar.HOUR_OF_DAY, 9);
                                    date2.set(Calendar.MINUTE, 0);
                                    date2.set(Calendar.SECOND, 0);
                                    date2.set(Calendar.MILLISECOND, 0);
                                    endAlarm = date2;
                                    beforeEndAlarm = endAlarm;
                                    endHour = 9;
                                    endMinute = 0;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "You must enter start date!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, mYear, mMonth, mDay);
                    Calendar currentDay = startAlarm;
                    currentDay.set(Calendar.DAY_OF_MONTH, startDay + 1);
                    datePickerDialog.getDatePicker().setMinDate(currentDay.getTimeInMillis());
                    datePickerDialog.show();
                }
            }
        });
        pickTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickDate.getText().toString().equals("Pick start date"))
                    Toast.makeText(getApplicationContext(), "Enter the date first!", Toast.LENGTH_SHORT).show();
                else {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                        @SuppressLint("ShowToast")
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, final int minute) {
                            Calendar currentTime = Calendar.getInstance();
                            if(startDay == currentTime.get(Calendar.DAY_OF_MONTH) && hourOfDay <= currentTime.get(Calendar.HOUR_OF_DAY)){
                                Toast.makeText(getApplicationContext(), "You can choose only an hour following from current time!", Toast.LENGTH_SHORT).show();
                            } else {
                                startHour = hourOfDay;
                                startMinute = minute;
                                startAlarm.set(Calendar.HOUR_OF_DAY, startHour - 1);
                                startAlarm.set(Calendar.MINUTE, startMinute);
                                startAlarm.set(Calendar.SECOND, 0);
                                startAlarm.set(Calendar.MILLISECOND, 0);
                                pickTimeFrom.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                                Toast.makeText(getApplicationContext(), "Let's add end time!", Toast.LENGTH_LONG).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        titleFrom.setVisibility(View.VISIBLE);
                                        funkcija(hour, minute);
                                    }
                                }, 350);
                            }
                        }
                    }, hour, minute, android.text.format.DateFormat.is24HourFormat(mContext));
                    timePickerDialog.show();
                }
            }
        });

        pickTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pickTimeFrom.getText().toString().matches("^[0-9]{2}\\:[0-9]{2}$"))
                    Toast.makeText(getApplicationContext(), "Enter start time first!", Toast.LENGTH_SHORT).show();
                else
                    funkcija(hour, minute);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(pickDateTo.getVisibility()==View.VISIBLE) {
                    if(startMonth < endMonth) {
                        goToNextActivity();
                    } else if(startMonth == endMonth && startDay < endDay) {
                        goToNextActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid values of start time and end time!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (startHour < endHour) {
                        goToNextActivity();
                    } else if (startHour == endHour && startMinute < endMinute) {
                            goToNextActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid values of start time and end time!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                double lat = i.getDoubleExtra("latitude", 0);
                double longi = i.getDoubleExtra("longitude", 0);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+lat+","+longi+"&daddr="+bLatitude+","+bLongitude));
                startActivity(intent);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void goToNextActivity() {
        Intent intent = new Intent(ClientBikeActivity.this, ReminderReceiver.class);
        intent.putExtra("startDay", startDay);
        intent.putExtra("startHour", startHour);
        intent.putExtra("startMonth", startMonthName);
        intent.putExtra("startMin", startMinute);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ClientBikeActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long delay = startAlarm.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        if(delay > 0) {
            long futureInMillis = SystemClock.elapsedRealtime() + delay;
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    futureInMillis, pendingIntent);
        }
        final ParseObject object = new ParseObject("Rents");
        object.put("client_id", clientId);
        object.put("bike_id", id);
        object.put("price", total);

        if(pickDateTo.getVisibility()!=View.INVISIBLE) {
            object.put("date_from", date1.getTime());
            object.put("date_to", date2.getTime());
        }
        else {
            Calendar begin_date = startAlarm;
            begin_date.set(Calendar.HOUR_OF_DAY, startAlarm.get(Calendar.HOUR_OF_DAY)+1);
            object.put("date_from", begin_date.getTime());
            Calendar end_date = endAlarm;
            end_date.set(Calendar.HOUR_OF_DAY, endAlarm.get(Calendar.HOUR_OF_DAY)+1);
            object.put("date_to", end_date.getTime());
        }
        object.put("hours", hours);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    rentId = object.getObjectId();
                    setAlarm();
                    setAlarmBeforeLeavingBike();
                    ParseQuery<ParseObject> bikeObj = new ParseQuery<ParseObject>("Bike");
                    bikeObj.whereEqualTo("objectId", id);
                    bikeObj.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if(e == null) {
                                parseObject.put("rented", true);
                                parseObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(getApplicationContext(), "You rented this bike successfully!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ClientBikeActivity.this, RentedBikesActivity.class);
                                            intent.putExtra("client_id", getIntent().getStringExtra("client_id"));
                                            startActivity(intent);
                                        } else {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });

                }else {
                    Toast.makeText(getApplicationContext(), "Rentering failed! Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAlarmBeforeLeavingBike() {
        Intent intent = new Intent(ClientBikeActivity.this, ReminderLeaveReceiver.class);
        intent.putExtra("endDay", endDay);
        intent.putExtra("endHour", endHour);
        intent.putExtra("endMonth", endMonthName);
        intent.putExtra("endMin", endMinute);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ClientBikeActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long delay = endAlarm.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        if(delay > 0) {
            long futureInMillis = SystemClock.elapsedRealtime() + delay;
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    futureInMillis, pendingIntent);
        }
    }

    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent1 = new Intent(this, CancelRentReceiver.class);
        intent1.putExtra("bikeId", id);
        intent1.putExtra("rentId", rentId);
        intent1.putExtra("endMonth", endMonthName);
        intent1.putExtra("endDay", endDay);
        intent1.putExtra("endHour", beforeEndAlarm.get(Calendar.DAY_OF_MONTH));
        intent1.putExtra("endMin", endMinute);
        PendingIntent pi1 = PendingIntent.getBroadcast(this, 0, intent1, 0);
        long delay = beforeEndAlarm.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        if(delay > 0) {
            long futureInMillis = SystemClock.elapsedRealtime() + delay;
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pi1);
        }
    }

    private void funkcija(int hour, int minute) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                if(!pickTimeFrom.getText().toString().equals("Pick start date")) {
                    endDay = startDay;
                    endMonth = startMonth;
                    endHour = hourOfDay;
                    endMinute = minute;
                    endMonthName = startMonthName;
                    endAlarm.set(Calendar.HOUR_OF_DAY, endHour);
                    endAlarm.set(Calendar.MINUTE, endMinute);
                    endAlarm.set(Calendar.SECOND, 0);
                    endAlarm.set(Calendar.MILLISECOND,0);
                    beforeEndAlarm = endAlarm;
                    beforeEndAlarm.set(Calendar.HOUR_OF_DAY, endHour - 1);
                    if(startHour >= endHour || (endHour <= Calendar.getInstance().get(Calendar.HOUR_OF_DAY) && startDay==Calendar.getInstance().get(Calendar.DAY_OF_MONTH))) {
                        Toast.makeText(getApplicationContext(), "Invalid values of start time and end time!", Toast.LENGTH_SHORT).show();
                    } else {
                        pickTimeTo.setText(String.format("%02d", hourOfDay)+ ":" + String.format("%02d", minute));
                        total = estimatePriceHourly();
                        titleTo.setVisibility(View.VISIBLE);
                        billingInfo.setText("Billing info ");
                        totalPrice.setText("Total price: " + total + " denars");
                        constraint3.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter start time first!", Toast.LENGTH_SHORT).show();
                }
            }
        }, hour, minute, android.text.format.DateFormat.is24HourFormat(mContext));
        timePickerDialog.show();
    }

    private int estimatePriceHourly() {
        int startHour = Integer.parseInt(pickTimeFrom.getText().toString().substring(0, 2));
        int endHour = Integer.parseInt(pickTimeTo.getText().toString().substring(0, 2));
        hours = endHour - startHour;
        int price = (endHour - startHour) * bPrice;
        return price;
    }

    private int estimatePriceDaily(int startDay, int startMonth, int endDay, int endMonth) {
        int monthDiff = endMonth - startMonth;
        if(monthDiff == 0){
            int dayDiff = endDay - startDay;
            hours = dayDiff * 16;
            return (dayDiff * 16) * bPrice;
        }
        return 0;
    }

    public void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String description = "Channel for notifications before renting";
            NotificationChannel notificationChannel =
                    new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    public void createNotificationChannelLeave() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String description = "Channel for notifications before leaving the bike";
            NotificationChannel notificationChannel =
                    new NotificationChannel("n1", "n1", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }
}

