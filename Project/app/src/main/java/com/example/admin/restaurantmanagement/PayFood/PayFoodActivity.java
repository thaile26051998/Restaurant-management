package com.example.admin.restaurantmanagement.PayFood;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.restaurantmanagement.R;
import com.example.admin.restaurantmanagement.RestaurantMenu.RestaurantMenuActivity;
import com.example.admin.restaurantmanagement.TableDiagram.TableDiagramActivity;
import com.example.admin.restaurantmanagement.TableDiagram.TableDiagramAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PayFoodActivity extends AppCompatActivity {
    private DatabaseReference mData;
    private DatabaseReference mData2;
    private ProgressDialog progressDialog;
    private ArrayList<PayFoodInfo> payFoodInfoArrayList;

    public static int totalPrice = 0;

    Button btnPay;
    RecyclerView recyclerView;
    TextView txtPaySumPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_food_activity);
        inItView();

        payFoodInfoArrayList = new ArrayList<>();

        progressDialog = new ProgressDialog(PayFoodActivity.this);
        progressDialog.setMessage("Vui lòng chờ giây lát...");
        progressDialog.show();

        mData = FirebaseDatabase.getInstance().getReference("Table/" + "tb" + Integer.toString(TableDiagramAdapter.pos) + "/ListOder/");
        mData2 = FirebaseDatabase.getInstance().getReference("Table/" + "tb" + Integer.toString(TableDiagramAdapter.pos) + "/");

        final PayFoodAdapter payFoodAdapter = new PayFoodAdapter(payFoodInfoArrayList);
        recyclerView.setAdapter(payFoodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                //Reset lại giá trị tổng tiền cần thanh toán mỗi khi load lại dữ liệu trên database
                totalPrice = 0;
                //

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //ImageUpload class require default constructor

                    PayFoodInfo payFoodInfo = snapshot.getValue(PayFoodInfo.class);

                    payFoodInfoArrayList.add(payFoodInfo);

                    totalPrice = totalPrice + Integer.parseInt(payFoodInfo.getTotal().toString());
                }

                txtPaySumPrice.append(Integer.toString(totalPrice) + "d");

                payFoodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payFood();
            }
        });


    }


    private void payFood() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(PayFoodActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        builder.setMessage("Bạn có chắc muốn thanh toán?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mData.setValue(null);
                mData2.child("TinhTrang").setValue("Trống");

                Toast.makeText(getApplicationContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();

                finish();

                Intent intent = new Intent(PayFoodActivity.this, TableDiagramActivity.class);
                startActivity(intent);

                totalPrice = 0;

            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void inItView() {
        recyclerView = (RecyclerView) findViewById(R.id.revPayFood);
        btnPay = (Button) findViewById(R.id.btnPay);
        txtPaySumPrice = (TextView) findViewById(R.id.txtPaySumPrice);
    }


}
