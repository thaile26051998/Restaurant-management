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
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class EditMenuManagementActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private Toolbar toolbar;
    private EditText edtFoodName, edtFoodPrice, edtDetail;
    private Button btnChoseImage;
    private ImageView imgFood;
    private Uri imgUri;
    public static final int REQUEST_CHOOSE_IMAGE = 1234;
    public static final String FB_STORAGE_FOOD = "Food/";
    public static final String FB_STORAGE_DRINK = "Drink/";
    public static final String FB_DATABASE_FOOD = "Menu/Food";
    public static final String FB_DATABASE_DRINK = "Menu/Drink";
    public int position;
    public int type; // 0 là đồ ăn, 1 là thức uống
    public String key;
    ArrayList<MenuManagementInfo> food;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_management_activity);
        inItView();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Sửa thông tin món ăn");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        Bundle bundle = this.getIntent().getExtras();
        position = bundle.getInt("position");
        type = bundle.getInt("type");
        if(type == 0) {
            mStorageRef = FirebaseStorage.getInstance().getReference(FB_STORAGE_FOOD);
            mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_FOOD);
        }
        else{
            mStorageRef = FirebaseStorage.getInstance().getReference(FB_STORAGE_DRINK);
            mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_DRINK);
        }

        food = (ArrayList<MenuManagementInfo>) bundle.getSerializable("infoFood");

        key = food.get(position).getFoodName();

        edtFoodName.setText(food.get(position).getFoodName());
        edtFoodPrice.setText(food.get(position).getPrice());
        edtDetail.setText(food.get(position).getDetail());
        Picasso.get().load(food.get(position).getUrl()).into(imgFood);

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
        edtFoodName = findViewById(R.id.edtFoodName);
        edtFoodPrice = findViewById(R.id.edtFoodPrice);
        btnChoseImage = findViewById(R.id.btnChooseImageAddFood);
        imgFood = findViewById(R.id.imgAddFoodOrder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            editFood();
        }

        return true;
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void editFood() {
        String name = edtFoodName.getText().toString();
        String price = edtFoodPrice.getText().toString();
        String detail = edtDetail.getText().toString();

        if(checkNameFood(name) == 1){
            edtFoodName.setError("Tên đã tồn tại");
            edtFoodName.requestFocus();
            return;
        }

        if(name.isEmpty()){
            edtFoodName.setError("Bạn cần nhập tên");
            edtFoodName.requestFocus();
            return;
        }

        if(price.isEmpty()){
            edtFoodPrice.setError("Bạn cần nhập giá");
            edtFoodPrice.requestFocus();
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
            final StorageReference ref = mStorageRef.child(FB_DATABASE_FOOD + System.currentTimeMillis() + "." + getImageExt(imgUri));

            //up file lên FB
            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();

                    MenuManagementInfo foodManagementInfo = new MenuManagementInfo(edtFoodName.getText().toString(),
                            edtFoodPrice.getText().toString(),
                            edtDetail.getText().toString(),
                            downloadUrl.toString());

                    //Save image info into firebase database
                    mDatabaseRef.child(key).setValue(null);
                    mDatabaseRef.child(edtFoodName.getText().toString()).setValue(foodManagementInfo);
                    Intent intent = new Intent(EditMenuManagementActivity.this, MenuManagementActivity.class);
                    finish();
                    FoodManagementAdapter.foodManagementFragment.getActivity().finish();
                    DrinkManagementAdapter.drinkManagementFragment.getActivity().finish();
                    myMessage();
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
            MenuManagementInfo foodManagementInfo = new MenuManagementInfo(edtFoodName.getText().toString(),
                    edtFoodPrice.getText().toString(),
                    edtDetail.getText().toString(),
                    food.get(position).getUrl().toString());

            mDatabaseRef.child(key).setValue(null);
            mDatabaseRef.child(edtFoodName.getText().toString()).setValue(foodManagementInfo);

            finish();
            FoodManagementAdapter.foodManagementFragment.getActivity().finish();
            DrinkManagementAdapter.drinkManagementFragment.getActivity().finish();
            Intent intent = new Intent(EditMenuManagementActivity.this, MenuManagementActivity.class);
            myMessage();
            startActivity(intent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imgFood.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void myMessage()
    {
        Toast.makeText(this, "Sửa thông tin menu thành công!", Toast.LENGTH_SHORT).show();
    }

    int checkNameFood(String name)
    {
        for(int i = 0; i < food.size(); i++)
        {
            if(name.equals(food.get(i).getFoodName()) && i != position)
            {
                return 1;
            }
        }
        return 0;
    }
}
