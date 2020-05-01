package com.paik.app.post;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.paik.app.ChatActivity;
import com.paik.app.R;
import com.paik.app.adapter.CommentAdapter;
import com.paik.app.report;
import com.paik.app.showProfile;
import com.paik.app.type_class.Guest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class show_post extends AppCompatActivity {

    TextView tvNick;
    TextView tvEmail;
    TextView tvTime;
    TextView tvTitle;
    TextView tvContents;
    ImageView ivUser;
    Button btnSend;
    EditText etComment;
    ProgressBar pbTmp;
    int maxNotiCount = 1000;
    private StorageReference mStorageRef;
    String nickname;
    Map<String, Object> comment = new HashMap<>();
    Map<String, Object> comments = new HashMap<>();
    CommentAdapter mAdapter;
    boolean click;
    List<Guest> mGuest;

    String writerEmail;
    String writerToken;
    String nick;
    String email = "";
    String time;
    String contents;
    String title = "";
    String docId = "";
    RecyclerView mRecyclerView;
    String postUserName = "NULL";

    ImageView ivMore;
    ImageView ivCancel;

    String moreSelect;

    public void getMore()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select region");

        builder.setItems(R.array.more, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int pos)
            {
                String[] items = getResources().getStringArray(R.array.more);
                //Toast.makeText(getApplicationContext(),items[pos], Toast.LENGTH_LONG).show();
                moreSelect = items[pos];
                if(moreSelect.equals(("report")))
                {
                    Intent in = new Intent(show_post.this, report.class);
                    in.putExtra("nick", postUserName);
                    in.putExtra("email",  getIntent().getStringExtra("email"));
                    startActivity(in);
                }
                else if(moreSelect.equals(("send message")))
                {
                    Intent in = new Intent(show_post.this, ChatActivity.class);

                    in.putExtra("friendId", getIntent().getStringExtra("email"));
                    in.putExtra("nick", postUserName);

                    startActivity(in);
                }
                else if(moreSelect.equals(("view profile")))
                {
                    Intent in = new Intent(show_post.this, showProfile.class);
                    in.putExtra("email", getIntent().getStringExtra("email"));
                    startActivity(in);
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);
        pbTmp = findViewById(R.id.pbTmp);
        ivUser = findViewById(R.id.ivUser);
        tvNick = findViewById(R.id.tvNick);
        //tvEmail = findViewById(R.id.tvEmail);
        tvTime = findViewById(R.id.tvTime);
        tvTitle = findViewById(R.id.tvTitle);
        tvContents = findViewById(R.id.tvContents);
        ivMore = findViewById(R.id.ivMore);
        ivCancel = findViewById(R.id.ivCancel);

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        email = getIntent().getStringExtra("email");
        time = getIntent().getStringExtra("time");

        if(time.equals("no"))
        {
            docId = getIntent().getStringExtra("docId");
        }else
        {
            docId = time + email;
        }


        etComment = findViewById(R.id.etComment);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strComment;
                boolean right = true;

                try{
                    strComment = etComment.getText().toString();
                }catch(Error e)
                {
                    right = false;
                    Toast.makeText(show_post.this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(strComment.equals(""))
                {
                    right = false;
                    Toast.makeText(show_post.this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(right)
                {

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time2 = df.format(c.getTime());
                    if(click)
                    {
                        Toast.makeText(show_post.this, "업로드중입니다...", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    click = true;
                    pbTmp.setVisibility(View.VISIBLE);

                    SharedPreferences sharedPrefs = getSharedPreferences("user_info", MODE_PRIVATE);
                    nickname = sharedPrefs.getString("nick", user.getEmail());


                    final Guest gt = new Guest();
                    final Map<String, Object> Mgt = new HashMap<>();
                    Mgt.put("time", time2);
                    Mgt.put("nick", nickname);
                    Mgt.put("contents", strComment);
                    Mgt.put("email", user.getEmail());


                    gt.time = time2;
                    gt.nick = nickname;
                    gt.contents = strComment;
                    gt.email = user.getEmail();




                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final DocumentReference sfDocRef =  db.collection("free").document("data")
                            .collection("dataCol").document(time + email);

                    db.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(sfDocRef);

                            // Note: this could be done without a transaction
                            //       by updating the population using FieldValue.increment()


                            int newCommentCount = Integer.valueOf(String.valueOf(snapshot.get("commentCount"))) + 1;


                            final Map<String, Object> tmpMap = new HashMap<>();
                            tmpMap.put("commentCount",  String.valueOf(newCommentCount));
                            tmpMap.put("comment" + String.valueOf(newCommentCount), Mgt);

                            transaction.update(sfDocRef, tmpMap);

                            //transaction.update(sfDocRef, "commentCount", String.valueOf(newCommentCount));
                            //transaction.update(sfDocRef, "comment" + String.valueOf(newCommentCount), gt);
                            // Success
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Log.d(TAG, "Transaction success!");

                            click = false;
                            pbTmp.setVisibility(View.GONE);

                            Toast.makeText(show_post.this, "댓글이 입력되었습니다.", Toast.LENGTH_LONG).show();





                            etComment.setText("");
                            mGuest.add(gt);
                            mAdapter = new CommentAdapter(mGuest, show_post.this);
                            mRecyclerView.setAdapter(mAdapter);

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    click = false;

                                    pbTmp.setVisibility(View.GONE);
                                    Toast.makeText(show_post.this, "Error code : 101. 개발자에게 알려주세요!", Toast.LENGTH_LONG).show();
                                    Log.w("myLog", "Transaction failure.", e);
                                }
                            });


                    sendFcm();
                    if(email.equals(user.getEmail()))
                    {
                        return;
                    }

                    final DocumentReference sfDocRef2 =  db.collection("notification").document(email);

                    final Map<String, Object> noti = new HashMap<>();
                    noti.put("docId", docId);
                    noti.put("email", user.getEmail());
                    noti.put("nick", nickname);
                    noti.put("notiMessage", strComment);
                    noti.put("time", time2);
                    noti.put("title", title);




                    db.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(sfDocRef2);

                            // Note: this could be done without a transaction
                            //       by updating the population using FieldValue.increment()


                            int notiCount = Integer.valueOf(String.valueOf(snapshot.get("notiCount"))) + 1;


                            final Map<String, Object> tmpMap = new HashMap<>();
                            tmpMap.put("notiCount",  String.valueOf(notiCount));
                            tmpMap.put("noti" + String.valueOf(notiCount), noti);

                            if(notiCount < maxNotiCount)
                            {
                                transaction.update(sfDocRef2, tmpMap);
                            }
                            //transaction.update(sfDocRef, "commentCount", String.valueOf(newCommentCount));
                            //transaction.update(sfDocRef, "comment" + String.valueOf(newCommentCount), gt);
                            // Success
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Log.d(TAG, "Transaction success!");


                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(show_post.this, "Error code : 100. 개발자에게 알려주세요!", Toast.LENGTH_LONG).show();
                                }
                            });









                }
            }
        });



        /*
        try {
            nick = getIntent().getStringExtra("nick");
            contents = getIntent().getStringExtra("contents");
            title = getIntent().getStringExtra("title");
        }catch(Exception e)
        {
            Toast.makeText(show_post.this, "time, contents, title 없음", Toast.LENGTH_SHORT).show();
        }*/

        mGuest = new ArrayList<>();

        mRecyclerView = (RecyclerView)findViewById(R.id.rvGuest);

        mRecyclerView.setNestedScrollingEnabled(false);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(show_post.this, RecyclerView.VERTICAL, false));


        mGuest = new ArrayList<>();



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference tmpRef = db.collection("free").document("data")
                .collection("dataCol").document(docId);



        tmpRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        int commentCount = Integer.valueOf(document.get("commentCount").toString());
                        comments = document.getData();

                        writerEmail = document.get("email").toString();
                        Log.d("myLog", "작성자의 이메일 : " + writerEmail);
                        for(int i = 1;i < commentCount + 1; i++)
                        {
                            comment = (Map<String, Object>)comments.get("comment" + i);
                            Guest g = new Guest();
                            g.time = comment.get("time").toString();
                            g.nick = comment.get("nick").toString();
                            g.contents = comment.get("contents").toString();
                            g.email = comment.get("email").toString();
                            mGuest.add(g);
                        }
                        nick = comments.get("nick").toString();
                        contents = comments.get("selfIntro").toString();
                        title = comments.get("contents").toString();
                        email = comments.get("email").toString();
                        time = comments.get("time").toString();

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


                        postUserName = nick;
                        tvNick.setText(nick);
                        //tvEmail.setText(email);
                        tvTime.setText(time);
                        tvTitle.setText(title);
                        tvContents.setText(contents);

                        mAdapter = new CommentAdapter(mGuest, show_post.this);
                        mRecyclerView.setAdapter(mAdapter);


                    } else {
                    }
                } else {
                }
            }
        });


        mAdapter = new CommentAdapter(mGuest, show_post.this);
        mRecyclerView.setAdapter(mAdapter);


        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMore();
            }
        });
    }
    void sendFcm()
    {




        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tmp = db.collection("user");
        tmp.whereEqualTo("email" , email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("myLog", "유저 info db 접근");

                    for (DocumentSnapshot document : task.getResult()) {

                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo = document.getData();

                        Log.d("myLog", userInfo.get("nick").toString() + " 정보 읽어옴");


                        writerToken = userInfo.get("token").toString();



                        //------------------------------------------fcm---------------------------------------

                        Log.d("myLog", "쓰레드 실행");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject root = new JSONObject();
                                    JSONObject notification = new JSONObject();
                                    notification.put("body", nickname + "님이 댓글을 다셨습니다.");
                                    notification.put("title", getString(R.string.app_name));
                                    root.put("notification", notification);
                                    root.put("clickAction", "show_post");

                                    root.put("to", writerToken);
                                    // Fcm 메시지 생성 end
                                    URL Url = new URL("https://fcm.googleapis.com/fcm/send");
                                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                                    conn.setRequestMethod("POST");
                                    conn.setDoOutput(true);
                                    conn.setDoInput(true);
                                    String SERVER_KEY = "AAAAre_ZRR0:APA91bGhn7bJkFf4drVYIObLhwis57IRkn9Vtts4WpvWh9-yEWL1sO9wY7BWEoJg3HEeeLwuzv22vU-RP-HyhXZx2UXdmoo92XvJdR4syWfjXmZuSkkZVS66DYEJ3iIe_4YTyD0C30uQ";
                                    conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                                    conn.setRequestProperty("Accept", "application/json");
                                    conn.setRequestProperty("Content-type", "application/json");
                                    OutputStream os = conn.getOutputStream();
                                    os.write(root.toString().getBytes("utf-8"));
                                    os.flush();
                                    int tmp = conn.getResponseCode();
                                    Log.d("myLog", String.valueOf(tmp));
                                }catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d("myLog", "fcm err : " + e.toString());
                                }
                            }
                        }).start();
                        //------------------------------------------fcm---------------------------------------




                    }
                }else{
                    Log.e("myLog", "db에러임 - showProfile");
                }

            }
        });






        /*
        Gson gson = new Gson();
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = "c7oF7YDh0z0:APA91bFBIWYjGEFcyXoxpf2YkPkRn1nYH-X-LVFv37xjuFT3y8IIvY2-J_nxfmsII-qoiSIyxprcqigSk5XxpLOQd4lldqpK8EH79ZlzsSSZyfgqgS0uEk3AmWvtRTN_guNHvzKWfuV7";
        notificationModel.nofification.title = "nick";
        notificationModel.nofification.text = "success";



        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationModel));

        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "key=AAAAre_ZRR0:APA91bGhn7bJkFf4drVYIObLhwis57IRkn9Vtts4WpvWh9-yEWL1sO9wY7BWEoJg3HEeeLwuzv22vU-RP-HyhXZx2UXdmoo92XvJdR4syWfjXmZuSkkZVS66DYEJ3iIe_4YTyD0C30uQ")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();//https://fcm.googleapis.com/fcm/send
//https://fcm.googleapis.com/fcm/send
//https://android.googleapis.com/gcm/send

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("myLog", "fcm 실패" + e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d("myLog", "fcm 성공" + response.toString());
            }
        });
//https://gcm-http.googleapis.com/gcm/send
        //
        */

    }
}
