package com.ccstudio.clothingcast.mycloset;

public class MyClosetPresenter {

    private MyCloset closet;
    private View view;

    public MyClosetPresenter(View view) {
        this.closet = new MyCloset();
        this.view = view;
    }

    public void updateBackground(int weather) {
        view.removeBackground(closet.getCurrentWeather());
        closet.setCurrentWeather(weather);
        view.updateBackground(closet.getCurrentWeather());
    }

    public void toEditMode() {
        view.toEditMode();
    }

    public interface View {
        void removeBackground(int weather);
        void updateBackground(int weather);
        void toClosetMain();
        void toEditMode();
        void toAddMode();
    }

}
