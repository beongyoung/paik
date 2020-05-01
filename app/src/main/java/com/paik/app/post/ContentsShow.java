package com.paik.app.post;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.paik.app.R;

import static android.content.ContentValues.TAG;

public class ContentsShow extends AppCompatActivity {
    String key;
    String email;
    String kind;
    String Title;
    TextView tvUser;
    TextView tvText;
    TextView tvTitle;
    TextView tvCategory;
    TextView tvGood;
    FirebaseDatabase database;
    DatabaseReference contentsInfoRef;
    DatabaseReference stackRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_show);

        tvUser = findViewById(R.id.tvUser);
        tvText = findViewById(R.id.tvText);
        tvTitle = findViewById(R.id.tvTitle);
        tvGood = findViewById(R.id.tvGood);



        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        tvGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                database = FirebaseDatabase.getInstance();


                key = getIntent().getStringExtra("key");
                Log.e("key값은 >>", key);
                String[] tmp = key.split("\\|");

                contentsInfoRef = database.getReference("contentsInfo/" + tmp[1] + "/"+tmp[2] + "/" + key);
                contentsInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(!dataSnapshot.child("goodList").hasChild(user.getUid())) {

                            contentsInfoRef.child("goodList").child(user.getUid()).setValue(getIntent().getStringExtra("nick"));

                            String tmp;
                            tmp = String.valueOf(dataSnapshot.child("title").getValue());
                            String tmp2[] = tmp.split("\\|");
                            int good;
                            good = Integer.valueOf(tmp2[2]);
                            good++;
                            contentsInfoRef.child("title").setValue(tmp2[0] + "|" + tmp2[1] + "|" + String.valueOf(good));
                            stackRef = database.getReference("contentsStack/" + key);
                            stackRef.setValue(tmp2[0] + "|" + tmp2[1] + "|" + String.valueOf(good));
                            Toast.makeText(ContentsShow.this, "이미 좋아요 표시를 했습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ContentsShow.this, "이미 좋아요 표시를 했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });








            }
        });

        email = getIntent().getStringExtra("email");
        kind = getIntent().getStringExtra("kind");
        Title = getIntent().getStringExtra("title");
        tvTitle.setText(Title);
        tvUser.setText(email);



        database = FirebaseDatabase.getInstance();


        key = getIntent().getStringExtra("key");
        Log.e("key값은 >>", key);
        String[] tmp = key.split("\\|");

        DatabaseReference myRef = database.getReference("contentsInfo/" + tmp[1] + "/"+tmp[2] + "/" + key);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                tvText.setText(
                        dataSnapshot.child("text").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
