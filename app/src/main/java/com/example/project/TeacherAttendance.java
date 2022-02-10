package com.example.project;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Holder.AttendanceViewHolder;
import com.example.project.Holder.DiaryViewHolder;
import com.example.project.Model.Diary;
import com.example.project.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class TeacherAttendance extends Activity {
    TextView date;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    private RecyclerView list_recyclerview;
    private RecyclerView.Adapter mAdapter;
    TextView attendanceitem_textview;
    Button attendance_save;
    CheckBox attendance_checkBox;
    List<User> iusers = null;
    //    private List<Student> studentList;
    private  User UserModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_attendance);
        date = findViewById(R.id.tvSelectedDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        attendanceitem_textview=findViewById(R.id.attendanceitem_textview);
        attendance_save=findViewById(R.id.attendance_save);

        datePickerDialog = new DatePickerDialog(TeacherAttendance.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        date.setText(year + "년" + (month + 1) + "월" + day + "일");
                        new FirebaseDatabaseHelper().readUser(new FirebaseDatabaseHelper.DataStatus() {
                            @Override
                            public void DataIsLoaded(List<User> users, List<String> keys) {
                                new AttendanceViewHolder().setConfig(list_recyclerview,TeacherAttendance.this,users,keys

                                );

                                iusers = users;
                            }

                            @Override
                            public void DataIsInserted() {

                            }

                            @Override
                            public void DataIsUpdated() {

                            }

                            @Override
                            public void DataIsDeleted() {

                            }
                        });{

                        }
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
        list_recyclerview=(RecyclerView)findViewById(R.id.attendance_recyclerView);
        attendance_checkBox=findViewById(R.id.attendance_checkBox);
        iusers = new ArrayList<User>();
//        list_recyclerview = new AttendanceViewHolder(iusers);

        // set the adapter object to the Recyclerview
//        list_recyclerview.setAdapter(mAdapter);

        attendance_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<iusers.size();i++){
                    User checkBox=iusers.get(i);
                    if(checkBox.isCheckBox()==true){
                        System.out.println(iusers.get(i).getUsername());
                        Log.d("이메일 : ", iusers.get(i).getEmail());
                        Log.d("토큰 : ", iusers.get(i).getPushToken());
                        String token = iusers.get(i).getPushToken();
                        String title = "돌보미어린이집입니다.";
                        String body = "아이가 출석을 완료했습니다.";
                        try {
                            String result = new PushMsgTask().execute(token, title, body).get();
                            Log.i("통신결과값", result + " ");
                        } catch (Exception e) {
                            Log.e("통신오류", e.toString());
                        }
                    }

                }
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();

                                // Log and toast
                                String msg = getString(R.string.msg_token_fmt, token);
                                Log.d(TAG, msg);

                                Toast.makeText(TeacherAttendance.this, "알림을 보냈습니다.", Toast.LENGTH_SHORT).show();
                                System.out.println(token);
                            }

                        });


                //파이어베이스 pushtoken값 넣어줬음
//                String token = "e_GyalRzCvc:APA91bEluQoubf9YAJqQyjofM_HDojx1UzgLvBsHQPJAFWjSjnSwUWmyBWL-Pejyumc-HW7PU3-teD4m_xyER5-hSQN0ubBbLCDYyD47TfuFzBlDsNidIqEiCXTJpk-UP-7q-ssDUexx";
//                String title = "돌보미어린이집입니다.";
//                String body = "아이가 출석을 완료했습니다.";
//                try {
//                    String result = new PushMsgTask().execute(token, title, body).get();
//                    Log.i("통신결과값", result + " ");
//                } catch (Exception e) {
//                    Log.e("통신오류", e.toString());
//                }
//                PendingIntent pendingIntent=PendingIntent.getActivity(TeacherAttendance.this,0
//                ,new Intent(getApplicationContext(),TeacherAttendance.class),
//                        PendingIntent.FLAG_CANCEL_CURRENT
//                );
//                NotificationCompat.Builder builder=new NotificationCompat.Builder(TeacherAttendance.this)
//                        .setSmallIcon(R.drawable.childicon1)
//                        .setContentTitle("공지!")
//                        .setContentText("출석")
//                        .setDefaults(Notification.DEFAULT_VIBRATE)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .setAutoCancel(true)
//                        .setContentIntent(pendingIntent);
//
//                NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//                notificationManager.notify(1,builder.build());

                Intent intent=new Intent(TeacherAttendance.this, TeacherMain.class);
                startActivity(intent);
            }
        });
//        sendGcm();
    }
}
