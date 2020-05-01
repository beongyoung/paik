package com.paik.app.guest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paik.app.ChatActivity;
import com.paik.app.PointShop;
import com.paik.app.R;
import com.paik.app.showProfile;

public class showGuestInfo extends AppCompatActivity {
    public TextView tvEmail;
    public TextView tvPrice;
    public TextView tvIntro;
    public TextView tvArea;
    public TextView tvContents;
    public TextView tvSendMessage;
    public TextView tvShowProfile;
    public boolean messagePoint = false;
    String email;
    public ImageView ivContents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_experts);


        tvShowProfile = findViewById(R.id.tvShowProfile);
        tvArea = findViewById(R.id.tvArea);
        tvContents = findViewById(R.id.tvContents);
        tvEmail = findViewById(R.id.tvEmail);
        ivContents = findViewById(R.id.ivContents);
        tvPrice = findViewById(R.id.tvPrice);
        tvIntro = findViewById(R.id.tvIntro);
        tvSendMessage = findViewById(R.id.tvSendMessage);


        tvShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(showGuestInfo.this, showProfile.class);
                in.putExtra("email", email);
                startActivity(in);
            }
        });

        tvSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPrefs = getSharedPreferences("chat_list", MODE_PRIVATE);


                final SharedPreferences.Editor editor = sharedPrefs.edit();
                //editor.remove(email);
                //editor.apply();
                if(sharedPrefs.contains(email)) {
                    Intent in = new Intent(showGuestInfo.this, ChatActivity.class);
                    in.putExtra("friendId", email);
                    startActivity(in);
                    return;
                }else if(!messagePoint) {
                    messagePoint = true;


                    Toast.makeText(showGuestInfo.this, "한 번 더 누르면 5p를 지불하고 채팅창으로 이동합니다.", Toast.LENGTH_LONG).show();

                    return;
                }





                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                FirebaseDatabase database;
                final DatabaseReference myRef;
                database = FirebaseDatabase.getInstance();


                myRef = database.getReference("user/" + user.getUid());

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int point =  Integer.valueOf(dataSnapshot.child("point").getValue().toString());
                        point -= 5;


                        if(point < 0)
                        {
                            Intent in = new Intent(showGuestInfo.this, PointShop.class);
                            startActivity(in);
                        }else {
                            myRef.child("point").setValue(String.valueOf(point));



                            editor.putString(email ,"");

                            editor.commit();Intent in = new Intent(showGuestInfo.this, ChatActivity.class);
                            in.putExtra("friendId", email);
                            startActivity(in);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });










            }
        });



        String nick = getIntent().getStringExtra("nick");
        email = getIntent().getStringExtra("email");
        //String photo = getIntent().getStringExtra("photo");
        String selfIntro = getIntent().getStringExtra("selfIntro");
        String price = getIntent().getStringExtra("price");
        String area = getIntent().getStringExtra("area");
        String contents = getIntent().getStringExtra("contents");

        tvArea.setText(area);
        tvContents.setText(contents);
        tvEmail.setText(nick + "\n" + email);
        tvPrice.setText(price);
        tvIntro.setText(selfIntro);



    }
}
