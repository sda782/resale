package com.aevumdev.resale.models

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser : LiveData<FirebaseUser> = MutableLiveData(auth.currentUser)

    fun logOut(){
        auth.signOut()
    }

    fun signIn(context: Context,email:String, password:String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(context, "Login success", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(context, "Login failed", Toast.LENGTH_LONG).show()
            }
        }
    }
}