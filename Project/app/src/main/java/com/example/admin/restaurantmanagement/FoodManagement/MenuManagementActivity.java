package com.example.admin.restaurantmanagement.FoodManagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.admin.restaurantmanagement.PayFood.PayFoodActivity;
import com.example.admin.restaurantmanagement.R;
import com.example.admin.restaurantmanagement.RestaurantMenu.MenuAdapter;
import com.example.admin.restaurantmanagement.RestaurantMenu.RestaurantMenuActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MenuManagementActivity extends AppCompatActivity {
    private TabLayout tabLayoutMenu;
    private ViewPager viewPagerMenu;
    private Toolbar toolbar_menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_management_activity);
        InitView();
        toolbar_menu = findViewById(R.id.nav_menu_management);
        setSupportActionBar(toolbar_menu);
        Objects.requireNonNull(getSupportActionBar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_menu.setTitle("Quản lý món ăn");
        toolbar_menu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        tabLayoutMenu.setupWithViewPager(viewPagerMenu);
        FragmentManager fragmentManager = getSupportFragmentManager();
        MenuManagementAdapter menuManagementAdapter = new MenuManagementAdapter(fragmentManager);
        viewPagerMenu.setAdapter(menuManagementAdapter);

    }

    private void InitView() {
        tabLayoutMenu = (TabLayout) findViewById(R.id.tabLayoutMenuManagement);
        viewPagerMenu = (ViewPager) findViewById(R.id.viewPagerMenuManagement);
        tabLayoutMenu.setTabMode(TabLayout.MODE_FIXED);
        tabLayoutMenu.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_food_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.action_add_food_management) {
            intent = new Intent(MenuManagementActivity.this, AddFoodManagementActivity.class);
            finish();
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_add_drink_management) {
            intent = new Intent(MenuManagementActivity.this, AddDrinkManagementActivity.class);
            finish();
            startActivity(intent);
        }
        return true;
    }

    public void finishActivity()
    {
        finish();
    }
}
