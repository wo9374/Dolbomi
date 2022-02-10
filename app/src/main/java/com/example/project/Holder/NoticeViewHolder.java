package com.example.project.Holder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

public class NoticeViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    public TextView mMenuTextView;
    public TextView mTitleTextView;
    public TextView mNameTextView;
    public TextView mContentsTextView;
    public TextView mDateTextView;
    public ImageView imageView;


    public NoticeViewHolder(View view) {
        super(view);
        mMenuTextView = (TextView)view.findViewById(R.id.notice_menu_text);
        mTitleTextView = (TextView)view.findViewById(R.id.notice_title_text);
        mNameTextView = (TextView)view.findViewById(R.id.notice_name_text);
        mContentsTextView = (TextView)view.findViewById(R.id.notice_contents_text);
        mDateTextView = (TextView)view.findViewById(R.id.notice_date_text);
        imageView = (ImageView)view.findViewById(R.id.noticebtn);

        view.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("선택하기");
        menu.add(0, 0 , getAdapterPosition(), "수정");
        menu.add(0, 1 , getAdapterPosition(), "삭제");
        menu.add(0, 2 , getAdapterPosition(), "보기");
    }
}
