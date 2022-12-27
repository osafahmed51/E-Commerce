package com.example.alibaba.Buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alibaba.Admin.AdminHome;
import com.example.alibaba.Seller.sellerProductCategory;
import com.example.alibaba.Model.Userrr;
import com.example.alibaba.R;
import com.example.alibaba.prevelant.Prevelant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class  login1_activity extends AppCompatActivity {
    EditText inputphone_login , inputpassword_login;
    Button Loginbtn;
    TextView forgot, i_am_admin , i_am_not_admin;
    CheckBox Remember_me;
    ProgressDialog loadingbar1;
    private String parentDbName="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        inputphone_login = findViewById(R.id.phone_login);
        inputpassword_login = findViewById(R.id.password_login);
        Loginbtn = findViewById(R.id.login_btn1);
        forgot = findViewById(R.id.forget_text_login);
        i_am_admin = findViewById(R.id.i_am_admin_login);
        i_am_not_admin=findViewById(R.id.i_am_not_admin_login);
        Remember_me = findViewById(R.id.checkbox_login);
        loadingbar1 =new ProgressDialog(this);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_forgt=new Intent(login1_activity.this, resetPassword.class);
                intent_forgt.putExtra("check","login");
                startActivity(intent_forgt);
            }
        });
        i_am_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginbtn.setText("Login Admin");
                i_am_admin.setVisibility(View.INVISIBLE);
                i_am_not_admin.setVisibility(View.VISIBLE);
                parentDbName="Admins";
            }
        });
        i_am_not_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginbtn.setText("Login User");
                i_am_not_admin.setVisibility(View.INVISIBLE);
                i_am_admin.setVisibility(View.VISIBLE);
                parentDbName="Admins";
            }
        });


        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Loginuser();

            }


            private void Loginuser() {
               String phone=inputphone_login.getText().toString();
               String pass=inputpassword_login.getText().toString();
               if(TextUtils.isEmpty(phone)){
                   Toast.makeText(login1_activity.this, "Please Enter Your Valid Phone Number To Login", Toast.LENGTH_SHORT).show();
               }
               else if(TextUtils.isEmpty(pass)){
                   Toast.makeText(login1_activity.this, "Please Enter Your Valid Password to Login", Toast.LENGTH_SHORT).show();
               }
               else{
                   loadingbar1.setTitle("Login Account");
                   loadingbar1.setMessage("Please wait we are checking credentials");
                   loadingbar1.setCanceledOnTouchOutside(false);
                   loadingbar1.show();

                   AllowAccessAccount(phone,pass);
               }
            }
        });

    }

    private void AllowAccessAccount(String phone,String pass) {

        if(Remember_me.isChecked()){
            Paper.book().write(Prevelant.UserPhoneKey,phone);
            Paper.book().write(Prevelant.UserPasswordKey,pass);
        }

        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(phone).exists())
                {
                    Userrr userdata = snapshot.child(parentDbName).child(phone).getValue(Userrr.class);
                    if(userdata.getPhone().equals(phone)){
                        if ((userdata.getPassword().equals(pass))){
                            if(parentDbName.equals("Admins"))
                             {
                                Toast.makeText(login1_activity.this, "Logged in successfuly", Toast.LENGTH_SHORT).show();
                                loadingbar1.dismiss();
                                Intent intent_admin=new Intent(login1_activity.this, AdminHome.class);
                                startActivity(intent_admin);
                            }
                            else if(parentDbName.equals("Users"))
                            {
                                Toast.makeText(login1_activity.this, "Logged in successfuly", Toast.LENGTH_SHORT).show();
                                loadingbar1.dismiss();
                                Intent intent_login=new Intent(login1_activity.this, Home_real.class);
                                Prevelant.currentOnlineUser=userdata;
                                startActivity(intent_login);

                            }

                        }
                        else{
                            Toast.makeText(login1_activity.this, "Password in incorrect", Toast.LENGTH_SHORT).show();
                            loadingbar1.dismiss();
                        }
                    }

                }
                else
                {
                    Toast.makeText(login1_activity.this, "Account with this" + phone +"do not exist", Toast.LENGTH_SHORT).show();
                    loadingbar1.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });


    }


}