package com.aevumdev.resale.models

import android.content.Context
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser: LiveData<FirebaseUser> = MutableLiveData(auth.currentUser)

    fun signIn(context: Context, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Login success", Toast.LENGTH_LONG).show()
                Log.d("REX", "Login success")
            } else {
                Toast.makeText(context, "Login failed", Toast.LENGTH_LONG).show()
                Log.d("REX", "Login failed")
            }
        }
    }

    fun signUp(context: Context, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "User created", Toast.LENGTH_LONG).show()
                Log.d("REX", "User created")
            }
            if (!task.isSuccessful) {
                Toast.makeText(context, "Couldn't create user", Toast.LENGTH_LONG).show()
                Log.d("REX", "Couldn't create user " + task.exception.toString())
            }
        }
    }
}