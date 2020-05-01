package com.paik.app.tab;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paik.app.company.CompanyA;
import com.paik.app.R;
import com.paik.app.adapter.FormAdapter;
import com.paik.app.company.RegistCompany;
import com.paik.app.type_class.Form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgencyFragment extends Fragment {
    RecyclerView mRecyclerView;
    List<Form> mForm;
    FormAdapter mAdapter;
    Map<String, Object> forms = new HashMap<>();
    Map<String, Object> form = new HashMap<>();
    int formCount;
    int doc_num;
    LinearLayout llCompanyA;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_agency, container, false);

        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        final FirebaseUser user = mAuth.getCurrentUser();
        /*
        mForm = new ArrayList<>();
        Form c = new Form();
        c.formName = "Unpaid Wages";
        c.imgUrl = "https://firebasestorage.googleapis.com/v0/b/paik-c0f08.appspot.com/o/documentImage%2Funpaid_wage.png?alt=media&token=d14db5d6-ba36-4043-8d59-67edbceb99db";


        Form d = new Form();
        d.formName = "Year-end settlement";
        d.imgUrl = "https://firebasestorage.googleapis.com/v0/b/paik-c0f08.appspot.com/o/documentImage%2Fanother.png?alt=media&token=86345e06-5f6f-422f-a426-6eac6db9e90b";
        */
        llCompanyA = v.findViewById(R.id.llCompanyA);

        llCompanyA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                CompanyA(col) -
                 */
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final DocumentReference docRef = db.collection("Company").document("CompanyA").collection("employee").document(user.getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("myLog", "회원가입 되어있음." + document.get("test"));
                                if(document.get("access").equals("T") || document.get("access").equals("t")) {
                                    Toast.makeText(getContext(), "Welcome to Company A", Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(getContext(), CompanyA.class);
                                    startActivity(in);
                                }else if(document.get("access").equals("F") || document.get("access").equals("f")) {

                                    Toast.makeText(getContext(), "Your account is waiting for verification (it may take a few days.))", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "To use this service, you must be authenticated", Toast.LENGTH_LONG).show();
                                Intent in = new Intent(getContext(), RegistCompany.class);
                                in.putExtra("Company_name", "CompanyA");
                                startActivity(in);
                            }
                        } else {
                            Log.e("myLog", user.getUid());
                        }
                    }
                });
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference tmp = db.collection("form").document("table").collection("all").document("doc1");
        tmp.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        forms = document.getData();
                        formCount = Integer.valueOf(forms.get("form_count").toString());
                        doc_num = Integer.valueOf(forms.get("doc_num").toString());
                        mForm = new ArrayList<>();
                        for(int i = 1; i < formCount + 1; i++) {
                            form = (Map<String, Object>) forms.get("form" + i);
                            Form tmp = new Form();
                            tmp.formName = form.get("formName").toString();
                            tmp.imgUrl = form.get("imgUrl").toString();
                            mForm.add(tmp);
                        }
                    }
                    mRecyclerView = (RecyclerView)v.findViewById(R.id.rvForm);
                    mRecyclerView.setNestedScrollingEnabled(false);
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    mRecyclerView.setHasFixedSize(true);
                    // use a linear layout manager
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                    mAdapter = new FormAdapter(mForm, getActivity());
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Log.d("test", "Error getting documents: ", task.getException());
                }
            }
        });
        return v;
    }
}
