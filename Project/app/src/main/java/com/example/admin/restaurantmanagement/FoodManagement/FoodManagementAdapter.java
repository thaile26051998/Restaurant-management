package com.example.admin.restaurantmanagement.FoodManagement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.restaurantmanagement.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodManagementAdapter extends RecyclerView.Adapter{
    public static final String FB_DATABASE_FOOD = "Menu/Food";
    ArrayList<MenuManagementInfo> menuFoodList;
    Bundle menuBundle;
    public static FoodManagementFragment foodManagementFragment;
    Integer posFood;

    public FoodManagementAdapter(ArrayList<MenuManagementInfo> menuInfos, FoodManagementFragment foodManagementFragment) {
        this.menuFoodList = menuInfos;
        this.foodManagementFragment = foodManagementFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.tab_manage_food,viewGroup,false);
        return new MyFoodManagementViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        final FoodManagementAdapter.MyFoodManagementViewHolder myFoodManagementViewHolder = (FoodManagementAdapter.MyFoodManagementViewHolder) viewHolder;

        myFoodManagementViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iEditFood= new Intent(v.getContext(), EditMenuManagementActivity.class);
                Bundle bEditFood = new Bundle();
                bEditFood.putSerializable("infoFood", menuFoodList);
                bEditFood.putInt("position", viewHolder.getAdapterPosition());
                bEditFood.putInt("type", 0);
                iEditFood.putExtras(bEditFood);
                //foodManagementFragment.getActivity().finish();
                v.getContext().startActivity(iEditFood);
            }
        });

        myFoodManagementViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFood(v.getContext(), viewHolder.getAdapterPosition());
            }
        });

        MenuManagementInfo menuFoodInfo = menuFoodList.get(i);
        myFoodManagementViewHolder.txtFoodName.setText(menuFoodInfo.getFoodName());
        myFoodManagementViewHolder.txtPrice.setText(menuFoodInfo.getPrice() + "đ");
        Picasso.get().load(menuFoodInfo.getUrl()).into(myFoodManagementViewHolder.imgFood);

    }


    @Override
    public int getItemCount() {
        return menuFoodList.size();
    }

    private class MyFoodManagementViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood, imgEdit, imgDelete;
        TextView txtFoodName, txtPrice;

        public MyFoodManagementViewHolder(View view) {
            super(view);
            imgFood = (ImageView) view.findViewById(R.id.imgOrderFood);
            txtFoodName = (TextView) view.findViewById(R.id.txtOrderFoodName);
            txtPrice = (TextView) view.findViewById(R.id.txtOrderFoodPrice);
            imgEdit = (ImageView) view.findViewById(R.id.imgBottom);
            imgDelete = (ImageView) view.findViewById(R.id.imgAddFoodOrder);
        }
    }

    public AlertDialog.Builder DeleteFood(final Context context, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("XÓA MÓN ĂN");
        builder.setMessage("Bạn có chắc muốn xóa \""+ menuFoodList.get(position).getFoodName() +"\" không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
                DatabaseReference myRef = database.getReference(FB_DATABASE_FOOD);
                myRef.child(menuFoodList.get(position).getFoodName()).setValue(null);

                foodManagementFragment.getActivity().finish();
                Intent intent = new Intent(context, MenuManagementActivity.class);
                myMessage();
                context.startActivity(intent);
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

    void myMessage()
    {
        Toast.makeText(foodManagementFragment.getActivity(), "Xóa món ăn thành công!", Toast.LENGTH_SHORT).show();
    }
}
