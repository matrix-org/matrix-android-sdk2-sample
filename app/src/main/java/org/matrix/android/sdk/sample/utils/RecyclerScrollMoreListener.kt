/*
 * Copyright (c) 2020 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.matrix.android.sdk.sample.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val VISIBLE_THRESHOLD = 10

internal class RecyclerScrollMoreListener(
    private val layoutManager: LinearLayoutManager,
    private val onLoadMore: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var previousTotalItemCount = 0

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        if (totalItemCount < previousTotalItemCount) {
            previousTotalItemCount = totalItemCount
        }
        if (totalItemCount > previousTotalItemCount) {
            previousTotalItemCount = totalItemCount
        }
        if (lastVisibleItemPosition + VISIBLE_THRESHOLD > totalItemCount) {
            onLoadMore()
        }
    }
}
