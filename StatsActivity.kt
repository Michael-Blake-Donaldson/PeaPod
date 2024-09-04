package com.example.emitracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class StatsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var currentUserId: String? = null

    private lateinit var totalTasksText: TextView
    private lateinit var todayTasksText: TextView
    private lateinit var weekTasksText: TextView
    private lateinit var statsProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUserId = currentUser.uid
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        totalTasksText = findViewById(R.id.totalTaskCount)
        todayTasksText = findViewById(R.id.todayTaskCount)
        weekTasksText = findViewById(R.id.weekTaskCount)
        statsProgressBar = findViewById(R.id.statsProgressBar)

        if (currentUserId != null) {
            fetchTaskStats()
        }
    }

    private fun fetchTaskStats() {
        statsProgressBar.visibility = View.VISIBLE
        totalTasksText.visibility = View.GONE
        todayTasksText.visibility = View.GONE
        weekTasksText.visibility = View.GONE

        db.collection("users")
            .document(currentUserId!!)
            .collection("tasks")
            .get()
            .addOnSuccessListener { documents ->
                val totalTasks = documents.size()
                totalTasksText.text = totalTasks.toString()

                val currentTime = System.currentTimeMillis()

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startOfToday = calendar.timeInMillis

                calendar.firstDayOfWeek = Calendar.SUNDAY
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startOfWeek = calendar.timeInMillis

                var todayCount = 0
                var weekCount = 0

                for (document in documents) {
                    val completedAt = document.getLong("completedAt") ?: 0L
                    if (completedAt >= startOfToday && completedAt <= currentTime) {
                        todayCount++
                    }
                    if (completedAt >= startOfWeek && completedAt <= currentTime) {
                        weekCount++
                    }
                }

                todayTasksText.text = todayCount.toString()
                weekTasksText.text = weekCount.toString()

                statsProgressBar.visibility = View.GONE
                totalTasksText.visibility = View.VISIBLE
                todayTasksText.visibility = View.VISIBLE
                weekTasksText.visibility = View.VISIBLE
            }
    }
}
