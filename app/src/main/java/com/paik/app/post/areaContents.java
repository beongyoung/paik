package com.paik.app.post;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paik.app.R;
import com.paik.app.adapter.AreaContentsAdapter;
import com.paik.app.type_class.contentsTitle;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class areaContents extends AppCompatActivity {
    RecyclerView mRecyclerView;
    List<contentsTitle> mContentsTitle;
    AreaContentsAdapter mAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ValueEventListener listener;
    int areaSize = 3;
    int contentsSize = 3;
    int userArea = 1;
    TextView tvKy;
    TextView tvSeoul;
    TextView tvChung;
    TextView NewPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_contents);
        tvKy = findViewById(R.id.tvKy);
        tvSeoul = findViewById(R.id.tvSeoul);
        tvChung = findViewById(R.id.tvChung);
        NewPost = findViewById(R.id.NewPost);
        NewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(areaContents.this, NewPost.class);
                startActivity(in);
            }
        });
        tvSeoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userArea =1;
                myRef.removeEventListener(listener);
                myRef = database.getReference("contentsInfo" + "/" + String.valueOf(userArea));
                myRef.addListenerForSingleValueEvent(listener);
            }
        });
        tvKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userArea = 2;
                myRef.removeEventListener(listener);
                myRef = database.getReference("contentsInfo" + "/" + String.valueOf(userArea));
                myRef.addListenerForSingleValueEvent(listener);
            }
        });
        tvChung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userArea = 3;
                myRef.removeEventListener(listener);
                myRef = database.getReference("contentsInfo" + "/" + String.valueOf(userArea));
                myRef.addListenerForSingleValueEvent(listener);
            }
        });
        mRecyclerView = (RecyclerView)findViewById(R.id.rvContents);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        // specify an adapter (see also next example)
        mContentsTitle = new ArrayList<>();
        mAdapter = new AreaContentsAdapter(mContentsTitle,this, "개발자의 글");
        mRecyclerView.setAdapter(mAdapter);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("contentsInfo" + "/" + String.valueOf(userArea));
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mContentsTitle.clear();
                for(int i = 1; i <= contentsSize; i++) {
                    contentsTitle a = new contentsTitle();
                    //a.key = String.valueOf(dataSnapshot.child("macro").child("contents").child(String.valueOf(i)).child("name").getValue()); //*추후 문제 생길 수 있지만 일단 기기
                    if(i == 1)
                        a.key = "먹방유튜버 콘텐츠";
                    else if(i == 2)
                        a.key = "음악유튜버 콘텐츠";
                    else if(i == 3)
                        a.key = "뷰티유튜버 콘텐츠";
                    //a.title = "이게 보이면 안되는거임";
                    //a.email = "이것도 안보여야 함.";
                    mContentsTitle.add(mContentsTitle.size(), a);
                    //Log.e("카테고리 입력", a.key);
                    int limit = 0;
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.child(String.valueOf(i)).getChildren()) {
                                                                       // 1먹방, 2음악, 3뷰티만 읽어옴.
                        limit++;
                        if(limit > 0) { }
                        String key = String.valueOf(dataSnapshot2.getKey());
                        a = new contentsTitle();
                        String[] tmp = key.split("\\|");
                        tmp[3] = tmp[3].replaceAll("\\+", ".");
                        a.email = tmp[3];
                        a.title = String.valueOf(dataSnapshot2.child("title").getValue());
                        a.key = key;
                        mContentsTitle.add(a);
                        //Log.e("콘텐츠 입력", a.title);
                    }
                }
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        //myRef.addValueEventListener(listener);
    }
}
