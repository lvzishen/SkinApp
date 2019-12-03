package com.goodmorning.view.recyclerview.view;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.creativeindia.goodmorning.R;
import com.goodmorning.view.recyclerview.interfaces.ILoadMoreFooter;
import com.goodmorning.view.recyclerview.interfaces.OnLoadMoreListener;
import com.goodmorning.view.recyclerview.interfaces.OnNetWorkErrorListener;

import org.apache.http.conn.ConnectTimeoutException;

public class CustomLoadingFooter extends RelativeLayout implements ILoadMoreFooter {
    private State mState = State.Normal;
    private View mMoreContainer;
//    private ImageView ivMore;
    private TextView tvMore;
    private LinearLayout llFooter;
    private Context context;

    public CustomLoadingFooter(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CustomLoadingFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public CustomLoadingFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public void initView(){
        mMoreContainer = inflate(getContext(), R.layout.layout_footer_more,this);
        llFooter = mMoreContainer.findViewById(R.id.ll_footer);
//        ivMore = mMoreContainer.findViewById(R.id.iv_more);
        tvMore = mMoreContainer.findViewById(R.id.tv_more);
//        llFooter.setVisibility(GONE);
        setOnClickListener(null);
        //初始化隐藏状态
        onReset();
    }

    public void setState(State status) {
        setState(status, true);
    }

    /**
     * 设置状态
     * @param state
     * @param showView
     */
    public void setState(State state, boolean showView){
        if (mState == state){
            return;
        }
        mState = state;
        switch (state){
            case Normal:
                setOnClickListener(null);
                mMoreContainer.setVisibility(GONE);
                tvMore.setVisibility(INVISIBLE);
                break;
            case Loading:
                setOnClickListener(null);
                mMoreContainer.setVisibility(GONE);
                tvMore.setVisibility(INVISIBLE);
//                tvMore.setText("正在加载...");
                break;
            case NoMore:
                setOnClickListener(null);
                mMoreContainer.setVisibility(VISIBLE);
                tvMore.setVisibility(VISIBLE);
                tvMore.setText(context.getString(R.string.load_more_bottom));
                break;
            case NetWorkError:
                mMoreContainer.setVisibility(GONE);
//                llFooter.setVisibility(GONE);
//                tvMore.setText("点击重新加载");
                break;
        }
    }

    @Override
    public void onReset() {
        onComplete();
    }

    @Override
    public void onLoading() {
        setState(State.Loading);
    }

    @Override
    public void onComplete() {
        setState(State.Normal);
    }

    @Override
    public void onNoMore() {
        setState(State.NoMore);
    }

    @Override
    public View getFootView() {
        return this;
    }

    @Override
    public void setNetworkErrorViewClickListener(final OnNetWorkErrorListener listener) {
        setState(ILoadMoreFooter.State.NetWorkError);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setState(ILoadMoreFooter.State.Loading);
                listener.reload();
            }
        });
    }

    @Override
    public void setOnClickLoadMoreListener(final OnLoadMoreListener listener) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setState(State.Loading);
                listener.onLoadMore();
            }
        });
    }
}
