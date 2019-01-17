package com.example.admin.restaurantmanagement.Login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.restaurantmanagement.R;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isConnected(SplashActivity.this))
            checkConnecttion(SplashActivity.this);
        else{
            setContentView(R.layout.activity_splash);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            },1000);
        }

    }
    public boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)  context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isConnectedOrConnecting()){
            android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if((mobile!=null && mobile.isConnectedOrConnecting()) || (wifi!=null && wifi.isConnectedOrConnecting())){
                return  true;
            }else return false;
        }else return  false;
    }

    public AlertDialog.Builder checkConnecttion(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Không có kết nối Internet");
        builder.setMessage("Bạn nên truy cập Wifi hoặc Dữ liệu di động để truy cập ứng dụng");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
        return builder;
    }
}
