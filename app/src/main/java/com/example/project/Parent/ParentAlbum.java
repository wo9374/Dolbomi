package com.example.project.Parent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project.Holder.AlbumViewHolder;
import com.example.project.Model.Album;
import com.example.project.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ParentAlbum extends AppCompatActivity {
    private RecyclerView album_recyclerview;

    FirebaseDatabase database;
    DatabaseReference albumdb;

    FirebaseRecyclerOptions<Album> options;
    FirebaseRecyclerAdapter<Album, AlbumViewHolder> adapter;

    public ImageView ex_image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_album);

        database = FirebaseDatabase.getInstance();
        albumdb = database.getReference("Album");

        album_recyclerview=(RecyclerView)findViewById(R.id.parent_gallery1) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(ParentAlbum.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        album_recyclerview.setLayoutManager(layoutManager);

        album_recyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if(e.getAction()==MotionEvent.ACTION_DOWN){
                    View child=rv.findChildViewUnder(e.getX(),e.getY());
                    ImageView tv=(ImageView)rv.getChildViewHolder(child).itemView.findViewById(R.id.select_image);
                    ex_image.setImageDrawable(tv.getDrawable());
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        ex_image=(ImageView)findViewById(R.id.ex_image);
        showTask();



    }//onCreate end

    private void showTask(){
        options = new FirebaseRecyclerOptions.Builder<Album>()
                .setQuery(albumdb, Album.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<Album, AlbumViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AlbumViewHolder holder, int i, @NonNull Album album) {
                Glide.with(holder.mAlbumImageView.getContext())
                        .load(album.albumImageUri)
                        .apply(new RequestOptions())
                        .into(holder.mAlbumImageView);
            }

            @NonNull
            @Override
            public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_album,parent,false);
                return new AlbumViewHolder(view);
            }
        };
        album_recyclerview.setAdapter(adapter);
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