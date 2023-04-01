package com.example.music_service;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.music_service.databinding.QueueActivityBinding;

public class QueueActivity extends AppCompatActivity {

    private QueueActivityBinding queueActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue_activity);

        queueActivityBinding = DataBindingUtil.setContentView(this, R.layout.queue_activity);
        QueueActivityViewModel queueActivityViewModel = new QueueActivityViewModel(this);

        queueActivityBinding.setViewModel(queueActivityViewModel);
    }
}
