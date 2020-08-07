/*
 * Copyright (c) 2018  sxenon
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

package com.sxenon.echovalley.arch.viewmodule.pull.list.strategy.adapter;


import com.sxenon.echovalley.arch.adapter.IAdapter;

import java.util.List;

public class DefaultAdapterDataHandler<T> implements IAdapterDataHandler<T> {
    @Override
    public void onMoreData(IAdapter<T> adapter, List<T> data) {
        adapter.addItems(adapter.getItemCount(),data);
    }

    @Override
    public void onNewData(IAdapter<T> adapter, List<T> data) {
        adapter.addItems(0,data);
    }

    @Override
    public void onInitData(IAdapter<T> adapter, List<T> data) {
        adapter.resetAllItems(data);
    }

    @Override
    public void onError(IAdapter<T> adapter, Throwable throwable) {
        adapter.resetAllItems(null);
    }
}
