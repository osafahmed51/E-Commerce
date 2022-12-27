package com.example.alibaba.Buyer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alibaba.Admin.AdminMaintainActivity;
import com.example.alibaba.Model.Product;
import com.example.alibaba.R;
import com.example.alibaba.ViewHolder.ProductViewHolder;
import com.example.alibaba.databinding.ActivityHomeRealBinding;
import com.example.alibaba.prevelant.Prevelant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home_real extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference productsref;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String type="";
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeRealBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productsref= FirebaseDatabase.getInstance().getReference().child("Products");
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle !=null)
        {

            type=getIntent().getStringExtra("Admin");
        }


        binding = ActivityHomeRealBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHomeReal.toolbar);
        FloatingActionButton fab=findViewById(R.id.fab);
        binding.appBarHomeReal.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.equals("Admin"))
                {
                    Intent intent_fab=new Intent(Home_real.this, CartActivity.class);
                    startActivity(intent_fab);

                }
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        View header_view=navigationView.getHeaderView(0);
        TextView userNameTeXTView=header_view.findViewById(R.id.user_profile_name);
        CircleImageView userImageVIEw=header_view.findViewById(R.id.user_profile_image);

     if(!type.equals("Admin"))
     {
         userNameTeXTView.setText(Prevelant.currentOnlineUser.getName());
         Picasso.get().load(Prevelant.currentOnlineUser.getImage()).into(userImageVIEw);
     }

        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Product> options=new FirebaseRecyclerOptions
                .Builder<Product>()
                .setQuery(productsref.orderByChild("productstate").equalTo("Approved"), Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
                holder.txttproductname.setText(model.getPname());
                holder.txtproductPrice.setText("Price =" + model.getPrice() + "$");
                holder.txttproductdescription.setText(model.getDescription());
                Picasso.get().load(model.getImage()).into(holder.productImag);
                String prodID=model.getPid();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(type.equals("Admin")){
                            Intent intentAdmnn=new Intent(Home_real.this, AdminMaintainActivity.class);
                            intentAdmnn.putExtra("pid",model.getPid());
                            startActivity(intentAdmnn);
                        }
                        else
                        {
                            Intent intent=new Intent(Home_real.this, ProductDetailsActivity2.class);
                            intent.putExtra("pid",model.getPid());
                            startActivity(intent);
                        }

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_real, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
//        if(id==R.id.action_settings){
//            return true;
//        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.nav_cart)
        {
            if(!type.equals("Admin"))
            {
                Intent intent_fab=new Intent(Home_real.this, CartActivity.class);
                startActivity(intent_fab);
            }


        }
        else if(id== R.id.nav_home)
        {

        }
        else if(id==R.id.nav_search)
        {

            Intent intent_search=new Intent(Home_real.this, SearchActivity.class);
            startActivity(intent_search);

        }
        else if(id==R.id.nav_setting)
        {
            if(!type.equals("Admin")) {
                Intent intentsetting = new Intent(Home_real.this, Settings_Activity.class);
                startActivity(intentsetting);
            }
        }
        else if(id==R.id.nav_logout)
        {

                Paper.book().destroy();
                Intent intentflag = new Intent(Home_real.this, MainActivity.class);
                intentflag.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentflag);


        }
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}