package com.ccstudio.clothingcast.View;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ccstudio.clothingcast.R;

//saving clothes to firebase storage
//view on MVP
public class AddClothesActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addclothes_act);
    }
}
