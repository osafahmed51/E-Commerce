package com.example.alibaba.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alibaba.Model.Cart;
import com.example.alibaba.R;
import com.example.alibaba.ViewHolder.CartViewHolder;
import com.example.alibaba.prevelant.Prevelant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView1;
    private RecyclerView.LayoutManager layoutManager1;
    private Button btn_next;
    private TextView total_price_txtv,txtMsg1;
    private int overAllAmount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView1 = findViewById(R.id.recyclerview_cart);
        recyclerView1.setHasFixedSize(true);
        btn_next = findViewById(R.id.next_btn_cart);
        total_price_txtv = findViewById(R.id.price_in_cart_total);
        layoutManager1 = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager1);
        txtMsg1=findViewById(R.id.msg1);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_price_txtv.setText(String.valueOf(overAllAmount));
                Intent intent_next=new Intent(CartActivity.this, ConfirmFinalOrder.class);
                intent_next.putExtra("Total Price" ,String.valueOf(overAllAmount));
                startActivity(intent_next);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();

        final DatabaseReference cart_ref = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cart_ref.child("User View")
                        .child(Prevelant.currentOnlineUser.getPhone())
                        .child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtprodname_cart.setText(model.getPname());
                holder.txtprodquantity_cart.setText( model.getQuantity());
                holder.txtprodprice_cart.setText("Price:" +model.getPrice());
                int oneTypeProdPriceT=Integer.valueOf(model.getPrice()) * Integer.valueOf(model.getQuantity());
                overAllAmount=oneTypeProdPriceT + overAllAmount;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0)
                                {
                                    Intent intentt =new Intent(CartActivity.this, ProductDetailsActivity2.class);
                                    intentt.putExtra("pid",model.getPid());
                                    startActivity(intentt);
                                }
                                if(which==1)
                                {
                                    cart_ref.child("User View")
                                            .child(Prevelant.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this, "Item Removed Successfuly", Toast.LENGTH_SHORT).show();
                                                        Intent intenttt=new Intent(CartActivity.this, Home_real.class);
                                                        startActivity(intenttt);


                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
               CartViewHolder holder=new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView1.setAdapter(adapter);
        adapter.startListening();

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
                    String userName=snapshot.child("name").getValue().toString();
                    if(shippingState.equals("shipped")){
                        total_price_txtv.setText("Dear User" + userName +"\n your order is placed successfuly" );
                        recyclerView1.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations your order has been placed successfully, soon you will recieve it ");
                        btn_next.setVisibility(View.GONE);
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        total_price_txtv.setText("Shipping State = Not Shipped" );
                        recyclerView1.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        btn_next.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}