package com.paik.app.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.paik.app.R;
import com.paik.app.tab.TabActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Regist2 extends AppCompatActivity {

    EditText etChannel;
    EditText etSelf;
    EditText etNick;

    String token = "";



    String area = "", contents = "";

    TextView tvArea;
    TextView tvContents;
    boolean click = false;
    String all_doc_num;
    String all_guest_count;
    int MaxDocSize = 100;
    int doc_num;
    TextView tvAsk;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getArea()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select region");

        builder.setItems(R.array.area, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int pos)
            {
                String[] items = getResources().getStringArray(R.array.area);
                //Toast.makeText(getApplicationContext(),items[pos], Toast.LENGTH_LONG).show();
                area = items[pos];

                tvArea.setText(area);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void getContents()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select job");

        builder.setItems(R.array.contents, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int pos)
            {
                String[] items = getResources().getStringArray(R.array.contents);
                //Toast.makeText(getApplicationContext(),items[pos], Toast.LENGTH_LONG).show();
                contents = items[pos];

                tvContents.setText(contents);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void getCountry()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Country");

        builder.setItems(R.array.country, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int pos)
            {
                String[] items = getResources().getStringArray(R.array.country);
                //Toast.makeText(getApplicationContext(),items[pos], Toast.LENGTH_LONG).show();
                contents = items[pos];

                tvContents.setText(contents);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist2);
        //final TextView tmp = findViewById(R.id.tmp);
        tvArea = findViewById(R.id.tvArea);
        tvContents = findViewById(R.id.tvContents);
        etSelf = findViewById(R.id.etSelf);
        tvAsk = findViewById(R.id.tvAsk);

        if(getIntent().getStringExtra("gender").equals("c"))
            tvAsk.setText("Where are you from?");

        tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getArea();
            }
        });
        tvContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(getIntent().getStringExtra("gender").equals("p"))
                    getContents();
                else
                    getCountry();
            }
        });


        etChannel = findViewById(R.id.etChannel);
        etNick = findViewById(R.id.etNick);
        final String result = "";
        Button btnSaveInfo = findViewById(R.id.btnSaveInfo);

        final CheckBox youtuber = (CheckBox)findViewById(R.id.checkBox1);
        final CheckBox plan = (CheckBox)findViewById(R.id.checkBox2);
        final CheckBox edit = (CheckBox)findViewById(R.id.checkBox3);


        final Button b = (Button)findViewById(R.id.btnSaveInfo);
        //FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();


        token = FirebaseInstanceId.getInstance().getToken();



        final FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("user").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("myLog", "기본 정보 불러올 수 있음." + document.get("test"));




                    } else {
                        Log.d("myLog", "기본정보 못불러옴." + user.getUid());


                    }
                } else {

                }
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean right = true;
                Intent intent = getIntent();


                FirebaseAuth mAuth;

                mAuth = FirebaseAuth.getInstance();
                String result = "";
                if(youtuber.isChecked() == true) result += youtuber.getText().toString() + "|";
                if(plan.isChecked() == true) result += plan.getText().toString() + "|";
                if(edit.isChecked() == true) result += edit.getText().toString() + "|";
                if(result.equals(""))
                {
                    right = false;
                    Toast.makeText(Regist2.this, "1번. 역할을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String nick = "";
                try
                {
                    nick = etNick.getText().toString();
                }catch(Exception e)
                {
                    Toast.makeText(Regist2.this, "2번. 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    right = false;
                    return;
                }
                if(nick.equals(""))
                {
                    Toast.makeText(Regist2.this, "2번. 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    right = false;
                    return;
                }

                if(area == "")
                {
                    right = false;
                    Toast.makeText(Regist2.this, "3번. 지역을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(contents == "")
                {
                    right = false;
                    Toast.makeText(Regist2.this, "4번. 콘텐츠를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                final FirebaseUser user = mAuth.getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();



                String selfIntro;
                try
                {
                    selfIntro = etSelf.getText().toString();
                }catch(Exception e)
                {
                    selfIntro = "아직 소개 없음";
                }

                if(selfIntro.equals(""))
                {
                    selfIntro = "아직 소개 없음";
                }

                String channel;
                try
                {
                    channel = etChannel.getText().toString();
                }catch(Exception e)
                {
                    channel = "no price";
                }
                if(channel.equals(""))
                {
                    channel = "no price";
                }

                Map<String, Object> profile = new HashMap<>();

                final String gender = intent.getStringExtra("gender");

                profile.put("gender", gender);
                profile.put("nick", nick);
                profile.put("contents", contents);
                profile.put("area", area);
                profile.put("role", result);
                profile.put("intro", selfIntro);
                profile.put("email", user.getEmail());
                profile.put("price", channel);
                profile.put("token", token);
                SharedPreferences sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);

                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nick", nick); // key, value를 이용하여 저장하는 형태
                editor.putString("area", area);
                editor.putString("contents", contents);

                editor.commit();
                //다양한 형태의 변수값을 저장할 수 있다.
                //editor.putString();
                //editor.putBoolean();
                //editor.putFloat();
                //editor.putLong();
                //editor.putInt();
                //editor.putStringSet();

                //최종 커밋


                db.collection("user").document(user.getUid()).set(profile);

                Map<String, Object> noti = new HashMap<>();
                noti.put("notiCount", "0");
                db.collection("notification").document(user.getEmail()).set(noti);


                final Map<String, Object> guestData = new HashMap<>();

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = df.format(c.getTime());

                guestData.put("area", area);
                guestData.put("contents", contents);
                guestData.put("selfIntro", selfIntro);
                guestData.put("price", channel);
                guestData.put("email", user.getEmail());
                guestData.put("time", time);
                guestData.put("nick", nick);

                if(gender.equals("p"))
                {




                    CollectionReference tmp = db.collection("guestMarket").document("table")
                            .collection("all");
                    tmp.whereEqualTo("read" , true).orderBy(FieldPath.documentId(), Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    doc_num = Integer.valueOf(document.get("doc_num").toString());
                                    Log.d("myLog34", String.valueOf(doc_num));
                                }
                                final DocumentReference sfDocRef = db.collection("guestMarket").document("table")
                                        .collection("all").document("doc" + String.valueOf(doc_num));

                                Log.d("myLog", "트랜젝션 시도");
                                db.runTransaction(new Transaction.Function<Void>() {


                                    @Override
                                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {

                                        DocumentSnapshot snapshot = transaction.get(sfDocRef);
                                        all_doc_num = snapshot.get("doc_num").toString();
                                        all_guest_count = snapshot.get("guest_count").toString();
                                        final Map<String, Object> uploadData = new HashMap<>();
                                        if(Integer.valueOf(all_guest_count) < MaxDocSize)
                                        {




                                            uploadData.put("guest_count", Integer.valueOf(all_guest_count) + 1);
                                            uploadData.put("guest" + String.valueOf(Integer.valueOf(all_guest_count) + 1), guestData);
                                            transaction.update(sfDocRef, uploadData);
                                        }
                                        else
                                        {

                                            uploadData.put("guest" + "1", guestData);
                                            db.collection("guestMarket").document("table")
                                                    .collection("all").document("doc" + String.valueOf(Integer.valueOf(all_doc_num) + 1))
                                                    .set(uploadData);
                                            db.collection("guestMarket").document("table")
                                                    .collection("all").document("doc" + String.valueOf(Integer.valueOf(all_doc_num) + 1))
                                                    .update("guest_count", 1);
                                            db.collection("guestMarket").document("table")
                                                    .collection("all").document("doc" + String.valueOf(Integer.valueOf(all_doc_num) + 1))
                                                    .update("read", true);

                                            final Map<String, Object> doc_num = new HashMap<>();
                                            doc_num.put("doc_num", String.valueOf(Integer.valueOf(all_doc_num) + 1));
                                            db.collection("guestMarket").document("table")
                                                    .collection("all").document("doc" + String.valueOf(Integer.valueOf(all_doc_num) + 1))
                                                    .update(doc_num);

                                        }


                                        //Toast.makeText(newGuest.this,String.valueOf(all_doc_num) , Toast.LENGTH_SHORT).show();
                                        //transaction.update(sfDocRef, "population", newPopulation);

                                        // Success
                                        return null;
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        click = false;

                                        //pbTmp.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),"성공!!!!!!!!!", Toast.LENGTH_LONG).show();
                                        //finish();

                                        Intent in = new Intent(Regist2.this, TabActivity.class);

                                        startActivity(in);
                                        Log.d("myLog", "Transaction success!");
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                click = false;
                                                Toast.makeText(getApplicationContext(),"업로드를 성공하였습니다.", Toast.LENGTH_LONG).show();



                                                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                DocumentReference docRef = db.collection("guestMarket").document("table")
                                                        .collection("all").document("doc0");
                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {




                                                            } else {

                                                                final Map<String, Object> doc_numtmp = new HashMap<>();
                                                                doc_numtmp.put("doc_num", String.valueOf(0));
                                                                doc_numtmp.put("guest_count", 0);
                                                                db.collection("guestMarket").document("table")
                                                                        .collection("all").document("doc0")
                                                                        .set(doc_numtmp);
                                                                final Map<String, Object> doc_num = new HashMap<>();
                                                                doc_num.put("doc_num", String.valueOf(1));
                                                                doc_num.put("guest_count", 1);
                                                                final Map<String, Object> uploadData = new HashMap<>();
                                                                uploadData.put("guest" + "1", guestData);
                                                                db.collection("guestMarket").document("table")
                                                                        .collection("all").document("doc1")
                                                                        .set(uploadData);

                                                                db.collection("guestMarket").document("table")
                                                                        .collection("all").document("doc1")
                                                                        .update(doc_num);

                                                                db.collection("guestMarket").document("table")
                                                                        .collection("all").document("doc1")
                                                                        .update("read", true);


                                                                Intent in = new Intent(Regist2.this, TabActivity.class);

                                                                startActivity(in);

                                                            }
                                                        } else {
                                                            Log.e("myLog", user.getUid());
                                                        }
                                                    }
                                                });

                                                Log.w("myLog", "Transaction failure.", e);

                                            }
                                        });
                            }
                        }
                    });

                }




                /*
                final DatabaseReference myRef2;
                myRef2 = database.getReference();
                final String newEmail = user.getEmail().replaceAll("\\.", "+");
                myRef2.child("uid").child(newEmail).setValue(user.getUid());

                final DatabaseReference myRef = database.getReference("users/" + user.getUid());
                myRef.child("role").setValue(String.valueOf(result));



                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Log.e("다음 값을 읽어옴.", "이름이겠지");
                        //myRef.child("users").child(user.getUid()).setValue(profile);
                        myRef.child("youtubeChannel").setValue(String.valueOf(etChannel.getText()));

                        myRef2.child("users").child(user.getUid()).child("nick").setValue(String.valueOf(etNick.getText()));

                        String[] name = user.getEmail().split("@");
                        //myRef.child("uid").child(name[0]).setValue(user.getUid());

                        final String before = String.valueOf(dataSnapshot.child("users").child(user.getUid()).child("contents").getValue());

                        if(dataSnapshot.hasChild("contents"))
                        {

                            final String tmp = dataSnapshot.child("contents").getValue().toString();


                            myRef2.child("contentsUser").child(tmp).child(user.getUid()).removeValue();

                            myRef2.child("contentsUser").child(rb.getText().toString()).child(user.getUid()).setValue("tmp");

                            myRef2.child("users").child(user.getUid()).child("contents").setValue(rb.getText().toString());
                            Toast.makeText(Regist2.this, tmp + " 유튜버에서 " + rb.getText().toString() + " 유튜버로 변경되었습니다.", Toast.LENGTH_LONG).show();



                        }
                        else {
                            myRef2.child("contentsUser").child(rb.getText().toString()).child(user.getUid()).setValue("tmp");

                            myRef.child("contents").setValue(rb.getText().toString());
                            Toast.makeText(Regist2.this, rb.getText().toString() + " 유튜버로 서버에 저장하였습니다.", Toast.LENGTH_LONG).show();


                        }

                        //myRef.child("users").child(user.getUid()).child("contents").setValue(rb.getText().toString());


                        Intent in = new Intent(Regist2.this, TabActivity.class);
                        Log.e("regist2", "regist2 에서 발생");

                        startActivity(in);
                        //Toast.makeText(Regist2.this, "포인트는 " , Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                */

            }
        });

        final TextView tv = (TextView)findViewById(R.id.textView2);






    }
}