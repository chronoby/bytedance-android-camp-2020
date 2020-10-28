package com.example.chapter3.homework;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Ch3Ex3Activity extends AppCompatActivity {
    private static final int PAGE_COUNT = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch3ex3);

        ViewPager pager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return new com.example.chapter3.homework.PlaceholderFragment();
            }
            @Override
            public int getCount() {
                return PAGE_COUNT;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return "Friend " + position;
            }
        });
        tabLayout.setupWithViewPager(pager);
    }
}