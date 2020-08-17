package com.ccstudio.clothingcast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

//https://github.com/firebase/snippets-android/blob/2228558fde6e576636e5ceadfe00ce0b9bdc0cdd/database/app/src/main/java/com/google/firebase/referencecode/database/MainActivity.java#L40-L44

public class MainActivity extends AppCompatActivity {

private Button btn_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_home = findViewById(R.id.movetoHome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Home 화면 이동
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent); // 액티비티 이동
            }
        });

    }
}
