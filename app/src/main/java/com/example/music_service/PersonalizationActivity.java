package com.example.music_service;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.music_service.databinding.ActivityPersonalizationBinding;
import com.example.music_service.viewModels.PersonalizationActivityViewModel;

import java.io.IOException;
import java.io.InputStream;

public class PersonalizationActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;

    private PersonalizationActivityViewModel personalizationActivityViewModel;

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        ActivityPersonalizationBinding activityPersonalizationBinding = DataBindingUtil.setContentView(this, R.layout.activity_personalization);
        personalizationActivityViewModel = new PersonalizationActivityViewModel(this);

        activityPersonalizationBinding.setViewModel(personalizationActivityViewModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            System.out.printf("Error with result code\n");
            return;
        }
        if (requestCode == PICK_IMAGE) {
            if (data == null) {
                System.out.printf("Error with no data\n");
                //Display an error
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                personalizationActivityViewModel.setNewProfileImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}