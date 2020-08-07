package com.sxenon.echovalley.arch.viewmodule.pull;

import android.content.Context;
import android.view.View;

import com.sxenon.echovalley.arch.util.CommonUtils;

/**
 * Bridge pattern
 */
public abstract class BaseRefreshViewModule<L extends IRefreshLayout,S extends IRefreshStrategy> implements IRefreshViewModule {
    private final IRefreshStrategy.PageInfo pageInfo = new IRefreshStrategy.PageInfo(-1, -1);
    private int mPullAction = IRefreshStrategy.PULL_ACTION_DOWN;

    private final S mPullStrategy;
    private final L mPullLayout;
    private final Context mContext;

    private int mStateWhat = RefreshStateWhat.WHAT_UNINITIALIZED;
    private Throwable mError;

    private View mEmptyView;
    private View mErrorView;

    private EventListener mEventListener;

    /**
     * Constructor
     *
     * @param context          上下文
     * @param pullLayout       刷新容器
     * @param pullStrategy 分页数据填充策略
     */
    public BaseRefreshViewModule(Context context, L pullLayout, S pullStrategy) {
        mPullLayout = pullLayout;
        mPullStrategy = pullStrategy;
        mContext = context;
    }

    //Component start
    public void setExtraComponents(View emptyView, View exceptionView) {
        mEmptyView = emptyView;
        mErrorView = exceptionView;

        CommonUtils.setViewVisibility(mEmptyView, View.GONE);
        CommonUtils.setViewVisibility(mErrorView, View.GONE);
    }

    public void resetExtraComponents(View emptyView, View exceptionView) {
        mEmptyView = emptyView;
        mErrorView = exceptionView;
    }
    //Component end

    //Action start
    public void endAllAnim() {
        endPullingDownAnim();
        endPullingUpAnim();
    }

    public void endPullingUpAnim(){
        mPullLayout.endPullingUp();
    }

    public void endPullingDownAnim(){
        mPullLayout.endPullingDown();
    }

    /**
     * For subclass call,see demo
     */
    public final void onBeginPullingDown() {
        mPullStrategy.onPullDown(pageInfo);
        mPullAction = IRefreshStrategy.PULL_ACTION_DOWN;
    }

    /**
     * For subclass call,see demo
     */
    protected final void onBeginPullingUp() {
        mPullStrategy.onPullUp(pageInfo);
        mPullAction = IRefreshStrategy.PULL_ACTION_UP;
    }

    public void toInitialize() {
        beginPullingDown();
    }

    public void beginPullingDown() {
        mPullLayout.beginPullingDown();
    }

    public void beginPullingUp() {
        mPullLayout.beginPullingUp();
    }
    //Action end

    //InstanceState start
    public InstanceState getCurrentInstanceState() {
        InstanceState instanceState = new InstanceState();
        instanceState.what = mStateWhat;
        instanceState.arg1 = pageInfo.currentPage;

        if (instanceState.what == RefreshStateWhat.WHAT_EXCEPTION) {
            instanceState.obj = mError;
        } else {
            instanceState.obj = getData();
        }
        return instanceState;
    }

    public void restoreInstanceState(InstanceState savedInstanceState) {
        if (savedInstanceState == null) {
            toInitialize();
            return;
        }
        pageInfo.currentPage = pageInfo.tempPage = savedInstanceState.arg1;
        mStateWhat = savedInstanceState.what;
        switch (savedInstanceState.what) {
            case RefreshStateWhat.WHAT_EMPTY:
                onEmpty();
                break;
            case RefreshStateWhat.WHAT_EXCEPTION:
                onError((Throwable) savedInstanceState.obj);
                break;
            case RefreshStateWhat.WHAT_UNINITIALIZED:
                toInitialize();
                break;
            case RefreshStateWhat.WHAT_NON_EMPTY:
                restoreData(savedInstanceState.obj);
                break;
        }
    }
    //InstanceState end

    //Event start
    public void setEventListener(EventListener eventListener) {
        this.mEventListener = eventListener;
    }

    public interface EventListener {
        void onEmpty();
        void onError(Throwable throwable);
        void onCancel();
    }

    @Override
    public void onNonEmpty() {
        getPullLayout().setVisibility(View.VISIBLE);
        mStateWhat = RefreshStateWhat.WHAT_NON_EMPTY;
        CommonUtils.setViewVisibility(mEmptyView, View.GONE);
        CommonUtils.setViewVisibility(mErrorView, View.GONE);
    }

    @Override
    public void onCancel() {
        endAllAnim();
        pageInfo.tempPage = pageInfo.currentPage;
        if ( mEventListener !=null){
            mEventListener.onCancel();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        endAllAnim();
        mStateWhat = RefreshStateWhat.WHAT_EXCEPTION;
        mError = throwable;
        CommonUtils.setViewVisibility(mEmptyView, View.GONE);
        CommonUtils.setViewVisibility(mErrorView, View.VISIBLE);
        if ( mEventListener !=null){
            mEventListener.onError(throwable);
        }
    }

    @Override
    public void onEmpty() {
        mStateWhat = RefreshStateWhat.WHAT_EMPTY;
        pageInfo.currentPage = pageInfo.tempPage = -1;
        CommonUtils.setViewVisibility(mErrorView, View.GONE);
        CommonUtils.setViewVisibility(mEmptyView, View.VISIBLE);
        if ( mEventListener !=null){
            mEventListener.onEmpty();
        }
    }
    //Event end

    //Getter start
    public View getExceptionView() {
        return mErrorView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public int getPullEventWhat() {
        return mStateWhat;
    }

    public L getPullLayout() {
        return mPullLayout;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public Throwable getError() {
        return mError;
    }

    public S getPullStrategy() {
        return mPullStrategy;
    }

    public abstract Object getData();
    //Getter end

    //Setter start
    public abstract void restoreData(Object data);

    public void setFillerEventWhat(int eventWhat) {
        mStateWhat = eventWhat;
    }

    public void setError(Throwable error) {
        mError = error;
    }

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

    //Setter end
}
