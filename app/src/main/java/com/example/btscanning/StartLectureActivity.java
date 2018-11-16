package com.example.btscanning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartLectureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_lecture);
        getActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
