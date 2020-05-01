package com.paik.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.paik.app.R;
import com.paik.app.post.show_post;
import com.paik.app.type_class.Guest;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<Guest> mGuest;
    Context context;
    private StorageReference mStorageRef;


    private Bitmap GetImageFromURL(String strImageURL) {
        Bitmap imgBitmap = null;

        try {
            URL url = new URL(strImageURL);
            URLConnection conn = url.openConnection();
            conn.connect();
            int nSize = conn.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);

            bis.close();
        } catch (Exception e) {
            Log.e("myLog", "url을 비트맵으로 변환 실패");
            e.printStackTrace();
            imgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_image);
        }
        return imgBitmap;
    }



    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView tvArea;
        public TextView tvTitle;
        public TextView tvNick;
        public TextView tvTime;

        public ImageView ivContents;
        public Button btnShow;
        public MyViewHolder(View itemView) {



            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime); // 시간ㅇ
            tvArea = itemView.findViewById(R.id.tvArea); //카테고리ㅇ
            tvTitle = itemView.findViewById(R.id.tvTitle); //타이틀ㅇ
            ivContents = itemView.findViewById(R.id.ivUser); //사진ㅇ
            btnShow = itemView.findViewById(R.id.btnChat); //버튼o
            tvNick = itemView.findViewById(R.id.tvNick); //닉네임ㅇ
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PostAdapter(List<Guest>  mGuest, Context context) {
        this.mGuest = mGuest;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_contents_drawer, parent, false);

        PostAdapter.MyViewHolder vh = new PostAdapter.MyViewHolder(v);
        return vh;
    }

    public static Bitmap setRoundCorner(Bitmap bitmap, int pixel) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, pixel, pixel, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final PostAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.tvTitle.setText(mGuest.get(position).contents);//제목
        holder.tvTime.setText(mGuest.get(position).nick + " | " + mGuest.get(position).area + " | " +  mGuest.get(position).time); // 시간
        holder.tvNick.setText(mGuest.get(position).selfIntro); //닉네임
        holder.tvArea.setText(mGuest.get(position).area);//카테고리


        //Drawable drawable = context.getResources().getDrawable(R.drawable.ic_person_black_24dp);

// drawable 타입을 bitmap으로 변경


        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_image);

        //bitmap = GetImageFromURL("https://yt3.ggpht.com/a/AGF-l7_cK8PJQgLUBqtnXdqJfqBvoAP6P_4pM3Jsfw=s288-c-k-c0xffffffff-no-rj-mo");
        try {
            //bitmap = ((BitmapDrawable) holder.ivContents.getDrawable()).getBitmap();
        }
        catch(Exception e)
        {
            Log.d("myLog", "이미지 없음");
        }
        //Bitmap Rbitmap = setRoundCorner(bitmap, 150);
        holder.ivContents.setImageBitmap(bitmap);

        //holder.tvNick.setText(mGuest.get(position).nick);
        holder.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, show_post.class);


                in.putExtra("nick", mGuest.get(position).nick);
                in.putExtra("email", mGuest.get(position).email);
                in.putExtra("time", mGuest.get(position).time);
                in.putExtra("contents",  mGuest.get(position).selfIntro);
                in.putExtra("area",  mGuest.get(position).area);
                in.putExtra("title",  mGuest.get(position).contents);


                context.startActivity(in);
            }
        });
        //holder.tvContents.setText(mContents.get(position).getText());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = mStorageRef.child("users/profile/"+ mGuest.get(position).email + ".jpg");

        //Url을 다운받기
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                Picasso.get().load(uri.toString()).fit().centerInside().into(holder.ivContents);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mGuest.size();
    }
}







