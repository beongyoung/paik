package com.paik.app.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paik.app.R;
import com.paik.app.adapter.NotificationAdapter;
import com.paik.app.type_class.Friend;
import com.paik.app.type_class.Guest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotiFragment extends Fragment {
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Friend> contents;
    List<Guest> mFriend;
    FirebaseDatabase database;
    TextView btnChatRoom;
    NotificationAdapter mAdapter;
    List<Guest> mGuest;
    Map<String, Object> noti = new HashMap<>();
    Map<String, Object> notis = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_noti, container, false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.rnFriend);
        // use this setting to improve performance if you know that changes
        //        // in content do not change the layout size of the RecyclerView
        //        mRecyclerView.setHasFixedSize(true);
        //
        //        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        // specify an adapter (see also next example)
        mFriend = new ArrayList<>();
        mAdapter = new NotificationAdapter(mFriend, getContext());
        mRecyclerView.setAdapter(mAdapter);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mGuest = new ArrayList<>();
        DocumentReference tmpRef = db.collection("notification").document(user.getEmail());
        tmpRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        int notiCount = Integer.valueOf(document.get("notiCount").toString());
                        notis = document.getData();
                        for(int i = 1;i < notiCount + 1; i++) {
                            noti = (Map<String, Object>)notis.get("noti" + i);
                            Guest g = new Guest();
                            g.area = noti.get("title").toString();
                            g.selfIntro = noti.get("docId").toString();
                            g.time = noti.get("time").toString();
                            g.nick = noti.get("nick").toString();
                            g.contents = noti.get("notiMessage").toString();
                            g.email = noti.get("email").toString();
                            g.price = String.valueOf(1);
                            mGuest.add(g);
                        }
                        Collections.reverse(mGuest);
                        mAdapter = new NotificationAdapter(mGuest, getActivity());
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    else {   }
                }
                else {  }
            }
        });
        return  v;
    }
}
