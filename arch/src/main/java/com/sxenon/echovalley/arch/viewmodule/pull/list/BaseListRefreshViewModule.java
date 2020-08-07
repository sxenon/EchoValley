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

package com.sxenon.echovalley.arch.viewmodule.pull.list;

import android.content.Context;

import com.sxenon.echovalley.arch.adapter.IAdapter;
import com.sxenon.echovalley.arch.viewmodule.pull.BaseRefreshViewModule;
import com.sxenon.echovalley.arch.viewmodule.pull.IRefreshLayout;
import com.sxenon.echovalley.arch.viewmodule.pull.IRefreshStrategy;
import com.sxenon.echovalley.arch.viewmodule.pull.list.strategy.IListRefreshStrategy;

import java.util.List;

public class BaseListRefreshViewModule<T, L extends IRefreshLayout> extends BaseRefreshViewModule<L, IListRefreshStrategy<T>> implements IListRefreshViewModule<T> {

    private IAdapter<T> mAdapter;

    private final int mDataSizeInFullPage;

    /**
     * Constructor
     *
     * @param context          上下文
     * @param pullLayout       刷新容器
     * @param listStrategy 分页数据填充策略
     * @param adapter 列表控件相关的adapter
     * @param dataSizeInFullPage 完整页数据个数
     */
    public BaseListRefreshViewModule(Context context, L pullLayout, IListRefreshStrategy<T> listStrategy, IAdapter<T> adapter, int dataSizeInFullPage) {
        super(context, pullLayout, listStrategy);
        mDataSizeInFullPage = dataSizeInFullPage;
        mAdapter = adapter;
    }

    public IAdapter<T> getAdapter() {
        return mAdapter;
    }

    @Override
    public void onListResponse(List<T> response) {
        endAllAnim();
        if ( response == null || response.isEmpty()) {
            getPullStrategy().onEmptyList(this, getPageInfo(),mAdapter,getPullAction());
        } else {
            onNonEmpty();
            if ( response.size()<mDataSizeInFullPage){
                getPullStrategy().onPartialList(this, response,mAdapter,getPageInfo(),getPullAction());
            }else {
                getPullStrategy().onFullList(this, response, mAdapter, getPageInfo(),getPullAction());
            }
        }
    }


    public void onListResponse(List<T> response, int action) {
        if ( IRefreshStrategy.PULL_ACTION_DOWN == action ){
            endPullingDownAnim();
        }else {
            endPullingUpAnim();
        }
        if ( response == null || response.isEmpty()) {
            getPullStrategy().onEmptyList(this, getPageInfo(),mAdapter,action);
        } else {
            onNonEmpty();
            if ( response.size()<mDataSizeInFullPage){
                getPullStrategy().onPartialList(this, response,mAdapter,getPageInfo(),action);
            }else {
                getPullStrategy().onFullList(this, response, mAdapter, getPageInfo(),action);
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        super.onError(throwable);
        getPullStrategy().onError(this, throwable, mAdapter, getPageInfo(),getPullAction());
    }

    @Override
    public final List<T> getData() {
        return mAdapter.getValues();
    }

    @Override
    public final void restoreData(Object data) {
        //noinspection unchecked
        onListResponse((List<T>) data);
    }
}
