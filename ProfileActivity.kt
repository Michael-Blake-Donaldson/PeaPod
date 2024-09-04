package com.example.emitracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailTextView: TextView
    private lateinit var signOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()

        emailTextView = findViewById(R.id.emailTextView)
        signOutButton = findViewById(R.id.signOutButton)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            emailTextView.text = currentUser.email
        } else {
            emailTextView.text = "No user found."
        }

        signOutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
