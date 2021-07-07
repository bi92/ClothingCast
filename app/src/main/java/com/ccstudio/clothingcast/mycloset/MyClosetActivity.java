package com.ccstudio.clothingcast.mycloset;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ccstudio.clothingcast.AddClothesActivity;
import com.ccstudio.clothingcast.HomeActivity;
import com.ccstudio.clothingcast.R;
import com.ccstudio.clothingcast.DiaryActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyClosetActivity  extends AppCompatActivity implements MyClosetPresenter.View, View.OnClickListener {

    private MyClosetPresenter presenter;

    private ImageView buttonSun;
    private ImageView buttonCloud;
    private ImageView buttonRain;
    private ImageView buttonSnow;
    private ImageView buttonEdit;
    private Button buttonAdd;
    private Button buttonBack;

    private float[] topRadii = {40,40,40,40,0,0,0,0}; //양쪽 다 둥근것
    private float[] rightRadii = {0,0,40,40,0,0,0,0}; //오른쪽만 둥근것
    private float[] leftRadii = {40,40,0,0,0,0,0,0}; //왼쪽만 둥근것

    private GradientDrawable backgroundCloset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycloset);

        setBottomNavigation();

        presenter = new MyClosetPresenter(this);

        //set ini
        buttonEdit = findViewById(R.id.button_closet_edit);
        buttonEdit.setOnClickListener(this);

        backgroundCloset = (GradientDrawable) findViewById(R.id.scrollview_closet).getBackground();
        buttonSun = findViewById(R.id.button_closet_sunny);
        buttonSun.setOnClickListener(this);
        buttonCloud = findViewById(R.id.button_closet_cloudy);
        buttonCloud.setOnClickListener(this);
        buttonRain = findViewById(R.id.button_closet_rainy);
        buttonRain.setOnClickListener(this);
        buttonSnow = findViewById(R.id.button_closet_snow);
        buttonSnow.setOnClickListener(this);

        buttonAdd = findViewById(R.id.button_closet_add);
        buttonAdd.setOnClickListener(this);
        buttonAdd.setVisibility(View.INVISIBLE); //add button visible only on edit mode
        buttonBack = findViewById(R.id.button_closet_back);
        buttonBack.setOnClickListener(this);
        buttonBack.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        backgroundCloset.setCornerRadii(rightRadii);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_closet_edit :
                //TODO : change page to editing mode
                presenter.toEditMode();
                break;
            case R.id.button_closet_back :
                presenter.toClosetMain();
                break;

            case R.id.button_closet_sunny :
                presenter.updateBackground(0);
                break;
            case R.id.button_closet_cloudy :
                presenter.updateBackground(1);
                break;
            case R.id.button_closet_rainy :
                presenter.updateBackground(2);
                break;
            case R.id.button_closet_snow :
                presenter.updateBackground(3);
                break;

            case R.id.button_closet_add :   //move to AddClothesActivity
                presenter.toAddMode();
                break;
            default:
        }
    }

    protected void setBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_closet);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Intent intent;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_diary:
                        intent = new Intent(getBaseContext(), DiaryActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.page_home:
                        intent = new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.page_closet:
                        //no changes (same page)
                        break;
                    default:
                }
                return true;
            }
        });
    }

    @Override
    public void removeBackground(int currentWeather) {
        switch (currentWeather) {
            case 0 :
                buttonSun.setBackgroundResource(0);
                break;
            case 1 :
                buttonCloud.setBackgroundResource(0);
                break;
            case 2 :
                buttonRain.setBackgroundResource(0);
                break;
            case 3 :
                buttonSnow.setBackgroundResource(0);
                break;
            default:
        }
    }

    @Override
    public void updateBackground(int currentWeather) {
        switch (currentWeather) {
            case 0 :
                backgroundCloset.setCornerRadii(rightRadii);
                buttonSun.setBackgroundResource(R.drawable.reactangle_20_top);
                break;
            case 1 :
                backgroundCloset.setCornerRadii(topRadii);
                buttonCloud.setBackgroundResource(R.drawable.reactangle_20_top);
                break;
            case 2 :
                backgroundCloset.setCornerRadii(topRadii);
                buttonRain.setBackgroundResource(R.drawable.reactangle_20_top);
                break;
            case 3 :
                backgroundCloset.setCornerRadii(leftRadii);
                buttonSnow.setBackgroundResource(R.drawable.reactangle_20_top);
                break;
            default:
        }
    }

    @Override
    public void toClosetMain() {
        buttonAdd.setVisibility(View.INVISIBLE);
        buttonBack.setVisibility(View.INVISIBLE);
        buttonEdit.setVisibility(View.VISIBLE);
    }

    @Override
    public void toEditMode() {
        buttonAdd.setVisibility(View.VISIBLE);
        buttonBack.setVisibility(View.VISIBLE);
        buttonEdit.setVisibility(View.INVISIBLE);
    }

    @Override
    public void toAddMode() {
        Intent intent = new Intent(this, AddClothesActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}



