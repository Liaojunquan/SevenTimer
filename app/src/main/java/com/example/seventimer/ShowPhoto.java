package com.example.seventimer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;

public class ShowPhoto extends AppCompatActivity {   //展示百度地图自己位置的定位

    public LocationClient mLocationClient = null;
    private MapView mapView = null;
    private double Latitude = 0;
    private double Longitude = 0;
    private float Radius = 0.0f;
    private float Direction = 0.0f;
    private BaiduMap baiduMap;
    private boolean isFirstLocation = true;
    //private boolean action = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_photo);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        mapView = (MapView)findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        Intent intent = getIntent();
        Latitude = intent.getDoubleExtra("Latitude",39.909);      //北京天安门坐标
        Longitude = intent.getDoubleExtra("Longitude",116.3972);
        Radius = intent.getFloatExtra("Radius",40.0f);
        Direction = intent.getFloatExtra("Direction",0.0f);
        //action = true;
        //Timelaps();
        navigateTo();
    }

    private void navigateTo(){
        /*if(isFirstLocation){
            LatLng ll = new LatLng(Latitude,Longitude);
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocation = false;
            Log.i("ShowPhoto","First");
        }
        MyLocationData locationData = new MyLocationData.Builder().accuracy(Radius).direction(Direction)
                .latitude(Latitude).longitude(Longitude).build();
        baiduMap.setMyLocationData(locationData);
        Log.i("ShowPhoto","action");*/

        //定位初始化
        mLocationClient = new LocationClient(MyApplication.getContext());
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        if(isOpenGPS())
            option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        //设置locationClientOption
        mLocationClient.setLocOption(option);
        //注册LocationListener监听器
        MapLocationListener mapLocationListener = new MapLocationListener();
        mLocationClient.registerLocationListener(mapLocationListener);
        //开启地图定位图层
        mLocationClient.start();
    }

    public class MapLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mapView == null){
                return;
            }
            Log.i("ShowPhoto","action");
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection())  // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
        }
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        //action = false;
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    private void Timelaps(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int tmp = 2;
                while (tmp > 0){
                    navigateTo();
                    tmp--;
                    try {
                        Thread.sleep(100);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                Thread.interrupted();
            }
        }).start();
    }

    private boolean isOpenGPS(){
        LocationManager locationManager
                = (LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}