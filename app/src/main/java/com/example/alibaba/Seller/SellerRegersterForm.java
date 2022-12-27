package com.example.alibaba.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alibaba.Buyer.MainActivity;
import com.example.alibaba.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegersterForm extends AppCompatActivity {
    private Button alreadyAccountbtn;
    private EditText inputphone,inputname,inputemail,inputpasswrd,inputaddress;
    private Button registerSeller;
    private FirebaseAuth mAuth;
    private  ProgressDialog loadingbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_regerster_form);
        alreadyAccountbtn=findViewById(R.id.AlreadyAccountRegSell);
        registerSeller=findViewById(R.id.registerSellerRegstr);
        inputname=findViewById(R.id.nameSellerRegst);
        inputphone=findViewById(R.id.phoneSellerRegst);
        inputemail=findViewById(R.id.emailSellerRegst);
        inputpasswrd=findViewById(R.id.passwordSellerRegst);
        inputaddress=findViewById(R.id.addressShopSellerRegst);
        mAuth=FirebaseAuth.getInstance();
        loadingbar =new ProgressDialog(this);



        alreadyAccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerRegersterForm.this,SellerLogin.class);
                startActivity(intent);
            }
        });

        registerSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });

    }

    private void registerSeller() {

        final String name124=inputname.getText().toString();
        final String phone124=inputphone.getText().toString();
        final String email124=inputemail.getText().toString();
        String password124=inputpasswrd.getText().toString();
        final String address124=inputaddress.getText().toString();

        if(!name124.equals("") && !phone124.equals("") && !email124.equals("") && !password124.equals("") && !address124.equals(""))
        {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please Wait we are checking credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            mAuth.createUserWithEmailAndPassword(email124,password124)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                final DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference()
                                        .child("sellers");
                                String sid=mAuth.getCurrentUser().getUid();
                                HashMap<String ,Object> sellerMap=new HashMap<>();
                                sellerMap.put("sid",sid);
                                sellerMap.put("phone",phone124);
                                sellerMap.put("name",name124);
                                sellerMap.put("email",email124);
                                sellerMap.put("address",address124);
                                rootRef.child(sid).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    loadingbar.dismiss();
                                                    Toast.makeText(SellerRegersterForm.this, "You are successfuly sign up", Toast.LENGTH_SHORT).show();

                                                    Intent intentuser=new Intent(SellerRegersterForm.this, SellerLogin.class);
                                                    intentuser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intentuser);
                                                    finish();
                                                }
                                            }
                                        });

                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Please fill all the Registration form", Toast.LENGTH_SHORT).show();
        }


    }
}