package com.example.admin.restaurantmanagement.RestaurantMenu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.admin.restaurantmanagement.RestaurantMenu.Fragment.DrinkMenuFragment;
import com.example.admin.restaurantmanagement.RestaurantMenu.Fragment.FoodMenuFragment;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends FragmentStatePagerAdapter {
    private List<String> titles;
    private List<Fragment> menuFragment;


    public MenuAdapter(FragmentManager fm) {
        super(fm);
        titles = new ArrayList<>();
        menuFragment = new ArrayList<>();

        //add fragment vao list
        menuFragment.add(new FoodMenuFragment());
        menuFragment.add(new DrinkMenuFragment());

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
