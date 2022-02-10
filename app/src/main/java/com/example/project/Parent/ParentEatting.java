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
import com.example.project.Holder.EattingViewHolder;
import com.example.project.Model.Eatting;
import com.example.project.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ParentEatting extends AppCompatActivity {
    private RecyclerView list_recyclerview;
    FirebaseDatabase database;
    DatabaseReference eattingdb;

    FirebaseRecyclerOptions<Eatting> options;
    FirebaseRecyclerAdapter<Eatting, EattingViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_eatting);

        database = FirebaseDatabase.getInstance();
        eattingdb = database.getReference("Eating");

        list_recyclerview = (RecyclerView) findViewById(R.id.parent_recycler_eatting);
        list_recyclerview.setLayoutManager(new LinearLayoutManager(ParentEatting.this));

        showTask();

    }

    private void showTask() {
        options = new FirebaseRecyclerOptions.Builder<Eatting>()
                .setQuery(eattingdb, Eatting.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Eatting, EattingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EattingViewHolder holder, int i, @NonNull Eatting eatting) {
                holder.nowDate.setText(eatting.getNowtime());
                Glide.with(holder.itemView.getContext())
                        .load(eatting.firstImageUrl)
                        .apply(new RequestOptions())
                        .into(holder.first_eat_image);
                Glide.with(holder.itemView.getContext())
                        .load(eatting.secondImageUrl)
                        .apply(new RequestOptions())
                        .into(holder.second_eat_image);
                Glide.with(holder.itemView.getContext())
                        .load(eatting.thirdImageUrl)
                        .apply(new RequestOptions())
                        .into(holder.third_eat_image);
                holder.eattingFirst.setText(eatting.getFirst());
                holder.eattingSecond.setText(eatting.getSecond());
                holder.eattingThrid.setText(eatting.getThird());
            }

            @NonNull
            @Override
            public EattingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_eatting,parent,false);

                return new EattingViewHolder(view);
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