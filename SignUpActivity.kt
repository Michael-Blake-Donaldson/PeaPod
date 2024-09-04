package com.example.emitracker

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var emailSignUp: EditText
    private lateinit var passwordSignUp: EditText
    private lateinit var signUpButton: Button
    private lateinit var signUpProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        emailSignUp = findViewById(R.id.emailSignUp)
        passwordSignUp = findViewById(R.id.passwordSignUp)
        signUpButton = findViewById(R.id.signUpButton)
        signUpProgressBar = findViewById(R.id.signUpProgressBar)

        signUpButton.setOnClickListener {
            val email = emailSignUp.text.toString().trim()
            val password = passwordSignUp.text.toString().trim()

            if (validateInput(email, password)) {
                signUpProgressBar.visibility = View.VISIBLE
                signUpButton.isEnabled = false

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        signUpProgressBar.visibility = View.GONE
                        signUpButton.isEnabled = true

                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                val userData = hashMapOf(
                                    "email" to email,
                                    "createdAt" to System.currentTimeMillis()
                                )
                                db.collection("users")
                                    .document(userId)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Sign-Up Successful", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, TaskActivity::class.java))
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to create user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "User ID not found.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Sign-Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            emailSignUp.error = "Email is required."
            emailSignUp.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailSignUp.error = "Please enter a valid email."
            emailSignUp.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            passwordSignUp.error = "Password is required."
            passwordSignUp.requestFocus()
            return false
        }

        if (password.length < 6) {
            passwordSignUp.error = "Password should be at least 6 characters."
            passwordSignUp.requestFocus()
            return false
        }

        return true
    }
}