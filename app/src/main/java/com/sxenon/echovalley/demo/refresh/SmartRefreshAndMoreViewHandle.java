package com.sxenon.echovalley.demo.refresh;

import android.content.Context;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.sxenon.echovalley.arch.adapter.IAdapter;
import com.sxenon.echovalley.arch.viewhandle.refresh.list.strategy.RefreshAndMoreListRefreshStrategy;

public class SmartRefreshAndMoreViewHandle<T> extends SmartRefreshViewHandle<T> {
    public SmartRefreshAndMoreViewHandle(Context context, RefreshAndMoreListRefreshStrategy<T> listStrategy, IAdapter<T> adapter, int dataSizeInFullPage, final SmartRefreshLayout smartRefreshLayout, final OnRefreshLoadMoreListener listener) {
        super(context, listStrategy, adapter, dataSizeInFullPage, smartRefreshLayout,listener);
        listStrategy.addEventListener(new RefreshAndMoreListRefreshStrategy.EventListener<T>() {

            @Override
            public void onInitialize() {

            }

            @Override
            public void onInitResult() {
                smartRefreshLayout.finishRefresh();
            }

            @Override
            public void onNoMoreResult() {
                smartRefreshLayout.finishLoadMoreWithNoMoreData();
            }

            @Override
            public void onCanMoreResult() {
                smartRefreshLayout.finishLoadMore();
            }

            @Override
            public void onEmptyResult() {
                smartRefreshLayout.finishRefreshWithNoMoreData();
            }
        });
    }
}
