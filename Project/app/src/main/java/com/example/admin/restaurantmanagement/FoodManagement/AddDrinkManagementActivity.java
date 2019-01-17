package com.example.admin.restaurantmanagement.FoodManagement;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.restaurantmanagement.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class AddDrinkManagementActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private Toolbar toolbar;
    private EditText edtDrinkName, edtDrinkPrice, edtDetail;
    private Button btnChoseImage;
    private ImageView imgDrink;
    private Uri imgUri;
    public static final int REQUEST_CHOOSE_IMAGE = 1234;
    public static final String FB_STORAGE_FOOD = "Food/";
    public static final String FB_STORAGE_DRINK = "Drink/";
    public static final String FB_DATABASE_FOOD = "Menu/Food";
    public static final String FB_DATABASE_DRINK = "Menu/Drink";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_drink_management_activity);
        inItView();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mStorageRef = FirebaseStorage.getInstance().getReference(AddFoodManagementActivity.FB_STORAGE_DRINK);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(AddFoodManagementActivity.FB_DATABASE_DRINK);
        toolbar.setTitle("Thêm thức uống");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnChoseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CHOOSE_IMAGE);
    }


    private void inItView() {
        toolbar = findViewById(R.id.nav_add_employ_management);
        edtDetail = findViewById(R.id.edtFoodDetail);
        edtDrinkName = findViewById(R.id.edtFoodName);
        edtDrinkPrice = findViewById(R.id.edtFoodPrice);
        btnChoseImage = findViewById(R.id.btnChooseImageAddFood);
        imgDrink = findViewById(R.id.imgAddFoodOrder);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            uploadDrink();
        }

        return true;
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadDrink() {
        final String name = edtDrinkName.getText().toString();
        String price = edtDrinkPrice.getText().toString();
        String detail = edtDetail.getText().toString();

        if(checkNameDrink(name) == 1){
            edtDrinkName.setError("Tên đã tồn tại");
            edtDrinkName.requestFocus();
            return;
        }

        if(name.isEmpty()){
            edtDrinkName.setError("Bạn cần nhập tên");
            edtDrinkName.requestFocus();
            return;
        }

        if(price.isEmpty()){
            edtDrinkPrice.setError("Bạn cần nhập giá");
            edtDrinkPrice.requestFocus();
            return;
        }

        if(detail.isEmpty()){
            edtDetail.setError("Bạn cần nhập mô tả");
            edtDetail.requestFocus();
            return;
        }

        if (imgUri != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading image");
            dialog.show();

            //Get the storage reference
            final StorageReference ref = mStorageRef.child(AddFoodManagementActivity.FB_STORAGE_DRINK + System.currentTimeMillis() + "." + getImageExt(imgUri));

            //up file lên FB
            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Đăng tải thành công", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    MenuManagementInfo foodManagementInfo = new MenuManagementInfo(edtDrinkName.getText().toString(),
                            edtDrinkPrice.getText().toString(),
                            edtDetail.getText().toString(),
                            downloadUrl.toString());

                    //Save image info into firebase database
                    mDatabaseRef.child(name).setValue(foodManagementInfo);
                    Intent intent = new Intent(AddDrinkManagementActivity.this, MenuManagementActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imgDrink.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    int checkNameDrink(String name)
    {
        for(int i = 0; i < DrinkManagementFragment.menuFoodList.size(); i++)
        {
            if(name.equals(DrinkManagementFragment.menuFoodList.get(i).getFoodName()))
            {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(AddDrinkManagementActivity.this, MenuManagementActivity.class);
        startActivity(intent);
    }
}
