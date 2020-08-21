package com.sxenon.echovalley.arch.viewhandle.refresh;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Bridge pattern
 */
public abstract class BaseRefreshViewHandle<S extends IRefreshStrategy> implements IRefreshViewHandle {
    private final IRefreshStrategy.PageInfo pageInfo = new IRefreshStrategy.PageInfo(-1, -1);
    private int mPullAction = IRefreshStrategy.PULL_ACTION_DOWN;

    private final S mRefreshStrategy;
    private final Context mContext;

    private List<CommonEventListener> mCommonEventListenerList = new ArrayList<>();

    public BaseRefreshViewHandle(Context context, S pullStrategy) {
        mRefreshStrategy = pullStrategy;
        mContext = context;
    }

    /**
     * For subclass call,see demo
     */
    public final void onBeginPullingDown() {
        mRefreshStrategy.onPullDown(pageInfo);
        mPullAction = IRefreshStrategy.PULL_ACTION_DOWN;
    }

    /**
     * For subclass call,see demo
     */
    protected final void onBeginPullingUp() {
        mRefreshStrategy.onPullUp(pageInfo);
        mPullAction = IRefreshStrategy.PULL_ACTION_UP;
    }

    //Action end

    //Event start
    public void addCommonEventListener(CommonEventListener commonEventListener){
        mCommonEventListenerList.add(commonEventListener);
    }

    public interface CommonEventListener {
        void onEmpty();
        void onError(Throwable throwable);
        void onCancel();
        void onNonEmpty();
    }

    @Override
    public void onNonEmpty() {
        for (CommonEventListener commonEventListener:mCommonEventListenerList){
            commonEventListener.onNonEmpty();
        }
    }

    @Override
    public void onCancel() {
        pageInfo.tempPage = pageInfo.currentPage;
        for (CommonEventListener commonEventListener : mCommonEventListenerList){
            commonEventListener.onCancel();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        for (CommonEventListener commonEventListener : mCommonEventListenerList){
            commonEventListener.onError(throwable);
        }
    }

    @Override
    public void onEmpty() {
        pageInfo.currentPage = pageInfo.tempPage = -1;
        for (CommonEventListener commonEventListener : mCommonEventListenerList){
            commonEventListener.onEmpty();
        }
    }
    //Event end

    //Getter start

    @Override
    public Context getContext() {
        return mContext;
    }

    public S getPullStrategy() {
        return mRefreshStrategy;
    }

    public abstract Object getData();

    public int getCurrentPageCount() {
        return pageInfo.currentPage;
    }

    public IRefreshStrategy.PageInfo getPageInfo() {
        return pageInfo;
    }

    /**
     * @return 上个动作是，上拉还是下拉的
     */
    public int getPullAction(){
        return mPullAction;
    }

    //Getter end
}
