package com.example.project.Parent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project.Holder.NoticeViewHolder;
import com.example.project.Model.Notice;
import com.example.project.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ParentNotice extends AppCompatActivity {
    private RecyclerView list_recyclerview;
    FirebaseDatabase database;
    DatabaseReference noticedb;

    FirebaseRecyclerOptions<Notice> options;
    FirebaseRecyclerAdapter<Notice, NoticeViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_notice);

        database = FirebaseDatabase.getInstance();
        noticedb = database.getReference("Notice");

        list_recyclerview = (RecyclerView) findViewById(R.id.parent_recycler_notice);
        list_recyclerview.setLayoutManager(new LinearLayoutManager(ParentNotice.this));

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

}