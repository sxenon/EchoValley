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

import java.util.List;

/**
 * PrevAndNext implement for IPullStrategy
 * Created by Sui on 2017/8/6.
 */

public class PrevAndNextListRefreshStrategy<T> extends BaseListRefreshStrategy<T> {
    private final int mInitPage;
    private List<EventListener<T>> mEventListenerList;

    public PrevAndNextListRefreshStrategy(int initPage) {
        super();
        mInitPage = initPage;
    }

    public PrevAndNextListRefreshStrategy(IAdapterDataHandler<T> adapterStrategy, int initPage) {
        super(adapterStrategy);
        mInitPage = initPage;
    }


    private void onNoPrevData(){
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onNoPrevData();
        }
    }

    private void onNoNextData() {
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onNoNextData();
        }
    }

    private void onFullNextData(IAdapter<T> adapter, List<T> data) {
        getAdapterDataHandler().onInitData(adapter, data);
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onFullNextData(data);
        }
    }

    private void onPartialNextData(IAdapter<T> adapter, List<T> data){
        getAdapterDataHandler().onInitData(adapter, data);
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onPartialNextData(data);
        }
    }

    private void onPrevData(IAdapter<T> adapter, List<T> data){
        getAdapterDataHandler().onInitData(adapter, data);
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onPrevData(data);
        }
    }

    private void onInitialize() {
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onInitialize();
        }
    }

    @Override
    public void onPartialList(IRefreshViewHandle refreshViewHandle, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        pageInfo.currentPage = pageInfo.tempPage;
        onPartialNextData(adapter,data);
    }

    @Override
    public void onFullList(IRefreshViewHandle refreshViewHandle, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        pageInfo.currentPage = pageInfo.tempPage;
        if ( IRefreshStrategy.PULL_ACTION_UP== refreshViewHandle.getPullAction()){
            onFullNextData(adapter, data);
        }else {
            onPrevData(adapter, data);
        }
    }

    @Override
    public void onEmptyList(IRefreshViewHandle refreshViewHandle, PageInfo pageInfo, IAdapter<T> adapter, int action) {
        pageInfo.tempPage = pageInfo.currentPage;
        if (pageInfo.currentPage == -1) {
            refreshViewHandle.onEmpty();
        } else {
            onNoNextData();
        }
    }

    @Override
    public void onPullDown(PageInfo pageInfo) {
        switch (pageInfo.currentPage) {
            case -1:
                onInitialize();
                pageInfo.tempPage = mInitPage;
                break;
            case 0:
                onNoPrevData();
                break;
            default:
                pageInfo.tempPage = pageInfo.currentPage - 1;
                break;
        }
    }

    @Override
    public void onPullUp(PageInfo pageInfo) {
        pageInfo.tempPage = pageInfo.currentPage + 1;
    }

    @Override
    public void onError(IRefreshViewHandle refreshViewHandle, Throwable throwable, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        getAdapterDataHandler().onError(adapter, throwable);
        pageInfo.currentPage = pageInfo.tempPage = -1;
    }

    public void addEventListener(EventListener<T> eventListener){
        mEventListenerList.add(eventListener);
    }

    public interface EventListener<T>{
        void onFullNextData(List<T> data);
        void onPartialNextData(List<T> data);
        void onPrevData(List<T> data);
        void onNoPrevData();
        void onNoNextData();
        void onInitialize();
    }
}
