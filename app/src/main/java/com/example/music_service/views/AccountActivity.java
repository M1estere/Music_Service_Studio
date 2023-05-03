package com.example.music_service.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.music_service.R;
import com.example.music_service.databinding.AccountActivityBinding;
import com.example.music_service.viewModels.AccountActivityViewModel;

public class AccountActivity extends AppCompatActivity {

    private AccountActivityViewModel activityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        AccountActivityBinding accountActivityBinding = DataBindingUtil.setContentView(this, R.layout.account_activity);
        activityViewModel = new AccountActivityViewModel(this);

        accountActivityBinding.setViewModel(activityViewModel);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.printf("Restarted\n");
        activityViewModel = new AccountActivityViewModel(this);
    }
}