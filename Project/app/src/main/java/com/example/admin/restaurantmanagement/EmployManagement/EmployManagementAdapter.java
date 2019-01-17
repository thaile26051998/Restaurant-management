package com.example.admin.restaurantmanagement.EmployManagement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.restaurantmanagement.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EmployManagementAdapter extends Adapter {
    public static EmployManagementActivity employManagementActivity;
    ArrayList<EmployManagementInfo> employManagementInfoList;
    Context context;

    public EmployManagementAdapter(Context context, ArrayList<EmployManagementInfo> employManagementInfoList, EmployManagementActivity employManagementActivity){
        this.employManagementInfoList = employManagementInfoList;
        this.context = context;
        this.employManagementActivity = employManagementActivity;
    }

    public EmployManagementAdapter( ArrayList<EmployManagementInfo> employManagementInfoList){
        this.employManagementInfoList=employManagementInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.tab_manage_employ,viewGroup,false);
        return new MyEmployViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        final EmployManagementAdapter.MyEmployViewHolder myEmployViewHolder = (EmployManagementAdapter.MyEmployViewHolder) viewHolder;

        myEmployViewHolder.imgDeleteEmploy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteEmployee(v.getContext(), viewHolder.getAdapterPosition());
            }
        });

        myEmployViewHolder.imgEditEmploy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iEditEmploy = new Intent(v.getContext(), EditEmployManagementActivity.class);
                Bundle bEditEmploy = new Bundle();
                bEditEmploy.putSerializable("infoEmploy", employManagementInfoList);
                bEditEmploy.putInt("position", viewHolder.getAdapterPosition());
                iEditEmploy.putExtras(bEditEmploy);
                v.getContext().startActivity(iEditEmploy);

            }
        });

        EmployManagementInfo employManagementInfo = employManagementInfoList.get(i);
        myEmployViewHolder.txtEmployName.setText(employManagementInfo.getEmployName());
        myEmployViewHolder.txtEmployPhone.setText(employManagementInfo.getPhone());
        Picasso.get().load(employManagementInfo.getUrl()).into(myEmployViewHolder.imgEmploy);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return employManagementInfoList.size();
    }

    private class MyEmployViewHolder extends RecyclerView.ViewHolder{
       ImageView imgEmploy, imgDeleteEmploy, imgEditEmploy;
       TextView txtEmployName, txtEmployPhone;

        public MyEmployViewHolder(View view) {
            super(view);
            imgEmploy = view.findViewById(R.id.imgEmploy);
            txtEmployName = view.findViewById(R.id.txtEmployName);
            txtEmployPhone = view.findViewById(R.id.txtEmployPhone);
            imgDeleteEmploy = view.findViewById(R.id.imgDeleteEmploy);
            imgEditEmploy = view.findViewById(R.id.imgEditEmploy);
        }
    }

    public AlertDialog.Builder DeleteEmployee(final Context context, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("XÓA MÓN ĂN");
        builder.setMessage("Bạn có chắc muốn xóa \""+ employManagementInfoList.get(position).getEmployName() +"\" không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EmployManagement employManagement = new EmployManagement(context);
                employManagement.DeleteEmployee(employManagementInfoList.get(position));
                //notifyDataSetChanged();

                Intent intent = new Intent(context, EmployManagementActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
