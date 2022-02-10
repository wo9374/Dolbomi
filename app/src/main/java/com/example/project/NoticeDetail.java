package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeDetail extends AppCompatActivity {
    public TextView mdetailMenuTextView;
    public TextView mdetailTitleTextView;
    public TextView mdetailNameTextView;
    public TextView mdetailContentsTextView;
    public TextView mdetailDateTextView;
    public ImageView detailimageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        mdetailMenuTextView = (TextView)findViewById(R.id.notice_menu_text);
        mdetailTitleTextView = (TextView)findViewById(R.id.notice_title_text);
        mdetailNameTextView = (TextView)findViewById(R.id.notice_name_text);
        mdetailContentsTextView = (TextView)findViewById(R.id.notice_contents_text);
        mdetailDateTextView = (TextView)findViewById(R.id.notice_date_text);
        detailimageView = (ImageView)findViewById(R.id.noticebtn);
    }
}
