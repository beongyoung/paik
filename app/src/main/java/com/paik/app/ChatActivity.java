package com.paik.app;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paik.app.adapter.MyAdapter;
import com.paik.app.type_class.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    DatabaseReference myRef;
    ChildEventListener listener;
    private EditText etSend;
    private Button btnSend;
    private String email;
    boolean myTalk = false;
    private String dir;
    private List<String> mCommentIds = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView friendActionBar;
    private boolean first = true;
    List<Chat> mChat;
    FirebaseDatabase database;
    private String date[] = {"none", "none"};
    private String cut[] = {"none", "none"};
    private int dateCount = 0;
    long lastPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 카톡색상에 회색 아이콘 색상을 설정
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#a9c7e2"));
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.BLACK);
        }
        String friendId;
        String idArr[] = {"none", "none"};
        friendId = getIntent().getStringExtra("friendId");
        database = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        friendActionBar = findViewById(R.id.friendActionBar);
        if (user != null) {
            // Name, email address, and profile photo Url
            //String name = user.getDisplayName();
            email = user.getEmail();
            String newEmail;
            String newFriendEmail;
            /*
            String[] tmp = email.split("@");
            idArr[0] = tmp[0];
            tmp = friendId.split("@");
            friendActionBar.setText(tmp[0]);
            idArr[1] = tmp[0];
            String[] tmp = {"", ""};
            tmp = email.split("@");
            tmp[1].substring(1);
            newEmail = tmp[0] + tmp[1];
            tmp = friendId.split("@");
            tmp[1].substring(1);
            newFriendEmail = tmp[0] + tmp[1];
            idArr[0] = newEmail;
            idArr[1] = newFriendEmail;
            Arrays.sort(idArr);
            */
            String[] tmp = friendId.split("@");
            friendActionBar.setText(getIntent().getStringExtra("nick"));
            /*
            newEmail = email.replaceAll("@", "");
            newEmail = newEmail.replaceAll("\\.", "");
            */
            newEmail = email.replaceAll("\\.", "+");
            /*
            newFriendEmail = friendId.replaceAll("@", "");
            newFriendEmail = newFriendEmail.replaceAll("\\.", "");
            */
            newFriendEmail = friendId.replaceAll("\\.", "+");
            idArr[0] = newEmail;
            idArr[1] = newFriendEmail;
            Arrays.sort(idArr);
            dir = idArr[0] + '|' + idArr[1];
            //Uri photoUrl = user.getPhotoUrl();
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            // The user's ID, unique to the Firebase project.
            // Do NOT use this value to authenticate with your backend server,
            // if you have one. Use FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }
        etSend = (EditText) findViewById(R.id.etSend);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myTalk) {
                    //Toast.makeText(ChatActivity.this, "아직 답장이 오지 않았습니다.", Toast.LENGTH_SHORT).show();
                    //return;
                }
                //first = true;
                String stText = etSend.getText().toString();
                etSend.setText("");
                if (!stText.equals("")) {
                    //Toast.makeText(ChatActivity.this, email + " : " + stText, Toast.LENGTH_SHORT).show();
                    // Write a message to the database
                    database = FirebaseDatabase.getInstance();
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = df.format(c.getTime());
                    DatabaseReference myRef = database.getReference("chats").child(dir).child(time);
                    Hashtable<String, String> chats = new Hashtable<String, String>();
                    chats.put("email", email);
                    chats.put("text", stText);
                    myRef.setValue(chats);
                }
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mChat = new ArrayList<>();
        mAdapter = new MyAdapter(mChat, email, ChatActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        //database = FirebaseDatabase.getInstance();//
        myRef = database.getReference("chats").child(dir);
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                //Chat chat = new Chat();
                chat.time = dataSnapshot.getKey();
                chat.email = dataSnapshot.child("email").getValue().toString();
                chat.text = dataSnapshot.child("text").getValue().toString();
                if (chat.email.equals(user.getEmail())) {
                    myTalk = true;
                } else {
                    myTalk = false;
                }
                // [START_EXCLUDE]
                // Update RecyclerView
                //mCommentIds.add(dataSnapshot.getKey());
                Log.e("다음 값을 읽어옴.", chat.text);
                if (first) {
                    Chat time = new Chat();
                    time.setEmail("time");
                    time.setTime(chat.getTime());
                    mChat.add(time);
                    first = false;
                    cut = chat.getTime().split(" ");
                    date[dateCount] = cut[0];
                    dateCount++;
                    mRecyclerView.setAdapter(mAdapter);
                    //mRecyclerView.scrollToPosition(mChat.size() - 1); // 새로고침
                } else {
                    cut = chat.getTime().split(" ");
                    dateCount++;
                    dateCount = dateCount % 2;
                    date[dateCount] = cut[0];
                    //Toast.makeText(ChatActivity.this,  "길이 : " + date[dateCount].length(), Toast.LENGTH_SHORT).show();
                    Log.d("#", "길이 : " + date[0] + " " + date[1]);
                    if (!(date[0].equals(date[1])))// && date[dateCount].length()==
                    {
                        Chat time = new Chat();
                        time.setEmail("time");
                        time.setTime(chat.getTime());
                        mChat.add(time);
                        mAdapter.notifyItemInserted(mChat.size() - 1);
                        mRecyclerView.setAdapter(mAdapter);
                        //mRecyclerView.scrollToPosition(mChat.size() - 1); // 새로고침
                    }
                }
                cut = chat.getTime().split(" ");
                date[dateCount] = cut[0];
                mChat.add(chat);
                mAdapter.notifyItemInserted(mChat.size() - 1);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 7);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.smoothScrollToPosition(mChat.size() - 1);
                    }
                }, 10);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        myRef.addChildEventListener(listener);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastPressed < 1500) {
            myRef.removeEventListener(listener); //여러번 호출되는 문제 해결책인듯.
            finish();
            //System.exit(0);//임시 (childEventListener이 잘 종료가 안되는거같아서 여러번 호출되는 문제 있음.)
        }
        Toast.makeText(this, "한번 더 누르면 종료", Toast.LENGTH_SHORT).show();
        lastPressed = System.currentTimeMillis();
    }
}