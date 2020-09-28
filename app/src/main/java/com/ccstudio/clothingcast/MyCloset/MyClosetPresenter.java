package com.ccstudio.clothingcast.MyCloset;

public class MyClosetPresenter {

    private MyCloset closet;
    private View view;

    public MyClosetPresenter(View view) {
        this.closet = new MyCloset();
        this.view = view;
    }

    public void updateBackground(int weather) {
        closet.setCurrentWeather(weather);
        view.removeBackground();
        view.updateWCurrentWeather(closet.getCurrentWeather());
        view.updateBackground();
    }

    public interface View {
        void removeBackground();
        void updateWCurrentWeather(int weather);
        void updateBackground();
    }

}
