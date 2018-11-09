package com.example.btscanning;

import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    LectureAdapter adapter;

    List<Lecture> lectureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        lectureList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for(int i = 0; i < 40; i++){
            lectureList.add(
                    new Lecture(123, 321, new Date(2006, 12, 31), ParcelUuid.fromString("cac426a3-344f-45c8-8819-fbcfe81e4b23"), "4331", "POOP")
            );

            lectureList.add(
                    new Lecture(123, 321, new Date(2006, 12, 31), ParcelUuid.fromString("cac426a3-344f-45c8-8819-fccfe81e4b23"), "4331", "PISS")
            );

            lectureList.add(
                    new Lecture(123, 321, new Date(2006, 12, 31), ParcelUuid.fromString("cac426a3-344f-45c8-8819-fccfe81e4b23"), "4331", "REEEEEEE")
            );
        }

        adapter = new LectureAdapter(this, lectureList);
        recyclerView.setAdapter(adapter);
    }
}
