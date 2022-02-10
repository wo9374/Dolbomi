package com.example.project.Holder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

public class DiaryViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView list_title;
        public TextView list_content;
        public TextView list_name;


        public DiaryViewHolder(View view) {
            super(view);
            list_title = (TextView)view.findViewById(R.id.item_title_text);
            list_content = (TextView)view.findViewById(R.id.item_contents_text);
            list_name = (TextView)view.findViewById(R.id.item_name_text);

            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("선택하기");
            menu.add(0, 0 , getAdapterPosition(), "수정");
            menu.add(0, 1 , getAdapterPosition(), "삭제");
        }
    }

