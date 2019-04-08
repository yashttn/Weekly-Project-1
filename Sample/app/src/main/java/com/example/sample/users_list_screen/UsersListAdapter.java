package com.example.sample.users_list_screen;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.sample.models.UsersModel;
import com.example.sample.R;

import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UsersListViewHolder> {

    private List<UsersModel> usersModelList;
    private IUserDetailsListener userDetailsListener;

    @NonNull
    @Override
    public UsersListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new UsersListViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowitem_users_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersListViewHolder usersListViewHolder, final int position) {
        final UsersModel usersModel = usersModelList.get(position);
        usersListViewHolder.user_id_tv.setText(String.valueOf(usersModel.getId()));
        usersListViewHolder.first_name_tv.setText(usersModel.getFirstName());
        usersListViewHolder.last_name_tv.setText(usersModel.getLastName());
        Glide.with(usersListViewHolder.itemView.getContext())
                .load(usersModel.getAvatar()).into(usersListViewHolder.profile_image_iv);

        Glide.with(usersListViewHolder.itemView.getContext())
                .asBitmap()
                .load(usersModel.getAvatar())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        usersListViewHolder.profile_image_iv.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        usersListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetailsListener != null) {
                    userDetailsListener.userDetailsClicked(usersModel);
                }
            }
        });

        usersListViewHolder.delete_user_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetailsListener != null) {
                    userDetailsListener.deleteUser(usersModel.getId());
                    usersModelList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        usersListViewHolder.share_image_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetailsListener != null) {
                    userDetailsListener.shareUser(usersModel);
                }
            }
        });
    }

    void setUsersModelList(List<UsersModel> usersModelList) {
        this.usersModelList = usersModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return usersModelList.size();
    }

    public void setUserDetailsListener(IUserDetailsListener userDetailsListener) {
        this.userDetailsListener = userDetailsListener;
    }

    class UsersListViewHolder extends RecyclerView.ViewHolder {

        TextView user_id_tv, first_name_tv, last_name_tv;
        ImageView delete_user_iv, share_image_iv, profile_image_iv;

        UsersListViewHolder(@NonNull View itemView) {
            super(itemView);

            user_id_tv = itemView.findViewById(R.id.user_id_tv);
            first_name_tv = itemView.findViewById(R.id.first_name_tv);
            last_name_tv = itemView.findViewById(R.id.last_name_tv);
            profile_image_iv = itemView.findViewById(R.id.profile_image_iv);
            delete_user_iv = itemView.findViewById(R.id.delete_icon);
            share_image_iv = itemView.findViewById(R.id.share_icon);
        }
    }
}
