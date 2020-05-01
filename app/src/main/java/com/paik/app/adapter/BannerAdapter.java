package com.paik.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.paik.app.R;
import com.paik.app.type_class.Contents;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.MyViewHolder> {

    private List<Contents> mContents;
    private boolean myTalk;
    String stEmail;
    Context context;


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvContents;
        public ImageView ivContents;
        public Button btnWebView;
        public MyViewHolder(View itemView) {



            super(itemView);

            tvContents = itemView.findViewById(R.id.tvContents);
            ivContents = itemView.findViewById(R.id.ivContents);
            btnWebView = itemView.findViewById(R.id.btnWebView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BannerAdapter(List<Contents>  mContents, Context context) {
        this.mContents = mContents;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BannerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_contents, parent, false);

        BannerAdapter.MyViewHolder vh = new BannerAdapter.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(BannerAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.btnWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String stFriendId = mFriend.get(position).getKey();

            }
        });

        holder.tvContents.setText(mContents.get(position).getText());
        Picasso.get().load(mContents.get(position).getPhoto()).fit().centerInside().into(holder.ivContents);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mContents.size();
    }
}
