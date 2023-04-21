package com.example.music_service.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.music_service.R;
import com.example.music_service.viewModels.QueueActivityViewModel;

public class QueueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue_activity);

        com.example.music_service.databinding.QueueActivityBinding queueActivityBinding = DataBindingUtil.setContentView(this, R.layout.queue_activity);
        QueueActivityViewModel queueActivityViewModel = new QueueActivityViewModel(this);

        queueActivityBinding.setViewModel(queueActivityViewModel);
    }
}
