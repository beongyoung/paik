package com.paik.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class showProfile extends AppCompatActivity {
    ImageView ivUser;
    TextView tvNick;
    TextView tvEmail;
    TextView tvContents;
    TextView tvArea;
    TextView tvGender;
    TextView tvCrew;
    TextView tvSelfIntro;
    TextView tvSendMessage;
    TextView tvReport;
    String email;
    String nick;
    ImageView ivCancel;
    private StorageReference mStorageRef;
    Map<String, Object> userInfo = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        ivUser = findViewById(R.id.ivUser);
        tvNick = findViewById(R.id.tvNick);
        tvEmail = findViewById(R.id.tvEmail);
        tvContents = findViewById(R.id.tvContents);
        tvArea = findViewById(R.id.tvArea);
        tvGender = findViewById(R.id.tvGender);
        tvSelfIntro = findViewById(R.id.tvSelfIntro);
        tvSendMessage = findViewById(R.id.tvSendMessage);
        tvReport = findViewById(R.id.tvReport);
        ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        email = getIntent().getStringExtra("email");
        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(showProfile.this, report.class);
                in.putExtra("email", email);
                in.putExtra("nick", nick);
                startActivity(in);
            }
        });
        tvSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(showProfile.this, ChatActivity.class);
                in.putExtra("friendId", email);
                in.putExtra("nick", nick);
                startActivity(in);
            }
        });
        Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.default_image);
        //bitmap = GetImageFromURL("https://yt3.ggpht.com/a/AGF-l7_cK8PJQgLUBqtnXdqJfqBvoAP6P_4pM3Jsfw=s288-c-k-c0xffffffff-no-rj-mo");
        try {
            //bitmap = ((BitmapDrawable) holder.ivContents.getDrawable()).getBitmap();
        }
        catch(Exception e) {
            Log.d("myLog", "이미지 없음");
        }
        //Bitmap Rbitmap = setRoundCorner(bitmap, 150);
        ivUser.setImageBitmap(bitmap);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = mStorageRef.child("users/profile/"+ email + ".jpg");
        //Url을 다운받기
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).fit().centerInside().into(ivUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tmp = db.collection("user");
        tmp.whereEqualTo("email" , email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("myLog", "유저 info db 접근");
                    for (DocumentSnapshot document : task.getResult()) {
                        userInfo = document.getData();
                        Log.d("myLog", userInfo.get("nick").toString() + " 정보 읽어옴");
                        try {
                            nick = userInfo.get("nick").toString();
                            tvNick.setText(nick);
                            tvEmail.setText(userInfo.get("email").toString());
                            tvContents.setText(userInfo.get("contents").toString());
                            tvArea.setText(String.valueOf(userInfo.get("area")));
                            tvGender.setText(userInfo.get("price").toString() + " won");
                            //tvCrew.setText(userInfo.get("crew").toString());
                            tvSelfIntro.setText(userInfo.get("intro").toString());
                        }
                        catch(Exception e) {
                            Log.e("myLog", "뭔가 못읽어옴 - " + e.getMessage());
                        }
                    }
                }
                else{
                    Log.e("myLog", "db에러임 - showProfile");
                }
            }
        });
    }
}
