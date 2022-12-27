package com.example.alibaba.Seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alibaba.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Selleraddnewproducts_Activity extends AppCompatActivity {
    private String CategoryName,Description,price,pname,saveCurrentDate,saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName,InputProductDescription,InputProductPrice;
    private static final int GalleryPick=1;
    private Uri ImageUri;
    private String productRandomKey,downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference productref,sellerRef;
    private ProgressDialog loadingbar;
    private String sName,sPhone,sEmail,sAddress,sPassword,sId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selleraddnewproduct);

        CategoryName=getIntent().getExtras().get("category").toString();
        ProductImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        productref=FirebaseDatabase.getInstance().getReference().child("Products");
        sellerRef=FirebaseDatabase.getInstance().getReference().child("sellers");

        Toast.makeText(this, CategoryName, Toast.LENGTH_SHORT).show();
        AddNewProductButton=findViewById(R.id.Add_prod_btn);
        loadingbar=new ProgressDialog(this);
        InputProductImage=findViewById(R.id.camera_addprod);
        InputProductName=findViewById(R.id.prod_Name);
        InputProductDescription=findViewById(R.id.prod_desc);
        InputProductPrice=findViewById(R.id.prod_price);
        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openGalley();

            }
        });
        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateProductData();
            }
        });

        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            sName =snapshot.child("name").getValue().toString();
                            sEmail=snapshot.child("email").getValue().toString();
                            sPhone=snapshot.child("phone").getValue().toString();
                            sAddress=snapshot.child("address").getValue().toString();
                            sId=snapshot.child("sid").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void validateProductData()
    {
        Description=InputProductDescription.getText().toString();
        price=InputProductPrice.getText().toString();
        pname=InputProductName.getText().toString();
        if(ImageUri ==null)
        {
            Toast.makeText(this, "Product Image is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please Enter Product Description", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price))
        {
            Toast.makeText(this, "Please Enter Product Price", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pname))
        {
            Toast.makeText(this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            storeInformation();
        }

    }

    private void storeInformation() {
        loadingbar.setTitle("Add New Products");
        loadingbar.setMessage("Dear seller, Please wait we are Adding new Products");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());
        productRandomKey=saveCurrentDate + saveCurrentTime ;
        StorageReference filepath=ProductImagesRef.child(ImageUri.getLastPathSegment()+".jpg");
        final UploadTask uploadTask=filepath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(Selleraddnewproducts_Activity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Selleraddnewproducts_Activity.this, "Product Image Uploaded Successfuly", Toast.LENGTH_SHORT).show();
                Task<Uri> urltask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                   if(task.isSuccessful())
                   {
                       downloadImageUrl=task.getResult().toString();
                       Toast.makeText(Selleraddnewproducts_Activity.this, "got the product url successfuly", Toast.LENGTH_SHORT).show();
                       saveProductInfoToDatabase();
                   }
                    }
                });
            }
        });

    }

    private void saveProductInfoToDatabase() {

        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("data",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",CategoryName);
        productMap.put("price",price);
        productMap.put("pname",pname);
        productMap.put("sellername",sName);
        productMap.put("selleraddress",sAddress);
        productMap.put("sellerphone",sPhone);
        productMap.put("selleremail",sEmail);
        productMap.put("sid",sId);
        productMap.put("productstate","Not Approved");


        productref.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Intent intent111=new Intent(Selleraddnewproducts_Activity.this, sellerProductCategory.class);
                            startActivity(intent111);
                            loadingbar.dismiss();
                            Toast.makeText(Selleraddnewproducts_Activity.this, "Products Added Successfuly", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingbar.dismiss();
                            String message=task.getException().toString();
                            Toast.makeText(Selleraddnewproducts_Activity.this, "Error:"+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void openGalley() {

        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
        ImageUri=data.getData();
        InputProductImage.setImageURI(ImageUri);
        }

    }



}