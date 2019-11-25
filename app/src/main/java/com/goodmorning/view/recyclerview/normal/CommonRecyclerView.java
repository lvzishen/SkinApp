package com.goodmorning.view.recyclerview.normal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.baselib.bitmap.util.DeviceUtil;
import com.goodmorning.utils.HandlerThreadUtil;
import com.goodmorning.view.recyclerview.StableLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的RecyclerView，内部集成了Adapter等通用的逻辑，只需要实现简单的回调即可。
 * Created by yeguolong on 17-8-22.
 */
public class CommonRecyclerView<ITEM extends IItem> extends RecyclerView {

    public abstract static class Callback {

        public abstract ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType);

        public abstract void buildList(List<IItem> list);

        public void filterList(List<IItem> list) {
        }

        public boolean onBindViewHolder(ViewHolder holder, IItem item, int position) {
            return false;
        }

        public void onDataSetChanged(int currentListSize) {
        }

        public void onItemRemoved(IItem iItem) {

        }

        public void onItemRemoved(List<IItem> list) {

        }
    }

    private Context mContext;
    protected BaseRecyclerAdapter mAdapter;
    protected List<IItem> mList = new ArrayList<>();
    private Callback mCallback;

    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_NOTIFY_DATA_SET_CHANGE = 2;
    private static final int MSG_UPDATE_LIST_ANIMITOR_ADD = 3;
    private static final int MSG_UPDATE_LIST_ANIMITOR_REMOVE = 4;
    private static final int MSG_ADD_ITEM = 5;
    private static final int MSG_REMOVE_ITEM = 6;
    private static final int MSG_CLEAR_LIST = 7;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_LIST:
                    List<IItem> list = (List<IItem>) msg.obj;
                    if (list != null) {
                        mList.clear();
                        mList.addAll(list);
                    }
                    if (mCallback != null) {
                        mCallback.filterList(mList);
                        notifyDataSetChange();
                    }
                    if (mAdapter == null) {
                        mAdapter = new BaseRecyclerAdapter(mContext, mList) {
                            @Override
                            public ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
                                if (mCallback != null)
                                    return mCallback.onCreateViewHolder(context, parent, viewType);
                                return null;
                            }

                            @Override
                            public void onBindViewHolder(ViewHolder holder, int position) {
                                if (mCallback != null && mCallback.onBindViewHolder(holder, getItem(position), position)) {
                                    return;
                                }
                                super.onBindViewHolder(holder, position);
                            }
                        };
                        setAdapter(mAdapter);
                    } else {
                        // mAdapter.setItemList(mList);
                        mAdapter.notifySetListDataChanged(mList);

                    }
                    break;
                case MSG_NOTIFY_DATA_SET_CHANGE:
                    if (mCallback != null) {
                        mCallback.onDataSetChanged(mList.size());
                    }
                    break;
                case MSG_UPDATE_LIST_ANIMITOR_ADD:
                    if (mAdapter == null) {
                        mAdapter = new BaseRecyclerAdapter(mContext, mList) {
                            @Override
                            public ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
                                if (mCallback != null)
                                    return mCallback.onCreateViewHolder(context, parent, viewType);
                                return null;
                            }

                            @Override
                            public void onBindViewHolder(ViewHolder holder, int position) {
                                if (mCallback != null && mCallback.onBindViewHolder(holder, getItem(position), position)) {
                                    return;
                                }
                                super.onBindViewHolder(holder, position);
                            }
                        };
                        setAdapter(mAdapter);
                    } else {
                        //  mAdapter.setItemList(mList);
                        if (mList.size() > 0) {
                            mAdapter.notifyItemInserted(0);
                        }
                    }
                    break;
                case MSG_UPDATE_LIST_ANIMITOR_REMOVE:
                    if (mAdapter == null) {
                        mAdapter = new BaseRecyclerAdapter(mContext, mList) {
                            @Override
                            public ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
                                if (mCallback != null)
                                    return mCallback.onCreateViewHolder(context, parent, viewType);
                                return null;
                            }

                            @Override
                            public void onBindViewHolder(ViewHolder holder, int position) {
                                if (mCallback != null && mCallback.onBindViewHolder(holder, getItem(position), position)) {
                                    return;
                                }
                                super.onBindViewHolder(holder, position);
                            }
                        };
                        setAdapter(mAdapter);
                    } else {
                        //  mAdapter.setItemList(mList);
//                        if (mList.size() != 0) {
                        mAdapter.notifyItemRemoved(msg.arg1);
                        mAdapter.notifyItemRangeChanged(msg.arg1, mAdapter.getItemCount());
//                        }
                    }
                    break;
                case MSG_ADD_ITEM:

                    IItem addiItem = (IItem) msg.obj;
                    if (!mList.contains(addiItem)) {
                        if (msg.arg1 != -1) {
                            mList.add(msg.arg1, addiItem);
                        } else {
                            mList.add(addiItem);
                        }
                    }
                    updateList(mList);
                    break;
