package com.goodmorning.ui.fragment;
import android.os.Bundle;
import android.util.Log;
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

import com.alibaba.fastjson.JSON;
import com.baselib.sp.SharedPref;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.adapter.LanguageAdapter;
import com.goodmorning.utils.CheckUtils;
import com.goodmorning.utils.CloudConstants;
import com.goodmorning.view.LanguageDialog;
import com.google.android.material.tabs.TabLayout;
import com.taobao.luaview.util.JsonUtil;

import org.thanos.core.MorningDataAPI;
import org.thanos.core.ResultCallback;
import org.thanos.core.bean.ChannelList;
import org.thanos.core.internal.requestparam.ChannelListRequestParam;

import java.util.ArrayList;
import java.util.List;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager tabVpager;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private LanguageDialog languageDialog;
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
            tvTab.setText(getString(R.string.tab_greeting)+i);
            tab.setCustomView(view);
        }
        requestChannelList();
    }

    private void setListener(){
        if (languageDialog != null){
            languageDialog.setOnSwitchLanguage(new LanguageAdapter.OnSwitchLanguage() {
                @Override
                public void onLanguage(String languge) {
                    languageDialog.dismiss();
                    ((MainActivity)HomeFragment.this.getActivity()).changeLanguage(languge);
                }
            });
        }
    }

    private void requestChannelList(){
        long cacheTime = CloudConstants.getChannelCacheTimeInSeconds();
        MorningDataAPI.requestChannelList(getApplicationContext(), new ChannelListRequestParam(true, 0L), new ResultCallback<ChannelList>() {
            @Override
            public void onSuccess(ChannelList data) {
                if (data != null){
                    if (CheckUtils.isShowLanguage()){
                        String json = JSON.toJSONString(data.languageItems);
                        SharedPref.setString(getApplicationContext(),SharedPref.LANGUAGE_TYPE,json);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                languageDialog = new LanguageDialog(getActivity());
                                languageDialog.show();
                                setListener();
                            }
                        });

                    }
//                    data.languageItems
                }
            }

            @Override
            public void onLoadFromCache(ChannelList data) {

            }

            @Override
            public void onFail(Exception e) {

            }
        });
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
