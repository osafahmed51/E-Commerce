package com.example.alibaba.Buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alibaba.R;
import com.example.alibaba.prevelant.Prevelant;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings_Activity extends AppCompatActivity {
    private CircleImageView circleImageView;
    private EditText phon_num_setting,full_name_setting, Adresss_setting;
    private TextView profile_change_textview,close_textview_setting,update_textview_setting;
    private Uri imageuri;
    private String myUrl,downloadImageUrl;
    private  UploadTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker="";
    private static  int Gallerypick=1;
    private DatabaseReference databaseReference;
    private Button securityCheck_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef=FirebaseStorage.getInstance().getReference().child("profile pictures");

        circleImageView=findViewById(R.id.img_profile_settings);
        phon_num_setting=findViewById(R.id.phonenum_setting);
        full_name_setting=findViewById(R.id.fullname_setting_layout);
        Adresss_setting=findViewById(R.id.address_setting_layout);
        close_textview_setting=findViewById(R.id.close_setting);
        update_textview_setting=findViewById(R.id.Update_setting_layout);
        profile_change_textview=findViewById(R.id.change_image_btn);
        securityCheck_btn=findViewById(R.id.security_Questions);

        userInfoDisplay(circleImageView,phon_num_setting,full_name_setting,Adresss_setting);


        securityCheck_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent=new Intent(Settings_Activity.this, resetPassword.class);
                setting_intent.putExtra("check","setting");
                startActivity(setting_intent);
                finish();
            }
        });
        close_textview_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update_textview_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });
        profile_change_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Settings_Activity.this, "Clicked on profile change", Toast.LENGTH_SHORT).show();
                checker="clicked";
                Intent gallery_intent=new Intent();
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                gallery_intent.setType("image/*");
                startActivityForResult(gallery_intent,Gallerypick);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallerypick && resultCode==RESULT_OK &&data!=null)
        {
              imageuri=data.getData();
              circleImageView.setImageURI(imageuri);
        }
        else
        {
            Toast.makeText(this, "Error , Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings_Activity.this,Settings_Activity.class));
            finish();
        }

    }

    private void updateOnlyUserInfo()
    {
       final DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Users");
        HashMap<String,Object> userdataa=new HashMap<>();
        userdataa.put("name",full_name_setting.getText().toString());
        userdataa.put("address",Adresss_setting.getText().toString());
        userdataa.put("phoneOrders",phon_num_setting.getText().toString());
        ref.child(Prevelant.currentOnlineUser.getPhone()).updateChildren(userdataa);
        startActivity(new Intent(Settings_Activity.this, Home_real.class));
        Toast.makeText(Settings_Activity.this, "Profile Updated Successfuly", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void userInfoSaved()
    {
        if(TextUtils.isEmpty(full_name_setting.getText().toString())){
            Toast.makeText(this, "Name is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Adresss_setting.getText().toString())){
            Toast.makeText(this, "Address is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phon_num_setting.getText().toString()))
        {
            Toast.makeText(this, "Phone number is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, we are updating your Account");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        if(imageuri!=null)
        {
            final StorageReference fileref=storageProfilePictureRef
                    .child(Prevelant.currentOnlineUser.getPhone() + ".jpg");
            uploadTask=fileref.putFile(imageuri);
            Task<Uri> urltask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    downloadImageUrl=fileref.getDownloadUrl().toString();
                    return fileref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadurl=task.getResult();
                        myUrl=downloadurl.toString();
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                                .child("Users");
                        HashMap<String,Object> userdataa=new HashMap<>();
                        userdataa.put("name",full_name_setting.getText().toString());
                        userdataa.put("address",Adresss_setting.getText().toString());
                        userdataa.put("phoneOrders",phon_num_setting.getText().toString());
                        userdataa.put("image",myUrl);
                        ref.child(Prevelant.currentOnlineUser.getPhone()).updateChildren(userdataa);
                        progressDialog.dismiss();
                        startActivity(new Intent(Settings_Activity.this,Home_real.class));
                        Toast.makeText(Settings_Activity.this, "Profile Updated Successfuly", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Settings_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
        {
            Toast.makeText(this, "image is not Uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(CircleImageView circleImageView, EditText phon_num_setting, EditText full_name_setting, EditText adresss_setting)
    {
        DatabaseReference userref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevelant.currentOnlineUser.getPhone());
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("image").exists()){
                         String image=snapshot.child("image").getValue().toString();
                         String name=snapshot.child("name").getValue().toString();
                         String pass=snapshot.child("password").getValue().toString();
                         String address=snapshot.child("address").getValue().toString();
                        Picasso.get().load(image).into(circleImageView);
                        full_name_setting.setText(name);
                        phon_num_setting.setText(pass);
                        Adresss_setting.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}