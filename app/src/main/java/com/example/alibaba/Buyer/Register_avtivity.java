package com.example.alibaba.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alibaba.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class Register_avtivity extends AppCompatActivity {
    Button register_btn;
    EditText inputname, inputphone, inputpassword;
    ProgressDialog loadingbar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_avtivity);
        FirebaseApp.initializeApp(this);
        register_btn = findViewById(R.id.register_btn);
        inputname = findViewById(R.id.Name_register);
        inputphone = findViewById(R.id.phone_number_register);
        inputpassword = findViewById(R.id.password_register);
        loadingbar= new ProgressDialog(this);
        Paper.init(this);
        mAuth = FirebaseAuth.getInstance();
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String name = inputname.getText().toString();
        String phone = inputphone.getText().toString();
        String pass = inputpassword.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please Set Your Password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please wait We are Checking the credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidataPhoneNum(name,phone,pass);

        }

    }

    private void ValidataPhoneNum(String name, String phone, String pass) {

        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("User").child(phone).exists())){
                    HashMap<String ,Object> userdatamap=new HashMap<>();
                    userdatamap.put("phone",phone);
                    userdatamap.put("password",pass);
                    userdatamap.put("name",name);
                    Rootref.child("Users").child(phone).updateChildren(userdatamap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Register_avtivity.this, "Congratulation , Your Account Has been Created", Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                    }
                                    else {
                                        loadingbar.dismiss();
                                        Toast.makeText(Register_avtivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(Register_avtivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(Register_avtivity.this, "Please Sig-up to Another Account", Toast.LENGTH_SHORT).show();
                    Intent intenti=new Intent(Register_avtivity.this, Home_real.class);
                    startActivity(intenti);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}