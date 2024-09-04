package com.example.emitracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TaskActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var currentUserId: String? = null

    private lateinit var customTaskEditText: EditText
    private lateinit var addCustomTaskButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get current user
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUserId = currentUser.uid
        } else {
            // If user is not authenticated, redirect to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Define buttons for each task
        val taskButton1 = findViewById<Button>(R.id.taskButton1)
        val taskButton2 = findViewById<Button>(R.id.taskButton2)
        val taskButton3 = findViewById<Button>(R.id.taskButton3)
        val statsButton = findViewById<Button>(R.id.statsButton)
        val viewTasksButton = findViewById<Button>(R.id.viewTasksButton)
        val viewProfileButton = findViewById<Button>(R.id.viewProfileButton)

        // Initialize custom task views
        customTaskEditText = findViewById(R.id.customTaskEditText)
        addCustomTaskButton = findViewById(R.id.addCustomTaskButton)

        // When a task is selected, mark it as completed and save to Firestore
        taskButton1.setOnClickListener { completeTask("Do the Dishes") }
        taskButton2.setOnClickListener { completeTask("Take out the Trash") }
        taskButton3.setOnClickListener { completeTask("Walk the Dog") }

        // Add Custom Task
        addCustomTaskButton.setOnClickListener { addCustomTask() }

        // View Completed Tasks
        viewTasksButton.setOnClickListener {
            startActivity(Intent(this, TaskListActivity::class.java))
        }

        // View Profile
        viewProfileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    // Method to mark task as completed and store it in Firestore
    private fun completeTask(taskName: String) {
        if (currentUserId == null) {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show()
            return
        }

        if (taskName.isEmpty()) {
            Toast.makeText(this, "Task name cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a map of the task
        val taskData = hashMapOf(
            "taskName" to taskName,
            "completedAt" to System.currentTimeMillis()
        )

        db.collection("users")
            .document(currentUserId!!)
            .collection("tasks")
            .add(taskData)
            .addOnSuccessListener {
                Toast.makeText(this, "$taskName completed!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to complete task: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Method to add a custom task
    private fun addCustomTask() {
        val customTaskName = customTaskEditText.text.toString().trim()
        if (customTaskName.isEmpty()) {
            Toast.makeText(this, "Please enter a task name.", Toast.LENGTH_SHORT).show()
            return
        }

        completeTask(customTaskName)
        customTaskEditText.text.clear()
    }
}
