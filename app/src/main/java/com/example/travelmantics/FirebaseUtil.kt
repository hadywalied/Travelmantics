package com.example.travelmantics


import android.annotation.SuppressLint
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


@SuppressLint("StaticFieldLeak")
object FirebaseUtil {

    private const val RC_SIGN_IN: Int = 123
    var mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var mDatabaseReference: DatabaseReference
    var mDeals = arrayListOf<TravelDeals>()
    lateinit var mStorage: FirebaseStorage
    lateinit var mStorageRef: StorageReference
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var mAuthListner: FirebaseAuth.AuthStateListener
    lateinit var caller: ListActivity

    var isAdmin: Boolean = false

    fun openFbReference(ref: String, callerActivity: ListActivity) {
        mFirebaseAuth = FirebaseAuth.getInstance()
        caller = callerActivity
        mAuthListner = FirebaseAuth.AuthStateListener {
            if (mFirebaseAuth.currentUser == null) {
                signIn()
            } else {
                val userId = mFirebaseAuth.uid
                checkAdmin(userId!!)
            }
            Toast.makeText(callerActivity.baseContext, "Welcome back!", Toast.LENGTH_LONG).show()

        }
        mDeals.clear()
        mDatabaseReference = mFirebaseDatabase.reference.child(ref)

        connectStorage()
    }

    private fun checkAdmin(uid: String) {
        isAdmin = false
        val ref = mFirebaseDatabase.reference.child("admins")
            .child(uid)
        val listener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                isAdmin = true
                caller.showMenu()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ref.addChildEventListener(listener)

    }

    private fun signIn() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )


        // Create and launch sign-in intent
        caller.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), RC_SIGN_IN
        )
    }


    fun attachListner() {
        mFirebaseAuth.addAuthStateListener(mAuthListner)
    }

    fun detachListner() {
        mFirebaseAuth.removeAuthStateListener(mAuthListner)
    }

    fun connectStorage(){
        mStorage= FirebaseStorage.getInstance()
        mStorageRef = mStorage.reference.child("deals_pictures")
    }



}
