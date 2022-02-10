package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class AttendanceActivity extends Activity {

    Button selectDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_layout);
        Button selectDate = findViewById(R.id.btnDate);


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDate=new Intent(AttendanceActivity.this, TeacherAttendance.class);
                startActivity(intentDate);
            }

        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            getActionBar().setBackgroundDrawable(getDrawable(R.color.colorPrimary));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            applyColors();
        }
    }

    // Apply the title/navigation bar color
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void applyColors() {
        getWindow().setStatusBarColor(Color.parseColor("#efc675"));
    }

}






