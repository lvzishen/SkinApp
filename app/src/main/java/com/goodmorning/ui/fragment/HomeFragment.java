package com.goodmorning.ui.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.cleanerapp.supermanager.R;
import com.goodmorning.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager tabVpager;
    private List<Fragment> mFragmentList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view){
        tabLayout = view.findViewById(R.id.tablayout);
        tabVpager = view.findViewById(R.id.tab_viewpager);
    }

    private void initData(){
        for (int i=0;i<10;i++){
            TabFrament tabFrament = new TabFrament();
            Bundle bundle1 = new Bundle();
            bundle1.putString(MainActivity.CONTENT, getString(R.string.tab_greeting)+i);
            tabFrament.setArguments(bundle1);
            mFragmentList.add(tabFrament);
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabVpager.setAdapter(new TabAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(tabVpager);

        for (int i=0;i<10;i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tab,null);
            TextView tvTab = (TextView) view.findViewById(R.id.tv_item);
            tvTab.setText("祝福"+i);
            tab.setCustomView(view);
        }
    }

    class TabAdapter extends FragmentStatePagerAdapter{

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}