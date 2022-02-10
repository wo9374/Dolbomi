package com.example.project.Holder;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.R;

public class AlbumViewHolder extends RecyclerView.ViewHolder{
    public ImageView mAlbumImageView;

    public AlbumViewHolder(View itemview){
        super(itemview);
        mAlbumImageView=(ImageView)itemview.findViewById(R.id.select_image);

    }


}
