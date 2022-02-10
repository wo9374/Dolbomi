package com.example.project;

import android.Manifest;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class gps extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    TextView txtResult=null;
    double longitude;
    double latitude;
    double altitude;
    String provider;
    GpsTracker gpsTracker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps);
        txtResult=(TextView)findViewById(R.id.txtResult);
        // 권한 설정.
        // https://gun0912.tistory.com/61
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(gps.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("돌보미 앱 사용을 위해선 다음과 같은 권한이 필요합니다.")
                .setDeniedMessage("권한을 거부하시면 이용이 불가능합니다. [설정] > [권한] 에서 권한을 허용해주세요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();

        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        while (true) {
//            gpsTracker = new GpsTracker(this);
//            latitude = gpsTracker.getLatitude();
//            longitude = gpsTracker.getLongitude();
//        }
        //실시간으로 위치 보이게 한다.

//        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//
//
//        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        if ( Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//            ActivityCompat.requestPermissions( gps.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
//                    0 );
//        }
//        else{
//            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            provider = location.getProvider();
//            longitude = location.getLongitude();
//            latitude = location.getLatitude();
//            altitude = location.getAltitude();
//            txtResult.setText("위치정보 : " + provider + "\n" +
//                    "위도 : " + longitude + "\n" +
//                    "경도 : " + latitude + "\n" +
//                    "고도  : " + altitude);
//
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    1000,
//                    1,
//                    gpsLocationListener);
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                    1000,
//                    1,
//                    gpsLocationListener);
//        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 구글 맵 객체를 불러온다.

        mMap = googleMap;

        gpsTracker = new GpsTracker(this);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        Log.i("위치", "lat:"+latitude+"lng:"+longitude);

        // 현재 위치에 대한 위치 설정
        LatLng myPosition = new LatLng(latitude, longitude);
        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions makerOptions = new MarkerOptions()
                .position(myPosition)
                .title("차량위치");
//        makerOptions
//                .position(seoul)
//                .title("원하는 위치(위도, 경도)에 마커를 표시했습니다.");

        // 구글지도(지구) 에서의 zoom 레벨은 1~23 까지 가능합니다.
        // 여러가지 zoom 레벨은 직접 테스트해보세요
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
        mMap.animateCamera(zoom);   // moveCamera 는 바로 변경하지만,
        // animateCamera() 는 근거리에선 부드럽게 변경합니다
        // 마커를 생성한다.
        mMap.addMarker(makerOptions);

        //카메라를 현재 위치로 옮긴다.,지도의 배율 표시
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition,17));

    }
    final LocationListener gpsLocationListener=new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {
            provider = location.getProvider();
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            altitude = location.getAltitude();

            txtResult.setText("위치정보 : " + provider + "\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude + "\n" +
                    "고도  : " + altitude);
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
}
