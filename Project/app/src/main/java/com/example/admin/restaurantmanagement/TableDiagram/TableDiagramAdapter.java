package com.example.admin.restaurantmanagement.TableDiagram;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.restaurantmanagement.R;
import com.example.admin.restaurantmanagement.RestaurantMenu.RestaurantMenuActivity;
import com.example.admin.restaurantmanagement.PayFood.PayFoodActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TableDiagramAdapter extends RecyclerView.Adapter {
    private DatabaseReference mData, mData2;
    private Context context;
    private List<TableInfo> tableInfoList = new ArrayList<>();
    public RecyclerView.ViewHolder viewHolder;
    public static int pos;
    String tableName[] = {"Bàn 01", "Bàn 02", "Bàn 03", "Bàn 04", "Bàn 05", "Bàn 06", "Bàn 07", "Bàn 08", "Bàn 09", "Bàn 10",
            "Bàn 11", "Bàn 12", "Bàn 13", "Bàn 14", "Bàn 15", "Bàn 16", "Bàn 17", "Bàn 18"};
    Boolean selected = false;

    public TableDiagramAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < tableName.length; i++) {
            TableInfo tableInfo = new TableInfo(tableName[i], selected);
            tableInfoList.add(tableInfo);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.tab_table_diagram, viewGroup, false);
        return new MyTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final View view = viewHolder.itemView;
        TextView txtTableName = view.findViewById(R.id.txtTableName);
        ImageView imgTable = view.findViewById(R.id.imgTable);
        ImageView imgSelect = view.findViewById(R.id.imgSelect);
        final int temp = i;
        imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_table_diagram, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuTableSelectFood:
                                Intent intentSelectedFood = new Intent(context, RestaurantMenuActivity.class);
                                pos =  temp + 1;
                                context.startActivity(intentSelectedFood);
                                break;
                            case R.id.menuTablePay:
                                Intent intentPay = new Intent(context, PayFoodActivity.class);
                                pos = temp + 1;
                                context.startActivity(intentPay);
                                break;
                            case R.id.menuTableCancel:
                                mData = FirebaseDatabase.getInstance().getReference("Table/" + "tb" + Integer.toString(TableDiagramAdapter.pos) + "/ListOder/");
                                mData2 = FirebaseDatabase.getInstance().getReference("Table/" + "tb" + Integer.toString(TableDiagramAdapter.pos) + "/");
                                cancelTable(view);
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        TableInfo tableInfo = tableInfoList.get(i);
        txtTableName.setText(tableInfo.getTableName());
    }

    @Override
    public int getItemCount() {
        return tableInfoList.size();
    }

    private void cancelTable(final View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        builder.setMessage("Bạn có chắc muốn hủy bàn ăn?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mData.setValue(null);
                mData2.child("TinhTrang").setValue("Trống");

                Toast.makeText(v.getContext(), "Hủy thành công", Toast.LENGTH_SHORT).show();

                ((Activity) v.getContext()).finish();

                Intent intent = new Intent(v.getContext(), TableDiagramActivity.class);
                v.getContext().startActivity(intent);

                PayFoodActivity.totalPrice = 0;

            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private class MyTableViewHolder extends RecyclerView.ViewHolder {
        public MyTableViewHolder(View view) {
            super(view);
        }
    }
}
