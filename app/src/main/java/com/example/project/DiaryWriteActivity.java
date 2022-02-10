package com.example.project;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Model.Diary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DiaryWriteActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mWriteTitleText;
    private EditText mWriteContentsText;
    private TextView mWriteNameText;
    private Button mWritebtn;
    private String uid;
    private Diary mDiary;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_diary_activity);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        // 윈도우 매니저 객체 얻어오고 디스플레이 객체 얻어오기
        int width = (int) (display.getWidth() * 0.9);
        // 얻어온 화면의 폭의 90프로만큼 width에 지정
        int height = (int) (display.getHeight() * 0.45);
        // 얻어온 화면의 높이의 45프로 만큼 height에 지정
        getWindow().getAttributes().width = width;
        //write_diary_activity 레이아웃의 폭을 width로 지정
        getWindow().getAttributes().height = height;
        //write_diary_activity 레이아웃의 폭을 height 지정
        getWindow().setGravity(Gravity.CENTER);
        //write_diary_activity 레이아웃 센터지정

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mWriteTitleText=findViewById(R.id.write_title_text);
        mWriteContentsText=findViewById(R.id.write_contents_text);
        mWriteNameText=findViewById(R.id.write_name_text);


        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                mWriteNameText.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        findViewById(R.id.write_upload_btn).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        //사용자가 입력하는 제목,내용,이름을 가져온다
        String title = mWriteTitleText.getText().toString().trim();
        String contents = mWriteContentsText.getText().toString().trim();
        String name = mWriteNameText.getText().toString().trim();


        //제목, 내용, 이름이 비었는지 아닌지를 체크 한다.
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "제목을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(contents)) {
            Toast.makeText(this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Diary diary = new Diary();
        diary.title = mWriteTitleText.getText().toString();
        diary.contents = mWriteContentsText.getText().toString();
        diary.name = mWriteNameText.getText().toString();


        FirebaseDatabase.getInstance().getReference().child("Diary").push().setValue(diary);
        Toast.makeText(this,"알림장이 추가되었습니다.",Toast.LENGTH_SHORT).show();
        DiaryWriteActivity.this.finish();
    }
}