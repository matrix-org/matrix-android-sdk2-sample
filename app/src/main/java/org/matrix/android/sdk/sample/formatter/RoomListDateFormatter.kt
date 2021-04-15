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

package org.matrix.android.sdk.sample.formatter

import com.stfalcon.chatkit.utils.DateFormatter
import java.util.*

class RoomListDateFormatter : DateFormatter.Formatter {

    override fun format(date: Date): String {
        return if (DateFormatter.isToday(date)) {
            DateFormatter.format(date, DateFormatter.Template.TIME)
        } else if (DateFormatter.isYesterday(date)) {
            "Yesterday"
        } else if (DateFormatter.isCurrentYear(date)) {
            DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH)
        } else {
            DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR)
        }
    }
}
