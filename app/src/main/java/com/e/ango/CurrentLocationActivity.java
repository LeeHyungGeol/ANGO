package com.e.ango;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.ango.API.AirWeatherTask;
import com.e.ango.API.PlayTask;
import com.e.ango.API.Play.PlayObject;
import com.e.ango.ListView.ListAdapter;
import com.e.ango.ListView.ListData;
import com.e.ango.Recommend.RecommendTask;
import com.e.ango.SurveyConnection.LoadImage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CurrentLocationActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    //    private Marker currentMarker = null;
    private Marker playMarker = null; /////////////////////////////////////////////////

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;


    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소

    Location mCurrentLocatiion;
    LatLng currentPosition;


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    // (참고로 Toast에서는 Context가 필요했습니다.)


    //////////////////////////////////////////////////////////////////놀거리 추천
    //날씨 출력 뷰
    public static TextView tv_Humidity, tv_Weather, tv_Temperature, tv_Air;
    public static ImageView iv_weatherIcon; // 날씨 아이콘 출력 뷰
//    private int drawableIcon = R.drawable.untitled3; // 날씨 아이콘 지

    public String currentLatitude, currentLongitude; // 현재 위치 위도, 경도

    private String cityName; // 지오코더로 건너받은 영어 전체 주소
    private String weather_type; // 날씨 타입

    private ArrayList<PlayObject> originalPlayObjects; // 전체 파싱 리스트
    private ArrayList<PlayObject> userPreferencePlayObjects; // 유저에게 제공할 추천 리스트

    //추천 놀거리 띄워줄 리스트 뷰
    public static ListView printList = null;

    private boolean flag_coordinate = false; // 좌표 받아 오면 true
    private int weatherTypeInt;
    private Bitmap bitmap;

    boolean flag_userPreferencePlayObjects;///////////////////////////////////////
    ArrayList<ListData> arrListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mLayout = findViewById(R.id.layout_main);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // .setInterval(UPDATE_INTERVAL_MS) // 현위치를 계속 업데이트 해주는데 지금은 필요x
        // .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ////////////////////////////////////////////////////놀거리 추천
        // 습도, 온도, 날씨, 미세먼지 띄워주는 텍스트뷰
        tv_Humidity = (TextView) findViewById(R.id.tv_humidity);
        tv_Temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_Weather = (TextView) findViewById(R.id.tv_weather);
        tv_Air = (TextView) findViewById(R.id.tv_air);

        //해당 날씨에 따른 아이콘 이미지뷰
        iv_weatherIcon = (ImageView) findViewById(R.id.iv_WeatherIcon);

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        Log.d(TAG, "onMapReady :");

        mMap = googleMap;

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            startLocationUpdates(); // 3. 위치 업데이트 시작


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions(CurrentLocationActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d(TAG, "onMapClick :");
            }
        });
    }


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Log.d(TAG , "locationCallback");
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());

                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet);

                //현재 위치
                setCurrentLocation(location);
                mCurrentLocatiion = location;

                ////////////////////////////////////놀거리 추천 1
                //현재위치 경도, 위도
                currentLatitude = String.valueOf(location.getLatitude());
                currentLongitude = String.valueOf(location.getLongitude());


                System.out.println("CURRENT : " + currentLongitude);
                if(currentLatitude != null && currentLongitude != null) flag_coordinate = true;
                if(flag_coordinate) getPlayLocation(currentLatitude, currentLongitude);
                System.out.println("FLAG_COORDINATES FIRST : " + flag_coordinate);
                //userPreferencePlayForMarker = new UserPreferencePlayForMarker(currentLatitude, currentLongitude);
            }
        }
    };


    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {
            System.out.println("startLocationUpdates");

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        } else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);


            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }

            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

