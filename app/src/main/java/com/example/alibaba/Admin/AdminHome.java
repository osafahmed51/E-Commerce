package com.example.alibaba.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.alibaba.Buyer.Home_real;
import com.example.alibaba.Buyer.MainActivity;
import com.example.alibaba.R;

public class AdminHome extends AppCompatActivity {
    private Button btn_checkorders, btn_logout,maintainnn_btn,approve_prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        btn_checkorders = findViewById(R.id.Admin_checkorderbtn);
        btn_logout = findViewById(R.id.Admin_logout);
        maintainnn_btn=findViewById(R.id.Admin_maintain_btn);
        approve_prod=findViewById(R.id.Admin_ApproveProd_btn);
        approve_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_checkorders=new Intent(AdminHome.this, AdminCheckNewProducts.class);
                startActivity(intent_checkorders);
            }
        });


        maintainnn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Admn=new Intent(AdminHome.this, Home_real.class);
                intent_Admn.putExtra("Admin","Admin");
                startActivity(intent_Admn);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_logout=new Intent(AdminHome.this, MainActivity.class);
                intent_logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_logout);
                finish();
            }
        });
        btn_checkorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_logout=new Intent(AdminHome.this, AdminNewOrderActivity.class);
                startActivity(intent_logout);
            }
        });

    }
}