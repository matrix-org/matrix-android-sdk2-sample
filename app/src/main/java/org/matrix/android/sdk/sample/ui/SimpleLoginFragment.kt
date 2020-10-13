package org.matrix.android.sdk.sample.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.matrix.android.sdk.api.Matrix
import org.matrix.android.sdk.api.MatrixCallback
import org.matrix.android.sdk.api.auth.data.HomeServerConnectionConfig
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.sample.R
import org.matrix.android.sdk.sample.SessionHolder

class SimpleLoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener {
            launchAuthProcess()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun launchAuthProcess() {
        val username = usernameField.text.toString().trim()
        val password = passwordField.text.toString().trim()
        val homeserver = homeserverField.text.toString().trim()

        // First, create a homeserver config
        // Be aware than it can throw if you don't give valid info
        val homeServerConnectionConfig = try {
            HomeServerConnectionConfig
                .Builder()
                .withHomeServerUri(Uri.parse(homeserver))
                .build()
        } catch (failure: Throwable) {
            Toast.makeText(requireContext(), "Home server is not valid", Toast.LENGTH_SHORT).show()
            return
        }
        // Then you can retrieve the authentication service.
        // Here we use the direct authentication, but you get LoginWizard and RegistrationWizard for more advanced feature
        //
        Matrix.getInstance(requireContext()).authenticationService().directAuthentication(
            homeServerConnectionConfig,
            username,
            password,
            "matrix-sdk-android2-sample",
            object : MatrixCallback<Session> {
                override fun onSuccess(session: Session) {
                    // When you got your session, open and launch sync
                    Toast.makeText(
                        requireContext(),
                        "Welcome ${session.myUserId}",
                        Toast.LENGTH_SHORT
                    ).show()
                    SessionHolder.currentSession = session
                    session.open()
                    session.startSync(true)
                    displayRoomList()
                }

                override fun onFailure(failure: Throwable) {
                    Toast.makeText(requireContext(), "Failure: $failure", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun displayRoomList() {
        val fragment = RoomListFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment).commit()
    }

}