package com.example.music_service.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.music_service.R;
import com.example.music_service.views.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText editTextMail;
    private EditText editTextPassword;

    public LoginFragment() {

    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextMail = view.findViewById(R.id.mail_edit_text);
        editTextPassword = view.findViewById(R.id.password_edit_text);

        Button registrationButton = view.findViewById(R.id.log_button);

        //TO_DO: REMOVE THIS OUT OF HERE
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail, password;
                mail = editTextMail.getText().toString();
                password = editTextPassword.getText().toString();

                if (mail.length() < 1 || mail.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter mail", Toast.LENGTH_SHORT).show();
                    clearFields();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    Toast.makeText(getActivity(), "Provide a valid e-mail", Toast.LENGTH_SHORT).show();
                    clearFields();
                    return;
                }

                if (password.length() < 8 || password.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter an 8-character password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
                                    openMainPage();
                                } else {
                                    Toast.makeText(getActivity(), "Authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void openMainPage() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private void clearFields() {
        editTextMail.setText("");
        editTextPassword.setText("");
    }
}