//                case MSG_ADD_ITEM_ANIMITOR:
//                        IItem iItem = (IItem) msg.obj;
//                        if (!mList.contains(iItem)) {
//                            if (msg.arg1 != -1) {
//                                mList.add(msg.arg1, iItem);
//                            } else {
//                                mList.add(iItem);
//                            }
//                        }
//                        if (mHandler != null) {
//                            mHandler.sendEmptyMessage(MSG_UPDATE_LIST_ANIMITOR_ADD);
//                        }
//                    break;
//                case MSG_REMOVE_ITEM_ANIMITOR: {
//                    synchronized (mList) {
//                        IItem removeiItem = (IItem) msg.obj;
//                        if (mList.contains(removeiItem)) {
//                            if (msg.arg1 != -1) {
//                                mList.remove(msg.arg1);
//                            } else {
//                                mList.remove(removeiItem);
//                            }
//                        }
//                        if (mHandler != null) {
//                            Message message = Message.obtain();
//                            if (msg.arg1 != -1) {
//                                message.arg1 = msg.arg1;
//                            }
//                            message.what = MSG_UPDATE_LIST_ANIMITOR_REMOVE;
//                            mHandler.sendMessage(message);
//                        }
//                    }
//                    break;
//                }
                case MSG_REMOVE_ITEM:

                    IItem removeiItem = (IItem) msg.obj;
                    if (mList.contains(removeiItem)) {
                        mList.remove(removeiItem);
                        if (mCallback != null) {
                            mCallback.onItemRemoved(removeiItem);
                        }
                    }
                    updateList(mList);
                    break;
                case MSG_CLEAR_LIST:
                    mList.clear();
                    updateList(mList);
                    break;
            }
        }
    };

    private static final int MSG_UPDATE_LIST_ASYNC = 1;
    private static final int MSG_BUILD_LIST = 2;
    //    private static final int MSG_ADD_ITEM = 3;
