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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project.Holder.EattingViewHolder;
import com.example.project.Model.Eatting;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TeacherEatting extends AppCompatActivity  {
    private RecyclerView list_recyclerview;
    FirebaseDatabase database;
    DatabaseReference eattingdb;

    FirebaseRecyclerOptions<Eatting> options;
    FirebaseRecyclerAdapter<Eatting, EattingViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_eatting);

        database = FirebaseDatabase.getInstance();
        eattingdb = database.getReference("Eating");

        list_recyclerview = (RecyclerView) findViewById(R.id.recycler_eatting);
        list_recyclerview.setLayoutManager(new LinearLayoutManager(TeacherEatting.this));
        FloatingActionButton actionButton = findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.floatingActionButton:
                        Intent actionButtonIntent = new Intent(TeacherEatting.this, EattingWriteActivity.class);
                        startActivity(actionButtonIntent);
                }
            }
        });
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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("수정")){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals("삭제"))
        {
            deleteTask(adapter.getRef(item.getOrder()).getKey());
            Toast.makeText(TeacherEatting.this, "삭제완료", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }
    private void deleteTask(String key) {
        eattingdb.child(key).removeValue();
    }

    private void showUpdateDialog(final String key, Eatting item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("수정하기");
        builder.setMessage("");
        builder.setMessage("변경할 내용을 입력하세요.");

        //수정시간
        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy.MM.dd 식단표");
        Date time = new Date();
        String time1 = format1.format(time);

        View update_layout = LayoutInflater.from(this).inflate(R.layout.customeatting_layout,null);

        final EditText item_first_update = update_layout.findViewById(R.id.edit_update_first);
        final EditText item_second_update = update_layout.findViewById(R.id.edit_update_second);
        final EditText item_third_update = update_layout.findViewById(R.id.edit_update_third);

        //숨길부분
        final TextView item_date_update = update_layout.findViewById(R.id.edit_update_dateeat);
        item_date_update.setVisibility(View.INVISIBLE);
        final TextView item_firsturl_update = update_layout.findViewById(R.id.edit_update_firsturl);
        item_firsturl_update.setVisibility(View.INVISIBLE);
        final TextView item_secondurl_update = update_layout.findViewById(R.id.edit_update_secondurl);
        item_secondurl_update.setVisibility(View.INVISIBLE);
        final TextView item_thirdurl_update = update_layout.findViewById(R.id.edit_update_thirdurl);
        item_thirdurl_update.setVisibility(View.INVISIBLE);


        item_first_update.setText(item.getFirst());
        item_second_update.setText(item.getSecond());
        item_third_update.setText(item.getThird());
        item_date_update.setText(time1);
        item_firsturl_update.setText(item.getFirstImageUrl());
        item_secondurl_update.setText(item.getSecondImageUrl());
        item_thirdurl_update.setText(item.getThirdImageUrl());


        builder.setView(update_layout);

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String first = item_first_update.getText().toString();
                String second = item_second_update.getText().toString();
                String thirl = item_third_update.getText().toString();
                String date = item_date_update.getText().toString();
                String firsturl = item_firsturl_update.getText().toString();
                String secondurl = item_secondurl_update.getText().toString();
                String thirdurl = item_thirdurl_update.getText().toString();


                Eatting eatting = new Eatting(date, firsturl, secondurl, thirdurl, first, second,thirl);
                eattingdb.child(key).setValue(eatting);

                Toast.makeText(TeacherEatting.this, "수정완료", Toast.LENGTH_SHORT).show();
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
