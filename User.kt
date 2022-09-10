package com.example.plantdiary


import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_user.*
import java.text.SimpleDateFormat
import java.util.*


class User : AppCompatActivity() {
    var storage : FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        profile_image.visibility = View.VISIBLE

        btn_info_save.setOnClickListener{
            updateinfo()
        }
    }


    fun updateinfo(){

        val name = nickname_edittext.text.toString()
        //val photo = profile_image.toString()

        if (name.length >0){
            val user = Firebase.auth.currentUser
            val db = Firebase.firestore

            val userinfo = userinfo(name)

            db.collection("users").document(user!!.getUid()).set(userinfo)
                .addOnSuccessListener { Log.d(TAG, "Successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        }
        Toast.makeText(this, "회원정보가 저장되었습니다.", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, MainActivity::class.java))

    }
}
