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
import com.sxenon.echovalley.arch.viewhandle.refresh.IRefreshViewHandle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RefreshAndMoreListStrategy
 * Created by Sui on 2017/8/6.
 */

public class RefreshAndMoreListRefreshStrategy<T> implements IListRefreshStrategy<T> {
    private final List<EventListener<T>> mEventListeners = new ArrayList<>();
    private OnInitializeListener mOnInitializeListener;

    public RefreshAndMoreListRefreshStrategy() {
        super();
    }

    private void onInitResult(List<T> data){
        for (EventListener<T> eventListener:mEventListeners){
            eventListener.onInitResult(data);
        }
    }

    private void onNoMoreResult(List<T> data) {
        for (EventListener<T> eventListener:mEventListeners){
            eventListener.onNoMoreResult(data);
        }
    }

    private void onCanMoreResult(List<T> data) {
        for (EventListener<T> eventListener:mEventListeners){
            eventListener.onCanMoreResult(data);
        }
    }

    private void onInitialize() {
        if (mOnInitializeListener!=null){
            mOnInitializeListener.onInitialize();
        }
    }

    private void onEmptyResult(){
        for (EventListener<T> eventListener:mEventListeners){
            eventListener.onEmptyResult();
        }
    }

    @Override
    public void onPullDown(PageInfo pageInfo) {
        if (pageInfo.currentPage == -1){
            onInitialize();
        }
        pageInfo.tempPage = 0;
    }

    @Override
    public void onPullUp(PageInfo pageInfo) {
        pageInfo.tempPage = pageInfo.currentPage + 1;
    }

    @Override
    public void onPartialList(IRefreshViewHandle refreshViewHandle, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        pageInfo.currentPage = pageInfo.tempPage;
        if (pageInfo.tempPage == 0) {
            adapter.resetAllItems(data);
            onInitResult(data);
        } else {
            adapter.addItems(adapter.getItemCount(),data);
            onNoMoreResult(data);
        }
    }

    @Override
    public void onFullList(IRefreshViewHandle refreshViewHandle, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        pageInfo.currentPage = pageInfo.tempPage;
        if (pageInfo.tempPage == 0) {
            adapter.resetAllItems(data);
            onInitResult(data);
        }else {
            adapter.addItems(adapter.getItemCount(),data);
            onCanMoreResult(data);
        }
    }

    @Override
    public void onEmptyList(IRefreshViewHandle refreshViewHandle, PageInfo pageInfo, IAdapter<T> adapter, int action) {
        pageInfo.tempPage = pageInfo.currentPage;
        if (action == PULL_ACTION_DOWN) {
            refreshViewHandle.onEmpty();
            onEmptyResult();
        }else {
            onNoMoreResult(Collections.<T>emptyList());
        }
    }

    @Override
    public void onError(IRefreshViewHandle refreshViewHandle, Throwable throwable, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        adapter.resetAllItems(null);
        pageInfo.currentPage = pageInfo.tempPage = -1;
    }

    public void setOnInitializeListener(OnInitializeListener onInitializeListener) {
        this.mOnInitializeListener = onInitializeListener;
    }

    public interface OnInitializeListener{
        void onInitialize();
    }

    public void addEventListener(EventListener<T> eventListener) {
        mEventListeners.add(eventListener);
    }

    public interface EventListener<T>{
        void onInitResult(List<T> data);
        void onNoMoreResult(List<T> data);
        void onCanMoreResult(List<T> data);
        void onEmptyResult();
    }
}
