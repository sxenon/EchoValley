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
    private EventListener<T> mOnPullEventListener;

    public PrevAndNextListRefreshStrategy(int initPage) {
        super();
        mInitPage = initPage;
    }

    public PrevAndNextListRefreshStrategy(IAdapterDataHandler<T> adapterStrategy, int initPage) {
        super(adapterStrategy);
        mInitPage = initPage;
    }


    private void onNoPrevData(){
        if ( mOnPullEventListener !=null){
            mOnPullEventListener.onNoPrevData();
        }
    }

    private void onNoNextData() {
        if ( mOnPullEventListener !=null){
            mOnPullEventListener.onNoNextData();
        }
    }

    private void onFullNextData(IAdapter<T> adapter, List<T> data) {
        getAdapterDataHandler().onInitData(adapter, data);
        if ( mOnPullEventListener !=null){
            mOnPullEventListener.onFullNextData(data);
        }
    }

    private void onPartialNextData(IAdapter<T> adapter, List<T> data){
        getAdapterDataHandler().onInitData(adapter, data);
        if ( mOnPullEventListener !=null){
            mOnPullEventListener.onPartialNextData(data);
        }
    }

    private void onPrevData(IAdapter<T> adapter, List<T> data){
        getAdapterDataHandler().onInitData(adapter, data);
        if ( mOnPullEventListener !=null){
            mOnPullEventListener.onPrevData(data);
        }
    }

    private void onInitialize() {
        if ( mOnPullEventListener !=null){
            mOnPullEventListener.onInitialize();
        }
    }

    @Override
    public void onPartialList(IRefreshViewHandle pullViewModule, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        pageInfo.currentPage = pageInfo.tempPage;
        onPartialNextData(adapter,data);
    }

    @Override
    public void onFullList(IRefreshViewHandle pullViewModule, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        pageInfo.currentPage = pageInfo.tempPage;
        if ( IRefreshStrategy.PULL_ACTION_UP==pullViewModule.getPullAction()){
            onFullNextData(adapter, data);
        }else {
            onPrevData(adapter, data);
        }
    }

    @Override
    public void onEmptyList(IRefreshViewHandle pullViewModule, PageInfo pageInfo, IAdapter<T> adapter, int action) {
        pageInfo.tempPage = pageInfo.currentPage;
        if (pageInfo.currentPage == -1) {
            pullViewModule.onEmpty();
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
    public void onError(IRefreshViewHandle pullViewModule, Throwable throwable, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        getAdapterDataHandler().onError(adapter, throwable);
        pageInfo.currentPage = pageInfo.tempPage = -1;
    }

    public void setEventListener(EventListener<T> eventListener) {
        this.mOnPullEventListener = eventListener;
    }

    public interface EventListener<R>{
        void onFullNextData(List<R> data);
        void onPartialNextData(List<R> data);
        void onPrevData(List<R> data);
        void onNoPrevData();
        void onNoNextData();
        void onInitialize();
    }

    public static class SimpleEventListener<R> implements EventListener<R> {

        @Override
        public void onFullNextData(List<R> data) {

        }

        @Override
        public void onPartialNextData(List<R> data) {

        }

        @Override
        public void onPrevData(List<R> data) {

        }

        @Override
        public void onNoPrevData() {

        }

        @Override
        public void onNoNextData() {

        }

        @Override
        public void onInitialize() {

        }
    }
}
