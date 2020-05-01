package com.paik.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.paik.app.R;
import com.paik.app.post.ContentsShow;
import com.paik.app.type_class.contentsTitle;

import java.util.List;


public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.MyViewHolder> {

private List<contentsTitle> mContents;
private boolean myTalk;
String stEmail;
Context context;
String kind;


// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class MyViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView tvNick;
    public TextView tvTime;
    public TextView tvGood;
    public ImageView ivUser;
    public Button btnChat;
    public TextView tvTitle;
    public TextView tvEmail;
    public String kindKr;
    public MyViewHolder(View itemView) {



        super(itemView);

        tvNick = itemView.findViewById(R.id.tvNick);
        ivUser = itemView.findViewById(R.id.ivUser);
        btnChat = itemView.findViewById(R.id.btnChat);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvTime = itemView.findViewById(R.id.tvTime);
        tvEmail = itemView.findViewById(R.id.tvEmail);
        tvGood = itemView.findViewById(R.id.tvGood);


    }
}

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContentsAdapter(List<contentsTitle>  mContents, Context context, String kind) {
        this.mContents = mContents;
        this.stEmail = stEmail;
        this.context = context;
        this.kind = kind;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_contents_drawer, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String stFriendId = mFriend.get(position).getKey();

                Intent in = new Intent(context, ContentsShow.class);


                String tmp2[] = mContents.get(position).getTitle().split("\\|");

                in.putExtra("key", mContents.get(position).getKey());
                in.putExtra("nick", tmp2[0]);
                in.putExtra("email", mContents.get(position).getEmail());
                in.putExtra("photo", mContents.get(position).getPhoto());
                in.putExtra("title", tmp2[1]);
                in.putExtra("kind", kind);
                context.startActivity(in);
            }
        });

        String tmp2[] = mContents.get(position).getTitle().split("\\|");// 닉네임 | 타이틀
        //holder.tvEmail.setText("  " + mContents.get(position).getEmail() + "  ");
        holder.tvNick.setText(tmp2[0]);
        holder.tvTitle.setText(tmp2[1]);
        holder.tvEmail.setText("이메일 : " + mContents.get(position).getEmail());
        holder.tvGood.setText("좋아요 : " + tmp2[2]);
        String key = mContents.get(position).getKey();
        String[] tmp = key.split("\\|");
        holder.tvTime.setText(tmp[0]);
        //Picasso.get().load("https://pbs.twimg.com/profile_images/981922316304044033/kDgpJFZ-_400x400.jpg").fit().centerInside().into(holder.ivUser);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mContents.size();
    }
}