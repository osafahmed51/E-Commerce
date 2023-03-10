package com.example.alibaba.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alibaba.Model.Cart;
import com.example.alibaba.R;
import com.example.alibaba.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProducts extends AppCompatActivity {
    private RecyclerView prodListRecycl;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String UserId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        UserId=getIntent().getStringExtra("uid");
        prodListRecycl=findViewById(R.id.recyclerview_UserProdActivity);
        prodListRecycl.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(this);
        prodListRecycl.setLayoutManager(layoutManager);

        cartListRef= FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View")
                .child(UserId).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef,Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtprodname_cart.setText(model.getPname());
                holder.txtprodquantity_cart.setText( model.getQuantity());
                holder.txtprodprice_cart.setText("Price:" +model.getPrice());

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                return new CartViewHolder(view);
            }
        };
    prodListRecycl.setAdapter(adapter);
    adapter.startListening();
    }

}