//            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mMap.setMyLocationEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        }

    }

    @Override
    protected void onStart() {

        Log.d(TAG ,"onStart");
        super.onStart();

        Log.d(TAG, "onStart");

        if(mMap == null) {
            if (checkPermission()) {

                Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

                if (mMap != null)
                    mMap.setMyLocationEnabled(true);

            }
        }
        System.out.println("1");
    }


    @Override
    protected void onStop() {
        Log.d(TAG ,"onStop");

        super.onStop();

        if (mFusedLocationClient != null) {
            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }

    }


    public void getPlayLocation(String currentLatitude, String currentLongitude) {
        System.out.println("GETCURRENTADDRESS 4");
        Log.d(TAG ,"getCurrentAddress 2");

        ////////////////////////////////////놀거리 추천 2
        //flag_coordinate == true == 좌표 여부
        System.out.println("FLAG_COORDINATES  : " + flag_coordinate);
        if (flag_coordinate) { //좌표가 넘어오면 그 다음 작업 시작
            System.out.println("FLAG_COORDINATES2  : " + flag_coordinate);

            try {
                System.out.println("currentlocation playTask");
                System.out.println("CURRENTTTTTT: " + currentLatitude + currentLongitude);

                originalPlayObjects = new PlayTask(currentLatitude, currentLongitude).execute().get();

                //미세먼지 구할 떄 필요한 주소(**구) 받아오기
                //서울특별시 **구 ...
                cityName = originalPlayObjects.get(0).getAddr1().split(" ")[1];
                System.out.println("cITYNAME : " + cityName);

                //파싱 받아온 오브젝트가 있고, 주소가 존재하면?
                if (originalPlayObjects != null && cityName != null) {
                    weather_type = new AirWeatherTask(currentLatitude, currentLongitude, cityName).execute().get();
                    if (weather_type != null) setWeatherIcon(weather_type);

                    //추천 놀거리
                    userPreferencePlayObjects = new RecommendTask(weather_type, originalPlayObjects).execute().get();

                    //추천 놀거리가 존재하면 마커 띄우기
                    if (userPreferencePlayObjects != null)
                        for (int i = 0; i < userPreferencePlayObjects.size(); i++) {
                            setPlayMarker(userPreferencePlayObjects.get(i));
                        }
                }
                //리스트 뷰에 뿌리기
                printListData(userPreferencePlayObjects);


                printList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Toast.makeText(CurrentLocationActivity.this, arrListData.get(position).getCategoryId(), Toast.LENGTH_LONG).show();
                        System.out.println("cur :" + arrListData.get(position).getCategoryId());
                        System.out.println("cur :" + arrListData.get(position).getContentId());

                        Intent intent = new Intent(getApplicationContext(), DetailInfoActivity.class);
                        intent.putExtra("content_id", arrListData.get(position).getContentId());
                        intent.putExtra("category_id", arrListData.get(position).getCategoryId());
                        intent.putExtra("xCoordinate", String.valueOf(userPreferencePlayObjects.get(position).getMapy()));
                        intent.putExtra("yCoordinate", String.valueOf(userPreferencePlayObjects.get(position).getMapx()));

                        //비트맵 이미지의 경우 바이트 타입으로 변환하여 넘겨 줍니다.
                        // 주의할점은 넘겨줄수 있는 데이터 크기 제약이 있기 때문에 이미지 크기를 줄여서 보내야 합니다.
                        Bitmap intentBitmap = arrListData.get(position).getBitmap();

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        float scale = (float) (256 / (float) intentBitmap.getWidth());
                        int image_w = (int) (intentBitmap.getWidth() * scale);
                        int image_h = (int) (intentBitmap.getHeight() * scale);

                        Bitmap resize = Bitmap.createScaledBitmap(intentBitmap, image_w, image_h, true);
                        resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        intent.putExtra("image", byteArray);

                        startActivity(intent);
                    }
                });

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }



    private void setWeatherIcon(String weather_type) {

        System.out.println("WEATHERTYPE : " + weather_type);
        //weather_type이 계산 됐을 경우
        if (weather_type != null) {
            //날씨 아이콘 이미지뷰 설정
            weatherTypeInt = setDrawableIcon(weather_type);
            Drawable drawable = getResources().getDrawable(weatherTypeInt);
            iv_weatherIcon.setImageDrawable(drawable);
        }
    }


    private void printListData(ArrayList<PlayObject> userPreferencePlayObjects) {
        int size = userPreferencePlayObjects.size();
        int idx = 0;

        arrListData = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ListData listData = new ListData();
            try {
                if (userPreferencePlayObjects.get(i).getFirstimage2() == null) {
                    bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.no_image);
                } else {
                    bitmap = new LoadImage().execute(userPreferencePlayObjects.get(i).getFirstimage2()).get();
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (userPreferencePlayObjects.size() == 0) {
                listData.setTitle("추천 놀거리가 없습니다.");
            } else {
                listData.setTitle(userPreferencePlayObjects.get(i).getTitle());
                listData.setSummary(userPreferencePlayObjects.get(i).getCategoryName());
                listData.setCategoryId(userPreferencePlayObjects.get(i).getPreferCategoryId());
                listData.setBitmap(bitmap);
                listData.setContentId(userPreferencePlayObjects.get(i).getContentid());
                if (userPreferencePlayObjects.get(i).getAddr2() == null)
                    listData.setAddress(userPreferencePlayObjects.get(i).getAddr1());
                else
                    listData.setAddress(userPreferencePlayObjects.get(i).getAddr1() + " " + userPreferencePlayObjects.get(i).getAddr2());
                //류저 추천 놀거리가 존재한다
            }
            arrListData.add(listData);
        }

        //리스트 뷰에 뿌리기
        printList = (ListView) findViewById(R.id.listView);
        ListAdapter listAdapter = new ListAdapter(arrListData);
        printList.setAdapter(listAdapter);

        System.out.println("flag_userPreferenceObjects : " );

    }


    public void setCurrentLocation(Location location) {

        System.out.println("///////////////////////////////////////////////1");
        Log.d(TAG ,"setCurrentLocation");
        //   if (currentMarker != null) currentMarker.remove();

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);

    }



    ////////////////////////////////////놀거리 추천 4
    public MarkerOptions setPlayMarker(PlayObject userPreferencePlayObjects){

        System.out.println("///////////////////////////////////////////////2");
        Log.d(TAG ,"setPlayLocation");

        System.out.println("놀거리 추천 4 1");
        System.out.println("놀거리 추천 4 2");
        System.out.println("PlayCoordinates : " + userPreferencePlayObjects.getMapx());

        LatLng playMarkerLatLng = new LatLng(userPreferencePlayObjects.getMapy(), userPreferencePlayObjects.getMapx());

        System.out.println("GETMAPX : " + userPreferencePlayObjects.getMapx());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(playMarkerLatLng);
        markerOptions.title(userPreferencePlayObjects.getTitle());
        markerOptions.snippet(String.valueOf(userPreferencePlayObjects.getDist()) + "m");
        markerOptions.draggable(true);

        playMarker = mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        System.out.println("놀거리 추천 4 3");
        System.out.println("놀거리 추천 4 4");
        return markerOptions;
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;

    }


    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                } else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CurrentLocationActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");

                        needRequest = true;

                        return;
                    }
                }

                break;
        }
    }

    public int setDrawableIcon(String weather_type) {
        int weatherInt = 0;
        if(weather_type != null) {
            switch (weather_type) {
                case "type_0":
                    weatherInt = R.drawable.medical_mask;
                    //return R.drawable.medical_mask;
                    break;
                case "type_1":
                    weatherInt = R.drawable.snowflake;
                    //return R.drawable.snowflake;
                case "type_2":
                case "type_3":
                case "type_4":
                    weatherInt = R.drawable.rain;
                    //return R.drawable.rain;
                    break;
                case "type_5":
                case "type_6":
                case "type_7":
                case "type_8":
                case "type_9":
                case "type_10":
                    weatherInt = R.drawable.cloud;
                    //return R.drawable.cloud;
                    break;
                case "type_11":
                case "type_12":
                case "type_13":
                case "type_14":
                case "type_15":
                case "type_16":
                    weatherInt = R.drawable.sun;
                    break;
                //return R.drawable.sun;
            }
            System.out.println("MAIN WEATHERINT : " + weatherInt);

        }
        return weatherInt;
    }
}