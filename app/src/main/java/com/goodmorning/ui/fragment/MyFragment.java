package com.goodmorning.ui.fragment;

import android.content.Context;
import android.os.Bundle;
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
import org.n.account.core.model.Account;
import org.n.account.core.ui.GlideCircleTransform;
import org.n.account.ui.view.AccountUIHelper;

import java.util.List;

public class MyFragment extends Fragment implements View.OnClickListener {
    private TextView mTextView;
    private ImageView mAccountHeaderImg;
    private TextView mAccountHeaderText,mVersion;
    private View mUser, mUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
//        mTextView = new TextView(getActivity());
//        mTextView.setGravity(Gravity.CENTER);
//        String content = getArguments().getString(MainActivity.CONTENT);
//        mTextView.setText(content);

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

        mVersion.setText("V"+getString(R.string.app_version));
        Account account = NjordAccountManager.getCurrentAccount(getActivity());
        showAccountInfo(account);
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

    public void onClickSetting(View v) {

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
