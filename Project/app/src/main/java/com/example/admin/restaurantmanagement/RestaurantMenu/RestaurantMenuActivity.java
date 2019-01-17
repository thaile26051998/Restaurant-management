package com.example.admin.restaurantmanagement.RestaurantMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.admin.restaurantmanagement.PayFood.PayFoodActivity;
import com.example.admin.restaurantmanagement.R;

import java.util.Objects;

public class RestaurantMenuActivity extends AppCompatActivity {
    private TabLayout tabLayoutMenu;
    private ViewPager viewPagerMenu;
    private Toolbar toolbar_menu;
    private TextView txtCart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_menu_activity);
        InitView();

        toolbar_menu = findViewById(R.id.nav_add_employ_management);
        setSupportActionBar(toolbar_menu);
        Objects.requireNonNull(getSupportActionBar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_menu.setTitle("Thực đơn");
        toolbar_menu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        tabLayoutMenu.setupWithViewPager(viewPagerMenu);
        FragmentManager fragmentManager = getSupportFragmentManager();
        MenuAdapter menuAdapter = new MenuAdapter(fragmentManager);
        viewPagerMenu.setAdapter(menuAdapter);

        txtCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCart();
            }
        });
    }

    private void viewCart() {
        finish();
        Intent intent = new Intent(RestaurantMenuActivity.this, PayFoodActivity.class);
        startActivity(intent);
    }

    private void InitView() {
        tabLayoutMenu = (TabLayout) findViewById(R.id.tabLayoutMenu);
        viewPagerMenu = (ViewPager) findViewById(R.id.viewPagerMenu);
        txtCart = (TextView) findViewById(R.id.txtCart);
        tabLayoutMenu.setTabMode(TabLayout.MODE_FIXED);
        tabLayoutMenu.setTabGravity(TabLayout.GRAVITY_FILL);
    }
}
