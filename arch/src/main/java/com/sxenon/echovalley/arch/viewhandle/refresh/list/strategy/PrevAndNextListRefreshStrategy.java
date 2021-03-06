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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PrevAndNext implement for IPullStrategy
 * Created by Sui on 2017/8/6.
 */

public class PrevAndNextListRefreshStrategy<T> implements IListRefreshStrategy<T> {
    private final int mInitPage;
    private List<EventListener<T>> mEventListenerList = new ArrayList<>();
    private OnInitializeListener mOnInitializeListener;

    public PrevAndNextListRefreshStrategy(int initPage) {
        super();
        mInitPage = initPage;
    }

    private void onNoPrevResult(){
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onNoPrevResult();
        }
    }

    private void onNoNextResult(List<T> data) {
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onNoNextResult(data);
        }
    }

    private void onCanNextResult(List<T> data){
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onCanNextResult(data);
        }
    }

    private void onPrevResult(List<T> data){
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onPrevResult(data);
        }
    }

    private void onInitialize() {
        if (mOnInitializeListener!=null){
            mOnInitializeListener.onInitialize();
        }
    }

    private void onEmptyResult(){
        for (EventListener<T> eventListener:mEventListenerList){
            eventListener.onEmptyResult();
        }
    }

    @Override
    public void onPartialList(IRefreshViewHandle refreshViewHandle, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        pageInfo.currentPage = pageInfo.tempPage;
        adapter.resetAllItems(data);
        if (IRefreshStrategy.PULL_ACTION_DOWN == refreshViewHandle.getPullAction()){
            onPrevResult(data);
        }else {
            onNoNextResult(data);
        }

    }

    @Override
    public void onFullList(IRefreshViewHandle refreshViewHandle, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        pageInfo.currentPage = pageInfo.tempPage;
        adapter.resetAllItems(data);
        if ( IRefreshStrategy.PULL_ACTION_UP== refreshViewHandle.getPullAction()){
            onCanNextResult(data);
        }else {
            onPrevResult(data);
        }
    }

    @Override
    public void onEmptyList(IRefreshViewHandle refreshViewHandle, PageInfo pageInfo, IAdapter<T> adapter, int action) {
        pageInfo.tempPage = pageInfo.currentPage;
        if (pageInfo.currentPage == -1) {
            refreshViewHandle.onEmpty();
            onEmptyResult();
        } else {
            onNoNextResult(Collections.<T>emptyList());
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
                onNoPrevResult();
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
        adapter.resetAllItems(null);
        pageInfo.currentPage = pageInfo.tempPage = -1;
    }

    public void setOnInitializeListener(OnInitializeListener onInitializeListener) {
        this.mOnInitializeListener = onInitializeListener;
    }

    public interface OnInitializeListener{
        void onInitialize();
    }

    public void addEventListener(EventListener<T> eventListener){
        mEventListenerList.add(eventListener);
    }

    public interface EventListener<T>{
        void onEmptyResult();
        void onCanNextResult(List<T> data);
        void onPrevResult(List<T> data);
        void onNoPrevResult();
        void onNoNextResult(List<T> data);
    }
}
