package com.example.alibaba.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alibaba.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLogin extends AppCompatActivity {
    private Button SignInbtn;
    private EditText inputpass_signin,inputemail_signin;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        SignInbtn=findViewById(R.id.LoginSellerBtn);
        inputemail_signin=findViewById(R.id.EmailSellerLogin);
        inputpass_signin=findViewById(R.id.PassSellerLogin);
        loadingbar=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();

        SignInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailLogin=inputemail_signin.getText().toString();
                final String passLogin=inputpass_signin.getText().toString();

                if(!passLogin.equals("") && !emailLogin.equals(""))
                {
                    loadingbar.setTitle("Login Seller Account");
                    loadingbar.setMessage("Please Wait we are checking credentials");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();
                    mAuth.signInWithEmailAndPassword(emailLogin,passLogin)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(SellerLogin.this, "You are Logged In successfuly", Toast.LENGTH_SHORT).show();
                                        Intent intentLogin=new Intent(SellerLogin.this, SellerHome.class);
                                        intentLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentLogin);
                                        finish();
                                    }
                                }
                              });
                }
            }
        });
    }
}