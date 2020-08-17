package com.sxenon.echovalley.demo.refresh;

import android.content.Context;

import androidx.annotation.NonNull;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.sxenon.echovalley.arch.adapter.IAdapter;
import com.sxenon.echovalley.arch.viewhandle.refresh.IRefreshStrategy;
import com.sxenon.echovalley.arch.viewhandle.refresh.list.BaseListRefreshViewHandle;
import com.sxenon.echovalley.arch.viewhandle.refresh.list.IListRefreshViewHandle;
import com.sxenon.echovalley.arch.viewhandle.refresh.list.strategy.IListRefreshStrategy;

public class SmartRefreshViewHandle<T> extends BaseListRefreshViewHandle<T> implements IListRefreshViewHandle<T> {
    private SmartRefreshLayout mSmartRefreshLayout;

    public SmartRefreshViewHandle(Context context, IListRefreshStrategy<T> listStrategy, IAdapter<T> adapter, int dataSizeInFullPage,SmartRefreshLayout smartRefreshLayout) {
        super(context, listStrategy, adapter, dataSizeInFullPage);
        mSmartRefreshLayout = smartRefreshLayout;
    }

    public void setListener(final OnRefreshLoadMoreListener listener) {
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                onBeginPullingUp();
                listener.onLoadMore(refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                onBeginPullingDown();
                listener.onRefresh(refreshLayout);
            }
        });
        addEventListener(new EventListener() {
            @Override
            public void onEmpty() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (getPullAction()== IRefreshStrategy.PULL_ACTION_DOWN){
                    mSmartRefreshLayout.finishRefresh(false);
                }else {
                    mSmartRefreshLayout.finishLoadMore(false);
                }
            }

            @Override
            public void onCancel() {
                if (getPullAction()== IRefreshStrategy.PULL_ACTION_DOWN){
                    mSmartRefreshLayout.finishRefresh(false);
                }else {
                    mSmartRefreshLayout.finishLoadMore(false);
                }
            }
        });
    }
}
