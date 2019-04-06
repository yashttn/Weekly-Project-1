package com.example.sample.add_user_dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sample.R;

public class AddUserDialogFragment extends DialogFragment {

    EditText addUserNameET, addUserJobET;
    Button addUserOkBtn, addUserCancelBtn;
    IAddUserDetails addUserDetails;
    String userName, userJob;
    int sent_by;

    public void setAddUserDetails(IAddUserDetails addUserDetails) {
        this.addUserDetails = addUserDetails;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            sent_by = bundle.getInt("sent_by");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_add_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addUserNameET = view.findViewById(R.id.add_user_name_et);
        addUserJobET = view.findViewById(R.id.add_user_job_et);
        addUserOkBtn = view.findViewById(R.id.add_user_ok_btn);
        addUserCancelBtn = view.findViewById(R.id.add_user_cancel_btn);

        addUserOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nullCheck()) {
                    if (addUserDetails != null) {
                        addUserDetails.onAddUserDetailsReceived(userName, userJob, sent_by);
                    }
                    dismiss();
                } else {
                    Toast.makeText(v.getContext(), "Name or Job Field is incorrect! ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addUserCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private boolean nullCheck() {
        userName = addUserNameET.getText().toString();
        userJob = addUserJobET.getText().toString();

        return !userName.equals("") && !userJob.equals("");
    }
}
