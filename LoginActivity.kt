package com.example.emitracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailLogin: EditText
    private lateinit var passwordLogin: EditText
    private lateinit var loginButton: Button
    private lateinit var loginProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        emailLogin = findViewById(R.id.emailLogin)
        passwordLogin = findViewById(R.id.passwordLogin)
        loginButton = findViewById(R.id.loginButton)
        loginProgressBar = findViewById(R.id.loginProgressBar)

        loginButton.setOnClickListener {
            val email = emailLogin.text.toString().trim()
            val password = passwordLogin.text.toString().trim()

            if (validateInput(email, password)) {
                loginProgressBar.visibility = View.VISIBLE
                loginButton.isEnabled = false

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        loginProgressBar.visibility = View.GONE
                        loginButton.isEnabled = true

                        if (task.isSuccessful) {
                            startActivity(Intent(this, TaskActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            emailLogin.error = "Email is required."
            emailLogin.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLogin.error = "Please enter a valid email."
            emailLogin.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            passwordLogin.error = "Password is required."
            passwordLogin.requestFocus()
            return false
        }

        return true
    }
}
