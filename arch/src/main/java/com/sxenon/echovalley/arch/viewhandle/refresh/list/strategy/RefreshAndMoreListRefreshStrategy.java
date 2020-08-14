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
import com.sxenon.echovalley.arch.viewhandle.refresh.list.strategy.adapter.IAdapterDataHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * RefreshAndMoreListStrategy
 * Created by Sui on 2017/8/6.
 */

public class RefreshAndMoreListRefreshStrategy<T> extends BaseListRefreshStrategy<T> {

    private final List<EventListener<T>> mEventListeners = new ArrayList<>();

    public RefreshAndMoreListRefreshStrategy() {
        super();
    }

    public RefreshAndMoreListRefreshStrategy(IAdapterDataHandler<T> adapterStrategy) {
        super(adapterStrategy);
    }

    private void onFullMoreData(IAdapter<T> adapter, List<T> data) {
        getAdapterDataHandler().onMoreData(adapter, data);
        for (EventListener<T> eventListener:mEventListeners){
            eventListener.onFullMoreData(data);
        }
    }

    private void onPartialMoreData(IAdapter<T> adapter, List<T> data){
        getAdapterDataHandler().onMoreData(adapter, data);
        for (EventListener<T> eventListener:mEventListeners){
            eventListener.onPartialMoreData(data);
        }
    }

    private void onInitData(IAdapter<T> adapter, List<T> data) {
        getAdapterDataHandler().onInitData(adapter, data);
        for (EventListener<T> eventListener:mEventListeners){
            eventListener.onInitData(data);
        }
    }

    private void onNoMoreData() {
        for (EventListener<T> eventListener:mEventListeners){
            eventListener.onNoMoreData();
        }
    }

    private void onCanMoreData() {
        for (EventListener<T> eventListener:mEventListeners){
            eventListener.onCanMoreData();
        }
    }

    private void onInitialize() {
        for (EventListener<T> eventListener:mEventListeners){
            eventListener.onInitialize();
        }
    }

    private void onNoData(){
        for (EventListener<T> eventListener:mEventListeners){
            eventListener.onNoData();
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
            onInitData(adapter, data);
        } else {
            onPartialMoreData(adapter,data);
        }
        onNoMoreData();
    }

    @Override
    public void onFullList(IRefreshViewHandle refreshViewHandle, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        pageInfo.currentPage = pageInfo.tempPage;
        if (pageInfo.tempPage == 0) {
            onInitData(adapter, data);
        }else {
            onFullMoreData(adapter, data);
        }
        onCanMoreData();
    }

    @Override
    public void onEmptyList(IRefreshViewHandle refreshViewHandle, PageInfo pageInfo, IAdapter<T> adapter, int action) {
        pageInfo.tempPage = pageInfo.currentPage;
        if (action == PULL_ACTION_DOWN) {
            refreshViewHandle.onEmpty();
            onNoData();
        }else {
            onNoMoreData();
        }
    }

    @Override
    public void onError(IRefreshViewHandle refreshViewHandle, Throwable throwable, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        getAdapterDataHandler().onError(adapter, throwable);
        pageInfo.currentPage = pageInfo.tempPage = -1;
    }

    public void addEventListener(EventListener<T> eventListener) {
        mEventListeners.add(eventListener);
    }

    public interface EventListener<R>{
        void onFullMoreData(List<R> data);
        void onPartialMoreData(List<R> data);
        void onInitData(List<R> data);
        void onNoMoreData();
        void onCanMoreData();
        void onInitialize();
        void onNoData();
    }

    public static class SimpleEventListener<R> implements EventListener<R> {

        @Override
        public void onFullMoreData(List<R> data) {

        }

        @Override
        public void onPartialMoreData(List<R> data) {

        }

        @Override
        public void onInitData(List<R> data) {

        }

        @Override
        public void onNoMoreData() {

        }

        @Override
        public void onCanMoreData() {

        }

        @Override
        public void onNoData() {

        }

        @Override
        public void onInitialize() {

        }
    }
}
