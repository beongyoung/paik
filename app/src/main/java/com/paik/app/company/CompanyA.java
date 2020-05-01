package com.paik.app.company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.paik.app.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.Calendar;

public class CompanyA extends AppCompatActivity {

    MaterialCalendarView materialCalendarView;
    String strDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_a);

        materialCalendarView = findViewById(R.id.calendarView);


        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
            }
        });

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {



                String tmp = "\\{";
                String[] day = materialCalendarView.getSelectedDate().toString().split(tmp);

                day[1] = day[1].substring(0, day[1].length()-1);


                if(strDate.equals(materialCalendarView.getSelectedDate().toString())) {
                    Intent in = new Intent(CompanyA.this, LaborContract.class);
                    in.putExtra("date", day[1]);
                    startActivity(in);
                    Toast.makeText(CompanyA.this, "You work on " +  materialCalendarView.getSelectedDate().toString(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    strDate = materialCalendarView.getSelectedDate().toString();
                    Toast.makeText(CompanyA.this, day[1], Toast.LENGTH_SHORT).show();
                }
            }
        });

        //materialCalendarView.getSelectedDate();

        /*
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);*/



    }
}