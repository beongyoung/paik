package com.paik.app.company;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.paik.app.R;

public class LaborContract extends AppCompatActivity {

    String date;
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labor_contract);
        date = getIntent().getStringExtra("date");
        tvDate = findViewById(R.id.tvDate);

        tvDate.setText(date);




    }
}
