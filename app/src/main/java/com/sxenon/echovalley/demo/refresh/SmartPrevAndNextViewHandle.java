package com.sxenon.echovalley.demo.refresh;

import android.content.Context;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.sxenon.echovalley.arch.adapter.IAdapter;
import com.sxenon.echovalley.arch.viewhandle.refresh.list.strategy.PrevAndNextListRefreshStrategy;

import java.util.List;

public class SmartPrevAndNextViewHandle<T> extends SmartRefreshViewHandle<T> {

    public SmartPrevAndNextViewHandle(Context context, PrevAndNextListRefreshStrategy<T> listStrategy, IAdapter<T> adapter, int dataSizeInFullPage, final SmartRefreshLayout smartRefreshLayout, OnRefreshLoadMoreListener listener) {
        super(context, listStrategy, adapter, dataSizeInFullPage, smartRefreshLayout, listener);
        listStrategy.addEventListener(new PrevAndNextListRefreshStrategy.EventListener<T>() {
            @Override
            public void onEmptyResult() {
                smartRefreshLayout.finishRefreshWithNoMoreData();
            }

            @Override
            public void onCanNextResult(List<T> data) {
                smartRefreshLayout.finishLoadMore();
            }

            @Override
            public void onPrevResult(List<T> data) {
                smartRefreshLayout.finishRefresh();
            }

            @Override
            public void onNoPrevResult() {
                smartRefreshLayout.finishRefreshWithNoMoreData();
            }

            @Override
            public void onNoNextResult(List<T> data) {
                smartRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        });
    }
}
