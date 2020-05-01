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
import com.paik.app.post.show_post;
import com.paik.app.type_class.Guest;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

private List<Guest> mGuest;
private boolean myTalk;
String stEmail;
Context context;


// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class MyViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView tvEmail;
    public ImageView ivUser;
    public Button btnChat;
    public TextView tvNick;
    public TextView tvContents;
    public TextView tvArea;
    public TextView tvTime;

    public MyViewHolder(View itemView) {



        super(itemView);

        tvTime = itemView.findViewById(R.id.tvTime);
        tvArea = itemView.findViewById(R.id.tvArea);
        tvContents = itemView.findViewById(R.id.tvContents);
        tvNick =  itemView.findViewById(R.id.tvNick);
        tvEmail = itemView.findViewById(R.id.tvEmail);
        ivUser = itemView.findViewById(R.id.ivUser);
        btnChat = itemView.findViewById(R.id.btnChat);
    }
}

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotificationAdapter(List<Guest>  mGuest, Context context) {
        this.mGuest = mGuest;
        this.stEmail = stEmail;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_friend, parent, false);

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

                Intent in = new Intent(context, show_post.class);
                String docId = mGuest.get(position).selfIntro;
                in.putExtra("docId", docId);
                in.putExtra("time", "no");
                in.putExtra("email", "no");
                context.startActivity(in);
            }
        });
        holder.tvNick.setText(mGuest.get(position).nick);
        holder.tvTime.setText(mGuest.get(position).time);
        holder.tvArea.setText(mGuest.get(position).area);
        holder.tvContents.setText(mGuest.get(position).contents);
        holder.tvEmail.setText(mGuest.get(position).email);
        //Picasso.get().load("https://pbs.twimg.com/profile_images/981922316304044033/kDgpJFZ-_400x400.jpg").fit().centerInside().into(holder.ivUser);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mGuest.size();
    }
}