package com.example.project;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class CustomCalendarView extends LinearLayout {
    ImageButton NextButton,PreviousButton;
    TextView CurrentDate;
    GridView gridView;
    private static final  int MAX_CALENDAR_DAYS=42;
    Calendar calendar= Calendar.getInstance(Locale.KOREAN);
    Context context;
    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy년 MM월"  ,Locale.KOREAN);
    SimpleDateFormat monthFormat=new SimpleDateFormat("MMMM",Locale.KOREAN);
    SimpleDateFormat yearFormate=new SimpleDateFormat("yyyy",Locale.KOREAN);
    SimpleDateFormat eventDateFormate=new SimpleDateFormat("yyyy-MM-dd",Locale.KOREAN);
    MyGridAdpater myGridAdpater;
    AlertDialog alertDialog;
    List<Date> dates=new ArrayList<>();
    List<Events> eventsList=new ArrayList<>();
    DBOpenHelper dbOpenHelper;
    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

        IntializeLayout();
        SetUpCalendar();


        PreviousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,-1);
                SetUpCalendar();
            }
        });

        NextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,1);
                SetUpCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView=LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout,null);
                final EditText EventName=addView.findViewById(R.id.eventname);
                final TextView EventTime=addView.findViewById(R.id.eventtime);
                ImageButton SetTime=addView.findViewById(R.id.seteventtime);
                Button AddEvent=addView.findViewById(R.id.addevent);
                SetTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar=Calendar.getInstance();
                        int hours=calendar.get(Calendar.HOUR_OF_DAY);
                        int minuts=calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog=new TimePickerDialog(addView.getContext(), R.style.DialogTheme
                                , new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar c=Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                c.set(Calendar.MINUTE,minute);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hformate=new SimpleDateFormat("K:mm a",Locale.KOREAN);
                                String event_Time=hformate.format(c.getTime());
                                EventTime.setText(event_Time);
                            }
                        },hours,minuts,false);
                        timePickerDialog.show();
                    }
                });
                final String date=eventDateFormate.format(dates.get(position));
                final String month=monthFormat.format(dates.get(position));
                final String year=yearFormate.format(dates.get(position));

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(), EventTime.getText().toString(),date,month,year);
                        SetUpCalendar();
                        alertDialog.dismiss();
                    }
                });
                builder.setView(addView);
                alertDialog=builder.create();
                alertDialog.show();
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String date=eventDateFormate.format(dates.get(i));
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView=LayoutInflater.from(getContext()).inflate(R.layout.show_events_layout,null);

                TextView Events=showView.findViewById(R.id.eventname);
                TextView Time=showView.findViewById(R.id.eventtime);
                TextView Date=showView.findViewById(R.id.eventdate);
                RecyclerView recyclerView=showView.findViewById(R.id.EventsRV);
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapter eventRecylerAdapter=new EventRecyclerAdapter(showView.getContext()
                        ,CollectEventByDate(date));
                recyclerView.setAdapter(eventRecylerAdapter);
                eventRecylerAdapter.notifyDataSetChanged();

                builder.setView(showView);
                alertDialog =builder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        SetUpCalendar();
                    }
                });
                return true;

            }
        });

    }
    private ArrayList<Events> CollectEventByDate(String date){
        ArrayList<Events> arratList=new ArrayList<>();
        dbOpenHelper=new DBOpenHelper(context);
        SQLiteDatabase database=dbOpenHelper.getReadableDatabase();
        Cursor cursor=dbOpenHelper.ReadEvents(date,database);
        while (cursor.moveToNext()){
            String event=cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time=cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String Date=cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month=cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String Year=cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events=new Events(event,time,Date,month,Year);
            arratList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();

        return arratList;
    }
    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    private void SaveEvent(String event,String time,String date,String month,String year){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database=dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,date,month,year,database);
        dbOpenHelper.close();
        Toast.makeText(context,"일정 저장",Toast.LENGTH_SHORT).show();
    }

    private void IntializeLayout(){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.calendar_layout,this);
        NextButton=view.findViewById(R.id.nextBtn);
        PreviousButton=view.findViewById(R.id.previousBtn);
        CurrentDate=view.findViewById(R.id.current_Date);
        gridView=view.findViewById(R.id.gridView);
    }

    private void SetUpCalendar(){
        String currwntDate=dateFormat.format(calendar.getTime());
        // 초기화 못한 문제 같은데 모르겠음
        CurrentDate.setText(currwntDate);
        dates.clear();
        Calendar monthCalendar=(Calendar)calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayofMonth=monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH,-FirstDayofMonth);

        CollectEventPerMonth(monthFormat.format(calendar.getTime()),yearFormate.format(calendar.getTime()));

        while (dates.size()<MAX_CALENDAR_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);

        }

        myGridAdpater=new MyGridAdpater(context,dates,calendar,eventsList);
        gridView.setAdapter(myGridAdpater);
    }

    private void CollectEventPerMonth(String Month,String year){
        eventsList.clear();
        dbOpenHelper=new DBOpenHelper(context);
        SQLiteDatabase database=dbOpenHelper.getReadableDatabase();
        Cursor cursor=dbOpenHelper.ReadEventsperMonth(Month,year,database);
        while(cursor.moveToNext()){
            String event=cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time=cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String date=cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month=cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String Year=cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events=new Events(event,time,date,month,Year);
            eventsList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();
    }
}
