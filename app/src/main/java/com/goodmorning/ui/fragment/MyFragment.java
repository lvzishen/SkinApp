package com.goodmorning.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.goodmorning.MainActivity;

public class MyFragment extends Fragment {
    private TextView mTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTextView = new TextView(getActivity());
        mTextView.setGravity(Gravity.CENTER);
        String content = getArguments().getString(MainActivity.CONTENT);
        mTextView.setText(content);
        return mTextView;
    }
}
