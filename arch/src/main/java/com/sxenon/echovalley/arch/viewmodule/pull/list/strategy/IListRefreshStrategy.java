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

package com.sxenon.echovalley.arch.viewmodule.pull.list.strategy;

import com.sxenon.echovalley.arch.adapter.IAdapter;
import com.sxenon.echovalley.arch.viewmodule.pull.IRefreshStrategy;
import com.sxenon.echovalley.arch.viewmodule.pull.IRefreshViewModule;

import java.util.List;

/**
 * IListStrategy
 * Created by Sui on 2017/9/3.
 */

public interface IListRefreshStrategy<R> extends IRefreshStrategy {
    void onFullList(IRefreshViewModule pullViewModule, List<R> data, IAdapter<R> adapter, PageInfo pageInfo, int action);

    void onPartialList(IRefreshViewModule pullViewModule, List<R> data, IAdapter<R> adapter, PageInfo pageInfo, int action);

    void onEmptyList(IRefreshViewModule pullViewModule, PageInfo pageInfo, IAdapter<R> adapter, int action);

    void onError(IRefreshViewModule pullViewModule, Throwable throwable, IAdapter<R> adapter, PageInfo pageInfo, int action);

}
