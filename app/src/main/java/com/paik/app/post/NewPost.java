package com.paik.app.post;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.paik.app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewPost extends AppCompatActivity {

    TextView tvSave;
    TextView tvArea;
    TextView tvContents;
    ImageView ivCancel;
    EditText etIntro;
    EditText etPrice;
    EditText etTitle;
    String area = "null";
    String contents = "null";
    String intro;
    String title;
    boolean click = false;
    ProgressBar pbTmp;

    int doc_num;
    int MaxDocSize = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        etIntro = findViewById(R.id.etIntro);
        tvArea = findViewById(R.id.tvArea);
        tvSave = findViewById(R.id.tvSave);
        etPrice = findViewById(R.id.etPrice);
        etTitle = findViewById(R.id.etTitle);
        pbTmp = findViewById(R.id.pbTmp);
        ivCancel = findViewById(R.id.ivCancel);


        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                boolean right = true;
                if(area == "null")
                {
                    right = false;
                    Toast.makeText(NewPost.this, "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    intro = etIntro.getText().toString();
                }catch(Error e)
                {
                    right = false;
                    Toast.makeText(NewPost.this, "내용을 적어주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(intro.equals(""))
                {
                    right = false;
                    Toast.makeText(NewPost.this, "내용을 적어주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    title =etTitle.getText().toString();
                }catch(Error e)
                {
                    right = false;
                    Toast.makeText(NewPost.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(title.equals(""))
                {
                    right = false;
                    Toast.makeText(NewPost.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(right)
                {
                    if(click) {
                        Toast.makeText(getApplicationContext(),"업로드중입니다....", Toast.LENGTH_LONG).show();

                        return;

                    }
                    pbTmp.setVisibility(View.VISIBLE);
                    click = true;
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();

                    final FirebaseUser user = mAuth.getCurrentUser();
                    final Map<String, Object> guestData = new HashMap<>();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    final String time = df.format(c.getTime());

                    guestData.put("area", area);
                    guestData.put("contents", title);
                    guestData.put("selfIntro", intro);
                    guestData.put("email", user.getEmail());
                    guestData.put("time", time);
                    guestData.put("commentCount", String.valueOf(0));
                    guestData.put("goodCount", String.valueOf(0));

                    SharedPreferences sharedPrefs = getSharedPreferences("user_info", MODE_PRIVATE);
                    String nick = sharedPrefs.getString("nick", user.getEmail());
                    guestData.put("nick", nick);
                    if(nick.equals(user.getEmail()))
                        Toast.makeText(NewPost.this, "닉네임을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();


                    //전체




                    final FirebaseFirestore db = FirebaseFirestore.getInstance();


                    CollectionReference tmp = db.collection("free").document("table")
                            .collection("all");

                    tmp.whereEqualTo("read" , true).orderBy(FieldPath.documentId(), Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    doc_num = Integer.valueOf(document.get("doc_num").toString());
                                    Log.d("myLog34", String.valueOf(doc_num));


                                }
                                final DocumentReference sfDocRef = db.collection("free").document("table")
                                        .collection("all").document("doc" + String.valueOf(doc_num));


                                db.collection("free").document("data")
                                        .collection("dataCol").document(time + user.getEmail())
                                        .set(guestData);//포스트 저장


                                Log.d("myLog", "테이블 트랜젝션 시도");
                                db.runTransaction(new Transaction.Function<Void>() {


                                    @Override
                                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {

                                        DocumentSnapshot snapshot = transaction.get(sfDocRef);
                                        String all_doc_num = snapshot.get("doc_num").toString();
                                        String all_guest_count = snapshot.get("guest_count").toString();
                                        Log.d("myLog", "all_doc_num : " + all_doc_num + "all_guest_count : " + all_guest_count);

                                        final Map<String, Object> uploadData = new HashMap<>();
                                        if (Integer.valueOf(all_guest_count) < MaxDocSize) {

                                            uploadData.put("guest" + String.valueOf(Integer.valueOf(all_guest_count) + 1), guestData);
                                            uploadData.put("guest_count", Integer.valueOf(all_guest_count) + 1);
                                            transaction.update(sfDocRef, uploadData);
                                        } else {

                                            uploadData.put("guest" + "1", guestData);
                                            db.collection("free").document("table")
                                                    .collection("all").document("doc" + String.valueOf(Integer.valueOf(all_doc_num) + 1))
                                                    .set(uploadData);
                                            db.collection("free").document("table")
                                                    .collection("all").document("doc" + String.valueOf(Integer.valueOf(all_doc_num) + 1))
                                                    .update("guest_count", 1);
                                            db.collection("free").document("table")
                                                    .collection("all").document("doc" + String.valueOf(Integer.valueOf(all_doc_num) + 1))
                                                    .update("read", true);

                                            final Map<String, Object> doc_num = new HashMap<>();
                                            doc_num.put("doc_num", String.valueOf(Integer.valueOf(all_doc_num) + 1));
                                            db.collection("free").document("table")
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
                                        Toast.makeText(getApplicationContext(), "업로드 성공", Toast.LENGTH_LONG).show();
                                        //finish();
                                        Log.d("myLog", "Transaction success!");
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                click = false;
                                                Toast.makeText(getApplicationContext(), "업로드 성공한 것 같습니다.", Toast.LENGTH_LONG).show();

                                                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                DocumentReference docRef = db.collection("free").document("table")
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
                                                                db.collection("free").document("table")
                                                                        .collection("all").document("doc0")
                                                                        .set(doc_numtmp);

                                                                final Map<String, Object> doc_num = new HashMap<>();
                                                                doc_num.put("doc_num", String.valueOf(1));
                                                                doc_num.put("guest_count", 1);
                                                                final Map<String, Object> uploadData = new HashMap<>();
                                                                uploadData.put("guest" + "1", guestData);
                                                                db.collection("free").document("table")
                                                                        .collection("all").document("doc1")
                                                                        .set(uploadData);

                                                                db.collection("free").document("table")
                                                                        .collection("all").document("doc1")
                                                                        .update(doc_num);

                                                                db.collection("free").document("table")
                                                                        .collection("all").document("doc1")
                                                                        .update("read", true);

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









                    /*
                    db.collection("free").document("table")
                            .collection("all").document("doc1").set(uploadData);
                    */
                    //지역별


                    //콘텐츠별


                    //지역&콘텐츠별
                    finish();
                }


            }
        });
        tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getArea();
            }
        });


    }



    public void getArea()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("카테고리 선택");

        builder.setItems(R.array.freeCategory, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int pos)
            {
                String[] items = getResources().getStringArray(R.array.freeCategory);
                //Toast.makeText(getApplicationContext(),items[pos], Toast.LENGTH_LONG).show();
                area = items[pos];

                tvArea.setText("카테고리 선택 : " + area);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void getContents()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("콘텐츠 선택");

        builder.setItems(R.array.contents, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int pos)
            {
                String[] items = getResources().getStringArray(R.array.contents);
                //Toast.makeText(getApplicationContext(),items[pos], Toast.LENGTH_LONG).show();
                contents = items[pos];

                tvContents.setText("콘텐츠 선택 : " + contents);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
