package com.example.alibaba.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alibaba.Model.Product;
import com.example.alibaba.R;
import com.example.alibaba.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCheckNewProducts extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedproductref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_products);
        recyclerView=findViewById(R.id.recyclerV_checkProdNew);
        recyclerView.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        unverifiedproductref= FirebaseDatabase.getInstance().getReference()
                .child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options=new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(unverifiedproductref.orderByChild("productstate").equalTo("Not Approved"),Product.class)
                .build();
        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model)
                    {
                        holder.txttproductname.setText(model.getPname());
                        holder.txttproductdescription.setText(model.getDescription());
                        holder.txtproductPrice.setText("Price =" + model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.productImag);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String productId =model.getPid();
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Yes"
                                                ,"No"
                                        };
                                AlertDialog.Builder builder=new AlertDialog.Builder(AdminCheckNewProducts.this);
                                builder.setTitle("Do You want to Approve this Product, are you sure?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which==0)
                                        {
                                            changeProductState(productId);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                     View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                     ProductViewHolder holder=new ProductViewHolder(view);
                     return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void changeProductState(String productId) {
        unverifiedproductref.child(productId).child("productstate")
                .setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AdminCheckNewProducts.this, "The item is Approved and Now this will be Visible", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}