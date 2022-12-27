package com.example.alibaba.Seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.alibaba.R;

public class sellerProductCategory extends AppCompatActivity {
    private ImageView tShirts,sportsTShirts,femaleDresses,sweathers;
    private ImageView glasses,hatsCaps,walletBagsPurses,shoes;
    private ImageView headPhonesHandFree,Laptops,watches,mobilePhones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sellerproductcategory);
        tShirts=findViewById(R.id.t_shirt_categ);
        sportsTShirts=findViewById(R.id.fmale1);
        femaleDresses=findViewById(R.id.fmale2);
        sweathers=findViewById(R.id.sweater_categ);
        glasses=findViewById(R.id.glasses_categ);
        hatsCaps=findViewById(R.id.hats_categ);
        walletBagsPurses=findViewById(R.id.bags_categ);
        shoes=findViewById(R.id.shoes_categ);
        headPhonesHandFree=findViewById(R.id.headphones_categ);
        Laptops=findViewById(R.id.laptops_categ);
        watches=findViewById(R.id.watch_categ);
        mobilePhones=findViewById(R.id.mobilels_categ);

        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(sellerProductCategory.this, Selleraddnewproducts_Activity.class);
                intent1.putExtra("category","T-Shirts");
                startActivity(intent1);
            }
        });

        sportsTShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(sellerProductCategory.this, Selleraddnewproducts_Activity.class);
                intent2.putExtra("category","Female Simple Dresses");
                startActivity(intent2);
            }
        });
        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(sellerProductCategory.this, Selleraddnewproducts_Activity.class);
                intent3.putExtra("category","Female t-shirts");
                startActivity(intent3);
            }
        });
        sweathers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4=new Intent(sellerProductCategory.this, Selleraddnewproducts_Activity.class);
                intent4.putExtra("category","Sweaters");
                startActivity(intent4);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5=new Intent(sellerProductCategory.this, Selleraddnewproducts_Activity.class);
                intent5.putExtra("category","Glasses");
                startActivity(intent5);
            }
        });
        walletBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6=new Intent(sellerProductCategory.this, Selleraddnewproducts_Activity.class);
                intent6.putExtra("category","BagsAndPurses");
                startActivity(intent6);
            }
        });
        hatsCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent7=new Intent(sellerProductCategory.this, Selleraddnewproducts_Activity.class);
                intent7.putExtra("category","HatsAndCaps");
                startActivity(intent7);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent8=new Intent(sellerProductCategory.this, Selleraddnewproducts_Activity.class);
                intent8.putExtra("category","Shoes");
                startActivity(intent8);
            }
        });
        headPhonesHandFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent9=new Intent(sellerProductCategory.this, Selleraddnewproducts_Activity.class);
                intent9.putExtra("category","HeadPhonesAndHandFree");
                startActivity(intent9);
            }
        });
        Laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent10=new Intent(sellerProductCategory.this, Selleraddnewproducts_Activity.class);
                intent10.putExtra("category","Laptops");
                startActivity(intent10);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent11=new Intent(sellerProductCategory.this, Selleraddnewproducts_Activity.class);
                intent11.putExtra("category","Watches");
                startActivity(intent11);
            }
        });
        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent12=new Intent(sellerProductCategory.this, Selleraddnewproducts_Activity.class);
                intent12.putExtra("category","Mobiles phones");
                startActivity(intent12);
            }
        });

    }
}