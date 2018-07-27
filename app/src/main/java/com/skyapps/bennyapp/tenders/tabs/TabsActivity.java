package com.skyapps.bennyapp.tenders.tabs;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.tenders.TendersActivity;
import com.skyapps.bennyapp.tenders.privateTenders;
import com.skyapps.bennyapp.tenders.publicTenders;

import java.util.ArrayList;
import java.util.List;
//
public class TabsActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    static ViewPager viewPager;

    static String finall;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        finall = getIntent().getStringExtra("Final");

        getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().putString("activity" , "tabs").commit();


        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Log.e("Testxp" , getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).getString("company" , ""));


    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DetailsTab(), "פרטי דרישה");
        adapter.addFragment(new MarketTab(), "פרטים לתמחור");
        adapter.addFragment(new ChatFragment(), "הודעות");
        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();



        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().putString("activity" , "").commit();
    }
}
