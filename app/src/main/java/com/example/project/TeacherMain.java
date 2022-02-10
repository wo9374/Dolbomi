package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class TeacherMain extends Activity {

    ImageView gps;
    ImageView diary;
    ImageView setting;
    ImageView teacherTmap;
    ImageView teacherEatting;
    ImageView Userface;
    ImageView calendar;
    ImageView attendance;
    TextView Username;

    private String uid;
    private String url;

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main);
        gps=findViewById(R.id.gps);
        diary=findViewById(R.id.diary);
        setting=findViewById(R.id.setting);
        teacherTmap=findViewById(R.id.teacherTmap);
        teacherEatting=findViewById(R.id.eatting);
        Userface=findViewById(R.id.face);
        Username=findViewById(R.id.teacherName);
        calendar=findViewById(R.id.calendar);
        attendance=findViewById(R.id.attendance);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userModel =  dataSnapshot.getValue(User.class);
                    Glide.with(TeacherMain.this)
                            .load(userModel.profileImageUrl)
                            .apply(new RequestOptions().circleCrop())
                            .into(Userface);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            //getActionBar().setBackgroundDrawable(getDrawable(R.color.colorPrimary));
        }

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Username.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        passPushTokenToServer();

    }

    void passPushTokenToServer() {
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token= FirebaseInstanceId.getInstance().getToken();
        Map<String,Object> nap=new HashMap<>();
        nap.put("pushToken",token);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(nap);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gps:
                Intent intent=new Intent(TeacherMain.this,TeacherChat.class);
                startActivity(intent);
                break;
            case R.id.diary:
                Intent dintent=new Intent(TeacherMain.this,TeacherDiary.class);
                startActivity(dintent);
                break;
            case R.id.setting:
                Intent intentSetting=new Intent(TeacherMain.this,TeacherSetting.class);
                startActivity(intentSetting);
                break;
            case R.id.teacherTmap:
                Intent intentGps=new Intent(TeacherMain.this,TeacherTmap.class);
                startActivity(intentGps);
                break;
            case R.id.eatting:
                Intent intentEatting=new Intent(TeacherMain.this,TeacherEatting.class);
                startActivity(intentEatting);
                break;
            case R.id.notice:
                Intent intentNotice=new Intent(TeacherMain.this,TeacherNotice.class);
                startActivity(intentNotice);
                break;
            case R.id.calendar:
                Intent intentCalendar=new Intent(TeacherMain.this,CustomCalendarActivity.class);
                startActivity(intentCalendar);
                break;
            case R.id.album:
                Intent intentAlbum=new Intent(TeacherMain.this,TeacherAlbum.class);
                startActivity(intentAlbum);
                break;
            case R.id.attendance:
                Intent intentAttendance=new Intent(TeacherMain.this, AttendanceActivity.class);
                startActivity(intentAttendance);
                break;
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

    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 로그아웃 되고 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }
}