/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.matrix.android.sdk.sample.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.amulyakhare.textdrawable.TextDrawable
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import org.matrix.android.sdk.api.session.content.ContentUrlResolver
import org.matrix.android.sdk.api.util.MatrixItem
import org.matrix.android.sdk.sample.SessionHolder

class AvatarRenderer(private val matrixItemColorProvider: MatrixItemColorProvider) {

    companion object {
        private const val THUMBNAIL_SIZE = 250
    }

    fun render(avatarUrl: String?, imageView: ImageView){
        val resolvedUrl = resolvedUrl(avatarUrl)
        Picasso.get()
            .load(resolvedUrl)
            .transform(CropCircleTransformation())
            .into(imageView)
    }

    fun render(matrixItem: MatrixItem, imageView: ImageView) {
        val resolvedUrl = resolvedUrl(matrixItem.avatarUrl)
        val placeholder = getPlaceholderDrawable(matrixItem)
        Picasso.get()
            .load(resolvedUrl)
            .placeholder(placeholder)
            .transform(CropCircleTransformation())
            .into(imageView)
    }


    fun getPlaceholderDrawable(matrixItem: MatrixItem): Drawable {
        val avatarColor = matrixItemColorProvider.getColor(matrixItem)
        return TextDrawable.builder()
            .beginConfig()
            .bold()
            .endConfig()
            .buildRound(matrixItem.firstLetterOfDisplayName(), avatarColor)
    }


    // PRIVATE API *********************************************************************************

    private fun resolvedUrl(avatarUrl: String?): String? {
        // Take care of using contentUrlResolver to use with mxc://
        return SessionHolder.currentSession?.contentUrlResolver()
            ?.resolveThumbnail(
                avatarUrl,
                THUMBNAIL_SIZE,
                THUMBNAIL_SIZE,
                ContentUrlResolver.ThumbnailMethod.SCALE
            )
    }
}
