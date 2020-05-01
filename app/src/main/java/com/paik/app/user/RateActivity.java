package com.paik.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paik.app.R;

public class RateActivity extends AppCompatActivity {
    String email;
    TextView tvRateId;
    Button[] btnScore;



    FirebaseUser user;
    FirebaseAuth mAuth;
    Button btnGo;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference myRef2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);


        tvRateId = findViewById(R.id.tvRateId);
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        tvRateId.setText(email);

        boolean right = false;


        btnScore = new Button[5];

        btnScore[4] = findViewById(R.id.button3);
        btnScore[3] = findViewById(R.id.button4);
        btnScore[2] = findViewById(R.id.button5);
        btnScore[1] = findViewById(R.id.button6);
        btnScore[0] = findViewById(R.id.button7);

        btnScore[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.equals("id"))
                {
                    Toast.makeText(RateActivity.this, "먼저 매칭을 해주세요!", Toast.LENGTH_LONG).show();
                    finish();
                }
                mAuth = FirebaseAuth.getInstance();

                user = mAuth.getCurrentUser();
                database = FirebaseDatabase.getInstance();
                myRef2 = database.getReference();
                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String[] name = email.split("@");


                        String uid = dataSnapshot.child("uid").child(name[0]).getValue().toString();
                        String tmp = dataSnapshot.child("users").child(uid).child("rate").getValue().toString();
                        double rate = Double.parseDouble(tmp);
                        tmp = dataSnapshot.child("users").child(uid).child("rateCount").getValue().toString();
                        int rateCount = Integer.parseInt(tmp);
                        if(rate == 0.0)
                        {
                            rate = 5.0;
                            rateCount = 1;
                        }
                        else
                        {
                            rate = (rate*rateCount + 5.0)/(rateCount + 1);
                            rateCount++;
                        }

                        myRef2.child("users").child(uid).child("rate").setValue(String.valueOf(rate));
                        myRef2.child("users").child(uid).child("rateCount").setValue(String.valueOf(rateCount));



                        tmp = dataSnapshot.child("users").child(user.getUid()).child("point").getValue().toString();
                        int point = Integer.parseInt(tmp);
                        if((rate - 5.0)*(rate - 5.0) < 1) {
                            Toast.makeText(RateActivity.this, "거의 맞추셨습니다. 5포인트 적립되었습니다.", Toast.LENGTH_LONG).show();
                            point += 5;
                        }else{
                            Toast.makeText(RateActivity.this, "아쉽지만" + rateCount + "명의 사람들이 " + name[0] + "에게 " + rate + "점을 주었습니다. ", Toast.LENGTH_LONG).show();
                        }
                        myRef2.child("users").child(user.getUid()).child("point").setValue(String.valueOf(point));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //Toast.makeText(RateActivity.this, "5포인트 적립되었습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        btnScore[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.equals("id"))
                {
                    Toast.makeText(RateActivity.this, "먼저 매칭을 해주세요!", Toast.LENGTH_LONG).show();
                    finish();
                }
                mAuth = FirebaseAuth.getInstance();

                user = mAuth.getCurrentUser();
                database = FirebaseDatabase.getInstance();
                myRef2 = database.getReference();
                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String[] name = email.split("@");


                        String uid = dataSnapshot.child("uid").child(name[0]).getValue().toString();
                        String tmp = dataSnapshot.child("users").child(uid).child("rate").getValue().toString();
                        double rate = Double.parseDouble(tmp);
                        tmp = dataSnapshot.child("users").child(uid).child("rateCount").getValue().toString();
                        int rateCount = Integer.parseInt(tmp);
                        if(rate == 0.0)
                        {
                            rate = 4.0;
                            rateCount = 1;
                        }
                        else
                        {
                            rate = (rate*rateCount + 4.0)/(rateCount + 1);
                            rateCount++;
                        }

                        myRef2.child("users").child(uid).child("rate").setValue(String.valueOf(rate));
                        myRef2.child("users").child(uid).child("rateCount").setValue(String.valueOf(rateCount));


                        tmp = dataSnapshot.child("users").child(user.getUid()).child("point").getValue().toString();
                        int point = Integer.parseInt(tmp);
                        if((rate - 4.0)*(rate - 4.0) < 1) {
                            Toast.makeText(RateActivity.this, "거의 맞추셨습니다. 5포인트 적립되었습니다.", Toast.LENGTH_LONG).show();
                            point += 5;
                        }else{
                            Toast.makeText(RateActivity.this, "아쉽지만" + rateCount + "명의 사람들이 " + name[0] + "에게 " + rate + "점을 주었습니다. ", Toast.LENGTH_LONG).show();
                        }
                        myRef2.child("users").child(user.getUid()).child("point").setValue(String.valueOf(point));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //Toast.makeText(RateActivity.this, "5포인트 적립되었습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        btnScore[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.equals("id"))
                {
                    Toast.makeText(RateActivity.this, "먼저 매칭을 해주세요!", Toast.LENGTH_LONG).show();
                    finish();
                }
                mAuth = FirebaseAuth.getInstance();

                user = mAuth.getCurrentUser();
                database = FirebaseDatabase.getInstance();
                myRef2 = database.getReference();
                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String[] name = email.split("@");


                        String uid = dataSnapshot.child("uid").child(name[0]).getValue().toString();
                        String tmp = dataSnapshot.child("users").child(uid).child("rate").getValue().toString();
                        double rate = Double.parseDouble(tmp);
                        tmp = dataSnapshot.child("users").child(uid).child("rateCount").getValue().toString();
                        int rateCount = Integer.parseInt(tmp);
                        if(rate == 0.0)
                        {
                            rate = 3.0;
                            rateCount = 1;
                        }
                        else
                        {
                            rate = (rate*rateCount + 3.0)/(rateCount + 1);
                            rateCount++;
                        }

                        myRef2.child("users").child(uid).child("rate").setValue(String.valueOf(rate));
                        myRef2.child("users").child(uid).child("rateCount").setValue(String.valueOf(rateCount));


                        tmp = dataSnapshot.child("users").child(user.getUid()).child("point").getValue().toString();
                        int point = Integer.parseInt(tmp);
                        if((rate - 3.0)*(rate - 3.0) < 1) {
                            Toast.makeText(RateActivity.this, "거의 맞추셨습니다. 5포인트 적립되었습니다.", Toast.LENGTH_LONG).show();
                            point += 5;
                        }else{
                            Toast.makeText(RateActivity.this, "아쉽지만" + rateCount + "명의 사람들이 " + name[0] + "에게 " + rate + "점을 주었습니다. ", Toast.LENGTH_LONG).show();
                        }
                        myRef2.child("users").child(user.getUid()).child("point").setValue(String.valueOf(point));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //Toast.makeText(RateActivity.this, "5포인트 적립되었습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        btnScore[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.equals("id"))
                {
                    Toast.makeText(RateActivity.this, "먼저 매칭을 해주세요!", Toast.LENGTH_LONG).show();
                    finish();
                }
                mAuth = FirebaseAuth.getInstance();

                user = mAuth.getCurrentUser();
                database = FirebaseDatabase.getInstance();
                myRef2 = database.getReference();
                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String[] name = email.split("@");


                        String uid = dataSnapshot.child("uid").child(name[0]).getValue().toString();
                        String tmp = dataSnapshot.child("users").child(uid).child("rate").getValue().toString();
                        double rate = Double.parseDouble(tmp);
                        tmp = dataSnapshot.child("users").child(uid).child("rateCount").getValue().toString();
                        int rateCount = Integer.parseInt(tmp);
                        if(rate == 0.0)
                        {
                            rate = 2.0;
                            rateCount = 1;
                        }
                        else
                        {
                            rate = (rate*rateCount + 2.0)/(rateCount + 1);
                            rateCount++;
                        }

                        myRef2.child("users").child(uid).child("rate").setValue(String.valueOf(rate));
                        myRef2.child("users").child(uid).child("rateCount").setValue(String.valueOf(rateCount));


                        tmp = dataSnapshot.child("users").child(user.getUid()).child("point").getValue().toString();
                        int point = Integer.parseInt(tmp);
                        if((rate - 2.0)*(rate - 2.0) < 1) {
                            Toast.makeText(RateActivity.this, "거의 맞추셨습니다. 5포인트 적립되었습니다.", Toast.LENGTH_LONG).show();
                            point += 5;
                        }else{
                            Toast.makeText(RateActivity.this, "아쉽지만" + rateCount + "명의 사람들이 " + name[0] + "에게 " + rate + "점을 주었습니다. ", Toast.LENGTH_LONG).show();
                        }
                        myRef2.child("users").child(user.getUid()).child("point").setValue(String.valueOf(point));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //Toast.makeText(RateActivity.this, "5포인트 적립되었습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        btnScore[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.equals("id"))
                {
                    Toast.makeText(RateActivity.this, "먼저 매칭을 해주세요!", Toast.LENGTH_LONG).show();
                    finish();
                }
                mAuth = FirebaseAuth.getInstance();

                user = mAuth.getCurrentUser();
                database = FirebaseDatabase.getInstance();
                myRef2 = database.getReference();
                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String[] name = email.split("@");


                        String uid = dataSnapshot.child("uid").child(name[0]).getValue().toString();
                        String tmp = dataSnapshot.child("users").child(uid).child("rate").getValue().toString();
                        double rate = Double.parseDouble(tmp);
                        tmp = dataSnapshot.child("users").child(uid).child("rateCount").getValue().toString();
                        int rateCount = Integer.parseInt(tmp);
                        if(rate == 0.0)
                        {
                            rate = 1.0;
                            rateCount = 1;
                        }
                        else
                        {
                            rate = (rate*rateCount + 1.0)/(rateCount + 1);
                            rateCount++;
                        }

                        myRef2.child("users").child(uid).child("rate").setValue(String.valueOf(rate));
                        myRef2.child("users").child(uid).child("rateCount").setValue(String.valueOf(rateCount));




                        tmp = dataSnapshot.child("users").child(user.getUid()).child("point").getValue().toString();
                        int point = Integer.parseInt(tmp);

                        if((rate - 1.0)*(rate - 1.0) < 1) {
                            Toast.makeText(RateActivity.this, "거의 맞추셨습니다. 5포인트 적립되었습니다.", Toast.LENGTH_LONG).show();
                            point += 5;
                        }else{
                            Toast.makeText(RateActivity.this, "아쉽지만" + rateCount + "명의 사람들이 " + name[0] + "에게 " + rate + "점을 주었습니다. ", Toast.LENGTH_LONG).show();
                        }
                        myRef2.child("users").child(user.getUid()).child("point").setValue(String.valueOf(point));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                finish();
            }
        });





    }
}
