package com.example.mpip.freeride.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.parse.*;

import java.util.List;

public class CancelRentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String bikeId = intent.getStringExtra("bikeId");
        final String rentId = intent.getStringExtra("rentId");
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Bike");
        query.whereEqualTo("objectId", bikeId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                parseObject.put("rented", false);
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Rents");
                        query1.whereEqualTo("objectId", rentId);
                        query1.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject obj, ParseException e) {
                                obj.deleteInBackground();
                            }
                        });
                    }
                });
            }
        });
    }
}
