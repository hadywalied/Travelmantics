package com.example.travelmantics

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_insert.*
import kotlinx.android.synthetic.main.activity_list.*


class ListActivity : AppCompatActivity() {

    /* var deals = arrayListOf<TravelDeals>()
     lateinit var mfirebase: FirebaseDatabase
     lateinit var mDB: DatabaseReference
     lateinit var mChildListener: ChildEventListener
 */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_activity_menu, menu)

        val insertMenu = menu?.findItem(R.id.insert_menu)
        insertMenu?.isVisible = FirebaseUtil.isAdmin
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.insert_menu -> {
                startActivity(Intent(this@ListActivity, InsertActivity::class.java))
                true
            }

            R.id.logout_menu -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        // ...
                    }
                FirebaseUtil.detachListner()
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onPause() {
        super.onPause()
        FirebaseUtil.detachListner()

    }

    override fun onResume() {
        super.onResume()

        FirebaseUtil.openFbReference("Travel", this@ListActivity)
        val mAdapter = DealAdapter()
        rvDeals.adapter = mAdapter
        val linear = LinearLayoutManager(this@ListActivity, RecyclerView.VERTICAL, false)
        rvDeals.layoutManager = linear

        FirebaseUtil.attachListner()

    }

    fun showMenu() {
        invalidateOptionsMenu()
    }



}
