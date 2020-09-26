package com.ccstudio.clothingcast.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ccstudio.clothingcast.HomeActivity;
import com.ccstudio.clothingcast.MainActivity;
import com.ccstudio.clothingcast.R;
import com.ccstudio.clothingcast.SignIn;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyClosetActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycloset);

        //set bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
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
//                    case R.id.page_closet:
//                        intent = new Intent(getBaseContext(), MyClosetActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(0, 0);
//                        break;
                }
                return true;
            }
        });




    }
}



