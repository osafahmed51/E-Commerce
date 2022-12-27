package com.example.alibaba.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alibaba.Model.Product;
import com.example.alibaba.R;
import com.example.alibaba.prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity2 extends AppCompatActivity {
    private ImageView imageView_productdetails;
    private TextView prod_name_details2,prod_desc_details2,prod_price_details2,result_details2_textv;
    private FloatingActionButton floatingActionButton_plus,floatingActionButton_minus;
    private int count=1;
    private String ProductID="";
    private Button addtocart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details2);
        addtocart=findViewById(R.id.btn_add_to_cart);
        imageView_productdetails=findViewById(R.id.product_details2_imgv);
        prod_name_details2=findViewById(R.id.product_name_details2);
        prod_desc_details2=findViewById(R.id.product_desc_details2);
        prod_price_details2=findViewById(R.id.product_Price_details2);
        floatingActionButton_plus=findViewById(R.id.plus_button_details2);
        floatingActionButton_minus=findViewById(R.id.cart_btn_for_details2_minus);
        result_details2_textv=findViewById(R.id.result_textv);
        ProductID=getIntent().getStringExtra("pid");

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addingToList();
            }
        });

        getProductDetails(ProductID);
        floatingActionButton_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(ProductDetailsActivity2.this, "Clicked", Toast.LENGTH_SHORT).show();
                count=count+1;
                String s_count=String.valueOf(count);
          //      Toast.makeText(ProductDetailsActivity2.this, "Count"+count, Toast.LENGTH_SHORT).show();
                  result_details2_textv.setText(s_count );
            }
        });
        floatingActionButton_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count <=1)
                {
                    count=1;
                    String s_count=String.valueOf(count);
                    result_details2_textv.setText(s_count );
                }
                else
                {
                    count=count-1;
                    String s_count=String.valueOf(count);
                    result_details2_textv.setText(s_count );
                }
            }
        });

    }

    private void addingToList() {

        String saveCurrentDate,saveCurrentTime;
        Calendar callForDate=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate=currentdate.format(callForDate.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH,mm,ss,a");
        saveCurrentTime=currentTime.format(callForDate.getTime());

        final DatabaseReference cart_ref=FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String ,Object> cart_map=new HashMap<>();
        cart_map.put("pid",ProductID);
        cart_map.put("pname",prod_name_details2.getText().toString());
        cart_map.put("price",prod_price_details2.getText().toString());
        cart_map.put("date",saveCurrentDate);
        cart_map.put("time",saveCurrentTime);
        cart_map.put("quantity",result_details2_textv.getText());
        cart_map.put("discount","");
        cart_ref.child("User View").child(Prevelant.currentOnlineUser.getPhone()).child("Products")
                .child(ProductID).updateChildren(cart_map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful())
                       {
                           cart_ref.child("Admin View").child(Prevelant.currentOnlineUser.getPhone()).child("Products")
                                   .child(ProductID).updateChildren(cart_map)
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful())
                                           {
                                               Toast.makeText(ProductDetailsActivity2.this, "Products Added Successfuly", Toast.LENGTH_SHORT).show();
                                               Intent intent_cart=new Intent(ProductDetailsActivity2.this, Home_real.class);
                                               startActivity(intent_cart);
                                           }
                                       }
                                   });
                       }
                    }
                });

    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productref= FirebaseDatabase.getInstance().getReference().child("Products");
        productref.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Product products=snapshot.getValue(Product.class);
                    prod_name_details2.setText(products.getPname());
                    prod_desc_details2.setText(products.getDescription());
                    prod_price_details2.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(imageView_productdetails);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void checkOrderState(){
        final DatabaseReference orderRef=FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevelant.currentOnlineUser.getPhone());
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shippingState=snapshot.child("state").getValue().toString();

                    if(shippingState.equals("shipped")){

                    }
                    else if(shippingState.equals("not shipped"))
                    {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}