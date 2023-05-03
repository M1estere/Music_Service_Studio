package com.example.music_service.viewModels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.music_service.R;
import com.example.music_service.views.TracksLoadActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegistrationViewModel {

    private FirebaseAuth mAuth;

    private View globView;

    private FirebaseFirestore firestore;
    private CollectionReference reference;

    private EditText editTextMail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextUsername;

    public RegistrationViewModel(View v) {
        mAuth = FirebaseAuth.getInstance();
        globView = v;

        firestore = FirebaseFirestore.getInstance();

        editTextName = globView.findViewById(R.id.name_edit_text);
        editTextUsername = globView.findViewById(R.id.username_edit_text);

        editTextMail = globView.findViewById(R.id.mail_edit_text);
        editTextPassword = globView.findViewById(R.id.password_edit_text);

        Button registrationButton = globView.findViewById(R.id.reg_button);

        registrationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    public void register() {
        String name, userName, mail, password;
        name = editTextName.getText().toString();
        userName = editTextUsername.getText().toString();
        mail = editTextMail.getText().toString();
        password = editTextPassword.getText().toString();

        if (name.length() < 1 || name.isEmpty()) {
            Toast.makeText(globView.getContext(), "Enter name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userName.length() < 1 || userName.isEmpty()) {
            Toast.makeText(globView.getContext(), "Enter userName", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mail.length() < 1 || mail.isEmpty()) {
            Toast.makeText(globView.getContext(), "Enter mail", Toast.LENGTH_SHORT).show();
            clearFields();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(globView.getContext(), "Provide a valid e-mail", Toast.LENGTH_SHORT).show();
            clearFields();
            return;
        }

        if (password.length() < 8 || password.isEmpty()) {
            Toast.makeText(globView.getContext(), "Enter an 8-character password", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(globView.getContext());
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

                        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentReference documentReference = reference.document("personal_info");
                                    documentReference.set(userData);
                                }
                            }
                        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                progressDialog.dismiss();
                                Toast.makeText(globView.getContext(), "Completed.",
                                        Toast.LENGTH_SHORT).show();

                                openMainPage();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(globView.getContext(), "Unable to save.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(globView.getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openMainPage() {
        Intent intent = new Intent(globView.getContext(), TracksLoadActivity.class);
        globView.getContext().startActivity(intent);
    }

    private void clearFields() {
        editTextMail.setText("");
        editTextPassword.setText("");
    }

}
