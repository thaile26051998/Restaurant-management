package com.example.admin.restaurantmanagement.FoodManagement;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.restaurantmanagement.R;
import com.example.admin.restaurantmanagement.RestaurantMenu.Fragment.DrinkMenuFragment;
import com.example.admin.restaurantmanagement.RestaurantMenu.Fragment.FoodMenuFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuManagementAdapter extends FragmentStatePagerAdapter {
    private List<String> titles;
    private List<Fragment> menuFragment;


    public MenuManagementAdapter(FragmentManager fm) {
        super(fm);
        titles = new ArrayList<>();
        menuFragment = new ArrayList<>();

        //add fragment vao list
        menuFragment.add(new FoodManagementFragment());
        menuFragment.add(new DrinkManagementFragment());

        //add titles
        titles.add("Món ăn");
        titles.add("Thức uống");
    }

    @Override
    public Fragment getItem(int i) {
        return menuFragment.get(i);
    }

    @Override
    public int getCount() {
        return menuFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
