package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Holder.DiaryViewHolder;
import com.example.project.Model.Diary;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherDiary extends AppCompatActivity {
    private RecyclerView list_recyclerview;
    FirebaseDatabase database;
    DatabaseReference diarydb;

    FirebaseRecyclerOptions<Diary> options;
    FirebaseRecyclerAdapter<Diary, DiaryViewHolder>  adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_diary);

        database = FirebaseDatabase.getInstance();
        diarydb = database.getReference("Diary");

        list_recyclerview = (RecyclerView) findViewById(R.id.recycler_eatting);
        list_recyclerview.setLayoutManager(new LinearLayoutManager(TeacherDiary.this));
        FloatingActionButton fab = findViewById(R.id.write_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherDiary.this, DiaryWriteActivity.class);
                startActivity(intent);
            }
        });

        showTask();
    }

    private void showTask() {
        options = new FirebaseRecyclerOptions.Builder<Diary>()
                .setQuery(diarydb, Diary.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Diary, DiaryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DiaryViewHolder holder, int position, @NonNull Diary diary) {
                holder.list_title.setText(diary.getTitle());
                holder.list_content.setText(diary.getContents());
                holder.list_name.setText(diary.getName());
            }

            @NonNull
            @Override
            public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_diary,parent,false);

                return new DiaryViewHolder(view);
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

        if(item.getTitle().equals("수정")){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals("삭제"))
        {
            deleteTask(adapter.getRef(item.getOrder()).getKey());
            Toast.makeText(TeacherDiary.this, "삭제완료", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    private void deleteTask(String key) {
        diarydb.child(key).removeValue();
    }

    private void showUpdateDialog(final String key, Diary item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("수정하기");
        builder.setMessage("");
        builder.setMessage("변경할 내용을 입력하세요.");

        View update_layout = LayoutInflater.from(this).inflate(R.layout.customdiary_layout,null);

        final EditText item_title_update = update_layout.findViewById(R.id.edit_update_title);
        final EditText item_content_update = update_layout.findViewById(R.id.edit_update_content);
        final TextView item_name_update = update_layout.findViewById(R.id.edit_update_date);

        item_title_update.setText(item.getTitle());
        item_content_update.setText(item.getContents());
        item_name_update.setText(item.getName());

        builder.setView(update_layout);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title = item_title_update.getText().toString();
                String content = item_content_update.getText().toString();
                String name = item_name_update.getText().toString();

                Diary diary = new Diary(title, content, name);
                diarydb.child(key).setValue(diary);

                Toast.makeText(TeacherDiary.this, "수정완료", Toast.LENGTH_SHORT).show();
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