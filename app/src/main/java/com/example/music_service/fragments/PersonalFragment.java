package com.example.music_service.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.music_service.R;
import com.example.music_service.adapters.viewPages.PersonalViewPageAdapter;
import com.example.music_service.models.User;
import com.example.music_service.views.AccountActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PersonalFragment extends Fragment implements TabLayoutMediator.TabConfigurationStrategy {

    private final String[] tabsTitles = new String[]{"PLAYLISTS", "SONGS"};
    private ViewPager2 viewPager2;
    private ImageView image;
    private TextView name;

    public PersonalFragment() {

    }

    @NonNull
    public static PersonalFragment newInstance(String param1, String param2) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.navigation_bar);
        viewPager2 = view.findViewById(R.id.frame_content);
        PersonalViewPageAdapter myViewPageAdapter = new PersonalViewPageAdapter(getActivity());
        viewPager2.setAdapter(myViewPageAdapter);

        image = getActivity().findViewById(R.id.user_image);
        image.setImageBitmap(User.getBitmap());

        name = getActivity().findViewById(R.id.name_txt);
        name.setText(User.getUserName());

        CardView accountButton = view.findViewById(R.id.account_button);

        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
            }
        });

        new TabLayoutMediator(tabLayout, viewPager2, this).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(tabsTitles[position]);

        if (position == 0)
            tab.view.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        else if (position == tabsTitles.length - 1)
            tab.view.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        else
            tab.view.setGravity(Gravity.CENTER);
    }

    @Override
    public void onResume() {
        super.onResume();

        image.setImageBitmap(User.getBitmap());
        name.setText(User.getUserName());
    }
}