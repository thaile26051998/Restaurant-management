package com.example.admin.restaurantmanagement.EmployManagement;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.restaurantmanagement.FoodManagement.AddDrinkManagementActivity;
import com.example.admin.restaurantmanagement.FoodManagement.MenuManagementActivity;
import com.example.admin.restaurantmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class AddEmployManagementActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edtEmail, edtPass, edtPhone, edtName;
    private Button btnChooseImage;
    private ImageView imgEmploy;
    private static final int REQUEST_CHOOSE_IMAGE = 1234;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabaseRef;
    private StorageReference firebaseStorageRef;
    public static final String FB_STORAGE_EMPLOY = "Employ/";
    public static final String FB_DATABASE_EMPLOY = "Employ";
    private Uri imgUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_employ_management_activity);
        inItView();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_EMPLOY);
        firebaseStorageRef = FirebaseStorage.getInstance().getReference(FB_STORAGE_EMPLOY);
        firebaseAuth = FirebaseAuth.getInstance();

        toolbar.setTitle("Thêm nhân viên");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_employ_management, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save_employ) {
            addEmploy();
        }

        return super.onOptionsItemSelected(item);
    }

    private void chooseImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CHOOSE_IMAGE);
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imgEmploy.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void addEmploy() {
        final String name = edtName.getText().toString();
        final String phone = edtPhone.getText().toString();
        final String email = edtEmail.getText().toString();
        final String pass = edtPass.getText().toString();

        if (name.isEmpty()) {
            edtName.setError("Bạn cần nhập tên");
            edtName.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            edtPhone.setError("Số điện thoại phải có 10 số");
            edtPhone.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Sai định dạng email");
            edtEmail.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            edtPass.setError("Mật khẩu phải nhiều hơn 6 ký tự");
            edtPass.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (imgUri != null) {
                        final ProgressDialog dialog = new ProgressDialog(AddEmployManagementActivity.this);
                        dialog.setTitle("Uploading image");
                        dialog.show();

                        final StorageReference ref = firebaseStorageRef.child(FB_DATABASE_EMPLOY + System.currentTimeMillis()
                                + "." + getImageExt(imgUri));

                        //up file lên FB
                        ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Đăng tải thành công", Toast.LENGTH_SHORT).show();

                                //lấy uri download
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                Uri downloadUrl = urlTask.getResult();

                                //Upload lên database
                                String uploadId = firebaseDatabaseRef.push().getKey();
                                EmployManagementInfo employManagementInfo = new EmployManagementInfo(name, phone, email, pass,
                                        downloadUrl.toString(), uploadId);
                                firebaseDatabaseRef.child(uploadId).setValue(employManagementInfo);

                                Intent intent = new Intent(AddEmployManagementActivity.this, EmployManagementActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //Show upload progress
                                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                dialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Vui lòng chọn hình ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void inItView() {
        toolbar = (Toolbar) findViewById(R.id.nav_add_employ_management);
        edtEmail = findViewById(R.id.edt_email_employ);
        edtPass = findViewById(R.id.edt_pass_employ);
        edtPhone = findViewById(R.id.edt_phone_employ);
        edtName = findViewById(R.id.edt_name_employ);
        btnChooseImage = findViewById(R.id.btnChooseImageAddFood);
        imgEmploy = findViewById(R.id.imgAddFoodOrder);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(AddEmployManagementActivity.this, EmployManagementActivity.class);
        startActivity(intent);
    }
}
