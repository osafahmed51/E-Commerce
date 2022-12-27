package com.example.alibaba.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alibaba.R;
import com.example.alibaba.Seller.sellerProductCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainActivity extends AppCompatActivity {
    private Button ApplyCHangesBtn,Delete_Btnn;
    private EditText nameMaintain,priceMaintain,descMaintain;
    private ImageView imgVMntn_Admin;
    private String ProductIdd="";
    private DatabaseReference prodReff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain);
        ApplyCHangesBtn=findViewById(R.id.ApplyChanges_Maintain);
        nameMaintain=findViewById(R.id.product_name_Maintain);
        priceMaintain=findViewById(R.id.product_price_Maintain);
        descMaintain=findViewById(R.id.product_desc_Maintain);
        imgVMntn_Admin=findViewById(R.id.ImageV_Maintain);
        Delete_Btnn=findViewById(R.id.Delete_Maintain);
        ProductIdd=getIntent().getStringExtra("pid");
        prodReff= FirebaseDatabase.getInstance().getReference().child("Products").child(ProductIdd);

        displaySpecificProductInfo();
        ApplyCHangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });
        Delete_Btnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodReff.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intenttt=new Intent(AdminMaintainActivity.this, sellerProductCategory.class);
                        startActivity(intenttt);
                        finish();
                        Toast.makeText(AdminMaintainActivity.this, "Product Deleted Successfuly", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void applyChanges() {
        String ppName=nameMaintain.getText().toString();
        String ppPrice=priceMaintain.getText().toString();
        String ppDesc=descMaintain.getText().toString();
        if(TextUtils.isEmpty(ppName))
        {
            Toast.makeText(this, "Please Enter the Product Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ppPrice))
        {
            Toast.makeText(this, "Please Enter the Product Price", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ppDesc))
        {
            Toast.makeText(this, "Please Enter the Product Description", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String,Object> maintain_map=new HashMap<>();
            maintain_map.put("pid", ProductIdd);
            maintain_map.put("pname",ppName);
            maintain_map.put("price",ppPrice);
            maintain_map.put("description",ppDesc);
            prodReff.updateChildren(maintain_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainActivity.this, "Changes Apllied Successfuly", Toast.LENGTH_SHORT).show();
                        Intent intenttt=new Intent(AdminMaintainActivity.this, sellerProductCategory.class);
                        startActivity(intenttt);
                        finish();

                    }
                }
            });
        }

    }

    private void displaySpecificProductInfo() {
        prodReff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String pName=snapshot.child("pname").getValue().toString();
                    String pPrice=snapshot.child("price").getValue().toString();
                    String pDesc=snapshot.child("description").getValue().toString();
                    String pimggg=snapshot.child("image").getValue().toString();

                    nameMaintain.setText(pName);
                    priceMaintain.setText(pPrice);
                    descMaintain.setText(pDesc);
                    Picasso.get().load(pimggg).into(imgVMntn_Admin);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}