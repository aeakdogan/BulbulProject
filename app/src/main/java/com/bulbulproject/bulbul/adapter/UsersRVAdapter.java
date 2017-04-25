package com.bulbulproject.bulbul.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.model.BulbulUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by fatih on 21/04/2017.
 */

public class UsersRVAdapter extends RecyclerView.Adapter<UsersRVAdapter.ListHolder> {
    private List<BulbulUser> users;
    private Context context;

    public UsersRVAdapter(List<BulbulUser> users, Context context) {
        this.users = users;
        this.context = context;
    }

    public static class ListHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public TextView userName;
        public ImageView userPhoto;

        public ListHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ll);
            userName = (TextView) linearLayout.findViewById(R.id.name);
            userPhoto = (ImageView) linearLayout.findViewById(R.id.profile_picture);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public UsersRVAdapter.ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_follower, parent, false);
        UsersRVAdapter.ListHolder llh = new UsersRVAdapter.ListHolder(ll);
        return llh;
    }

    @Override
    public void onBindViewHolder(UsersRVAdapter.ListHolder holder, final int position) {
        final BulbulUser tmpUser = users.get(position);
        holder.userName.setText(tmpUser.getUsername());
        if (tmpUser.getProfilePhoto() != null && tmpUser.getProfilePhoto().length() > 0) {
            Picasso.with(context).load(tmpUser.getProfilePhoto()).into(holder.userPhoto);
        }
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context.getApplicationContext(), UserActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("id", tmpUser.getId());
//                context.getApplicationContext().startActivity(intent);
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return users.size();
    }
}
