package com.example.testdvasnaver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PolylineOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final Marker marker = new Marker();
    private final Marker marker1 = new Marker();
    private final Marker marker2 = new Marker();
    private final Marker marker3 = new Marker();
    private final Marker marker4 = new Marker();
    private final Marker marker5 = new Marker();
    private final Marker marker6 = new Marker();
    private final Marker marker7 = new Marker();
    private final Marker marker8 = new Marker();
    private final Marker marker9 = new Marker();
    private final Marker marker10 = new Marker();

    CountDownTimer timerLoLa, timerCircle;

    private FusedLocationSource locationSource;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private NaverMap mNaverMap;

    double longitude, latitude, altitude;

    //gps ??????
    LocationManager locManager;
    Location mLoastLoction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NaverMapSdk.getInstance(this).setClient(new NaverMapSdk.NaverCloudPlatformClient("q1tzu1x325"));

        locationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);

        MapView MapView = (com.naver.maps.map.MapView) findViewById(R.id.mapView);
        MapView.getMapAsync(this);


        gpsCoordinate();

        timerLoLa = new CountDownTimer(9000000, 1000) {
            @Override
            public void onTick(long l) {
                Log.e("??????", "" + latitude);
                Log.e("??????", "" + longitude);
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        this.mNaverMap = naverMap;

        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(
                        35.1477, 129.0674),
                17
        );

        naverMap.setCameraPosition(cameraPosition);

        insert_marker();





        //????????? ?????????
        PolylineOverlay polyline = new PolylineOverlay();
        polyline.setCoords(Arrays.asList(
                new LatLng(37.57152, 126.97714),
                new LatLng(37.56607, 126.98268),
                new LatLng(37.55855, 126.97822)
        ));
        polyline.setMap(naverMap);


        timerCircle = new CountDownTimer(900000, 1000) {
            @Override
            public void onTick(long l) {
                CircleOverlay circle = new CircleOverlay();
                circle.setCenter(new LatLng(latitude, longitude));
                circle.setRadius(1);
                circle.setColor(Color.GREEN);
                circle.setMap(naverMap);
            }

            @Override
            public void onFinish() {

            }
        }.start();


        mNaverMap = naverMap;
        mNaverMap.setLocationSource(locationSource);
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);

        //???????????? ?????? ?????? ??? ?????? ?????????
//        naverMap.addOnLocationChangeListener(location ->
//                Toast.makeText(this,
//                        location.getLatitude() + ", " + location.getLongitude(),
//                        Toast.LENGTH_SHORT).show());


        UiSettings uiSettings = mNaverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(false);
        LocationButtonView locationButtonView = findViewById(R.id.location);
        locationButtonView.setMap(mNaverMap);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //?????? ?????? ??????
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // ?????? ???????????? ??????????????? ????????? ??? ??????????????? onLocationChanged()????????? ?????? ??????????????? ????????? ????????? ?????? ???????????????.
            String provider = location.getProvider();  // ????????????
            longitude = location.getLongitude(); // ??????
            latitude = location.getLatitude(); // ??????
            altitude = location.getAltitude(); // ??????

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    public void gpsCoordinate() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLoastLoction = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                1,
                gpsLocationListener);
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000,
                1,
                gpsLocationListener);

    }

    public void setMarker(Marker marker, double lat, double lng, String text) {

        // ?????? ??????
        marker.setWidth(Marker.SIZE_AUTO);
        marker.setHeight(Marker.SIZE_AUTO);

        // ????????? ??????
        marker.setIconPerspectiveEnabled(true);

        // ????????? ??????
        marker.setIcon(OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_red));

        // ????????? ?????????
        marker.setAlpha(0.7f);

        // ?????? ??????
        marker.setPosition(new LatLng(lat, lng));

        // ?????? ????????????
        marker.setZIndex(0);

        // ?????? ?????????
        marker.setCaptionText(text);

        // ?????? ??????
        marker.setMap(mNaverMap);

    }


    public void insert_marker() {

        // json ?????? ???????????? ????????????
        // assets ????????? ????????? ???????????? ??????
        // ??????????????? (AssetManager) ????????????
        AssetManager assetManager = getAssets();

        // assets/test.json ?????? ?????? ?????? InputStream
        try {
            InputStream is = assetManager.open("node_Busan.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            String line = reader.readLine();

            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }

            String jsonData = buffer.toString();

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(jsonData);

            // features ????????????
            JSONArray parse_features = (JSONArray) obj.get("features");

            // features ?????? ?????? ??????
            String name = "";
            double o1 = 0;
            double o2 = 0;

            for (int i = 0; i < parse_features.size(); i++) {
                JSONObject jsonObject = (JSONObject) parse_features.get(i);

                // properties ????????????
                JSONObject parse_properties = (JSONObject) jsonObject.get("properties");

                // geometry ????????????
                JSONObject parse_geometry = (JSONObject) jsonObject.get("geometry");

                // coordinates ?????? ?????? ????????????
                JSONArray parse_coordinates = (JSONArray) parse_geometry.get("coordinates");


                // ????????? ??????
                name = (String) parse_properties.get("NODE_NAME");

                // ??????
                o1 = (double) parse_coordinates.get(1);

                // ??????
                o2 = (double) parse_coordinates.get(0);

                // ?????? ?????????
                setMarker(new Marker(), o1, o2, name);

            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }



}
