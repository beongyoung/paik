package com.paik.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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


public class AreaContentsAdapter extends RecyclerView.Adapter<AreaContentsAdapter.MyViewHolder> {

private List<contentsTitle> mContents;
private boolean myTalk;
String stEmail;
Context context;
String kind;
boolean show_change = false;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class MyViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView tvEmail;
    public TextView tvTime;
    public ImageView ivUser;
    public Button btnChat;
    public TextView tvTitle;
    public TextView tvCategory;
    public String kindKr;
    public TextView tvNick;
    public TextView tvGood;
    public MyViewHolder(View itemView) {



        super(itemView);

        tvCategory = itemView.findViewById(R.id.tvCategory);
        tvEmail = itemView.findViewById(R.id.tvEmail);
        ivUser = itemView.findViewById(R.id.ivUser);
        btnChat = itemView.findViewById(R.id.btnChat);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvTime = itemView.findViewById(R.id.tvTime);
        tvNick = itemView.findViewById(R.id.tvNick);
        tvGood = itemView.findViewById(R.id.tvGood);

    }
}

    // Provide a suitable constructor (depends on the kind of dataset)
    public AreaContentsAdapter(List<contentsTitle>  mContents, Context context, String kind) {
        this.mContents = mContents;
        this.stEmail = stEmail;
        this.context = context;
        this.kind = kind;
    }




    @Override
    public int getItemViewType(int position) {




        if(mContents.get(position).getKey().length() < 20){//콘텐츠 이름 들어올 때
            show_change = true;
            return 2; // viewType을 2로 설정
        }
        else{
            return 1;// viewType을 1로 설정
        }

    }



    // Create new views (invoked by the layout manager)
    @Override
    public AreaContentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_contents_drawer, parent, false);

        if(viewType == 1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_contents_drawer, parent, false);
        }
        else
        {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_show_change, parent, false);

        }



        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (show_change) {
            try {
                Log.e("카테고리 입력", String.valueOf(position) );
                holder.tvCategory.setText(mContents.get(position).getKey()); //***임시
            }catch(Exception e)
            {
                holder.btnChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { //***임시로 해둔건데 잘됨. 근데 왜 되는지 모르겠음..
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
            }
            show_change = false;
        }
        else {


            holder.btnChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //***임시로 해둔건데 잘됨. 근데 왜 되는지 모르겠음..
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
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mContents.size();
    }
}