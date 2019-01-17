package com.example.admin.restaurantmanagement.EmployManagement;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EmployManagement {
    Context context;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabaseRef;
    private StorageReference firebaseStorageRef;

    public static final String FB_STORAGE_EMPLOY = "Employ/";
    public static final String FB_DATABASE_EMPLOY = "Employ";

    EmployManagement(Context context){
        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_EMPLOY);
        firebaseStorageRef = FirebaseStorage.getInstance().getReference(FB_STORAGE_EMPLOY);
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public void DeleteEmployee(final EmployManagementInfo employee)
    {
        final String userEmail = employee.getEmail();
        String userPassword = employee.getPass();

        //Xóa user khỏi firebaseDatabaseRef
        firebaseDatabaseRef.child(employee.getKeyID()).setValue(null);

        //Xóa user khỏi firebaseAuth
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(userEmail, userPassword);

        firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Xóa nhân viên thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //Xóa user khỏi firebaseStorage
        //do something
    }

}
