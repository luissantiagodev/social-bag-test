package com.mx.testsocialbag.helpers

import android.util.Log
import com.google.firebase.database.FirebaseDatabase

object FirebaseHelper {

    private val mDatabaseReference =
        FirebaseDatabase.getInstance("https://socialbagtest-default-rtdb.firebaseio.com")

    fun uploadData(
        data: HashMap<String, Any>,
        onCompleted: () -> Unit,
        onError: (error: String) -> Unit
    ) {

        Log.e("FIREBASE HELPER", "UPLOADING DATA TO FIREBASE ${data}")

        mDatabaseReference
            .getReference("logs")
            .push()
            .setValue(data)
            .addOnSuccessListener {
                onCompleted()
            }
            .addOnFailureListener {
                onError(it.localizedMessage!!)
            }

    }


}