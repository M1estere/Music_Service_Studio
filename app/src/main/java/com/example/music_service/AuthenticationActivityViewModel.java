package com.example.music_service;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.music_service.models.globals.Globs;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationActivityViewModel implements TabLayoutMediator.TabConfigurationStrategy {

    private final String[] tabsTitles = new String[]{"Sign Up", "Sign In"};

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private AuthViewPageAdapter myViewPageAdapter;

    private Activity authActivity;

    private FirebaseAuth mAuth;

    public AuthenticationActivityViewModel(Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        authActivity = activity;
        if (Globs.recheckLogin) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null){
                openMainPage();
            }
        }

        tabLayout = authActivity.findViewById(R.id.nav_bar);
        viewPager2 = authActivity.findViewById(R.id.frame_content);
        myViewPageAdapter = new AuthViewPageAdapter((FragmentActivity) authActivity);
        viewPager2.setAdapter(myViewPageAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, this).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.printf("We are on %d\n", tab.getPosition());
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void openMainPage() {
        Globs.recheckLogin = false;

        Intent intent = new Intent(authActivity, MainActivity.class);
        authActivity.startActivity(intent);
    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(tabsTitles[position]);
    }
}
