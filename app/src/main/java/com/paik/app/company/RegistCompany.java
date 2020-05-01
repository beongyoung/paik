package com.paik.app.company;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paik.app.R;
import com.paik.app.user.Regist2;

import java.util.HashMap;
import java.util.Map;

public class RegistCompany extends AppCompatActivity {

    EditText etTel;
    EditText etRealName;
    Button btnSaveInfo;
    String realName = "";
    String tel = "";
    boolean right = true;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_company);
        btnSaveInfo = findViewById(R.id.btnSaveInfo);
        etRealName = findViewById(R.id.etRealName);
        etTel = findViewById(R.id.etTel);


        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realName = "";
                try
                {
                    realName = etRealName.getText().toString();
                }catch(Exception e)
                {
                    Toast.makeText(RegistCompany.this, "please input real Name", Toast.LENGTH_SHORT).show();
                    right = false;
                    return;
                }
                if(realName.equals(""))
                {
                    Toast.makeText(RegistCompany.this, "please input real name", Toast.LENGTH_SHORT).show();
                    right = false;
                    return;
                }

                tel = "";
                try
                {
                    tel = etTel.getText().toString();
                }catch(Exception e)
                {
                    Toast.makeText(RegistCompany.this, "please input phone Number", Toast.LENGTH_SHORT).show();
                    right = false;
                    return;
                }
                if(realName.equals(""))
                {
                    Toast.makeText(RegistCompany.this, "please input phone number", Toast.LENGTH_SHORT).show();
                    right = false;
                    return;
                }



                mAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();

                Map<String, Object> profile = new HashMap<>();

                profile.put("real_name", realName);
                profile.put("phone_number", tel);
                profile.put("access", "F");

                db.collection("Company").document("CompanyA").collection("employee").document(user.getUid()).set(profile);


                finish();


            }
        });








    }
}
