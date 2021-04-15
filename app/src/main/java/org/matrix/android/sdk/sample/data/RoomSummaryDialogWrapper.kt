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

package org.matrix.android.sdk.sample.data

import com.stfalcon.chatkit.commons.models.IDialog
import com.stfalcon.chatkit.commons.models.IUser
import org.matrix.android.sdk.api.session.room.model.RoomSummary

class RoomSummaryDialogWrapper(val roomSummary: RoomSummary) : IDialog<TimelineEventMessageWrapper> {

    override fun getId() = roomSummary.roomId

    override fun getDialogPhoto() = roomSummary.avatarUrl

    override fun getDialogName() = roomSummary.displayName

    override fun getUsers(): MutableList<out IUser> {
        return ArrayList<TimelineEventSenderWrapper>()
    }

    override fun getLastMessage() = roomSummary.latestPreviewableEvent?.let { TimelineEventMessageWrapper(it) }

    override fun setLastMessage(message: TimelineEventMessageWrapper?) {
        // noop
    }

    override fun getUnreadCount() = roomSummary.notificationCount
}
