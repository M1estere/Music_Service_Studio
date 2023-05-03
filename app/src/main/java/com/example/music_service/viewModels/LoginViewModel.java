package com.example.music_service.viewModels;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel {

    private FirebaseAuth mAuth;

    private View globView;

    private EditText editTextMail;
    private EditText editTextPassword;

    public LoginViewModel(View v) {
        mAuth = FirebaseAuth.getInstance();
        globView = v;

        editTextMail = globView.findViewById(R.id.mail_edit_text);
        editTextPassword = globView.findViewById(R.id.password_edit_text);

        Button registrationButton = globView.findViewById(R.id.log_button);

        registrationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String mail, password;
        mail = editTextMail.getText().toString();
        password = editTextPassword.getText().toString();

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

        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(globView.getContext(), "Done", Toast.LENGTH_SHORT).show();
                            openMainPage();
                        } else {
                            Toast.makeText(globView.getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
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
