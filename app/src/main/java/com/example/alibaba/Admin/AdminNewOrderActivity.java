package com.example.alibaba.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.alibaba.Model.AdminOrders;
import com.example.alibaba.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView11;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        recyclerView11=findViewById(R.id.recyclerview_newOrders);
        recyclerView11.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef,AdminOrders.class)
                .build();
        FirebaseRecyclerAdapter<AdminOrders,AdminNewOrdersViewHolder> adapter=new FirebaseRecyclerAdapter<AdminOrders, AdminNewOrdersViewHolder>(options) {
            @SuppressLint("RecyclerView")
            @Override
            protected void onBindViewHolder(@NonNull AdminNewOrdersViewHolder holder, int position, @NonNull AdminOrders model) {
                holder.usernameOrd.setText("Name :" + model.getName());
                holder.totalpriceOrder.setText("Total:" + model.getTotalAmount());
                holder.AddressOrd.setText("Address:" + model.getAddress());
                holder.phoneOrd.setText("Phone:" + model.getPhone());
                holder.DateTimeOrd.setText("Date:" + model.getDate());
                holder.orderbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID=getRef(position).getKey();
                        Intent intentorder_btn=new Intent(AdminNewOrderActivity.this, AdminUserProducts.class);
                        intentorder_btn.putExtra("uid",uID);
                        startActivity(intentorder_btn);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrderActivity.this);
                        builder.setTitle("Have you shipped this order ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    String uID=getRef(position).getKey();
                                    RemoveOrder(uID);
                                }
                                else
                                {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminNewOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders,parent,false);
                return new AdminNewOrdersViewHolder(view);
            }
        };
        recyclerView11.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoveOrder(String uID) {
        ordersRef.child(uID).removeValue();
    }

    public static class AdminNewOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView usernameOrd,phoneOrd,totalpriceOrder,AddressOrd,DateTimeOrd;
        public Button orderbtn;
        public AdminNewOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameOrd=itemView.findViewById(R.id.order_username);
            phoneOrd=itemView.findViewById(R.id.order_phoneNumber);
            totalpriceOrder=itemView.findViewById(R.id.order_price);
            AddressOrd=itemView.findViewById(R.id.order_Address);
            DateTimeOrd=itemView.findViewById(R.id.order_DateTime);
            orderbtn=itemView.findViewById(R.id.showOrderbtn);

        }
    }
}