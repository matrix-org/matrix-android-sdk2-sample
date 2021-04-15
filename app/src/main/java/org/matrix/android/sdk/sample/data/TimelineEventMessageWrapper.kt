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

import com.stfalcon.chatkit.commons.models.IMessage
import java.util.*
import org.matrix.android.sdk.api.session.events.model.EventType
import org.matrix.android.sdk.api.session.events.model.toModel
import org.matrix.android.sdk.api.session.room.model.message.MessageContent
import org.matrix.android.sdk.api.session.room.timeline.TimelineEvent

data class TimelineEventMessageWrapper(private val timelineEvent: TimelineEvent) : IMessage {

    override fun getId() = timelineEvent.localId.toString()

    override fun getText(): String {
        // This is where you can format according to the type
        // You might want to use getClearType in this case so you get the decrypted type if needed.
        return when (timelineEvent.root.getClearType()) {
            EventType.MESSAGE -> formatMessage(timelineEvent)
            else -> "Event of type ${timelineEvent.root.getClearType()} not rendered yet"
        }
    }

    private fun formatMessage(timelineEvent: TimelineEvent): String {
        // You can use the toModel extension method to serialize the json map to one of the sdk defined content.
        val messageContent = timelineEvent.root.getClearContent().toModel<MessageContent>() ?: return ""
        return messageContent.body
    }

    override fun getUser() = TimelineEventSenderWrapper(timelineEvent.senderInfo)

    override fun getCreatedAt() = Date(timelineEvent.root.originServerTs ?: 0)
}
