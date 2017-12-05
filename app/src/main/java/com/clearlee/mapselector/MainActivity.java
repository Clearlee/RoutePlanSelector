package com.clearlee.mapselector;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.clearlee.mapselector.Util.Common;

import java.net.URISyntaxException;


public class MainActivity extends Activity implements View.OnClickListener {

    //测试地址
    public static final Double DESTINATION_LAT = 30.633433;
    public static final Double DESTINATION_LNG = 104.158106;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        TextView btnBaiduMap = (TextView) findViewById(R.id.map_baidu);
        TextView btnGaodeMap = (TextView) findViewById(R.id.map_gaode);
        TextView btnTencentMap = (TextView) findViewById(R.id.map_tencent);
        btnBaiduMap.setOnClickListener(this);
        btnGaodeMap.setOnClickListener(this);
        btnTencentMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_gaode:
                openGaodeMap();
                break;
            case R.id.map_baidu:
                openBaiduMap();
                break;
            case R.id.map_tencent:
                openTencentMap();
                break;
        }
    }

    private void openTencentMap() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        Uri uri = Uri.parse("qqmap://map/routeplan?type=drive&to=我的目的地&tocoord=" + DESTINATION_LAT + "," + DESTINATION_LNG);
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "您尚未安装腾讯地图", Toast.LENGTH_LONG).show();
        }
    }


    //http://lbsyun.baidu.com/index.php?title=uri/api/android
    private void openBaiduMap() {
        if (Common.isAvilible(this, "com.baidu.BaiduMap")) {
            try {
                Intent intent = Intent.getIntent("intent://map/direction?" +
                        "destination=latlng:" + DESTINATION_LAT + "," + DESTINATION_LNG + "|name:我的目的地" +        //终点
                        "&mode=driving&" +          //导航路线方式
                        "&src=appname#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                startActivity(intent); //启动调用
            } catch (URISyntaxException e) {
                Log.e("intent", e.getMessage());
            }
        } else {
            Toast.makeText(this, "您尚未安装百度地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    //http://lbs.amap.com/api/amap-mobile/guide/android/route
    private void openGaodeMap() {
        if (Common.isAvilible(this, "com.autonavi.minimap")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            Uri uri = Uri.parse("amapuri://route/plan/?did=BGVIS2&dlat="+DESTINATION_LAT+"&dlon="+DESTINATION_LNG+"&dname=我的目的地&dev=0&t=0");
            intent.setData(uri);

            startActivity(intent);
        } else {
            Toast.makeText(this, "您尚未安装高德地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}
