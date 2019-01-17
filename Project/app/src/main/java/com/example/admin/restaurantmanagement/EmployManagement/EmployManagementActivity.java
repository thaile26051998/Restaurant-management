package com.example.admin.restaurantmanagement.EmployManagement;

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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.admin.restaurantmanagement.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class EmployManagementActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerViewEmploy;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private ArrayList<EmployManagementInfo> employManagementInfoArrayList;
    private EmployManagementAdapter employManagementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_employ_activity);
        inItView();
        employManagementInfoArrayList = new ArrayList<>();

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

        databaseReference = FirebaseDatabase.getInstance().getReference(AddEmployManagementActivity.FB_DATABASE_EMPLOY);
        progressDialog = new ProgressDialog(EmployManagementActivity.this);
        progressDialog.setMessage("Vui lòng chờ giây lát...");
        progressDialog.show();

        DisplayEmployeeOnScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_employ_management,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_add_employ_management){
            Intent intent = new Intent(EmployManagementActivity.this, AddEmployManagementActivity.class);
            finish();
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void inItView() {
        recyclerViewEmploy = (RecyclerView) findViewById(R.id.revEmployManage);
        toolbar = findViewById(R.id.nav_employ_management);
    }

    public void DisplayEmployeeOnScreen(){
        employManagementInfoArrayList = GetListEmployee();

        employManagementAdapter  = new EmployManagementAdapter(this, employManagementInfoArrayList, this);
        recyclerViewEmploy.setAdapter(employManagementAdapter);
        recyclerViewEmploy.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        employManagementAdapter.notifyDataSetChanged();
    }

    private ArrayList<EmployManagementInfo> GetListEmployee(){
        final ArrayList<EmployManagementInfo> lstEmployee = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //ImageUpload class require default constructor
                    EmployManagementInfo employManagementInfo = snapshot.getValue(EmployManagementInfo.class);
                    lstEmployee.add(employManagementInfo);
                    employManagementAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        return lstEmployee;
    }


}
