package com.example.project.Parent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Holder.DiaryViewHolder;
import com.example.project.Model.Diary;
import com.example.project.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ParentDiary extends AppCompatActivity {
    private RecyclerView list_recyclerview;
    FirebaseDatabase database;
    DatabaseReference diarydb;

    FirebaseRecyclerOptions<Diary> options;
    FirebaseRecyclerAdapter<Diary, DiaryViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_diary);

        database = FirebaseDatabase.getInstance();
        diarydb = database.getReference("Diary");

        list_recyclerview = (RecyclerView) findViewById(R.id.parent_recycler_diary);
        list_recyclerview.setLayoutManager(new LinearLayoutManager(ParentDiary.this));

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
}