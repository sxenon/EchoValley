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

package com.sxenon.echovalley.arch.viewhandle.refresh.single;

import android.content.Context;

import androidx.annotation.NonNull;

import com.sxenon.echovalley.arch.viewhandle.refresh.BaseRefreshViewHandle;
import com.sxenon.echovalley.arch.viewhandle.refresh.single.strategy.ISingleRefreshStrategy;

public abstract class BaseSingleRefreshViewHandle<T> extends BaseRefreshViewHandle<ISingleRefreshStrategy<T>> implements ISingleRefreshViewHandle<T> {
    private T mData;

    /**
     * Constructor
     *
     * @param context          上下文
     * @param singleStrategy 分页数据填充策略
     */
    public BaseSingleRefreshViewHandle(Context context, ISingleRefreshStrategy<T> singleStrategy) {
        super(context, singleStrategy);
    }

    @Override
    public final Object getData() {
        return mData;
    }

    @Override
    public final void restoreData(Object data) {
        //noinspection unchecked
        mData = (T) data;
        onSingleData(mData);
    }

    @Override
    public void onSingleData(T data) {
        mData = data;
        if ( data == null) {
            //onEmpty(); maybe no more,but no empty
            getPullStrategy().onEmpty(this, getPageInfo());
        } else {
            onNonEmpty();
            fillViewByData(data);
            getPullStrategy().onSingle(this, data, getPageInfo());
        }
    }

    @Override
    public void onError(Throwable throwable) {
        super.onError(throwable);
        getPullStrategy().onError(this, throwable, getPageInfo());
    }

    public abstract void fillViewByData(@NonNull T response);
}
