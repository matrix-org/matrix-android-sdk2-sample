package org.matrix.android.sdk.sample.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import kotlinx.android.synthetic.main.fragment_room_list.*
import org.matrix.android.sdk.api.MatrixCallback
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.session.room.roomSummaryQueryParams
import org.matrix.android.sdk.api.util.toMatrixItem
import org.matrix.android.sdk.sample.R
import org.matrix.android.sdk.sample.SessionHolder
import org.matrix.android.sdk.sample.data.RoomSummaryDialogWrapper
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
        return inflater.inflate(R.layout.fragment_room_list, container, false)
    }

    private val avatarRenderer by lazy {
        AvatarRenderer(MatrixItemColorProvider(resources))
    }

    private val imageLoader = ImageLoader { imageView, url, payload ->
        avatarRenderer.render(url, imageView)
    }
    private val roomAdapter = DialogsListAdapter<RoomSummaryDialogWrapper>(imageLoader)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar(toolbar, displayBack = false)
        roomSummaryList.setAdapter(roomAdapter)
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
            avatarRenderer.render(userMatrixItem, toolbarAvatarImageView)
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
        session.signOut(true, object : MatrixCallback<Unit> {
            override fun onSuccess(data: Unit) {
                SessionHolder.currentSession = null
                activity?.finish()
            }

            override fun onFailure(failure: Throwable) {
                activity?.let { Toast.makeText(it, "Failure: $failure", Toast.LENGTH_SHORT).show() }
            }
        })
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

