package com.example.alibaba.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.alibaba.Model.Product;
import com.example.alibaba.R;
import com.example.alibaba.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {
    private Button searchbuttn;
    private EditText productNameSearch;
    private RecyclerView recyclerViewInSearch;
    private String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchbuttn=findViewById(R.id.searchbtnn);
        productNameSearch=findViewById(R.id.productnameSearchEdittxt);
        recyclerViewInSearch=findViewById(R.id.recyclerview_InSearch);
        recyclerViewInSearch.setLayoutManager(new GridLayoutManager(SearchActivity.this,2));
        searchbuttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput=productNameSearch.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference refSearch=FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Product> options=new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(refSearch.orderByChild("pname").startAt(searchInput),Product.class)
                .build();
        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
                holder.txttproductname.setText(model.getPname());
                holder.txtproductPrice.setText(model.getPrice());
                holder.txttproductdescription.setText(model.getDescription());
                Picasso.get().load(model.getImage()).into(holder.productImag);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(SearchActivity.this, ProductDetailsActivity2.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
              return new ProductViewHolder(view);
            }
        };
        recyclerViewInSearch.setAdapter(adapter);
        adapter.startListening();
    }
}