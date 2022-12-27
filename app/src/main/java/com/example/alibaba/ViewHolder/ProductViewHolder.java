package com.example.alibaba.ViewHolder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alibaba.Buyer.Home_real;
import com.example.alibaba.Interfaces.ItemClickListener;
import com.example.alibaba.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txttproductname,txttproductdescription,txtproductPrice;
    public ImageView productImag;
    public ItemClickListener itemClickListener;
    public CardView cardView;
    public RelativeLayout relativeLayout;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        txttproductname=itemView.findViewById(R.id.productname_layoutres);
        txttproductdescription=itemView.findViewById(R.id.product_desc_layoutres);
        txtproductPrice=itemView.findViewById(R.id.product_price_layoutres);
        productImag=itemView.findViewById(R.id.ImageV_layoutres);
        cardView=itemView.findViewById(R.id.cardview_in_homeReal);
        relativeLayout=itemView.findViewById(R.id.r1_Homereal);
    }
    public void setItemCLickListener(ItemClickListener listener)
    {
        this.itemClickListener=listener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(itemView,getAdapterPosition(),false);
    }
}
