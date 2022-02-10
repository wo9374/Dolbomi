package com.example.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class TeacherTmap extends AppCompatActivity {
    TMapView tMapView;
    double latitude;
    double longitude;
    TextView time;
    private static final String TAG = "MainActivity";
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_tmap);
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("3f208364-4dd5-40b5-8267-c2fe51d464c4");
        linearLayoutTmap.addView(tMapView);
        tMapView.setIconVisibility(true);   //현재위치로 표시될 아이콘을 표시할지 여부를 설정합니다
        setGps();
        tMapView.setZoom(23);
        float []distance=new float[2];
        float actual_distance;
        Location.distanceBetween(latitude,longitude,35.857742,128.620717,distance);
        actual_distance=distance[0];
        System.out.println(distance);
        Log.d(TAG, String.valueOf(distance));

        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //Toast.makeText(MapEvent.this, "onPress~!", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                //Toast.makeText(MapEvent.this, "onPressUp~!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(TeacherTmap.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("돌보미 앱 사용을 위해선 다음과 같은 권한이 필요합니다.")
                .setDeniedMessage("권한을 거부하시면 이용이 불가능합니다. [설정] > [권한] 에서 권한을 허용해주세요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }


    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                tMapView.setLocationPoint(longitude, latitude);
                tMapView.setCenterPoint(longitude, latitude);


            }
            final TMapPoint tMapPointStart = new TMapPoint(latitude, longitude); // 현재위치(출발지)
            final TMapPoint tMapPointEnd = new TMapPoint(35.857742, 128.620717); // 유치원


            try {
                AsyncTask<String, Void, TeacherTmap> asyncTask = new AsyncTask<String, Void, TeacherTmap>() {

                    @SuppressLint("WrongThread")
                    @Override
                    protected TeacherTmap doInBackground(String... url) {
                        // 때력박는다.
                        TMapPolyLine tMapPolyLine = null;
                        try {
                            tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        }
                        tMapPolyLine.setLineColor(Color.BLUE);
                        tMapPolyLine.setLineWidth(2);
                        tMapView.addTMapPolyLine("Line1", tMapPolyLine);
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                        TMapData tMapData = new TMapData();
                        tMapData.findPathData(tMapPointStart, tMapPointEnd, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                Log.d("test", "거리 :" + tMapPolyLine.getDistance());
                                ((TextView)findViewById(R.id.tmaptextv)).setText("총 거리 : "+String.format("%.2f",tMapPolyLine.getDistance()/1000)+"KM");
                                ((TextView)findViewById(R.id.tmaptime)).setText("도착예정 시간: "+String.format("%.0f",tMapPolyLine.getDistance()/1000/60*50)+"분입니다.");
                            }
                        });

                        StrictMode.setThreadPolicy(policy);
                        return null;
                    }
                };

                asyncTask.execute().get(); // 쓰래드 run 같은 느낌



                //메인쓰레드 처리작업 비동기화 방식으로 변경

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    public void setGps() {

        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

