package com.morning5.vocabularytrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_overview = findViewById(R.id.button_overview);
        Button button_add_word = findViewById(R.id.button_add_word);

        button_overview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        button_add_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddWordActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    public void onButtonClickStudyInterface(View v) {
        Intent intent = new Intent(MainActivity.this, StudyInterfaceActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
