package com.example.admin.restaurantmanagement.TableManagement;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import com.example.admin.restaurantmanagement.R;


public class TableManagementActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerViewTable;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    public static ArrayList<TableManagementInfo> tableManagementInfoArrayList;
    private TableManagementAdapter tableManagementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_table_activity);
        inItView();
        tableManagementInfoArrayList = new ArrayList<>();
        tableManagementInfoArrayList.clear();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference(AddTableManagementActivity.FB_DATABASE_TABLE);
        progressDialog = new ProgressDialog(TableManagementActivity.this);
        progressDialog.setMessage("Vui lòng chờ giây lát...");
        progressDialog.show();

        DisplayTableOnScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_table_management,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_add_table){
            Intent intent = new Intent(TableManagementActivity.this, AddTableManagementActivity.class);
            finish();
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void inItView() {
        recyclerViewTable = (RecyclerView) findViewById(R.id.revTableManage);
        toolbar = findViewById(R.id.nav_table_management);
    }

    public void DisplayTableOnScreen(){
        tableManagementInfoArrayList = GetListTable();

        tableManagementAdapter  = new TableManagementAdapter(tableManagementInfoArrayList, this);
        recyclerViewTable.setAdapter(tableManagementAdapter);
        recyclerViewTable.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tableManagementAdapter.notifyDataSetChanged();
    }

    private ArrayList<TableManagementInfo> GetListTable(){
        final ArrayList<TableManagementInfo> lstTable = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //ImageUpload class require default constructor
                    TableManagementInfo tableManagementInfo = snapshot.getValue(TableManagementInfo.class);
                    lstTable.add(tableManagementInfo);
                    tableManagementAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        return lstTable;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.management_table_activity);
//
//        RecyclerView recyclerViewTable = (RecyclerView) findViewById(R.id.revTableManage);
//        TableManagementAdapter tableManagementAdapter  =new TableManagementAdapter();
//        recyclerViewTable.setAdapter(tableManagementAdapter);
//        recyclerViewTable.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//    }
}
