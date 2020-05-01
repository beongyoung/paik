package com.paik.app.post;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paik.app.R;
import com.paik.app.adapter.ContentsAdapter;
import com.paik.app.type_class.contentsTitle;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ContentsDrawer extends AppCompatActivity {

    TextView NewPost;
    RecyclerView mRecyclerView;
    List<contentsTitle> mContentsTitle;
    ContentsAdapter mAdapter;
    TextView tvCategory;
    DatabaseReference myRef;

    FirebaseDatabase database;
    ValueEventListener listener;
    String temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_drawer);

        tvCategory = findViewById(R.id.tvCategory);
        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ContentsDrawer.this, areaContents.class);
                startActivity(in);
            }
        });


        NewPost = findViewById(R.id.NewPost);

        NewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ContentsDrawer.this, NewPost.class);
                startActivity(in);
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.rvContents);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // specify an adapter (see also next example)

        mContentsTitle = new ArrayList<>();


        mAdapter = new ContentsAdapter(mContentsTitle, this, "개발자의 글");
        mRecyclerView.setAdapter(mAdapter);



        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();




        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("contentsStack");

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mContentsTitle.clear();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    contentsTitle a = new contentsTitle();
                    String key = String.valueOf(dataSnapshot2.getKey());
                    String[] tmp = key.split("\\|");


                    //리스너 안에 리스너



                    //리스너 안에 리스너


                    tmp[3] = tmp[3].replaceAll("\\+", ".");




                    //String nic[] = temp.split("\\|");

                    //Log.e("temp적용.", temp);

                    a.email = tmp[3];
                    a.title = String.valueOf(dataSnapshot.child(key).getValue());
                    a.key = key;
                    mContentsTitle.add(a);//정순
                    //Log.e("몇번 읽어오나 실험.", a.title);
                }

                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        myRef.addListenerForSingleValueEvent(listener);
    }

}
