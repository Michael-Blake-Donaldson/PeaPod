package com.example.emitracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Task(
    val taskName: String = "",
    val completedAt: Long = 0L
)

class TaskListActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var currentUserId: String? = null

    private lateinit var taskListView: ListView
    private lateinit var taskListProgressBar: ProgressBar
    private lateinit var taskListAdapter: TaskListAdapter
    private var taskList: MutableList<Task> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUserId = currentUser.uid
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        taskListView = findViewById(R.id.taskListView)
        taskListProgressBar = findViewById(R.id.taskListProgressBar)

        taskListAdapter = TaskListAdapter(this, taskList)
        taskListView.adapter = taskListAdapter

        if (currentUserId != null) {
            fetchTaskList()
        }
    }

    private fun fetchTaskList() {
        taskListProgressBar.visibility = View.VISIBLE
        taskListView.visibility = View.GONE

        db.collection("users")
            .document(currentUserId!!)
            .collection("tasks")
            .get()
            .addOnSuccessListener { documents ->
                taskList.clear()
                for (document in documents) {
                    val task = document.toObject(Task::class.java)
                    taskList.add(task)
                }
                taskListAdapter.notifyDataSetChanged()

                taskListProgressBar.visibility = View.GONE
                taskListView.visibility = View.VISIBLE
            }
    }
}
