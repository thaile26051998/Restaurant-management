package com.example.admin.restaurantmanagement.FoodManagement;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.restaurantmanagement.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DrinkManagementFragment extends Fragment {
    public String FB_DATABASE_FOOD = "Menu/Drink";
    public static ArrayList<MenuManagementInfo> menuFoodList = new ArrayList<>();
    RecyclerView revFoodManagement;
    private DatabaseReference mDatabaseRef;
    private ProgressDialog progressDialog;
    public static String FOOD_MENU = "DRINK MENU";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.management_food_activity, container, false);
        revFoodManagement = (RecyclerView) view.findViewById(R.id.revFoodManage);
        progressDialog = new ProgressDialog(container.getContext());
        progressDialog.setMessage("Vui lòng chờ giây lát...");
        progressDialog.show();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_FOOD);
        menuFoodList.clear();

        final DrinkManagementAdapter drinkManagementAdapter = new DrinkManagementAdapter(menuFoodList, this);
        revFoodManagement.setAdapter(drinkManagementAdapter);
        revFoodManagement.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //ImageUpload class require default constructor
                    MenuManagementInfo menuManagementInfo = snapshot.getValue(MenuManagementInfo.class);
                    menuFoodList.add(menuManagementInfo);
                }
                drinkManagementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
