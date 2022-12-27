package com.example.alibaba.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.alibaba.Admin.AdminCheckNewProducts;
import com.example.alibaba.Buyer.MainActivity;
import com.example.alibaba.Model.Product;
import com.example.alibaba.R;
import com.example.alibaba.ViewHolder.ProductViewHolder;
import com.example.alibaba.ViewHolder.itemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SellerHome extends AppCompatActivity {
    private Button logout_bntn,add;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView_home;
    private DatabaseReference UnveriFiedProdRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);


       bottomNavigationView=findViewById(R.id.bottomNavView);
       recyclerView_home=findViewById(R.id.recyclerview_sellerHome);
       recyclerView_home.setHasFixedSize(true);
       recyclerView_home.setLayoutManager(new LinearLayoutManager(this));
       UnveriFiedProdRef= FirebaseDatabase.getInstance().getReference().child("Products");



       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId())
               {
                   case R.id.navigation_home:
                       return true;
                   case R.id.navigation_add:
                       Intent intnn=new Intent(SellerHome.this, sellerProductCategory.class);
                       startActivity(intnn);
                       return true;
                   case R.id.navigation_logOut:
                       final FirebaseAuth mAuth;
                       mAuth=FirebaseAuth.getInstance();
                       mAuth.signOut();
                       Intent intentLogout=new Intent(SellerHome.this, MainActivity.class);
                       intentLogout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(intentLogout);
                       finish();
                       return true;
               }

               return false;

           }
       });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Product> options=new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(UnveriFiedProdRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Product.class)
                .build();
        FirebaseRecyclerAdapter<Product, itemViewHolder> adapter=
                new FirebaseRecyclerAdapter<Product, itemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull itemViewHolder holder, int position, @NonNull Product model)
                    {
                        holder.txttproductname.setText(model.getPname());
                        holder.txttproductdescription.setText(model.getDescription());
                        holder.txtproductPrice.setText("Price =" + model.getPrice());
                        holder.txtproductPrice.setText("State =" + model.getProductstate());
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
                                AlertDialog.Builder builder=new AlertDialog.Builder(SellerHome.this);
                                builder.setTitle("Do You want to Delete this Product, are you sure?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which==0)
                                        {
                                            deleteProduct(productId);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.selleritemview,parent,false);
                        itemViewHolder holder=new itemViewHolder(view);
                        return holder;
                    }
                };
        recyclerView_home.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteProduct(String productId) {
        UnveriFiedProdRef.child(productId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SellerHome.this, "The item is Deleted Successfuly", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}