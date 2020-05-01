package com.paik.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class report extends AppCompatActivity {

    TextView tvNick;
    TextView tvSave;
    ImageView ivCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tvNick = findViewById(R.id.tvNick);
        tvSave = findViewById(R.id.tvSave);
        ivCancel = findViewById(R.id.ivCancel);



        tvNick.setText(getIntent().getStringExtra("nick"));

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //서버 업로드
                Toast.makeText(report.this, "Thank you for your report. //테스트// 신고된 이메일 : " + getIntent().getStringExtra("email"), Toast.LENGTH_LONG).show();
                finish();
            }
        });




    }
}
