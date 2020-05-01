package com.paik.app;




import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paik.app.tab.TabActivity;
import com.paik.app.user.Regist;

public class  MainActivity extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;

    String value;

    public int s = 0;
    SignInButton btnSignIn;
    Button btnSignOut;
    Button btnDisconnect;
    Button btnGo;
    boolean login = false;

    boolean isRegister = false;
    boolean start = false;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        // Views
        //mStatusTextView = findViewById(R.id.status);
        //mDetailTextView = findViewById(R.id.detail);






        // Button listeners
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        btnDisconnect = findViewById(R.id.btnDisconnect);
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeAccess();
            }
        });

        btnGo = findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, TabActivity.class);
                startActivity(in);
            }
        });

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


    }
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);



            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }



    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "뭔지모르겠지만 회원가입 실패", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]


    // [START signin]
    private void signIn() {
        value = null;
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        isRegister = false;
        start = false;
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        value = null;

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(final FirebaseUser user) {
        if (user != null) {
            SharedPreferences sharedPrefs = getSharedPreferences("user_info", MODE_PRIVATE);
            SharedPreferences.Editor ed;
            if(sharedPrefs.contains("nick")){
                String nick = sharedPrefs.getString("nick", "");
                String contents = sharedPrefs.getString("contents", "");
                String area = sharedPrefs.getString("area", "");
                Log.d("myLog", "shared pref로 들어감\nnick : " + nick + "\ncontents : " + contents + "\narea : " + area + "\n");
                Intent in = new Intent(MainActivity.this, TabActivity.class);
                startActivity(in);
            }else {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final DocumentReference docRef = db.collection("user").document(user.getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("myLog", "회원가입 되어있음." + document.get("test"));


                                if (!start) {
                                    start = true;



                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    SharedPreferences sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                                    editor.putString("nick",String.valueOf(document.get("nick")));
                                                    editor.putString("area",String.valueOf(document.get("area")));
                                                    editor.putString("contents",String.valueOf(document.get("contents")));
                                                    editor.commit();
                                                } else {
                                                    Log.d(TAG, "No such document");
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });




                                    Intent in = new Intent(MainActivity.this, TabActivity.class);
                                    startActivity(in);
                                    finish();
                                }

                            } else {
                                Log.d("myLog", "아직 회원가입 안함." + user.getUid());
                                isRegister = true;

                                database = FirebaseDatabase.getInstance();

                                myRef = database.getReference("user/" + user.getUid());
                                myRef.child("point").setValue("10");

                                Intent in = new Intent(MainActivity.this, Regist.class);
                                startActivity(in);

                            }
                        } else {
                            Log.e("myLog", user.getUid());
                        }
                    }
                });

            }
    /*
            database = FirebaseDatabase.getInstance();

            myRef = database.getReference("users/" + user.getUid());

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(!dataSnapshot.exists())
                    {//회원가입이 안되어있음.
                        if (!isRegister) {
                            isRegister = true;


                            Intent in = new Intent(MainActivity.this, Regist.class);
                            Log.e("new 인텐트 넘어감.", "넘어갔음.");
                            startActivity(in);
                        }
                    } else {
                        if (dataSnapshot.hasChild("role")) {
                            if (!start) {
                                start = true;
                                Intent in = new Intent(MainActivity.this, TabActivity.class);
                                Log.e("new role이 존재함.", "role - TabActivity로");
                                startActivity(in);
                                finish();

                            }
                        } else {
                            if (!start) {
                                start = true;
                                Intent in = new Intent(MainActivity.this, Regist2.class);
                                Log.e("new 스타트", "스타트가 되었음.");
                                startActivity(in);
                            }
                        }


                    }





                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

    */

            //findViewById(R.id.btnGo).setVisibility(View.VISIBLE);

        } else {

            findViewById(R.id.btnGo).setVisibility(View.GONE);
            /*
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
            findViewById(R.id.signOutAndDisconnect).setVisibility(View.GONE);
            */
        }
    }

}




