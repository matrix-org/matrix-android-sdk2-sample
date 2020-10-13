package org.matrix.android.sdk.sample.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import org.matrix.android.sdk.api.Matrix
import org.matrix.android.sdk.sample.R
import org.matrix.android.sdk.sample.SessionHolder

class MainActivity : AppCompatActivity() {

    private lateinit var matrix: Matrix

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.divider)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        matrix = Matrix.getInstance(this)
        if (savedInstanceState == null) {
            if (SessionHolder.currentSession != null) {
                displayRoomList()
            } else {
                displayLogin()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun displayLogin() {
        val fragment = SimpleLoginFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }


    private fun displayRoomList() {
        val fragment = RoomListFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}