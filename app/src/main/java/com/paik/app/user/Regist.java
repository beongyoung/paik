package com.paik.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paik.app.R;

public class Regist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);


        final RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);
        final Button b = (Button)findViewById(R.id.button1);
        final TextView tv = (TextView)findViewById(R.id.textView2);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = rg.getCheckedRadioButtonId();

                RadioButton rb = (RadioButton) findViewById(id);


                b.setVisibility(View.VISIBLE);

                tv.setVisibility(View.VISIBLE);
                tv.setText(rb.getText().toString() + "로 서버에 저장합니다.");



            }
        });


        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int id = rg.getCheckedRadioButtonId();

                //getCheckedRadioButtonId() 의 리턴값은 선택된 RadioButton 의 id 값.
                RadioButton rb = (RadioButton) findViewById(id);

                final String gender;
                if(rb.getText().toString().equals("전문가"))
                    gender = "p";
                else
                    gender = "c";




                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();







                //db.collection("user").document(user.getUid()).set(profile);
                Intent in = new Intent(Regist.this, Regist2.class);
                in.putExtra("gender", gender);
                startActivity(in);

                /*
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                Hashtable<String, String> profile = new Hashtable<String, String>();
                profile.put("email", user.getEmail());
                profile.put("gender", gender);
                profile.put("key", user.getUid());
                profile.put("point", String.valueOf(0));
                final DatabaseReference myRef = database.getReference("users/" + user.getUid());
                myRef.setValue(profile);
                String[] name = user.getEmail().split("@");
                Log.e("regist1", "regist1 에서 발생");
                Intent in = new Intent(Regist.this, Regist2.class);
                startActivity(in);
                */



            } // end onClick()
        });  // end Listener





    }
}