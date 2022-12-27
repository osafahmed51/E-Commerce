package com.example.alibaba.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alibaba.R;
import com.example.alibaba.prevelant.Prevelant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class resetPassword extends AppCompatActivity {
    private String check="";
    private TextView pageTitle,questionTitle;
    private EditText phone_resetpswrd, quest1_reset,quest2_reset;
    private Button verify_reset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        pageTitle=findViewById(R.id.ResetTxtv_inResetActvty);
        questionTitle=findViewById(R.id.txtv2_inResetpswrd);
        phone_resetpswrd=findViewById(R.id.phone_num_Enter_reset);
        quest1_reset=findViewById(R.id.question1_reset);
        quest2_reset=findViewById(R.id.question2_reset);
        verify_reset=findViewById(R.id.verifyBtn_reset);
        check=getIntent().getStringExtra("check");

    }


    @Override
    protected void onStart() {
        super.onStart();
        phone_resetpswrd.setVisibility(View.GONE);
        if(check.equals("setting"))
        {
            pageTitle.setText("set Questions");
            questionTitle.setText("Please Enter The Following Questions?");
            verify_reset.setText("Set");
            displayPreviousAns();

            verify_reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setAnswers();
                }
            });

        }
        else if(check.equals("login"))
        {
            phone_resetpswrd.setVisibility(View.VISIBLE);
            verify_reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });
        }

    }

    private void verifyUser() {

        String phone123=phone_resetpswrd.getText().toString();
        String ans11=quest1_reset.getText().toString().toLowerCase();
        String ans22=quest2_reset.getText().toString().toLowerCase();
       if(!phone123.equals("") && !ans11.equals("") && !ans22.equals(""))
       {
           DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                   .child("Users").child(phone123);
           ref.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(snapshot.exists())
                   {
                       if(snapshot.hasChild("security question"))
                       {
                           String ans1=snapshot.child("security question").child("answer1").getValue().toString();
                           String ans2=snapshot.child("security question").child("answer2").getValue().toString();

                           if(!ans11.equals(ans1))
                           {
                               Toast.makeText(resetPassword.this, "Your 1st Answer is Incorrect", Toast.LENGTH_SHORT).show();
                           }
                           else if(!ans22.equals(ans2))
                           {
                               Toast.makeText(resetPassword.this, "Your 2nd Answer is Incorrect", Toast.LENGTH_SHORT).show();
                           }
                           else
                           {
                               AlertDialog.Builder builder=new AlertDialog.Builder(resetPassword.this);
                               builder.setTitle("New Password");

                               final EditText newpassword=new EditText(resetPassword.this);
                               newpassword.setHint("Write password here...");
                               builder.setView(newpassword);

                               builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       if(!newpassword.getText().toString().equals(""))
                                       {
                                           ref.child("password")
                                                   .setValue(newpassword.getText().toString())
                                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if(task.isSuccessful())
                                                           {
                                                               Toast.makeText(resetPassword.this, "password changes successfuly", Toast.LENGTH_SHORT).show();
                                                               Intent intwnt=new Intent(resetPassword.this, login1_activity.class);
                                                               startActivity(intwnt);
                                                           }
                                                       }
                                                   });
                                       }
                                   }
                               });
                               builder.show();
                           }
                       }
                       else
                       {
                           Toast.makeText(resetPassword.this, "You have not set the Security Questions", Toast.LENGTH_SHORT).show();
                       }
                   }
                   else
                   {
                       Toast.makeText(resetPassword.this, "This phone num not exist", Toast.LENGTH_SHORT).show();
                   }
               }


               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
       }
       else
       {
           Toast.makeText(this, "Please complete the form", Toast.LENGTH_SHORT).show();
       }




    }

    private void displayPreviousAns() {
        DatabaseReference reffff=FirebaseDatabase.getInstance().getReference()
                .child("Users").child(Prevelant.currentOnlineUser.getPhone());
        reffff.child("security question")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String answerrrr1=snapshot.child("security question").child("answer1").getValue().toString();
                            String answerrrr2=snapshot.child("security question").child("answer2").getValue().toString();
                            quest1_reset.setText(answerrrr1);
                            quest2_reset.setText(answerrrr2);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void setAnswers()
    {
        String ans1=quest1_reset.getText().toString().toLowerCase();
        String ans2=quest2_reset.getText().toString().toLowerCase();
        if(quest1_reset.equals("") && (quest2_reset.equals("")))
        {
            Toast.makeText(resetPassword.this, "Please Enter Both Answers Correctly", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference reffff= FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(Prevelant.currentOnlineUser.getPhone());
            HashMap<String,Object> userdataMap=new HashMap<>();
            userdataMap.put("answer1",ans1);
            userdataMap.put("answer2",ans2);

            reffff.child("security question").updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(resetPassword.this, "You Have Answered Security Questions", Toast.LENGTH_SHORT).show();
                                Intent intentnn=new Intent(resetPassword.this, Home_real.class);
                                startActivity(intentnn);
                            }
                        }
                    });
        }
    }
}