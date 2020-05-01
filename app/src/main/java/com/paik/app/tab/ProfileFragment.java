package com.paik.app.tab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.paik.app.R;
import com.paik.app.user.Regist;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    ImageView ivUser;
    TextView tvNick;
    TextView tvEmail;
    TextView tvContents;
    TextView tvArea;
    TextView tvGender;
    TextView tvCrew;
    TextView tvSelfIntro;
    private StorageReference mStorageRef;
    Bitmap bitmap;
    String stEmail;
    String stUid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView tvRate;
    TextView tvPoint;
    Context c = getContext();
    FirebaseAuth mAuth;
    Button btnGo;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView btnSignout;
    TextView changeInfo;
    Map<String, Object> userInfo = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //tvEmail = getView().findViewById(R.id.tvEmail);
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        //tvRate = v.findViewById(R.id.tvRate);
        //tvPoint = v.findViewById(R.id.tvPoint);
        tvEmail = v.findViewById(R.id.tvEmail);
        btnSignout = v.findViewById(R.id.btnSignout);
        changeInfo = v.findViewById(R.id.changeInfo);
        tvNick = v.findViewById(R.id.tvNick);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvContents = v.findViewById(R.id.tvContents);
        tvArea = v.findViewById(R.id.tvArea);
        tvGender = v.findViewById(R.id.tvGender);
        tvSelfIntro = v.findViewById(R.id.tvSelfIntro);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = mStorageRef.child("users/profile/"+ user.getEmail() + ".jpg");
        //Url을 다운받기
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).fit().centerInside().into(ivUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tmp = db.collection("user");
        tmp.whereEqualTo("email" , user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("myLog", "유저 info db 접근");
                    for (DocumentSnapshot document : task.getResult()) {
                        userInfo = document.getData();
                        Log.d("myLog", userInfo.get("nick").toString() + " 정보 읽어옴");
                        try {
                            tvNick.setText(userInfo.get("nick").toString());
                            tvEmail.setText(userInfo.get("email").toString());
                            tvContents.setText(userInfo.get("contents").toString());
                            tvArea.setText(String.valueOf(userInfo.get("area")));
                            tvGender.setText(userInfo.get("price").toString() + " won");
                            //tvCrew.setText(userInfo.get("crew").toString());
                            tvSelfIntro.setText(userInfo.get("intro").toString());
                        }catch(Exception e) {
                            Log.e("myLog", "뭔가 못읽어옴 - " + e.getMessage());
                        }
                    }
                }else{
                    Log.e("myLog", "db에러임 - showProfile");
                }
            }
        });

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info",MODE_PRIVATE);
                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("nick");// key, value를 이용하여 저장하는 형태
                editor.remove("contents");
                editor.remove("area");
                editor.commit();
                mAuth.signOut();
                getActivity().finish();
            }
        });
        changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), Regist.class);
                startActivity(in);
            }
        });
        tvEmail.setText(user.getEmail());
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ivUser = v.findViewById(R.id.ivUser);
        //Picasso.get().load("https://pbs.twimg.com/profile_images/981922316304044033/kDgpJFZ-_400x400.jpg").fit().centerInside().into(ivUser);
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(getActivity(), ImageActivity.class);
                //startActivity(i);
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });
        return  v;
    }

    public void uploadImage(){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference mountainsRef = mStorageRef.child("users").child("profile").child(user.getEmail()+".jpg");//firebase - storage에 저장될 경로
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 1, baos);// 두번째 매개변수는 퀄리티를 말함. 0부터 100까지 선택 가능.
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                Log.d("urI", downloadUrl.toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Uri image = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
                ivUser.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
    }
}
