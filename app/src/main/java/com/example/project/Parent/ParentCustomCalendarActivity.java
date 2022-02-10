package com.example.project.Parent;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project.R;

public class ParentCustomCalendarActivity extends AppCompatActivity {
    ParentCustomCalendarView customCalendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customCalendarView=(ParentCustomCalendarView)findViewById(R.id.parent_custom_calendar_view);
        setContentView(R.layout.parent_calendar_layout_activity);

    }
}
