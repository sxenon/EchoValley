package com.sxenon.echovalley.demo.refresh;

import android.content.Context;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.sxenon.echovalley.arch.adapter.IAdapter;
import com.sxenon.echovalley.arch.viewhandle.refresh.list.strategy.NewAndMoreListRefreshStrategy;

import java.util.List;

public class SmartNewAndMoreViewHandle<T> extends SmartRefreshViewHandle<T> {
    public SmartNewAndMoreViewHandle(Context context, NewAndMoreListRefreshStrategy<T> listStrategy, IAdapter<T> adapter, int dataSizeInFullPage, final SmartRefreshLayout smartRefreshLayout, OnRefreshLoadMoreListener listener) {
        super(context, listStrategy, adapter, dataSizeInFullPage, smartRefreshLayout, listener);
        listStrategy.addEventListener(new NewAndMoreListRefreshStrategy.EventListener<T>() {
            @Override
            public void onEmptyResult() {
                smartRefreshLayout.finishRefreshWithNoMoreData();
            }

            @Override
            public void onCanMoreResult(List<T> data) {
                smartRefreshLayout.finishLoadMore();
            }

            @Override
            public void onNewResult(List<T> data) {
                smartRefreshLayout.finishRefresh();
            }

            @Override
            public void onInitResult(List<T> data) {
                smartRefreshLayout.finishRefresh();
            }

            @Override
            public void onNoMoreResult(List<T> data) {
                smartRefreshLayout.finishLoadMoreWithNoMoreData();
            }

            @Override
            public void onNoNewResult() {
                smartRefreshLayout.finishRefreshWithNoMoreData();
            }
        });
    }
}
