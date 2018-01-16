package com.example.muskan.medical_help;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.muskan.medical_help.Fragments.TimelineFragment;
import com.example.muskan.medical_help.Fragments.RemindersFragment;
import com.example.muskan.medical_help.Helpers.RemindersPagerAdapter;


public class RemindersMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reminder);
        initToolbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        RemindersPagerAdapter adapter = new RemindersPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TimelineFragment(), "Timeline");
        adapter.addFragment(new RemindersFragment(), "Reminders");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_remind);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }
}
