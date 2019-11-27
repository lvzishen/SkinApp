package com.goodmorning.ui.fragment;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.baselib.cloud.CloudPropertyManager;
import com.baselib.sp.SharedPref;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.adapter.LanguageAdapter;
import com.goodmorning.bean.DataListItem;
import com.goodmorning.bean.DayPicture;
import com.goodmorning.manager.ContentManager;
import com.goodmorning.manager.ImageLoader;
import com.goodmorning.utils.CheckUtils;
import com.goodmorning.utils.CloudConstants;
import com.goodmorning.utils.CloudControlUtils;
import com.goodmorning.utils.HomeGreetingHelper;
import com.goodmorning.utils.ResUtils;
import com.goodmorning.utils.TextUtils;
import com.goodmorning.view.dialog.LanguageDialog;
import com.goodmorning.view.dialog.PicDialog;
import com.google.android.material.tabs.TabLayout;

import org.thanos.netcore.MorningDataAPI;
import org.thanos.netcore.ResultCallback;
import org.thanos.netcore.bean.ChannelList;
import org.thanos.netcore.helper.JsonHelper;
import org.thanos.netcore.internal.requestparam.ChannelListRequestParam;

import java.util.ArrayList;
import java.util.List;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager tabVpager;
    private TextView tvTitle;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private LanguageDialog languageDialog;
    private Activity mActivity;
    private AlphaAnimation mHideAnimation	= null;
    private AlphaAnimation mShowAnimation	= null;
    private Handler handler = new Handler();
    DayPicture dayPicture;
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
        tvTitle = view.findViewById(R.id.tv_title);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideTitleGreetings();
            }
        });
