package com.morning5.vocabularytrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*button_edit = (Button) findViewById(R.id.button_edit);
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onButtonClickSwitchToEditWord();
            }
        });*/
    }


    public void onButtonClickSwitchToAddWord(View v) {
        Intent addWordIntent = new Intent(getBaseContext(), AddWordActivity.class);
        startActivity(addWordIntent);
    }

    public void onButtonClickSwitchToEditWord(View v)
    {
        Intent intent = new Intent(getBaseContext(), EditWordActivity.class);
        startActivity(intent);
    }
}
