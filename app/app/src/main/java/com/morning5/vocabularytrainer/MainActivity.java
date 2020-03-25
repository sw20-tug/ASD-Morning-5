package com.morning5.vocabularytrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClickSwitchToAddWord(View v) {
        Intent addWordIntent = new Intent(getBaseContext(), AddWordActivity.class);
        startActivity(addWordIntent);
    }
}
