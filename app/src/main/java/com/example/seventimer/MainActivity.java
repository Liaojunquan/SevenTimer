package com.example.seventimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public LocationClient mLocationClient;
    private TextView positionText;
    public String Latitude = "";
    public String Longitude = "";
    public double lat = 0;
    public double lng = 0;
    public float rad = 0.0f;
    public float dire = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_main);
        positionText = (TextView)findViewById(R.id.position_text);
        Button civilButton = (Button) findViewById(R.id.civil_btn);
        Button civilLightButton = (Button) findViewById(R.id.civillight_btn);
        Button twoWeakButton = (Button) findViewById(R.id.two_btn);
        Button astroButton = (Button) findViewById(R.id.astro_btn);
        Button meteoButton = (Button) findViewById(R.id.meteo_btn);
        Button mapButton = (Button)findViewById(R.id.showMe_btn);
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }
        else {
            requestLocation(getApplicationContext());
        }
        civilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.7timer.info/bin/civil.php?lon=" + Longitude + "&lat=" + Latitude + "&lang=zh-CN&ac=0&unit=metric&tzshift=0"));
                startActivity(intent);*/
                openHTML(1);
            }
        });
        civilLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.7timer.info/bin/civillight.php?lon=" + Longitude + "&lat=" + Latitude + "&lang=zh-CN&ac=0&unit=metric&tzshift=0"));
                startActivity(intent);*/
                openHTML(2);
            }
        });
        twoWeakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.7timer.info/bin/two.php?lon=" + Longitude + "&lat=" + Latitude + "&lang=zh-CN&ac=0&unit=metric&tzshift=0"));
                startActivity(intent);*/
                openHTML(3);
            }
        });
        astroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.7timer.info/bin/astro.php?lon=" + Longitude + "&lat=" + Latitude + "&lang=zh-CN&ac=0&unit=metric&tzshift=0"));
                startActivity(intent);*/
                openHTML(4);
            }
        });
        meteoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.7timer.info/bin/meteo.php?lon=" + Longitude + "&lat=" + Latitude + "&lang=zh-CN&ac=0&unit=metric&tzshift=0"));
                startActivity(intent);*/
                openHTML(5);
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lat != 0 && lng != 0){
                    Intent intent = new Intent(MainActivity.this,ShowPhoto.class);
                    /*intent.putExtra("Latitude",lat);
                    intent.putExtra("Longitude",lng);
                    intent.putExtra("Radius",rad);
                    intent.putExtra("Direction",dire);*/
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(),"经纬度数据出错",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestLocation(final Context context){
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setScanSpan(5000);  //5定位秒一次
        int tmp = isOpen(context);
        if(tmp == 0){
            Toast.makeText(context,"网络和定位没打开",Toast.LENGTH_LONG).show();
            return;
        }
        else if (tmp == 1 || tmp == 3){   //打开GPS
            option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        }
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "必须同意所有权限", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
                requestLocation(getApplicationContext());
            } else {
                Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            BigDecimal b1 = BigDecimal.valueOf(bdLocation.getLatitude());
            lat = bdLocation.getLatitude();
            BigDecimal b2 = BigDecimal.valueOf(bdLocation.getLongitude());
            lng = bdLocation.getLongitude();
            Latitude = Double.toString(b1.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue());
            Longitude = Double.toString(b2.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue());
            rad = bdLocation.getRadius();
            dire = bdLocation.getDirection();
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度: ").append(Latitude).append("\n");
            currentPosition.append("经度: ").append(Longitude).append("\n");
            //currentPosition.append("半径: ").append(bdLocation.getRadius()).append("\n");
            //currentPosition.append("方向: ").append(bdLocation.getDirection()).append("\n");
            currentPosition.append("地区: ").append(bdLocation.getCountry()).append(bdLocation.getProvince())
                    .append(bdLocation.getCity()).append(bdLocation.getDistrict())
                    .append(bdLocation.getStreet()).append("\n");
            currentPosition.append("定位方式: ");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }
            else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                currentPosition.append("Network");
            }
            positionText.setText(currentPosition);
        }
    }

    public int isOpen(final Context context) {
        boolean gps = false;
        boolean network = false;
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(gps && network)
            return 3;
        if (gps)
            return 1;
        if (network)
            return 2;
        return 0;
    }

    public void openHTML(int index){
        Intent intent = new Intent(MainActivity.this,ShowHTML.class);
        intent.putExtra("Latitude",Latitude);
        intent.putExtra("Longitude",Longitude);
        intent.putExtra("index",index);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient = null;
    }
}