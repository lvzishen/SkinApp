package com.goodmorning.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import com.baselib.statistic.StatisticLoggerX;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.goodmorning.adapter.LanguageAdapter;
import com.goodmorning.config.GlobalConfig;
import com.goodmorning.manager.ContentManager;
import com.goodmorning.ui.activity.MyCollectActivity;
import com.goodmorning.ui.activity.SettingActivity;
import com.goodmorning.utils.ActivityCtrl;
import com.goodmorning.utils.AppUtils;
import com.goodmorning.view.dialog.LanguageDialog;
import com.goodmorning.view.dialog.ShareDialog;
import com.nox.Nox;

import org.n.account.core.api.NjordAccountManager;
import org.n.account.core.contract.ILoginCallback;
import org.n.account.core.data.NjordAccountReceiver;
import org.n.account.core.model.Account;
import org.n.account.core.ui.GlideCircleTransform;
import org.n.account.net.impl.INetCallback;
import org.n.account.ui.view.ProfileCenterActivity;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

public class MyFragment extends Fragment implements View.OnClickListener {
    public static final String KEY_EXTRA_MY = "key_extra_my";
    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "login.MyFrage";
    private TextView tvMyLang;
    private ImageView mAccountHeaderImg, ivMyUpdate, ivMyTip;
    private TextView mAccountHeaderText, mVersion;
    private View mUser;
    private RelativeLayout mUpdate, llLang, llSettings;
    private RelativeLayout rlShare;
    private LinearLayout llCollect;
    private LanguageDialog languageDialog;
    private Activity mActivity;
//    private boolean isLogin;
    private Account account;
//    private LoginApi mLoginApi;
//    protected LoadingDialog mLoadingDialog;
//    private String mLoadingStr = "";
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
        rlShare = view.findViewById(R.id.ll_my_share);
        rlShare.setOnClickListener(this);
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
                AppUtils.changeLanguage(mActivity,languge);
                mActivity.finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(MainActivity.KEY_EXTRA_ISMINE,true);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    public void showAccountInfo(Account account) {
        if (account != null && !account.isGuest()) {
//            if (account.isGuest()){
//                isLogin = false;
//            }else {
//                isLogin = true;
//            }
            Glide.with(getApplicationContext()).load(account.mPictureUrl)
                    .placeholder(R.drawable.ic_account_header)
                    .apply(bitmapTransform(new CircleCrop()))
                    .into(mAccountHeaderImg);
            mAccountHeaderText.setText(account.mNickName);
//            ContentManager.getInstance().setLogin(isLogin);
        } else {
//            mAccountNameTv.setText(R.string.sign_in_to);
            Glide.with(getApplicationContext()).load(R.drawable.ic_account_header)
                    .apply(bitmapTransform(new CircleCrop()))
                    .into(mAccountHeaderImg);
            mAccountHeaderText.setText(R.string.sign_in_to);
//            isLogin = false;
//            ContentManager.getInstance().setLogin(isLogin);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_user) {
            account = NjordAccountManager.getCurrentAccount(getApplicationContext());
            if (NjordAccountManager.isLogined(getContext())) {
//                AccountUIHelper.jumpProfileCenter(getActivity());
                if (account == null || account.isGuest()){
                    if (getActivity() instanceof MainActivity)
                        ((MainActivity) getActivity()).startMyLogin(getActivity());
//                    AccountUIHelper.startLogin(getActivity());
                }
            } else {
                if (getActivity() instanceof MainActivity)
                    ((MainActivity) getActivity()).startMyLogin(getActivity());
//                AccountUIHelper.startLogin(getActivity());
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
            ActivityCtrl.gotoSettingAcitivity(getApplicationContext(), SettingActivity.class);
            mActivity.finish();
        } else if (v.getId() == R.id.ll_my_share){
            ShareDialog shareDialog = new ShareDialog(mActivity);
            shareDialog.show();
        }
    }

    public void quitLogin(){
//        NjordAccountManager.localLogout(getApplicationContext());
        NjordAccountManager.logout(getApplicationContext(), new INetCallback<String>() {
            public void onStart() {
            }

            public void onFinish() {
            }

            public void onSuccess(String result) {
                NjordAccountManager.registerGuest(getApplicationContext(), new ILoginCallback() {
                    @Override
                    public void onPrePrepare(int i) {

                    }

                    @Override
                    public void onPrepareFinish() {

                    }

                    @Override
                    public void onPreLogin(int i) {

                    }

                    @Override
                    public void onLoginSuccess(Account account) {
//                        account = NjordAccountManager.getCurrentAccount(getApplicationContext());
                        showAccountInfo(account);
                    }

                    @Override
                    public void onLoginFailed(int i, String s) {

                    }
                });
            }

            public void onFailure(int errorCode, String msg) {
            }
        });
//        ProfileCenterActivity.this.finish();

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
