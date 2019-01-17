package com.example.admin.restaurantmanagement.Management;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.admin.restaurantmanagement.EmployManagement.EmployManagementActivity;
import com.example.admin.restaurantmanagement.FoodManagement.MenuManagementActivity;
import com.example.admin.restaurantmanagement.Login.LoginActivity;
import com.example.admin.restaurantmanagement.R;
import com.example.admin.restaurantmanagement.TableManagement.TableManagementActivity;

public class ManagementActivity extends AppCompatActivity {
    Button btnLogOut, btnManageEmploy, btnManageFood, btnManageTable, btnStatistic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_activity);
        inItView();

        btnManageFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagementActivity.this, MenuManagementActivity.class);
                startActivity(intent);
            }
        });

        btnManageTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagementActivity.this, TableManagementActivity.class);
                startActivity(intent);
            }
        });

        btnManageEmploy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagementActivity.this, EmployManagementActivity.class);
                startActivity(intent);
            }
        });

        btnStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ManagementActivity.this, .class);
//                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagementActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToLogin(ManagementActivity.this);
            }
        });

    }

    //quay trở lại màn hình đăng nhập
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            returnToLogin(ManagementActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public AlertDialog.Builder returnToLogin(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ĐĂNG XUẤT");
        builder.setMessage("Bạn có chắc muốn đăng xuất?");

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

        return builder;
    }

    private void inItView() {
        btnLogOut = findViewById(R.id.btnLogOut);
        btnManageEmploy = findViewById(R.id.btnManageEmploy);
        btnManageFood = findViewById(R.id.btnManageFood);
        btnManageTable = findViewById(R.id.btnManageTable);
        btnStatistic = findViewById(R.id.btnLogOut);
    }
}
