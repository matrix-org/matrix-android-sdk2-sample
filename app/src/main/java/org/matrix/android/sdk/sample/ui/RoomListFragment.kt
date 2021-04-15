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

package org.matrix.android.sdk.sample.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import kotlinx.coroutines.launch
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.session.room.roomSummaryQueryParams
import org.matrix.android.sdk.api.util.toMatrixItem
import org.matrix.android.sdk.sample.R
import org.matrix.android.sdk.sample.SessionHolder
import org.matrix.android.sdk.sample.data.RoomSummaryDialogWrapper
import org.matrix.android.sdk.sample.databinding.FragmentRoomListBinding
import org.matrix.android.sdk.sample.formatter.RoomListDateFormatter
import org.matrix.android.sdk.sample.utils.AvatarRenderer
import org.matrix.android.sdk.sample.utils.MatrixItemColorProvider

class RoomListFragment : Fragment(), ToolbarConfigurable {

    private val session = SessionHolder.currentSession!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _views = FragmentRoomListBinding.inflate(inflater, container, false)
        return views.root
    }

    private var _views: FragmentRoomListBinding? = null
    private val views get() = _views!!

    private val avatarRenderer by lazy {
        AvatarRenderer(MatrixItemColorProvider(resources))
    }

    private val imageLoader = ImageLoader { imageView, url, payload ->
        avatarRenderer.render(url, imageView)
    }
    private val roomAdapter = DialogsListAdapter<RoomSummaryDialogWrapper>(imageLoader)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar(views.toolbar, displayBack = false)
        views.roomSummaryList.setAdapter(roomAdapter)
        roomAdapter.setDatesFormatter(RoomListDateFormatter())
        roomAdapter.setOnDialogClickListener {
            showRoomDetail(it.roomSummary)
        }

        // Create query to listen to room summary list
        val roomSummariesQuery = roomSummaryQueryParams {
            memberships = Membership.activeMemberships()
        }
        // Then you can subscribe to livedata..
        session.getRoomSummariesLive(roomSummariesQuery).observe(viewLifecycleOwner) {
            // ... And refresh your adapter with the list. It will be automatically updated when an item of the list is changed.
            updateRoomList(it)
        }

        // You can also listen to user. Here we listen to ourself to get our avatar
        session.getUserLive(session.myUserId).observe(viewLifecycleOwner) { user ->
            val userMatrixItem = user.map { it.toMatrixItem() }.getOrNull() ?: return@observe
            avatarRenderer.render(userMatrixItem, views.toolbarAvatarImageView)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            try {
                session.signOut(true)
            } catch (failure: Throwable) {
                activity?.let {
                    Toast.makeText(it, "Failure: $failure", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            SessionHolder.currentSession = null
            activity?.finish()
        }
    }

    private fun showRoomDetail(roomSummary: RoomSummary) {
        val roomDetailFragment = RoomDetailFragment.newInstance(roomSummary.roomId)
        (activity as MainActivity).supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, roomDetailFragment)
            .commit()
    }

    private fun updateRoomList(roomSummaryList: List<RoomSummary>?) {
        if (roomSummaryList == null) return
        val sortedRoomSummaryList = roomSummaryList.sortedByDescending {
            it.latestPreviewableEvent?.root?.originServerTs
        }.map {
            RoomSummaryDialogWrapper(it)
        }
        roomAdapter.setItems(sortedRoomSummaryList)
    }
}
