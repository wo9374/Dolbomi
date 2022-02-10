package com.example.project;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CustomCalendarActivity extends AppCompatActivity {
    CustomCalendarView customCalendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customCalendarView=(CustomCalendarView)findViewById(R.id.custom_calendar_view);
        setContentView(R.layout.calendar_layout_activity);

    }
}
