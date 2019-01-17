package com.example.admin.restaurantmanagement.TableManagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.restaurantmanagement.FoodManagement.MenuManagementInfo;
import com.example.admin.restaurantmanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Objects;

public class EditTableManagementActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edtTableName, edtTableStatus;
    private FirebaseAuth firebaseAuth;
    public ArrayList<TableManagementInfo> tableManagementInfoArrayList = new ArrayList<>();
    private DatabaseReference mDatabaseRef;
    public static final String FB_DATABASE_TABLE = "Table";
    public int position;
    public String key;
    ArrayList<TableManagementInfo> tables;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_table_management_activity);
        inItView();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_TABLE);

        Bundle bundle = this.getIntent().getExtras();
        position = bundle.getInt("position");
        tables = (ArrayList<TableManagementInfo>) bundle.getSerializable("infoTable");

        key = tables.get(position).getTableName();

        edtTableName.setText(tables.get(position).getTableName());
        edtTableStatus.setText(tables.get(position).getStatus());

        toolbar.setTitle("Chỉnh sửa bàn ăn bàn ăn");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_table_management, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_table_management) {
            editTable();
        }

        return true;
    }


    private void editTable() {
        String name = edtTableName.getText().toString();
        String stt = edtTableStatus.getText().toString();

        if (checkNameTable(name) == 1) {
            edtTableName.setError("Tên bàn ăn đã tồn tại");
            edtTableName.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            edtTableName.setError("Bạn cần nhập tên");
            edtTableName.requestFocus();
            return;
        }

        if (!stt.equals("Trống")) {
            edtTableStatus.setError("Bàn ăn ban đầu phải trống");
            edtTableStatus.requestFocus();
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading image");
        dialog.show();
        TableManagementInfo tableManagementInfo = new TableManagementInfo(stt, name);

        //Save image info into firebase database

        mDatabaseRef.child(key).setValue(null);
        mDatabaseRef.child(name).setValue(tableManagementInfo);

        //Finish màn hình hiện tại và chuyển về màn hình quản lý món ăn
        Intent intent = new Intent(EditTableManagementActivity.this, TableManagementActivity.class);
        finish();
        TableManagementAdapter.tableManagementActivity.finish();
        Toast.makeText(this, "Sửa thông tin bàn ăn thành công!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }


    private void inItView() {
        toolbar = (Toolbar) findViewById(R.id.nav_add_table_management);
        edtTableName = findViewById(R.id.edt_name_table);
        edtTableStatus = findViewById(R.id.edt_status_table);
    }

    int checkNameTable(String name)
    {
        for(int i = 0; i < tables.size(); i++)
        {
            if(name.equals(tables.get(i).getTableName()) && i != position)
            {
                return 1;
            }
        }
        return 0;
    }
}
