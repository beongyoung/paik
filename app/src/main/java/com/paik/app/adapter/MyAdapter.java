package com.paik.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.paik.app.R;
import com.paik.app.type_class.Chat;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
private String[] mDataset;
private List<Chat> mChat;
private boolean myTalk;
private boolean isDate = false;
String stEmail;
Context context;
String cut[] = {"none", "none"};
private StorageReference mStorageRef;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class MyViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView mTextView;
    public TextView timeView;
    public TextView idDate;
    public static ImageView ivUser;
    public MyViewHolder(View itemView) {



        super(itemView);





///////////////////////////////////////////////////////////////////
        timeView = itemView.findViewById(R.id.timeView);

        ivUser = itemView.findViewById(R.id.ivUser);
        mTextView = (TextView)itemView.findViewById(R.id.mTextView);
        idDate = itemView.findViewById(R.id.idDate);

    }
}

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Chat>  mChat, String stEmail, Context context) {
        this.mChat = mChat;
        this.stEmail = stEmail;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {




        if(mChat.get(position).getEmail().equals(stEmail)){ //채팅 내역의 보낸 사람 email이 나의 email과 일치하는가?
            myTalk = true;
            return 1; // viewType을 1로 설정
        }
        else if(mChat.get(position).getEmail().equals("time"))
        {
            isDate = true;
            return 3;
        }
        else{
            myTalk = false;
            return 2;// viewType을 2로 설정
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date, parent, false);

        if(viewType == 1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.right_text_view, parent, false);
        }else if(viewType == 2){

            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);




        }
        else if(viewType == 3) //사진 등 다른거 보낼 때 사용
        {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.date, parent, false);
        }

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that


        if(isDate)
        {
            isDate = false;

            cut = mChat.get(position).getTime().split(" ");



            try {
                holder.idDate.setText(cut[0]);//*임시
            }catch(Exception e)
            {
                //Log.e("콘텐츠 띄우는데 에러남.", e.toString());
            }
            return;
        }

        if(!myTalk) {
            cut = mChat.get(position).getEmail().split("@");
            //holder.mTextView.setText(cut[0] + " :" + mChat.get(position).getText());
            holder.mTextView.setText(mChat.get(position).getText());
            //Picasso.get().load("https://pbs.twimg.com/profile_images/981922316304044033/kDgpJFZ-_400x400.jpg").fit().centerInside().into(holder.ivUser);



            mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference pathReference = mStorageRef.child("users/profile/"+ mChat.get(position).getEmail() + ".jpg");

            //Url을 다운받기
            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {


                    //Picasso.get().load(uri.toString()).fit().centerInside().into(holder.ivUser);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });





        }
        else {
            holder.mTextView.setText(mChat.get(position).getText());
        }

        if(mChat.get(position).getTime().equals("")) {
            if(mChat.get(position+1).getTime().equals(""))
            {
                cut = mChat.get(position + 2).getTime().split(" ");
            }
            else
                cut = mChat.get(position + 1).getTime().split(" ");
        }
        cut = mChat.get(position).getTime().split(" ");

        holder.timeView.setText(cut[1]);
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, mChat.get(position).getEmail(), Toast.LENGTH_SHORT).show();

                //Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show(); // position위치가 몇번째 값인지 보여줌
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mChat.size();
    }
}