package com.paik.app.tab;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.paik.app.R;
import com.paik.app.adapter.BannerAdapter;
import com.paik.app.adapter.PostAdapter;
import com.paik.app.post.NewPost;
import com.paik.app.type_class.Contents;
import com.paik.app.type_class.Guest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeFragment extends Fragment {
    TextView tvNewPost;
    RecyclerView mRecyclerView1;
    LinearLayoutManager mLayoutManager;
    ProgressBar pbTmp;
    List<Contents> mContents;
    FirebaseDatabase database;
    BannerAdapter mAdapter1;
    RelativeLayout rlContents;
    RelativeLayout guestMarketButton;
    int dateCount = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    RecyclerView mRecyclerView;
    TextView tvReset;
    List<Guest> mGuest;
    List<Guest> mResult;
    PostAdapter mAdapter;
    TextView newGuest;
    Button btnReadMore;
    TextView tvSelectArea;
    TextView tvSelectContents;
    Map<String, Object> guests = new HashMap<>();
    Map<String, Object> guest = new HashMap<>();
    List<Guest> shared;
    int guest_count = 0;
    int doc_num = 0;
    String areaFilter = "";
    String contentsFilter = "";
    int current = 0;
    int loc;
    List<Guest> tmp;
    public void  recyclerviewController(int mode) {
        mResult = new ArrayList<>();
        tmp = new ArrayList<>();
        HashMap map = new HashMap();
        for(int i = 0 ; i< mGuest.size(); i++) {
            Object value = map.get(mGuest.get(i).email);
            if (value == null) {
                map.put(mGuest.get(i).email, "exist");
                tmp.add(mGuest.get(i));
                Log.d("myLog", mGuest.get(i).email + value);

            }
            else {

                tmp.add(mGuest.get(i));
            }
        }
        if(areaFilter.equals("") && contentsFilter.equals(""))
            mResult = tmp;
        else {
            boolean add;
            for (int i = 0; i < tmp.size(); i++) {
                add = true;
                if(!areaFilter.equals("")) {
                    if(!tmp.get(i).area.equals(areaFilter)) {
                        add = false;
                    }
                }
                if(!contentsFilter.equals("")) {
                    if(!tmp.get(i).area.equals(contentsFilter)) {
                        add = false;
                    }
                }

                if (add) {
                    mResult.add(tmp.get(i));
                }
                // Log.d("myLog", mResult.get(i).area);
            }
        }
        Log.d("myLog", "중복확인");
        Guest gtTmp = new Guest();
        gtTmp.area = "Removed";
        gtTmp.contents = "Removed";
        gtTmp.email = "Removed";
        gtTmp.nick = "Removed";
        gtTmp.price = "Removed";
        gtTmp.selfIntro = "Removed";
        mAdapter = new PostAdapter(mResult, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        if(mode == -1) {
            Log.d("myLog", String.valueOf(loc - 1) + " 이동");
            mRecyclerView.scrollToPosition(loc - 3);
        }
    }

    public void getArea() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("지역 선택");
        builder.setItems(R.array.area, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int pos)
            {
                String[] items = getResources().getStringArray(R.array.area);
                //Toast.makeText(getApplicationContext(),items[pos], Toast.LENGTH_LONG).show();
                areaFilter = items[pos];
                tvSelectArea.setText(areaFilter);
                recyclerviewController(0);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void getContents() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("카테고리 선택");
        builder.setItems(R.array.freeCategory, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int pos)
            {
                String[] items = getResources().getStringArray(R.array.freeCategory);
                //Toast.makeText(getApplicationContext(),items[pos], Toast.LENGTH_LONG).show();
                contentsFilter = items[pos];

                tvSelectContents.setText(contentsFilter);
                recyclerviewController(0);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_free, container, false);
        pbTmp = v.findViewById(R.id.pbTmp);
        tvNewPost = v.findViewById(R.id.tvNewPost);
        tvNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), NewPost.class);
                startActivity(in);
            }
        });
        tvReset = v.findViewById(R.id.tvReset);
        btnReadMore = v.findViewById(R.id.btnReadMore);
        tvSelectContents = v.findViewById(R.id.tvSelectContents);
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentsFilter = "";
                areaFilter = "";
                tvSelectContents.setText("카테고리별");
                recyclerviewController(0);
            }
        });
        tvSelectContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContents();
            }
        });
        newGuest = v.findViewById(R.id.tvNewPost);
        newGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), NewPost.class);
                startActivity(in);
            }
        });
        mRecyclerView = (RecyclerView)v.findViewById(R.id.rvGuest);
        mRecyclerView.setNestedScrollingEnabled(false);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mGuest = new ArrayList<>();
        mAdapter = new PostAdapter(mGuest, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doc_num--;
                if(doc_num < 1) {
                    Toast.makeText(getActivity(), "더이상 찾을 자료가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                pbTmp.setVisibility(View.VISIBLE);
                loc = mGuest.size();
                Log.d("myLog", "doc" + doc_num + " 읽어옴");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference tmp = db.collection("free").document("table")
                        .collection("all");
                tmp.whereEqualTo("doc_num" , String.valueOf(doc_num)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                guests = document.getData();
                                guest_count = Integer.valueOf(guests.get("guest_count").toString());
                                doc_num = Integer.valueOf(guests.get("doc_num").toString());
                                for(int i = guest_count; i > 0; i--) {
                                    guest = (Map<String, Object>) guests.get("guest" + i);
                                    Log.d("myLog", guest.get("nick").toString());
                                    Guest tmp = new Guest();
                                    tmp.area = guest.get("area").toString();
                                    tmp.contents = guest.get("contents").toString();
                                    tmp.email = guest.get("email").toString();
                                    tmp.nick = guest.get("nick").toString();
                                    tmp.selfIntro = guest.get("selfIntro").toString();
                                    tmp.time = guest.get("time").toString();
                                    mGuest.add(tmp);
                                }
                                recyclerviewController(-1);
                                pbTmp.setVisibility(View.GONE);
                            }
                        } else {
                            pbTmp.setVisibility(View.GONE);
                            Log.d("test", "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tmp = db.collection("free").document("table")
                .collection("all");
        tmp.whereEqualTo("read" , true).orderBy(FieldPath.documentId(), Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        guests = document.getData();
                        guest_count = Integer.valueOf(guests.get("guest_count").toString());
                        doc_num = Integer.valueOf(guests.get("doc_num").toString());
                        for(int i = guest_count; i > 0; i--) {
                            guest = (Map<String, Object>) guests.get("guest" + i);
                            Log.d("myLog", guest.get("nick").toString());
                            Guest tmp = new Guest();
                            tmp.area = guest.get("area").toString();
                            tmp.contents = guest.get("contents").toString();
                            tmp.email = guest.get("email").toString();
                            tmp.nick = guest.get("nick").toString();
                            tmp.selfIntro = guest.get("selfIntro").toString();
                            tmp.time = guest.get("time").toString();
                            mGuest.add(tmp);
                        }
                        recyclerviewController(0);
                        boolean add;
                        /*
                        shared = new ArrayList<>();
                        for (int i = 0; i < mGuest.size(); i++) {
                            add = false;
                            SharedPreferences sharedPrefs = getSharedPreferences("user_info", MODE_PRIVATE);
                            String myArea = sharedPrefs.getString("area", "");
                            if(!mGuest.get(i).area.equals(myArea)) {
                                add = true;
                            }
                            if (add) {
                                shared.add(mGuest.get(i));
                            }
                            //Log.d("myLog", mGuest.get(i).area);
                        }*/
                    }
                }
                else {
                    Log.d("test", "Error getting documents: ", task.getException());
                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}
