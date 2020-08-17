package com.sxenon.echovalley.demo.refresh;

import android.content.Context;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.sxenon.echovalley.arch.adapter.IAdapter;
import com.sxenon.echovalley.arch.viewhandle.refresh.list.strategy.NewAndMoreListRefreshStrategy;

public class SmartNewAndMoreViewHandle<T> extends SmartRefreshViewHandle<T> {
    public SmartNewAndMoreViewHandle(Context context, NewAndMoreListRefreshStrategy<T> listStrategy, IAdapter<T> adapter, int dataSizeInFullPage, final SmartRefreshLayout smartRefreshLayout, OnRefreshLoadMoreListener listener) {
        super(context, listStrategy, adapter, dataSizeInFullPage, smartRefreshLayout, listener);
        listStrategy.addEventListener(new NewAndMoreListRefreshStrategy.EventListener<T>() {
            @Override
            public void onEmptyResult() {
                smartRefreshLayout.finishRefreshWithNoMoreData();
            }

            @Override
            public void onCanMoreResult() {
                smartRefreshLayout.finishLoadMore();
            }

            @Override
            public void onNewResult() {
                smartRefreshLayout.finishRefresh();
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
            public void onNoNewResult() {
                smartRefreshLayout.finishRefreshWithNoMoreData();
            }

            @Override
            public void onInitialize() {

            }
        });
    }
}
