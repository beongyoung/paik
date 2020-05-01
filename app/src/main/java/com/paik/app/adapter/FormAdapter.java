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

import com.paik.app.F_UnpaidWages;
import com.paik.app.R;
import com.paik.app.type_class.Contents;
import com.paik.app.type_class.Form;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.MyViewHolder> {

    private List<Form> mForm;
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
    public FormAdapter(List<Form>  mForm, Context context) {
        this.mForm = mForm;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FormAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_office_category, parent, false);

        FormAdapter.MyViewHolder vh = new FormAdapter.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FormAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.btnWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String stFriendId = mFriend.get(position).getKey();


                Intent in = new Intent(context, F_UnpaidWages.class);

                Log.d("myLog", mForm.get(position).formName);

                if(mForm.get(position).formName.equals("Unpaid Wages")) {

                    context.startActivity(in);


                }


            }
    });

        holder.tvContents.setText(mForm.get(position).formName);
        Picasso.get().load(mForm.get(position).imgUrl).fit().centerInside().into(holder.ivContents);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mForm.size();
    }
}