//        tvTitle.setText(getString(R.string.string_app_name));
    }

    private void hideTitleGreetings() {
        String text = HomeGreetingHelper.showText(getApplicationContext());
        if (android.text.TextUtils.isEmpty(text)) {
            tvTitle.setText(getString(R.string.string_app_name));
        } else {
            tvTitle.setText(text);
        }
        setHideAnimation(tvTitle, 2000, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvTitle.setText(getString(R.string.string_app_name));
                setShowAnimation(tvTitle, 1000, null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * View渐隐动画效果
     */
    private void setHideAnimation(View view, int duration, Animation.AnimationListener listener) {
        if (null == view || duration < 0) {
            return;
        }
        if (null != mHideAnimation) {
            mHideAnimation.cancel();
        }
        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        mHideAnimation.setAnimationListener(listener);
        view.startAnimation(mHideAnimation);
    }

    /**
     * View渐现动画效果
     */
    private void setShowAnimation(View view, int duration, Animation.AnimationListener listener) {
        if (null == view || duration < 0) {
            return;
        }
        if (null != mShowAnimation) {
            mShowAnimation.cancel();
        }
        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        mHideAnimation.setAnimationListener(listener);
        view.startAnimation(mShowAnimation);
    }


    private void initData(){
        mActivity = getActivity();
        String cloudData = CloudControlUtils.getCloudData(getApplicationContext(), CloudPropertyManager.PATH_EVERYDAY_PIC,"day_pic");
        JsonHelper<DayPicture> jsonHelper = new JsonHelper<DayPicture>() {
        };
        dayPicture = jsonHelper.getJsonObject(cloudData);
        tabLayout.setTabTextColors(ResUtils.getColor(R.color.color_9D9D9D),ResUtils.getColor(R.color.black));
        tabLayout.setSelectedTabIndicatorColor(ResUtils.getColor(R.color.black));
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 获取 tab 组件
                View view = tab.getCustomView();
                if (null != view && view instanceof LinearLayout) {
                    for (int i=0;i<((LinearLayout) view).getChildCount();i++){
                        View childView = ((LinearLayout) view).getChildAt(i);
                        if (null != childView && childView instanceof TextView){
                            // 改变 tab 选择状态下的字体大小
                            ((TextView) childView).setTextSize(14);
                            // 改变 tab 选择状态下的字体颜色
                            ((TextView) childView).setTextColor(ResUtils.getColor(R.color.black));
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null != view && view instanceof LinearLayout) {
                    for (int i=0;i<((LinearLayout) view).getChildCount();i++){
                        View childView = ((LinearLayout) view).getChildAt(i);
                        if (null != childView && childView instanceof TextView){
                            // 改变 tab 选择状态下的字体大小
                            ((TextView) childView).setTextSize(13);
                            // 改变 tab 选择状态下的字体颜色
                            ((TextView) childView).setTextColor(ResUtils.getColor(R.color.color_9D9D9D));
                        }
                    }

                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null != view && view instanceof LinearLayout) {
                    for (int i=0;i<((LinearLayout) view).getChildCount();i++){
                        View childView = ((LinearLayout) view).getChildAt(i);
                        if (null != childView && childView instanceof TextView){
                            // 改变 tab 选择状态下的字体大小
                            ((TextView) childView).setTextSize(14);
                            // 改变 tab 选择状态下的字体颜色
                            ((TextView) childView).setTextColor(ResUtils.getColor(R.color.black));
                        }
                    }
                }
            }
        });
        requestChannelList();
    }

    /**
     * 添加数据
     */
    private void addData(){
        if (mActivity == null){
            return;
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChannelList.LangCategoryInfo langCategoryInfo = ContentManager.getInstance().getChannelContent();
                if (langCategoryInfo == null){
                    return;
                }
                ArrayList<ChannelList.Category> categories = langCategoryInfo.categoryList;
                for (int i=0;i<categories.size();i++){
                    TabFragment tabFragment = new TabFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt(MainActivity.CONTENT, categories.get(i).id);
                    tabFragment.setArguments(bundle1);
                    mFragmentList.add(tabFragment);
                }
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                tabVpager.setAdapter(new TabAdapter(getChildFragmentManager()));
                tabLayout.setupWithViewPager(tabVpager);
                for (int i=0;i<categories.size();i++){
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tab,null);
                    TextView tvTab = view.findViewById(R.id.tv_item);
                    ImageView ivTab = view.findViewById(R.id.iv_tab);
                    String text = categories.get(i).text;
                    String[] txts = TextUtils.channelText(text);
                    tvTab.setText(txts[0]);
                    if (txts.length > 1){
                        ImageLoader.displayImageByName(getContext(),txts[1],R.drawable.ic_launcher,R.drawable.ic_launcher,ivTab);
                    }
                    tab.setCustomView(view);
                }
                tabLayout.getTabAt(0).select();

            }
        });

    }

    private void setListener(){
        if (languageDialog != null){
            languageDialog.setOnSwitchLanguage(new LanguageAdapter.OnSwitchLanguage() {
                @Override
                public void onLanguage(String languge) {
                    //切换语言重新请求接口
                    languageDialog.dismiss();
                    if (mActivity == null){
                        return;
                    }
                    ((MainActivity)mActivity).changeLanguage(languge);
                }
            });
        }
    }

    private void requestChannelList(){
        long cacheTime = CloudConstants.getChannelCacheTimeInSeconds();
        MorningDataAPI.requestChannelList(getApplicationContext(), new ChannelListRequestParam(false, 0L), new ResultCallback<ChannelList>() {
            @Override
            public void onSuccess(ChannelList data) {
                if (mActivity == null){
                    return;
                }
                if (data != null){
                    if (CheckUtils.isShowLanguage()){
                        String json = JSON.toJSONString(data.languageItems);
                        SharedPref.setString(getApplicationContext(),SharedPref.LANGUAGE_TYPE,json);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                languageDialog = new LanguageDialog(mActivity);
                                languageDialog.show();
                                setListener();
                            }
                        });
                        checkShowDayPic();
                    }else {
                        checkShowDayPic();
                    }
                    if (data.langCategoryInfos != null){
                        String json = JSON.toJSONString(data.langCategoryInfos);
                        SharedPref.setString(getApplicationContext(),SharedPref.CHANNEL_CONTENT,json);
                    }
                }
                addData();
            }

            @Override
            public void onLoadFromCache(ChannelList data) {

            }

            @Override
            public void onFail(Exception e) {
                addData();
                checkShowDayPic();
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

    @Override
    public void onStop() {
        super.onStop();
        if (languageDialog != null && languageDialog.isShowing()){
            languageDialog.dismiss();
        }
    }

    /**
     * 检查显示每日一图
     */
    private void checkShowDayPic(){
        if (dayPicture == null){
            return;
        }
        if (CheckUtils.isShowPic(dayPicture.getStartTime(),dayPicture.getEndTime())){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((languageDialog != null && !languageDialog.isShowing()) || languageDialog == null){
                        //显示每日一图,语言列表优先级高，当前正在显示语言列表，轮训检查是否关闭，关闭后展示每日一图
                        handler.removeCallbacks(this);
                        if (mActivity == null){
                            return;
                        }
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PicDialog picDialog = new PicDialog(mActivity);
                                DataListItem dataListItem = new DataListItem();
                                dataListItem.setType(DataListItem.DATA_TYPE_2);
                                dataListItem.setPicUrl(dayPicture.getPicUrl());
                                picDialog.setDataListItem(dataListItem);
                                picDialog.show();
                            }
                        });
                    }else {
                        handler.postDelayed(this,1000);
                    }
                }
            },1000);
        }
    }
}
