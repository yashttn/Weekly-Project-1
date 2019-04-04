package com.example.sample.user_details_screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sample.R;
import com.example.sample.models.UsersModel;

public class UserDetailsScreenFragment extends Fragment {

    ImageView detailsProfileImageTV;
    TextView detailsUserIdTV, detailsFirstNameTV, detailsLastNameTV;
    Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailsProfileImageTV = view.findViewById(R.id.details_profile_image_iv);
        detailsUserIdTV = view.findViewById(R.id.details_user_id_tv);
        detailsFirstNameTV = view.findViewById(R.id.details_first_name_tv);
        detailsLastNameTV = view.findViewById(R.id.details_last_name_tv);

        if (bundle != null) {
            UsersModel usersModel = bundle.getParcelable("users_model");
            detailsUserIdTV.setText("" + usersModel.getId());
            detailsFirstNameTV.setText(usersModel.getFirstName());
            detailsLastNameTV.setText(usersModel.getLastName());
            Glide.with(view.getContext())
                    .load(usersModel.getAvatar())
                    .into(detailsProfileImageTV);
        }
    }

}
