package com.example.project.Holder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

public class EattingViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
    public TextView nowDate;
    public ImageView first_eat_image;
    public ImageView second_eat_image;
    public ImageView third_eat_image;
    public TextView eattingFirst;
    public TextView eattingSecond;
    public TextView eattingThrid;

    public EattingViewHolder(@NonNull View itemView) {

        super(itemView);
        nowDate =itemView.findViewById(R.id.nowDate);
        first_eat_image=itemView.findViewById(R.id.first_eat_image);
        second_eat_image=itemView.findViewById(R.id.second_eat_image);
        third_eat_image=itemView.findViewById(R.id.third_eat_image);
        eattingFirst=itemView.findViewById(R.id.TeattingFirst);
        eattingSecond=itemView.findViewById(R.id.TeattingSecond);
        eattingThrid=itemView.findViewById(R.id.TeattingThird);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("선택하기");
        menu.add(0, 0 , getAdapterPosition(), "수정");
        menu.add(0, 1 , getAdapterPosition(), "삭제");
    }
}
