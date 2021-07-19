package com.example.mpip.freeride.domain;

public class BikeDistance implements Comparable<BikeDistance>{
    public Bike bike;
    public double distance;

    public BikeDistance(){};

    public BikeDistance(Bike bike, double distance) {
        this.bike=bike;
        this.distance=distance;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    @Override
    public int compareTo(BikeDistance bikeDistance) {
        return Double.compare(distance, bikeDistance.getDistance());

    }
}
