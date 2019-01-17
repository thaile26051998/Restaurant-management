package com.example.admin.restaurantmanagement.RestaurantMenu.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.restaurantmanagement.OrderActivity.OrderActivity;
import com.example.admin.restaurantmanagement.R;
import com.example.admin.restaurantmanagement.RestaurantMenu.Fragment.DrinkMenuFragment;
import com.example.admin.restaurantmanagement.RestaurantMenu.MenuInfo;
import com.example.admin.restaurantmanagement.RestaurantMenu.RestaurantMenuActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DrinkMenuAdapter extends RecyclerView.Adapter{
    ArrayList<MenuInfo> menuDrinkList;
    DrinkMenuFragment drinkMenuFragment;

    public DrinkMenuAdapter(ArrayList<MenuInfo> menuInfos, DrinkMenuFragment drinkMenuFragment) {
        this.menuDrinkList = menuInfos;
        this.drinkMenuFragment =drinkMenuFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.tab_menu_food, viewGroup, false);
        return new DrinkMenuAdapter.MyDrinkMenuViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        final DrinkMenuAdapter.MyDrinkMenuViewHolder myFoodMenuViewHolder = (DrinkMenuAdapter.MyDrinkMenuViewHolder) viewHolder;

        MenuInfo menuInfo = menuDrinkList.get(i);
        myFoodMenuViewHolder.txtMenuNameFood.setText(menuInfo.getFoodName());
        myFoodMenuViewHolder.txtMemuPrice.setText(menuInfo.getPrice()+"d");

        Picasso.get().load(menuInfo.getUrl()).into(myFoodMenuViewHolder.imgMenuFood);

        myFoodMenuViewHolder.imgAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iDrink= new Intent(v.getContext(), OrderActivity.class);
                Bundle bDrink = new Bundle();
                bDrink.putSerializable("infoFood", menuDrinkList);
                bDrink.putInt("position", viewHolder.getAdapterPosition());
                bDrink.putInt("type", 0);
                iDrink.putExtras(bDrink);
                v.getContext().startActivity(iDrink);
                ((Activity)v.getContext()).finish();
            }
        });
    }


    @Override
    public int getItemCount() {
        return menuDrinkList.size();
    }

    private class MyDrinkMenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMenuFood, imgAddFood;
        TextView txtMenuNameFood, txtMemuPrice;

        public MyDrinkMenuViewHolder(View view) {
            super(view);
            imgMenuFood = view.findViewById(R.id.imgOrderFood);
            txtMenuNameFood = view.findViewById(R.id.txtMenuFoodName);
            txtMemuPrice = view.findViewById(R.id.txtOrderFoodPrice);
            imgAddFood = view.findViewById(R.id.imgbMenuAddFood);
        }
    }
}
