package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project.Holder.NoticeViewHolder;
import com.example.project.Model.Notice;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TeacherNotice extends AppCompatActivity {
    private RecyclerView list_recyclerview;
    FirebaseDatabase database;
    DatabaseReference noticedb;

    FirebaseRecyclerOptions<Notice> options;
    FirebaseRecyclerAdapter<Notice, NoticeViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_notice);

        database = FirebaseDatabase.getInstance();
        noticedb = database.getReference("Notice");

        list_recyclerview = (RecyclerView) findViewById(R.id.recycler_notice);
        list_recyclerview.setLayoutManager(new LinearLayoutManager(TeacherNotice.this));
        FloatingActionButton fab = findViewById(R.id.notice_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherNotice.this, NoticeWriteActivity.class);
                startActivity(intent);
            }
        });
        showTask();

    }

    private void showTask() {
        options = new FirebaseRecyclerOptions.Builder<Notice>()
                .setQuery(noticedb, Notice.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Notice, NoticeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final NoticeViewHolder holder, int i, @NonNull Notice notice) {
                holder.mMenuTextView.setText(notice.getNoticemenu());
                holder.mTitleTextView.setText(notice.getTitle());
                holder.mNameTextView.setText(notice.getName());
                holder.mContentsTextView.setText(notice.getContents());
                holder.mDateTextView.setText(notice.getDate());
                Glide.with(holder.itemView.getContext())
                        .load(notice.noticeImageUrl)
                        .apply(new RequestOptions())
                        .into(holder.imageView);
            }

            @NonNull
            @Override
            public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_notice,parent,false);

                return new NoticeViewHolder(view);
            }
        };
        list_recyclerview.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("수정"))
        {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals("삭제"))
        {
            deleteTask(adapter.getRef(item.getOrder()).getKey());
            Toast.makeText(TeacherNotice.this, "삭제완료", Toast.LENGTH_SHORT).show();
        }
        else if(item.getTitle().equals("보기"))
        {

        }

        return super.onContextItemSelected(item);
    }

    private void deleteTask(String key) {
        noticedb.child(key).removeValue();
    }

    private void showUpdateDialog(final String key, Notice item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("수정하기");
        builder.setMessage("");
        builder.setMessage("변경할 내용을 입력하세요.");

        //수정시간
        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy/MM/dd HH:mm");
        Date time = new Date();
        String time1 = format1.format(time);

        View update_layout = LayoutInflater.from(this).inflate(R.layout.customnotice_layout,null);

        final Spinner item_noticemenu_update = update_layout.findViewById(R.id.edit_update_spinner);
        final EditText item_title_update = update_layout.findViewById(R.id.edit_update_title);
        final TextView item_name_update = update_layout.findViewById(R.id.edit_update_name);
        final EditText item_content_update = update_layout.findViewById(R.id.edit_update_content);
        final TextView item_date_update = update_layout.findViewById(R.id.edit_update_date);

        //숨길부분
        final TextView item_menu_update = update_layout.findViewById(R.id.edit_update_menu);
        item_menu_update.setVisibility(View.INVISIBLE);
        final TextView item_url_update = update_layout.findViewById(R.id.edit_update_url);
        item_url_update.setVisibility(View.INVISIBLE);

        //수정스피너
        String[] str=getResources().getStringArray(R.array.notice_grade_array);
        item_noticemenu_update.setPrompt("반을 선택해주세요.");
        ArrayAdapter gradeAdapter= new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,str);
        item_noticemenu_update.setAdapter(gradeAdapter);
        item_noticemenu_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        item_menu_update.setText(item.getNoticemenu());
        item_title_update.setText(item.getTitle());
        item_name_update.setText(item.getName());
        item_content_update.setText(item.getContents());
        item_url_update.setText(item.getNoticeImageUrl());
        item_date_update.setText(time1);

        builder.setView(update_layout);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String noticemenu = item_noticemenu_update.getSelectedItem().toString();
                String title = item_title_update.getText().toString();
                String name = item_name_update.getText().toString();
                String content = item_content_update.getText().toString();
                String date = item_date_update.getText().toString();
                String noticeImageUrl = item_url_update.getText().toString();


                Notice notice = new Notice(noticemenu, title, name, content, date, noticeImageUrl);
                noticedb.child(key).setValue(notice);

                Toast.makeText(TeacherNotice.this, "수정완료", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
