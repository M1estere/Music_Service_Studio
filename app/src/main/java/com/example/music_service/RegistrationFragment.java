package com.example.music_service;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextMail;
    private EditText editTextPassword;

    private Button registrationButton;

    private FirebaseFirestore firestore;
    private CollectionReference reference;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();

        editTextName = view.findViewById(R.id.name_edit_text);
        editTextUsername = view.findViewById(R.id.username_edit_text);
        editTextMail = view.findViewById(R.id.mail_edit_text);
        editTextPassword = view.findViewById(R.id.password_edit_text);

        registrationButton = view.findViewById(R.id.reg_button);

        registrationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name, userName, mail, password;
                name = editTextName.getText().toString();
                userName = editTextUsername.getText().toString();
                mail = editTextMail.getText().toString();
                password = editTextPassword.getText().toString();

                if (name.length() < 1 || name.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userName.length() < 1 || userName.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter userName", Toast.LENGTH_SHORT).show();
                    return;
                }

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

                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Registering...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(mail, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = authResult.getUser();
                                reference = firestore.collection(user.getUid());

                                Map<String, String> userData = new HashMap<>();
                                userData.put("name", name);
                                userData.put("username", userName);

                                progressDialog.setMessage("Saving user data...");

                                reference.add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Completed.",
                                                Toast.LENGTH_SHORT).show();

                                        openMainPage();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Unable to save.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
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