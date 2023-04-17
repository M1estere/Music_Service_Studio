package com.example.music_service.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.music_service.R;
import com.example.music_service.databinding.AccountActivityBinding;
import com.example.music_service.viewModels.AccountActivityViewModel;

public class AccountActivity extends AppCompatActivity {

    private AccountActivityBinding accountActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        accountActivityBinding = DataBindingUtil.setContentView(this, R.layout.account_activity);
        AccountActivityViewModel activityViewModel = new AccountActivityViewModel(this);

        accountActivityBinding.setViewModel(activityViewModel);
    }
}