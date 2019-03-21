package com.example.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import static com.example.sample.GlobalConstants.*;

public class LoginScreenFragment extends Fragment {

    EditText email_et, password_et;
    Button login_btn, sign_up_btn;
    String email_text, password_text;
    ILoginFragmentCommunicator loginFragmentCommunicator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email_et = view.findViewById(R.id.edit_text_email);
        password_et = view.findViewById(R.id.edit_text_password);
        login_btn = view.findViewById(R.id.button_login);
        sign_up_btn = view.findViewById(R.id.button_signup);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textExtractor();
                if (nullCheck()) {
                    if (validCheck()) {
                        loginFragmentCommunicator.sendLoginDetails(email_text, password_text);
                    } else {
                        Toast.makeText(v.getContext(), "Email or Password field not correct!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textExtractor();
                if (nullCheck()) {
                    if (validCheck()) {
                        loginFragmentCommunicator.sendRegistrationDetails(email_text, password_text);
                    } else {
                        Toast.makeText(v.getContext(), "Email or Password field not correct!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    /**
     * It extracts strings from edittexts of username and password.
     */
    private void textExtractor() {
        email_text = email_et.getText().toString();
        password_text = password_et.getText().toString();
        email_et.setText("");
        password_et.setText("");
    }

    /**
     * It checks for null value of strings
     * @return whether email_text and password_text contains null
     */
    private boolean nullCheck() {
        return email_text != null && password_text != null;
    }

    /**
     * It checks for blank value of strings & email validation.
     * @return whether strings are blank and email is valid or not.
     */
    private boolean validCheck() {
        if (!email_text.equals("") && !password_text.equals("")) {
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            return pattern.matcher(email_text).matches();
        }
        return false;
    }

    /**
     * It initializes this class's ILoginFragmentCommunicator's instance
     * with the "this" provided in other activities
     * @param loginFragmentCommunicator ILoginFragmentCommunicator's instance
     */
    public void setLoginFragmentCommunicator(ILoginFragmentCommunicator loginFragmentCommunicator) {
        this.loginFragmentCommunicator = loginFragmentCommunicator;
    }

    /**
     * used to communicate with other activities
     */
    interface ILoginFragmentCommunicator {
        void sendLoginDetails(String email, String password);
        void sendRegistrationDetails(String email, String password);
    }
}
