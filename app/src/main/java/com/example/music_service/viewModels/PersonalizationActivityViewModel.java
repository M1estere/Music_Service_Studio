package com.example.music_service.viewModels;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.BaseObservable;

import com.example.music_service.PersonalizationActivity;
import com.example.music_service.R;
import com.example.music_service.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class PersonalizationActivityViewModel extends BaseObservable {

    private Activity activity;

    private ImageView image;
    private ImageView cameraTemp;

    private EditText nameEditText;
    private EditText nicknameEditText;
    private boolean changedImage = false;

    public PersonalizationActivityViewModel(Activity act) {
        activity = act;

        image = activity.findViewById(R.id.user_image);
        cameraTemp = activity.findViewById(R.id.camera);

        nameEditText = activity.findViewById(R.id.name_edit_text);
        nicknameEditText = activity.findViewById(R.id.nickname_edit_text);

        nameEditText.setText(User.getName());
        nicknameEditText.setText(User.getUserName());

        image.setImageBitmap(User.getBitmap());
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PersonalizationActivity.PICK_IMAGE);
    }

    public void setNewProfileImage(Bitmap bitmap) {
        changedImage = true;
        cameraTemp.setVisibility(View.GONE);

        image.setImageBitmap(RoundedBitmapDrawableFactory.create(activity.getResources(), bitmap).getBitmap());
    }

    public void setStartProfileImage(Bitmap bitmap) {
        cameraTemp.setVisibility(View.GONE);

        image.setImageBitmap(RoundedBitmapDrawableFactory.create(activity.getResources(), bitmap).getBitmap());
    }

    public void cancel() {
        activity.onBackPressed();
    }

    public void save() {
        String name = getNewName();
        String nickname = getNewUsername();
        if (name.length() < 1 || nickname.length() < 1) return;

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("username", nickname);

        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Saving data...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        CollectionReference collectionReference = firestore.collection(user.getUid());
        ;
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentReference documentReference = collectionReference.document("personal_info");
                    documentReference.update(userData);
                }
            }
        });
        User.setName(name);
        User.setUserName(nickname);

        if (!changedImage) {
            progressDialog.dismiss();
            activity.onBackPressed();
            return;
        }
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imagesReference = storageReference.child("users_images/" + userId + ".jpg");

        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        User.setBitmap(bitmap);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesReference.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(activity, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                activity.onBackPressed();
            }
        });
    }

    public String getNewName() {
        if (nameEditText.getText().length() < 1) {
            Toast.makeText(activity, "Enter a name", Toast.LENGTH_SHORT).show();
            return "";
        }

        return nameEditText.getText().toString();
    }

    public String getNewUsername() {
        if (nicknameEditText.getText().length() < 1) {
            Toast.makeText(activity, "Enter a nickname", Toast.LENGTH_SHORT).show();
            return "";
        }

        return nicknameEditText.getText().toString();
    }
}
