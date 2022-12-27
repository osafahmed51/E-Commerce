package com.example.alibaba.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alibaba.Interfaces.ItemClickListener;
import com.example.alibaba.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtprodname_cart,txtprodquantity_cart,txtprodprice_cart;
    public ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtprodname_cart=itemView.findViewById(R.id.cart_prod_name);
        txtprodquantity_cart=itemView.findViewById(R.id.cart_prod_quantity);
        txtprodprice_cart=itemView.findViewById(R.id.cart_prod_price);

    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
