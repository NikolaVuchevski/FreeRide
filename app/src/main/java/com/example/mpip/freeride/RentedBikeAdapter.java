package com.example.mpip.freeride;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mpip.freeride.domain.Bike;
import com.example.mpip.freeride.domain.RentedBike;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.*;

import java.util.Calendar;
import java.util.Date;

public class RentedBikeAdapter extends BaseAdapter {
    private Context mContext;
    private final RentedBike[] bikes;
    private final String[] rent_ids;
    String clientId;
    LayoutInflater inflter;
    double myLat, myLong;
    private Calendar calendar;

    public RentedBikeAdapter(Context mContext, RentedBike[] bikes, String[] rent_ids, String clientId, double myLat, double myLong){
        this.mContext = mContext;
        this.bikes = bikes;
        this.clientId = clientId;
        this.rent_ids = rent_ids;
        this.myLat = myLat;
        this.myLong = myLong;
        inflter = (LayoutInflater.from(mContext));
    }

    @Override
    public int getCount() {
        return bikes.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View listing = layoutInflater.inflate(R.layout.rented_gridview_item, parent, false);
        Bitmap bitmap = bikes[position].getBike().getImage();
        final ImageView icon = (ImageView) listing.findViewById(R.id.icon); // get the reference of ImageView
        icon.setImageBitmap(bitmap); // set logo images
        TextView textView = (TextView) listing.findViewById(R.id.textView);
        textView.setText(bikes[position].getBike().getName());
        final TextView rentDescription = (TextView) listing.findViewById(R.id.rentDescription);
        final TextView rentDescription2 = (TextView) listing.findViewById(R.id.rentDescription2);
        FloatingActionButton fab = (FloatingActionButton) listing.findViewById(R.id.cancelBike);
        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Rents");
        rentDescription.setText(bikes[position].getTimeFrom());
        SpannableString spannableString = new SpannableString(rentDescription.getText());
        ImageSpan imageSpan = new ImageSpan(((Activity)mContext).getApplicationContext(), R.drawable.ic_today);
        int start = 0;
        int end = 1;
        int flag = 0;
        spannableString.setSpan(imageSpan, start, end, flag);
        rentDescription.setText(spannableString);
        rentDescription2.setText(bikes[position].getTimeTo());
        spannableString = new SpannableString(rentDescription2.getText());
        spannableString.setSpan(imageSpan, start, end, flag);
        rentDescription2.setText(spannableString);
        listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Bike");
                query.whereEqualTo("objectId", bikes[position].getBike().getId());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject parseObject, ParseException e) {
                       Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?saddr=" + myLat + "," + myLong + "&daddr=" + parseObject.get("latitude") + "," + parseObject.get("longitude")));
                       mContext.startActivity(intent);
                    }
                });
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Bike");
                query.whereEqualTo("objectId", bikes[position].getBike().getId());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        parseObject.put("rented", false);
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Rents");
                                query1.whereEqualTo("bike_id", bikes[position].getBike().getId());
                                query1.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject obj, ParseException e) {
                                        obj.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                Intent intent = new Intent(mContext, RentedBikesActivity.class);
                                                intent.putExtra("client_id", clientId);
                                                mContext.startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        return listing;
    }

    private String getStringDateFromDate(Date dateFrom) {
        String [] subs = dateFrom.toString().split(" ");
        String dayName = subs[0];
        String month = subs[1];
        String dayOfMonth = subs[2];
        String time = subs[3].substring(0, 5);
        int hour = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(3, 5));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dayName);
        stringBuilder.append(", ").append(month).append(" ").append(dayOfMonth).append(" at ");
        stringBuilder.append(String.format("%02d", hour)).append(":").append(String.format("%02d", minutes));
        return stringBuilder.toString();
    }
}
