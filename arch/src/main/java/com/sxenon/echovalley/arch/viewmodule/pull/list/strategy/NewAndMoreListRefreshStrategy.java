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

package com.sxenon.echovalley.arch.viewmodule.pull.list.strategy;

import com.sxenon.echovalley.arch.adapter.IAdapter;
import com.sxenon.echovalley.arch.viewmodule.pull.IRefreshStrategy;
import com.sxenon.echovalley.arch.viewmodule.pull.IRefreshViewModule;
import com.sxenon.echovalley.arch.viewmodule.pull.list.strategy.adapter.IAdapterDataHandler;

import java.util.List;

/**
 * NewAndMore implement for IPullStrategy
 * Created by Sui on 2017/8/6.
 */

public class NewAndMoreListRefreshStrategy<T> extends BaseListRefreshStrategy<T> {
    private EventListener<T> mEventListener;

    public NewAndMoreListRefreshStrategy() {
        super();
    }

    public NewAndMoreListRefreshStrategy(IAdapterDataHandler<T> adapterStrategy) {
        super(adapterStrategy);
    }

    private void onFullMoreData(IAdapter<T> adapter, List<T> data) {
        getAdapterDataHandler().onMoreData(adapter, data);
        if ( mEventListener !=null){
            mEventListener.onFullMoreData(data);
        }
    }

    private void onPartialMoreData(IAdapter<T> adapter, List<T> data){
        getAdapterDataHandler().onMoreData(adapter, data);
        if ( mEventListener !=null){
            mEventListener.onPartialMoreData(data);
        }
    }

    private void onNewData(IAdapter<T> adapter, List<T> data) {
        getAdapterDataHandler().onNewData(adapter, data);
        if ( mEventListener !=null){
            mEventListener.onNewData(data);
        }
    }

    private void onInitData(IAdapter<T> adapter, List<T> data) {
        getAdapterDataHandler().onInitData(adapter, data);
        if ( mEventListener !=null){
            mEventListener.onInitData(data);
        }
    }

    private void onNoMoreData() {
        if ( mEventListener !=null){
            mEventListener.onNoMoreData();
        }
    }

    private void onNoNewData() {
        if ( mEventListener !=null){
            mEventListener.onNoNewData();
        }
    }

    private void onInitialize() {
        if ( mEventListener !=null){
            mEventListener.onInitialize();
        }
    }

    @Override
    public void onPartialList(IRefreshViewModule pullViewModule, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
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
    public void onFullList(IRefreshViewModule pullViewModule, List<T> data, IAdapter<T> adapter, PageInfo pageInfo, int action) {
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
    public void onEmptyList(IRefreshViewModule pullViewModule, PageInfo pageInfo, IAdapter<T> adapter, int action) {
        if ( adapter.getItemCount() == 0 ){
            pullViewModule.onEmpty();
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
    public void onError(IRefreshViewModule pullViewModule, Throwable throwable, IAdapter<T> adapter, PageInfo pageInfo, int action) {
        getAdapterDataHandler().onError(adapter,throwable);
        pageInfo.currentPage = pageInfo.tempPage = -1;
    }

    public void setEventListener(EventListener<T> eventListener) {
        this.mEventListener = eventListener;
    }

    public interface EventListener<R> {
        void onFullMoreData(List<R> data);
        void onPartialMoreData(List<R> data);
        void onNewData(List<R> data);
        void onInitData(List<R> data);
        void onNoMoreData();
        void onNoNewData();
        void onInitialize();
    }

    public static class SimpleEventListener<R> implements EventListener<R> {

        @Override
        public void onFullMoreData(List<R> data) {

        }

        @Override
        public void onPartialMoreData(List<R> data) {

        }

        @Override
        public void onNewData(List<R> data) {

        }

        @Override
        public void onInitData(List<R> data) {

        }

        @Override
        public void onNoMoreData() {

        }

        @Override
        public void onNoNewData() {

        }

        @Override
        public void onInitialize() {

        }
    }

}
