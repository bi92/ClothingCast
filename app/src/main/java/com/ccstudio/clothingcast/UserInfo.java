package com.ccstudio.clothingcast;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.HashMap;
import java.util.Map;

public class UserInfo extends AppCompatActivity implements View.OnClickListener{

    ConstraintLayout genderLayout;
    ScrollView nameLayout;
    Button genderNext;
    Button nameNext;
    ImageView femaleImg;
    ImageView maleImg;
    ImageView femaleCheck;
    ImageView maleCheck;
    ImageView genderImg;
    TextView enterName;
    NumberPicker ageSelect;

    //save user info
    boolean gender; // true : female    false : male
    String name;
    int age = 20;

    Intent mainactivity;
    FirebaseUser user;
    FirebaseFirestore db;
    private static final String TAG = "GoogleActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        genderLayout = (ConstraintLayout)findViewById(R.id.profile_01);
        nameLayout = (ScrollView)findViewById(R.id.profile_02);
        genderLayout.setVisibility(View.VISIBLE);
        nameLayout.setVisibility(View.INVISIBLE);

        genderNext = (Button)findViewById(R.id.profile_next_01);
        genderNext.setOnClickListener(this);
        nameNext = (Button)findViewById(R.id.profile_next_02);
        nameNext.setOnClickListener(this);
        genderNext.setEnabled(false);

        femaleImg = (ImageView)findViewById(R.id.female_img);
        femaleImg.setOnClickListener(this);
        maleImg = (ImageView)findViewById(R.id.male_img);
        maleImg.setOnClickListener(this);
        femaleCheck = (ImageView)findViewById(R.id.check_female);
        maleCheck = (ImageView)findViewById(R.id.check_male);
        genderImg = (ImageView)findViewById(R.id.gender_img);
        enterName = (TextView)findViewById(R.id.enter_name);

        ageSelect = (NumberPicker)findViewById(R.id.age_selector);
        ageSelect.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                age = newVal;
            }
        });

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mainactivity = new Intent(this, MainActivity.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_next_01 : genderToName(); break;
            case R.id.profile_next_02 : userInfoConfirm(); break;

            case R.id.female_img : selectGender(R.id.female_img); break;
            case R.id.male_img : selectGender(R.id.male_img); break;
        }
    }

    public void genderToName() {
        if(gender) {
            genderImg.setImageResource(R.drawable.female);
        }
        else {
            genderImg.setImageResource(R.drawable.male);
        }

        genderLayout.setVisibility(View.INVISIBLE);
        nameLayout.setVisibility(View.VISIBLE);
    }

    public void selectGender(int id) {
        if(!genderNext.isEnabled()) genderNext.setEnabled(true);

        if(id == R.id.female_img){
            gender = true;
            femaleImg.setBackground(ContextCompat.getDrawable(this ,R.drawable.rectangle_round));
            maleImg.setBackground(null);
            femaleCheck.setVisibility(View.VISIBLE);
            maleCheck.setVisibility(View.INVISIBLE);
        }
        else{
            gender = false;
            femaleImg.setBackground(null);
            maleImg.setBackground(ContextCompat.getDrawable(this ,R.drawable.rectangle_round));
            femaleCheck.setVisibility(View.INVISIBLE);
            maleCheck.setVisibility(View.VISIBLE);
        }
    }

    public void userInfoConfirm() {
        name = enterName.getText().toString();

        if(TextUtils.isEmpty(name)) {
            Toast.makeText(this,"이름을 입력해주세요",Toast.LENGTH_LONG).show();
            return;
        }

        //upload to firebase
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", name);
        docData.put("gender",gender);
        docData.put("age",age);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("YEON")
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });

        String uid = user.getUid();

        db.collection("users").document(uid)
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "사용자 정보 저장 됨");
                        //change page to mainactivity
                        startActivity(mainactivity);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "사용자 정보 저장 에러", e);
                    }
                });

    }
}
