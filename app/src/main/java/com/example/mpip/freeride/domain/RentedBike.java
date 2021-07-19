package com.example.mpip.freeride.domain;

import com.example.mpip.freeride.domain.Bike;


public class RentedBike {
    private Bike bike;
    private String timeFrom;
    private String timeTo;

    public RentedBike() {}
    public RentedBike(Bike bike, String timeFrom, String timeTo) {
        this.bike = bike;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }
}
