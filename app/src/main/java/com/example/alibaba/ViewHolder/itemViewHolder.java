package com.example.alibaba.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alibaba.Interfaces.ItemClickListener;
import com.example.alibaba.R;

public class itemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txttproductname,txttproductdescription,txtproductPrice,txtproductStatus;
    public ImageView productImag;
    public ItemClickListener itemClickListener;


    public itemViewHolder(@NonNull View itemView) {
        super(itemView);
        txttproductname=itemView.findViewById(R.id.productname_sellerHOmeLay);
        txttproductdescription=itemView.findViewById(R.id.product_desc_sellerHOmeLay);
        txtproductPrice=itemView.findViewById(R.id.product_price_sellerHOmeLay);
        txtproductStatus=itemView.findViewById(R.id.product_state_sellerHOmeLay);
        productImag=itemView.findViewById(R.id.ImageV_sellerHOmeLay);
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

