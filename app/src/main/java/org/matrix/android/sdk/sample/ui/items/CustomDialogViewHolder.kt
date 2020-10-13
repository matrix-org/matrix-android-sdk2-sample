package org.matrix.android.sdk.sample.ui.items

import android.view.View
import com.stfalcon.chatkit.dialogs.DialogsListAdapter.DialogViewHolder
import org.matrix.android.sdk.sample.data.RoomSummaryDialogWrapper

class CustomDialogViewHolder(itemView: View) : DialogViewHolder<RoomSummaryDialogWrapper>(
    itemView
) {
    override fun onBind(dialog: RoomSummaryDialogWrapper) {
        super.onBind(dialog)


    }
}