//    private static final int MSG_REMOVE_ITEM = 4;
//    private static final int MSG_CLEAR_LIST = 5;
//    private static final int MSG_ADD_ITEM_ANIMITOR = 8;
//    private static final int MSG_REMOVE_ITEM_ANIMITOR = 9;
    private Handler mNonUIHandler;

    private void initNonUIHandler() {
        if (isInEditMode())
            return;
        mNonUIHandler = new Handler(HandlerThreadUtil.getNonUiLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_UPDATE_LIST_ASYNC:
                        List<IItem> list = (List<IItem>) msg.obj;
                        if (mCallback != null) {
                            mCallback.filterList(list);
                        }
                        updateDataSet(list);
                        notifyDataSetChange();
                        break;
                    case MSG_BUILD_LIST:
                        if (mCallback != null) {
                            List<IItem> buildlist = new ArrayList<>();
                            mCallback.buildList(buildlist);
                            updateList(buildlist);
                        }
                        break;

                }
            }
        };
    }

    public CommonRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context.getApplicationContext();
        initNonUIHandler();
        setLayoutManager(initLayoutManager(mContext));
        initTouchConfiguration(context);
    }

    private void notifyDataSetChange() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_NOTIFY_DATA_SET_CHANGE);
        }
    }

    public void updateDataSet(List<IItem> list) {
        if (mHandler != null) {
            Message message = mHandler.obtainMessage();
            message.obj = list;
            message.what = MSG_UPDATE_LIST;
            mHandler.sendMessage(message);
        }
    }

    protected LayoutManager initLayoutManager(Context context) {
        return new StableLinearLayoutManager(context);
    }

    public void reloadData() {
        if (mNonUIHandler != null) {
            mNonUIHandler.sendEmptyMessage(MSG_BUILD_LIST);
        }
    }

    public List<IItem> getItemList() {
        return mList;
    }

    /**
     * 获取当前列表数量
     */
    public int getCurrentListSize() {
        return mList.size();
    }

    /**
     * 添加一条数据
     */
    public void addItem(IItem iItem) {
        addItem(-1, iItem);
    }

    /**
     * 添加一条数据
     */
    public void addItem(int position, IItem iItem) {
        if (mHandler != null) {
            Message msg = Message.obtain();
            msg.what = MSG_ADD_ITEM;
            msg.obj = iItem;
            msg.arg1 = position;
            mHandler.sendMessage(msg);
        }
    }

    public void setItemList(List<ITEM> list) {
        synchronized (mList) {
            this.mList.clear();
            this.mList.addAll(list);
        }
    }

    /**
     * 立即清空List数据，不刷新
     */
    public void clearListImmediately() {
        mList.clear();
    }

    /**
     * 立即从数据列表中删除一条数据，不负责刷新View
     */
    public void removeItemImmediately(int position) {
        if (position >= 0 && mList.size() > position) {
            IItem iItem = mList.remove(position);
            if (mCallback != null) {
                mCallback.onItemRemoved(iItem);
            }
        }
    }

    /**
     * 立即从数据列表中删除一条数据，不负责刷新View
     */
    public void removeItemImmediately(IItem iItem) {
        if (mList.contains(iItem)) {
            mList.remove(iItem);
            if (mCallback != null) {
                mCallback.onItemRemoved(iItem);
            }
        }
    }

    /**
     * 立即从数据列表中删除一条数据，不负责刷新View
     */
    public void removeItemImmediately(List<IItem> list) {
        mList.removeAll(list);
        if (mCallback != null) {
            mCallback.onItemRemoved(list);
        }
    }

    /**
     * 删除一条数据
     */
    public void removeItem(IItem iItem) {
        if (mHandler != null) {
            mHandler.obtainMessage(MSG_REMOVE_ITEM, iItem).sendToTarget();
        }
    }

    /**
     * 清空列表
     */
    public void clearList() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_CLEAR_LIST);
        }
    }


    public void updateList() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST);
        }
    }

    /**
     * 刷新列表
     */
    private void updateList(List<IItem> list) {
        if (mHandler != null) {
            Message message = mHandler.obtainMessage();
            message.obj = list;
            message.what = MSG_UPDATE_LIST;
            mHandler.sendMessage(message);
        }
    }

    public IItem getItem(int position) {
        if (mAdapter != null)
            return mAdapter.getItem(position);
        return null;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public int getItemViewType(int position) {
        if (mList != null && position >= 0 && position < mList.size()) {
            IItem iItem = mList.get(position);
            if (iItem != null) {
                return iItem.getType();
            }
        }
        return 0;
    }

    public void dateChanged() {
        if (mCallback != null) {
            mCallback.onDataSetChanged(mList.size());
        }
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow, int type) {
        if (offsetInWindow == null) {
            offsetInWindow = new int[2];
            offsetInWindow[0] = offsetInWindow[1] = 0;
        }
        boolean result = super.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow, type);
        if (result && (dxConsumed == 0 && offsetInWindow[0] == 0) && (dyConsumed == 0 && offsetInWindow[1] == 0)) {
            // consumed == 0表示RecyclerView自己沒滾動，offsetInWindow == 0表示要Parent滾動但Parent沒滾動
            // 這種情況下就認為滾動結束了
            return false;
        }
        return result;
    }
