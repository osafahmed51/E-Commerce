package com.example.alibaba.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alibaba.Model.Userrr;
import com.example.alibaba.R;
import com.example.alibaba.Seller.SellerHome;
import com.example.alibaba.Seller.SellerRegersterForm;
import com.example.alibaba.prevelant.Prevelant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Button lgnbtn , joinbtn;
    ProgressDialog loadingbar1;
    private TextView sellertxtV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lgnbtn=findViewById(R.id.login_btn1);
        joinbtn=findViewById(R.id.join_btn);
        sellertxtV=findViewById(R.id.SellerOnMainActivity);
        Paper.init(this);
        loadingbar1=new ProgressDialog(this);


        sellertxtV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentn=new Intent(MainActivity.this, SellerRegersterForm.class);
                startActivity(intentn);
            }
        });




        lgnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this , login1_activity.class);
                startActivity(intent);
            }
        });
        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_join=new Intent(MainActivity.this, Register_avtivity.class);
                startActivity(intent_join);
            }
        });


        String UserPhoneKey=Paper.book().read(Prevelant.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevelant.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != ""){
            if(!TextUtils.isEmpty(UserPhoneKey) && (!TextUtils.isEmpty(UserPasswordKey))){
                AllowAccess(UserPhoneKey,UserPasswordKey);
                loadingbar1.setTitle("Already logged in");
                loadingbar1.setMessage("Please wait");
                loadingbar1.setCanceledOnTouchOutside(false);
                loadingbar1.show();



            }
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null)
        {
            Intent intentuser=new Intent(MainActivity.this, SellerHome.class);
            intentuser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentuser);
            finish();
        }

    }

    private void AllowAccess(String userPhoneKey, String userPasswordKey) {
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(userPhoneKey).exists()){
                    Userrr userdata=snapshot.child("Users").child(userPhoneKey).getValue(Userrr.class);
                    if(userdata.getPhone().equals(userPhoneKey)){
                        if(userdata.getPassword().equals(userPasswordKey)){
                            Toast.makeText(MainActivity.this, "You are Already Logged in...", Toast.LENGTH_SHORT).show();
                            loadingbar1.dismiss();
                            Intent intent11=new Intent(MainActivity.this, Home_real.class);
                            Prevelant.currentOnlineUser=userdata;
                            startActivity(intent11);

                        }
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "Account with this" + userPhoneKey+"can't exist", Toast.LENGTH_SHORT).show();
                    loadingbar1.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}