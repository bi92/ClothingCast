package com.ccstudio.clothingcast.mycloset;

public class MyCloset {

    private int currentWeather = 0;

    public MyCloset() {};

    public MyCloset(int weather) {
        this.currentWeather = weather;
    }

    public int getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(int weather) {
        this.currentWeather = weather;
    }

}