//    public void addItemWithAnimitor(int position, IItem iItem) {
//        updateDataSet();
//        if (mNonUIHandler != null) {
//            Message msg = Message.obtain();
//            msg.what = MSG_ADD_ITEM_ANIMITOR;
//            msg.obj = iItem;
//            msg.arg1 = position;
//            mNonUIHandler.sendMessageDelayed(msg, 100L);
//        }
//    }
//
//    public void removeItemWithAnimitor(int position, IItem iItem) {
//        updateDataSet();
//        if (mNonUIHandler != null) {
//            Message msg = Message.obtain();
//            msg.what = MSG_REMOVE_ITEM_ANIMITOR;
//            msg.obj = iItem;
//            msg.arg1 = position;
//            mNonUIHandler.sendMessageDelayed(msg, 100L);
//        }
//    }

    private long mLongPressedTime = 300L;
    private int mTouchSlot = 0;
    private boolean mTouchConfigInited = false;
    private float mBottomMargin = 40;
    private void initTouchConfiguration(Context cxt){
        if(!mTouchConfigInited) {
            mTouchConfigInited = true;
            mLongPressedTime = ViewConfiguration.getLongPressTimeout();
            mLongPressedTime =  mLongPressedTime<300L?300L:mLongPressedTime;
            mTouchSlot = ViewConfiguration.get(mContext).getScaledTouchSlop();
            mBottomMargin = DeviceUtil.dip2px(cxt,30);
        }
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if(newState == SCROLL_STATE_IDLE){
                if(mShouldKeepScrolling) {
                    mTouchHandler.sendEmptyMessageDelayed(SCROLLING, 0l);
                    if(!mTouchHandler.hasMessages(UPDATE_MOVMENT))
                        mTouchHandler.sendEmptyMessageDelayed(UPDATE_MOVMENT,0L);
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private void addScrollerListener(){
        if(mScrollerAdded)
            return;
        mScrollerAdded = true;
        addOnScrollListener(mOnScrollListener);
    }

    private static final int STARTWATCHLONGPRESS = 100;
    private static final int ENDWATCHLONGPRESS = 101;
    private static final int ENTERLONGPRESS = 102;

    private static final int SCROLLING = 103;
    private static final int STOP_SCROLL = 105;
    //主要是手指滑动，重新确定选中区域
    private static final int UPDATE_MOVMENT = 106;
    private Handler mTouchHandler = new Handler(){
        private LinearInterpolator mInterpolater = new LinearInterpolator();
        public void handleMessage(Message msg){
            switch (msg.what){
                case STARTWATCHLONGPRESS:
                    sendEmptyMessageDelayed(ENTERLONGPRESS,mLongPressedTime);
                    break;
                case ENDWATCHLONGPRESS:
                    removeMessages(ENTERLONGPRESS);
                    break;
                case ENTERLONGPRESS:
                    if(mEnableLongPress) {
                        if(getLayoutManager() == null || getLayoutManager().getChildCount() == 0)
                            return;
                        mUnderSelectingState = true;
                        //performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                        //增加触感反馈
                        try {
                            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                        }catch (Exception e){}
                        if(getParent() !=  null)
                            getParent().requestDisallowInterceptTouchEvent(true);
                        //选中这个item
                        changePosState(SelectableWidget.FULL_CHECKED,mStartPos);
                        mChoosen = new Interval(mStartPos,mStartPos);
                    }
                    break;
                case SCROLLING:
                    addScrollerListener();
                    if(getLayoutManager() == null)
                        return;
                    int factor = 1;
                    if(mScrollingUp){
                        factor = -1;
                    }
                    if(canScrollVertically(factor)) {
                        int height = getItemHeight();
                        smoothScrollBy(0, factor*height,mInterpolater);
                    }
                    break;
                case STOP_SCROLL:
                    break;
                case UPDATE_MOVMENT:
                    RecyclerView.ViewHolder holder = transformIntoViewHolder(mLastX,mLastY);
                    if(holder != null){
                        int mCurrentPos = holder.getPosition();
                        if(mStartPos < 0 || mChoosen == null){
                            return;
                        }else{
                           Interval interval = mTemp;
                           mTemp.build(mStartPos,mCurrentPos);
                           Interval[] removalAndIncremental = mChoosen.getRemovalAndIncremental(interval);
                           //需要移除的项
                           Interval removal = removalAndIncremental[0];
                           //需要增加的项
                           Interval incremental = removalAndIncremental[1];
                           if(removal != null){
                               for(int i = removal.start ; i <= removal.end;i++){
                                   changePosState(SelectableWidget.UNCHECKED,i);
                               }
                           }

                           if(incremental != null){
                               for(int i = incremental.start;i <= incremental.end;i++){
                                   changePosState(SelectableWidget.FULL_CHECKED,i);
                               }
                           }
                           mChoosen.start = interval.start;
                           mChoosen.end = interval.end;
                        }

                    }
                    break;
            }
        }

    };


    /**
     * 改变网格中
     * @param state
     * @param pos
     */
    private void changePosState(int state,int pos){
        if(pos < 0)
            return;
        ViewHolder holder = CommonRecyclerView.this.findViewHolderForPosition(pos);
        if(holder != null && holder instanceof SelectableWidget){
            if(((SelectableWidget) holder).setState(state)) {
                getAdapter().notifyItemChanged(pos);
            }
        }
    }
    /**
     * 记录当前的选中区间，如果用set比较浪费空间
     */
    static class Interval{
        int start = 0;
        int end = 0;

        public Interval(int x,int x1){
            build(x,x1);
        }
        public void build(int x,int x1){
            if(x > x1){
                start = x1;
                end = x;
            }else{
                start = x;
                end = x1;
            }
        }

        /**
         * 获取需要去掉的部分
         * @param newOne
         * @return
         */
        public Interval[] getRemovalAndIncremental(Interval newOne){
            Interval[] intervals = new Interval[2];
            if(newOne.start == end || newOne.end == start){
                intervals[1] = newOne;
                if(newOne.start == end){
                    if(end > start){
                        intervals[0] = new Interval(start,end-1);
                    }
                }else{
                    if(end> start){
                        intervals[0] = new Interval(start+1,end);
                    }
                }
            }else if(newOne.start == start){
                //选择区域在下方
                if(newOne.end >= end){
                    intervals[0] = null;
                    if(newOne.end > end){
                        intervals[1] = new Interval(end+1,newOne.end);
                    }
                }else{
                    intervals[0] = new Interval(newOne.end+1,end);
                    intervals[1] = null;
                }
            }else if(newOne.end == end){
                //选择区域在上方
                if(newOne.start >= start){
                    intervals[1] = null;
                    if(newOne.start > start){
                        intervals[0] = new Interval(start,newOne.start-1);
                    }
                }else{
                    intervals[0] = null;
                    intervals[1] = new Interval(newOne.start,start-1);
                }
            }
            return intervals;
        }

        @Override
        public String toString() {
            return ": "+start+" to "+end;
        }
    }

    private boolean mUseHeader = true;
    private void setHasHeader(boolean hasHeader){
        mUseHeader = hasHeader;
    }
    private int getHeaderHeight(){
        if(!mUseHeader)
            return 0;
        if(getLayoutManager() != null && getLayoutManager().getChildCount()>0)
            return getLayoutManager().getChildAt(0).getHeight();
        return 0;
    }

    private int getItemHeight(){
        if(getLayoutManager() != null){
            int firstHeight = 0;
            int lastHeight = 0;
            if(getLayoutManager().getChildCount() > 0){
                firstHeight = getLayoutManager().getChildAt(0).getHeight();
                lastHeight = getLayoutManager().getChildAt(getLayoutManager().getChildCount()-1).getHeight();
                return Math.max(firstHeight,lastHeight);
            }
        }
        return 0;
    }

    private boolean mUnderSelectingState = false;
    private boolean mEnableLongPress = true;
    private boolean mShouldWatchLongPress = true;
    private boolean mShouldKeepScrolling = false;
    private boolean mScrollingUp = false;
    private boolean mScrollerAdded = false;

    float mStartX = 0;
    float mStartY = 0;

    float mLastX = 0;
    float mLastY = 0;
    //如果是长按事件，这个位置就是事件的起点
    int  mStartPos = -1;
    int  mLastPos = -1;

    Interval mChoosen = null;
    Interval mTemp = new Interval(0,0);

    public boolean processEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                LayoutManager lm = getLayoutManager();
                if(lm != null && lm instanceof GridLayoutManager){
                    setEnableLongPressState(true);
                }else{
                    setEnableLongPressState(false);
                }
                mStartX = event.getX();
                mStartY = event.getY();

                mLastX = event.getX();
                mLastY = event.getY();
                mUnderSelectingState = false;
                mTouchHandler.sendEmptyMessage(STARTWATCHLONGPRESS);
                RecyclerView.ViewHolder holder = transformIntoViewHolder(mStartX, mStartY);
                if (holder != null) {
                    mStartPos = holder.getAdapterPosition();
                    mLastPos = mStartPos;
                }else{
                    mStartPos = -1;
                    mLastPos = -1;
                }
                mChoosen  = new Interval(mStartPos,mStartPos);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float currentX = event.getX();
                float currentY = event.getY();

                mLastX = currentX;
                mLastY = currentY;

                if (!mUnderSelectingState && mShouldWatchLongPress && ((currentX - mStartX) * (currentX - mStartX) + (currentY - mStartY) * (currentY - mStartY)) > mTouchSlot * mTouchSlot) {
                    mTouchHandler.sendEmptyMessage(ENDWATCHLONGPRESS);
                    mShouldWatchLongPress = false;
                }

                if (mUnderSelectingState) {
                    //检测是否滑动到了当前recyclerview的底部，如果到了就自动滑动
                    float threshHold = mBottomMargin;
                    float bottomMax = threshHold + currentY;
                    float topMax = currentY - threshHold;
                    if (bottomMax >= (getHeight() - getPaddingBottom())) {
                        mScrollingUp = false;
                        if(!mShouldKeepScrolling) {
                            mTouchHandler.removeMessages(SCROLLING);
                            mTouchHandler.sendEmptyMessageDelayed(SCROLLING, 0L);
                        }
                        mShouldKeepScrolling = true;
                    } else if (topMax <= getPaddingTop()) {
                        mScrollingUp = true;
                        if(!mShouldKeepScrolling) {
                            mTouchHandler.removeMessages(SCROLLING);
                            mTouchHandler.sendEmptyMessageDelayed(SCROLLING, 0L);
                        }
                        mShouldKeepScrolling = true;
                    } else {
                        mShouldKeepScrolling = false;
                        mTouchHandler.removeMessages(SCROLLING);
                    }
                    if(!mTouchHandler.hasMessages(UPDATE_MOVMENT))
                        mTouchHandler.sendEmptyMessageDelayed(UPDATE_MOVMENT,5L);
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                mTouchHandler.sendEmptyMessage(ENDWATCHLONGPRESS);
                mUnderSelectingState = false;
                mShouldKeepScrolling = false;
                mShouldWatchLongPress = true;
                mStartX = 0;
                mStartY = 0;
                break;
            }
        }
        return true;
    }

    public void setEnableLongPressState(boolean enable){
        mEnableLongPress = enable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        processEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        processEvent(event);
        if(!mUnderSelectingState) {
            return super.onTouchEvent(event);
        }else{
            return true;
        }
    }

    private RecyclerView.ViewHolder transformIntoViewHolder(float x, float y){
        LayoutManager lm  = getLayoutManager();
        try {
            View hitTarget = null;
            View backTarget = null;
            if (lm != null) {
                for (int i = 0; i < lm.getChildCount(); i++) {
                    View view = lm.getChildAt(i);

                    boolean isXHit = x <= view.getRight() && x > view.getLeft();
                    boolean isYHit = y > view.getTop() && y <= view.getBottom();
                    if (isXHit && isYHit) {
                        hitTarget = view;
                        break;
                    } else if (x > view.getLeft() && y > view.getTop()) {
                        if(backTarget == null)
                            backTarget = view;
                        else{
                            if(view.getTop() > backTarget.getTop()){
                                backTarget = view;
                            }else if(view.getTop() == backTarget.getTop()){
                                if(view.getLeft() > backTarget.getLeft()){
                                    backTarget = view;
                                }
                            }
                        }
                    }
                }

                View res = hitTarget;
                if(res == null)
                    res = backTarget;
                if(res == null)
                    res = lm.getChildAt(0);
                if(res != null)
                    return getChildViewHolder(res);
            }
        }finally {
        }
        return null;
    }
    public List<IItem>  getList(){
        return mList;
    }
}
