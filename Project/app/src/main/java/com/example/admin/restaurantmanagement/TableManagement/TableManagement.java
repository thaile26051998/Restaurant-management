package com.example.admin.restaurantmanagement.TableManagement;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TableManagement {
    Context context;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabaseRef;


    public static final String FB_DATABASE_EMPLOY = "Table";

    TableManagement(Context context){
        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_EMPLOY);
        this.context = context;
    }

    public void DeleteTable(final TableManagementInfo table)
    {

    }

}
