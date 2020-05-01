package com.paik.app;


import androidx.appcompat.app.AppCompatActivity;

public class chatRoom extends AppCompatActivity {
    /*
    FirebaseUser user;
    FirebaseAuth mAuth;
    Button btnGo;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String userUid;
    List<Friend> mFriend;

    LinearLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        RecyclerView mRecyclerView;
        mRecyclerView = (RecyclerView)findViewById(R.id.rnFriend);//<< 이코드 잘못됨. recycler view 새로만들어야함.

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        mFriend = new ArrayList<>();

        mAdapter = new NotificationAdapter(mFriend, this); //여기에 맞는 어뎁터를 만들어줌 >> ChatRoomAdapter
        mRecyclerView.setAdapter(mAdapter);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        userUid = user.getUid();


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myRef.child("userUid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {


                    Friend friend = dataSnapshot2.getValue(Friend.class);//이코드 잘못됨. 받을 때 새로운 class로 받음.(NewChat 클래스 만들어서)
                    mFriend.add(friend); //물론 이것도 바꿔야함.
                    mAdapter.notifyItemInserted(mFriend.size() - 1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    */
}
