package com.goodmorning.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.creativeindia.goodmorning.R;
import com.goodmorning.MainActivity;
import com.nox.ContextSafeAction;
import com.nox.Nox;
import com.nox.NoxUpdateAction;
import com.nox.data.NoxInfo;
import com.nox.update.a;

import org.n.account.core.api.NjordAccountManager;
import org.n.account.core.exception.AuthFailureError;
import org.n.account.core.model.Account;
import org.n.account.core.net.HeaderStrategy;
import org.n.account.core.ui.GlideCircleTransform;
import org.n.account.core.utils.NjordIdHelper;
import org.n.account.core.utils.SessionHelper;
import org.n.account.net.HttpMethod;
import org.n.account.net.NetClientFactory;
import org.n.account.ui.view.AccountUIHelper;

import java.util.List;

import okhttp3.Request;

public class MyFragment extends Fragment implements View.OnClickListener {
    private TextView mTextView;
    private ImageView mAccountHeaderImg;
    private TextView mAccountHeaderText, mVersion;
    private View mUser, mUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        initViews(view);
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

        mVersion.setText("V" + getString(R.string.app_version));
        Account account = NjordAccountManager.getCurrentAccount(getActivity());
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


    private void showAccountInfo(Account account) {
        if (account != null) {
            Glide.with(getActivity()).load(account.mPictureUrl)
                    .placeholder(R.drawable.ic_account_header)
                    .bitmapTransform(new GlideCircleTransform(getActivity()))
                    .into(mAccountHeaderImg);
            mAccountHeaderText.setText(account.mNickName);
        } else {
//            mAccountNameTv.setText(R.string.sign_in_to);
            Glide.with(getActivity()).load(R.drawable.ic_account_header)
                    .bitmapTransform(new GlideCircleTransform(getActivity()))
                    .into(mAccountHeaderImg);
            mAccountHeaderText.setText(R.string.sign_in_to);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_user) {
            if (NjordAccountManager.isLogined(getContext())) {
                AccountUIHelper.jumpProfileCenter(getActivity());
            } else {
                AccountUIHelper.startLogin(getActivity());
            }
        } else if (v.getId() == R.id.ll_check_and_update) {
            onClickUpdate(v);
        }
    }

    public void onClickUpdate(View v) {
        Nox.manualCheckUpdate(getContext());
//        Nox.manualCheckUpdate(getContext(), getContext().getPackageName(), new NoxUpdateAction(getContext(), "") {
//            @Override
//            public void onUpdate(NoxInfo noxInfo) {
//
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
