package com.paik.app.tab;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paik.app.R;

public class TabActivity extends AppCompatActivity {
    Fragment fragment;
    Activity talk;
    long lastPressed;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    switchFragment(fragment);
                    return true;
                case R.id.navigation_dashboard:
                    fragment = new NotiFragment();
                    switchFragment(fragment);
                    return true;
                case R.id.navigation_free:
                    fragment = new FreeFragment();
                    switchFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    switchFragment(fragment);
                    return true;
                case R.id.navigation_map:
                    fragment = new AgencyFragment();
                    switchFragment(fragment);
                    return true;
            }
            Log.e("myLog",  "fragment 못불러옴");
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new HomeFragment();
        fragmentTransaction.add(R.id.viewer, fragment);
        fragmentTransaction.commit();
        fragment = new HomeFragment();
        switchFragment(fragment);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void switchFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.viewer, fragment);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastPressed < 1500) {
            //mAuth.signOut();
            //finish();
            ActivityCompat.finishAffinity(this);
        }
        Toast.makeText(this, "한번 더 누르면 종료", Toast.LENGTH_SHORT).show();
        lastPressed = System.currentTimeMillis();
    }
}
