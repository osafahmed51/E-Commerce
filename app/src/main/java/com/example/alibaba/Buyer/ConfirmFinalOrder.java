package com.example.alibaba.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alibaba.R;
import com.example.alibaba.prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrder extends AppCompatActivity {
    private EditText nameedittxtf,phoneedittxtf,addressedttxtf,cityedttxtf;
    private Button confirmorderbuttn;
    private String totalAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount=getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Amount =" + totalAmount, Toast.LENGTH_SHORT).show();

        nameedittxtf=findViewById(R.id.shipment_name);
        phoneedittxtf=findViewById(R.id.shipment_phone);
        addressedttxtf=findViewById(R.id.shipment_Address);
        cityedttxtf=findViewById(R.id.shipment_city);
        confirmorderbuttn=findViewById(R.id.shipment_Confirm_btn);
        confirmorderbuttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();

            }
        });
    }

    private void check() {
        if(TextUtils.isEmpty(nameedittxtf.getText().toString())){
            Toast.makeText(this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(phoneedittxtf.getText().toString())){
            Toast.makeText(this, "Please Enter Your Phone number", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(addressedttxtf.getText().toString())){
            Toast.makeText(this, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(cityedttxtf.getText().toString())){
            Toast.makeText(this, "Please Enter Your City", Toast.LENGTH_SHORT).show();
        }
        else
        {
            confirmOrder();
        }
    }

    private void confirmOrder() {
        final String saveCurrentTime,saveCurrentDate;
        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentaDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentaDate.format(calForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        final DatabaseReference orderref= FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(Prevelant.currentOnlineUser.getPhone());
        HashMap<String,Object> order_map=new HashMap<>();
        order_map.put("totalAmount",totalAmount);
        order_map.put("name",nameedittxtf.getText().toString());
        order_map.put("phone",phoneedittxtf.getText().toString());
        order_map.put("name",addressedttxtf.getText().toString());
        order_map.put("phone",cityedttxtf.getText().toString());
        order_map.put("date",saveCurrentDate);
        order_map.put("time",saveCurrentTime);
        order_map.put("state","not shipped");
        orderref.updateChildren(order_map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List").child("user View")
                            .child(Prevelant.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrder.this, "Your final order has been placed successfuly", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(ConfirmFinalOrder.this, Home_real.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

    }
}