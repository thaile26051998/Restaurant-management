package com.example.admin.restaurantmanagement.TableManagement;

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
import android.widget.TextView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.widget.ImageView;

import com.example.admin.restaurantmanagement.FoodManagement.EditMenuManagementActivity;
import com.example.admin.restaurantmanagement.FoodManagement.MenuManagementActivity;
import com.example.admin.restaurantmanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TableManagementAdapter extends RecyclerView.Adapter {
    public static TableManagementActivity tableManagementActivity;
    ArrayList<TableManagementInfo> tableManagementInfoList = new ArrayList<>();
    Context context;

    public TableManagementAdapter(ArrayList<TableManagementInfo> tableManagementInfoList, TableManagementActivity tableManagementActivity) {
        this.tableManagementInfoList = tableManagementInfoList;
        this.tableManagementActivity = tableManagementActivity;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.tab_manage_table, viewGroup, false);
        return new MyTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        View view = viewHolder.itemView;
        TextView txtTableName = (TextView) view.findViewById(R.id.txtTableNameManage);
        ImageView imgDeleteTable = view.findViewById(R.id.imgbDeleteTableManage);
        ImageView imgEditTable = view.findViewById(R.id.imgbEditTableManage);


        imgDeleteTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              DeleteTable(v.getContext(), viewHolder.getAdapterPosition());
            }
        });

        imgEditTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iEditTable = new Intent(v.getContext(), EditTableManagementActivity.class);
                Bundle bEditTable = new Bundle();
                bEditTable.putSerializable("infoTable", tableManagementInfoList);
                bEditTable.putInt("position", viewHolder.getAdapterPosition());
                iEditTable.putExtras(bEditTable);
                v.getContext().startActivity(iEditTable);
            }
        });

        TableManagementInfo tableManagementInfo = tableManagementInfoList.get(i);
        txtTableName.setText("Bàn " + tableManagementInfo.getTableName());
    }

    @Override
    public int getItemCount() {
        return tableManagementInfoList.size();
    }

    private class MyTableViewHolder extends RecyclerView.ViewHolder {
        public MyTableViewHolder(View view) {
            super(view);
        }
    }

    public AlertDialog.Builder DeleteTable(final Context context, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("XÓA MÓN ĂN");
        builder.setMessage("Bạn có chắc muốn xóa bàn \"" + tableManagementInfoList.get(position).getTableName() + "\" không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
                DatabaseReference myRef = database.getReference(AddTableManagementActivity.FB_DATABASE_TABLE);
                myRef.child(tableManagementInfoList.get(position).getTableName()).setValue(null);

                tableManagementActivity.finish();
                Intent intent = new Intent(context, TableManagementActivity.class);
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
}

