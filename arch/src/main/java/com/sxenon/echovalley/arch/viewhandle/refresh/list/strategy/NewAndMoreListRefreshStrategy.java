/*
 * Copyright (c) 2017  sxenon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sxenon.echovalley.arch.viewhandle.refresh.list.strategy;

import com.sxenon.echovalley.arch.adapter.IAdapter;
import com.sxenon.echovalley.arch.viewhandle.refresh.IRefreshStrategy;
import com.sxenon.echovalley.arch.viewhandle.refresh.IRefreshViewHandle;
import com.sxenon.echovalley.arch.viewhandle.refresh.list.strategy.adapter.IAdapterDataHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * NewAndMore implement for IPullStrategy
 * Created by Sui on 2017/8/6.
 */

public class NewAndMoreListRefreshStrategy<T> extends BaseListRefreshStrategy<T> {
    private List<EventListener<T>> mEventListenerList = new ArrayList<>();

    public NewAndMoreListRefreshStrategy() {
        super();
    }

    public NewAndMoreListRefreshStrategy(IAdapterDataHandler<T> adapterStrategy) {
        super(adapterStrategy);
    }

    private void onFullMoreData(IAdapter<T> adapter, List<T> data) {
        getAdapterDataHandler().onMoreData(adapter, data);
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onFullMoreData(data);
        }
    }

    private void onPartialMoreData(IAdapter<T> adapter, List<T> data){
        getAdapterDataHandler().onMoreData(adapter, data);
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onPartialMoreData(data);
        }
    }

    private void onNewData(IAdapter<T> adapter, List<T> data) {
        getAdapterDataHandler().onNewData(adapter, data);
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onNewData(data);
        }
    }

    private void onInitData(IAdapter<T> adapter, List<T> data) {
        getAdapterDataHandler().onInitData(adapter, data);
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onInitData(data);
        }
    }

    private void onNoMoreData() {
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onNoMoreData();
        }
    }

    private void onNoNewData() {
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onNoNewData();
        }
    }

    private void onInitialize() {
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onInitialize();
        }
    }

    @Override
    public void onPartialList(IRefreshViewHandle refreshViewHandle, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        if (adapter.getItemCount() == 0) {
            onInitData(adapter, data);
            onNoMoreData();
        } else if (IRefreshStrategy.PULL_ACTION_DOWN==action) {//refresh
            onNewData(adapter, data);
        } else {
            onPartialMoreData(adapter,data);
        }
        pageInfo.currentPage = pageInfo.tempPage;

    }

    @Override
    public void onFullList(IRefreshViewHandle refreshViewHandle, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        if (adapter.getItemCount() == 0) {
            onInitData(adapter, data);
        } else if (IRefreshStrategy.PULL_ACTION_DOWN==action) {//refresh
            onNewData(adapter, data);
        } else {
            onFullMoreData(adapter, data);
        }
        pageInfo.currentPage = pageInfo.tempPage;
    }

    @Override
    public void onEmptyList(IRefreshViewHandle refreshViewHandle, PageInfo pageInfo, IAdapter<T> adapter, int action) {
        if ( adapter.getItemCount() == 0 ){
            refreshViewHandle.onEmpty();
        }else {
            if (IRefreshStrategy.PULL_ACTION_DOWN==action){
                onNoNewData();
            }else {
                onNoMoreData();
            }
        }
    }

    @Override
    public void onPullDown(PageInfo pageInfo) {
        if (pageInfo.currentPage == -1){
            onInitialize();
        }
        pageInfo.tempPage = -0;
    }

    @Override
    public void onPullUp(PageInfo pageInfo) {
        pageInfo.tempPage = pageInfo.currentPage + 1;
    }

    @Override
    public void onError(IRefreshViewHandle refreshViewHandle, Throwable throwable, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        getAdapterDataHandler().onError(adapter,throwable);
        pageInfo.currentPage = pageInfo.tempPage = -1;
    }

    public void addEventListener(EventListener<T> eventListener){
        mEventListenerList.add(eventListener);
    }

    public interface EventListener<T> {
        void onFullMoreData(List<T> data);
        void onPartialMoreData(List<T> data);
        void onNewData(List<T> data);
        void onInitData(List<T> data);
        void onNoMoreData();
        void onNoNewData();
        void onInitialize();
    }
}
