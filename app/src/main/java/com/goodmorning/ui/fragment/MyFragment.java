package com.goodmorning.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.adapter.LanguageAdapter;
import com.goodmorning.manager.ContentManager;
import com.goodmorning.ui.activity.MyCollectActivity;
import com.goodmorning.ui.activity.SettingActivity;
import com.goodmorning.utils.ActivityCtrl;
import com.goodmorning.utils.AppUtils;
import com.goodmorning.view.dialog.LanguageDialog;
import com.nox.Nox;

import org.n.account.core.api.NjordAccountManager;
import org.n.account.core.model.Account;
import org.n.account.core.ui.GlideCircleTransform;
import org.n.account.ui.view.AccountUIHelper;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class MyFragment extends Fragment implements View.OnClickListener {
    private TextView tvMyLang;
    private ImageView mAccountHeaderImg, ivMyUpdate, ivMyTip;
    private TextView mAccountHeaderText, mVersion;
    private View mUser;
    private RelativeLayout mUpdate, llLang, llSettings;
    private LinearLayout llCollect;
    private LanguageDialog languageDialog;
    private Activity mActivity;
    private boolean isLogin;
    private Account account;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        initViews(view);
        initData();
        return view;
    }

    private void initViews(View view) {
        mAccountHeaderImg = view.findViewById(R.id.account_img);
        mAccountHeaderText = view.findViewById(R.id.account_info);
        mUser = view.findViewById(R.id.ll_user);
        mUpdate = view.findViewById(R.id.ll_check_and_update);
        mVersion = view.findViewById(R.id.tv_version);
        mUser.setOnClickListener(this);
        mUpdate.setOnClickListener(this);

        llCollect = view.findViewById(R.id.ll_my_collect);
        llLang = view.findViewById(R.id.ll_my_lang);
        llSettings = view.findViewById(R.id.ll_my_set);
        tvMyLang = view.findViewById(R.id.tv_my_lang);
        ivMyUpdate = view.findViewById(R.id.iv_my_update);
        ivMyTip = view.findViewById(R.id.iv_my_tip);
        llCollect.setOnClickListener(this);
        llLang.setOnClickListener(this);
        llSettings.setOnClickListener(this);

        mVersion.setText("V" + getString(R.string.app_version));
//        if (NjordAccountManager.isLogined(getContext())) {
//        } else {
//            AccountUIHelper.startLogin(getActivity());
//        }
        account = NjordAccountManager.getCurrentAccount(getApplicationContext());
        showAccountInfo(account);

//        Account account = NjordAccountManager.getCurrentAccount(getActivity());
//        if (null != account) {
//            String psu = NjordIdHelper.getPSU(account);
//            String key = NjordIdHelper.getKey(account);
//            String random = NjordIdHelper.getRandom(account);
//            try {
//                String session = SessionHelper.composeCookieWithSession(
//                        getActivity(),
//                        key,
//                        psu,
//                        random,
//                        "asd".getBytes());
//                Log.i("BAASAS", "session=" + session);
//                String str[] = session.split(";");
//                String psu1 = str[0].substring(4);
//                Log.i("BAASAS", "psu1=" + psu1);
//                String pmc = str[1].substring(4);
//                Log.i("BAASAS", "pmc=" + pmc);
//            } catch (AuthFailureError ex) {
//            }
//        }
//        NetClientFactory.provideClient(getContext()).newAssembler()
//                .url(NetParamsProvider.Url.TASK_LIST(mContext))
//                .method(HttpMethod.POST)
//                .requestBody(NetParamsProvider.Request.buildTaskList(mContext))
//                .addNetStrategy(new HeaderStrategy(getContext()))
//                .addNetStrategy(new TaskNetStrategy(mContext))
//                .parser(new TaskParser(mContext))
//                .callback(callback)
//                .build().execute();
    }

    private void initData() {
        mActivity = getActivity();
        if (Nox.canUpdate(getApplicationContext())) {
            ivMyTip.setVisibility(View.VISIBLE);
            ivMyUpdate.setVisibility(View.VISIBLE);
            mUpdate.setClickable(true);
            mUpdate.setEnabled(true);
        } else {
            ivMyTip.setVisibility(View.GONE);
            ivMyUpdate.setVisibility(View.GONE);
            mUpdate.setClickable(false);
            mUpdate.setEnabled(false);
        }
        tvMyLang.setText(ContentManager.getInstance().getLang());
    }

    private void setListener() {
        languageDialog.setOnSwitchLanguage(new LanguageAdapter.OnSwitchLanguage() {
            @Override
            public void onLanguage(String languge) {
                languageDialog.dismiss();
                if (mActivity == null) {
                    return;
                }
                ((MainActivity) mActivity).changeLanguage(languge);
            }
        });
    }

    private void showAccountInfo(Account account) {
        if (account != null) {
            Glide.with(getApplicationContext()).load(account.mPictureUrl)
                    .placeholder(R.drawable.ic_account_header)
                    .bitmapTransform(new GlideCircleTransform(getApplicationContext()))
                    .into(mAccountHeaderImg);
            mAccountHeaderText.setText(account.mNickName);
            isLogin = true;
        } else {
//            mAccountNameTv.setText(R.string.sign_in_to);
            Glide.with(getApplicationContext()).load(R.drawable.ic_account_header)
                    .bitmapTransform(new GlideCircleTransform(getApplicationContext()))
                    .into(mAccountHeaderImg);
            mAccountHeaderText.setText(R.string.sign_in_to);
            isLogin = false;
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_user) {
            if (NjordAccountManager.isLogined(getContext())) {
//                AccountUIHelper.jumpProfileCenter(getActivity());
                if (account == null){
                    AccountUIHelper.startLogin(getActivity());
                }
            } else {
                AccountUIHelper.startLogin(getActivity());
            }
        } else if (v.getId() == R.id.ll_check_and_update) {
//            onClickUpdate(v);
            AppUtils.launchAppDetail(getApplicationContext(), AppUtils.getPackageName(getApplicationContext()));
        } else if (v.getId() == R.id.ll_my_collect) {
            //收藏列表
            ActivityCtrl.gotoActivityOpenSimple(getActivity(), MyCollectActivity.class);
        } else if (v.getId() == R.id.ll_my_lang) {
            //语言列表
            languageDialog = new LanguageDialog(mActivity);
            languageDialog.show();
            setListener();
        } else if (v.getId() == R.id.ll_my_set) {
            //设置
            ActivityCtrl.gotoSettingAcitivity(getApplicationContext(), SettingActivity.class,isLogin);
        }
    }

    public void quitLogin(){
        NjordAccountManager.localLogout(getApplicationContext());
        account = NjordAccountManager.getCurrentAccount(getApplicationContext());
        showAccountInfo(account);
    }

    public void onClickUpdate(View v) {
//        Nox.manualCheckUpdate(getContext());
//        Log.e("MyFragment","isUpdate="+Nox.canUpdate(getActivity()));
//        Nox.manualCheckUpdate(getContext(), getContext().getPackageName(), new NoxUpdateAction(getContext(), "") {
//            @Override
//            public void onUpdate(NoxInfo noxInfo) {
//                Log.e("MyFragment","isUpdate="+noxInfo.canUpdate());
//            }
//        }, new ContextSafeAction<List<a>>(getContext()){
//
//            @Override
//            protected void a(Context context, List<com.nox.update.a> as) {
//
//            }
//        }, new ContextSafeAction<Context>(getContext()) {
//
//            @Override
//            protected void a(Context context, Context context2) {
//
//            }
//        });
    }
}
