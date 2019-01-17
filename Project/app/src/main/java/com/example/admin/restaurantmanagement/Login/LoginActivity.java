package com.example.admin.restaurantmanagement.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.restaurantmanagement.Management.ManagementActivity;
import com.example.admin.restaurantmanagement.R;
import com.example.admin.restaurantmanagement.TableDiagram.TableDiagramActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPass;
    private Button btnLogin;
    private ConstraintLayout constraintLayout;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity);
        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_restaurant);
        Bitmap blurredBitmap = BlurBuilder.blur(this, originalBitmap);
        constraintLayout.setBackground(new BitmapDrawable(getResources(), blurredBitmap));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
    }

    private void logIn() {
        String email = edtEmail.getText().toString();
        String pass = edtPass.getText().toString();
        if (email.equals("admin@gmail.com") && pass.equals("admin")) {
            startActivity(new Intent(LoginActivity.this, ManagementActivity.class));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Sai định dạng email");
            edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Đang đăng nhập. Xin vui lòng đợi...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(LoginActivity.this, TableDiagramActivity.class));
                        }
                    }
                });
    }

    private void inItView() {
        constraintLayout = (ConstraintLayout) findViewById(R.id.login_container);
        edtEmail = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
    }

    //Fix lỗi nhấn back từ màn hình login quay lại trang trước đó
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
