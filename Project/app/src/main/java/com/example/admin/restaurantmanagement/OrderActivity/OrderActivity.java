package com.example.admin.restaurantmanagement.OrderActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.restaurantmanagement.FoodManagement.MenuManagementInfo;
import com.example.admin.restaurantmanagement.R;
import com.example.admin.restaurantmanagement.RestaurantMenu.Adapter.FoodMenuAdapter;
import com.example.admin.restaurantmanagement.RestaurantMenu.MenuInfo;
import com.example.admin.restaurantmanagement.RestaurantMenu.RestaurantMenuActivity;
import com.example.admin.restaurantmanagement.TableDiagram.TableDiagramAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class OrderActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private DatabaseReference mData;

    ImageView imgOrderFoodInfo, imgAdd, imgMinus, imgClose;
    TextView txtOrderFoodName, txtOrderFoodDetail, txtNumOrderFood;
    Button btnOrderFoodPrice;

    public static final String FB_STORAGE_FOOD = "Food/";
    public static final String FB_STORAGE_DRINK = "Drink/";
    public static final String FB_DATABASE_FOOD = "Menu/Food";
    public static final String FB_DATABASE_DRINK = "Menu/Drink";
    public int posFood;
    public int type; // 0 là đồ ăn, 1 là thức uống
    public String key;
    String orderPrice = "0";

    ArrayList<MenuInfo> food;

    OrderFoodInfo oderFood;

    int res;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
        inItView();
        Bundle foodBundle = null;
        getBundleFromMenu(foodBundle);

        txtOrderFoodName.setText(food.get(posFood).getFoodName());
        txtOrderFoodDetail.setText(food.get(posFood).getDetail());
        btnOrderFoodPrice.setText(food.get(posFood).getPrice() + "d");
        Picasso.get().load(food.get(posFood).getUrl()).into(imgOrderFoodInfo);

        orderPrice = food.get(posFood).getPrice();
        res = Integer.parseInt(orderPrice);

        mData = FirebaseDatabase.getInstance().getReference();

        orderPrice = food.get(posFood).getPrice();

        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusFood();
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(OrderActivity.this, RestaurantMenuActivity.class);
                startActivity(intent);
            }
        });

        btnOrderFoodPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();

                oderFood = new OrderFoodInfo(txtOrderFoodName.getText().toString(),
                        food.get(posFood).getPrice().toString(),
                        food.get(posFood).getDetail().toString(),
                        food.get(posFood).getUrl().toString(), txtNumOrderFood.getText().toString(), Integer.toString(res));

                mData.child("Table/" + "tb" + Integer.toString(TableDiagramAdapter.pos) + "/ListOder/").push().setValue(oderFood);

                finish();
                Intent intent = new Intent(OrderActivity.this, RestaurantMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void inItView() {
        imgClose = findViewById(R.id.imgCloseOrder);
        imgAdd = findViewById(R.id.imgAdd);
        imgMinus = findViewById(R.id.imgMinus);
        imgOrderFoodInfo = findViewById(R.id.imgOrderFoodInfo);
        btnOrderFoodPrice = findViewById(R.id.btnOrderFoodPrice);
        txtOrderFoodName = findViewById(R.id.txtOrderFoodName);
        txtOrderFoodDetail = findViewById(R.id.txtOrderFoodDetail);
        txtNumOrderFood = findViewById(R.id.txtNumOrderFood);
    }

    private void getBundleFromMenu(Bundle foodBundle) {
        foodBundle = this.getIntent().getExtras();
        posFood = foodBundle.getInt("position");
        type = foodBundle.getInt("type");
        if (type == 0) {
            mStorageRef = FirebaseStorage.getInstance().getReference(FB_STORAGE_FOOD);
            mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_FOOD);
        } else {
            mStorageRef = FirebaseStorage.getInstance().getReference(FB_STORAGE_DRINK);
            mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_DRINK);
        }

        food = (ArrayList<MenuInfo>) foodBundle.getSerializable("infoFood");

        key = food.get(posFood).getFoodName();
    }

    private void minusFood() {
        int temp = Integer.parseInt(txtNumOrderFood.getText().toString());
        if (temp <= 1) return;
        else {
            txtNumOrderFood.setText(String.valueOf(temp - 1));
            res = Integer.parseInt(String.valueOf(temp - 1)) * Integer.parseInt(orderPrice);
            btnOrderFoodPrice.setText("+ " + String.valueOf(res) + "d");
        }
    }

    private void addFood() {
        int temp = Integer.parseInt(txtNumOrderFood.getText().toString());
        txtNumOrderFood.setText(String.valueOf(temp + 1));
        res = Integer.parseInt(String.valueOf(temp + 1)) * Integer.parseInt(orderPrice);
        btnOrderFoodPrice.setText("+ " + String.valueOf(res) + "d");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(OrderActivity.this, RestaurantMenuActivity.class);
        startActivity(intent);
    }
}
