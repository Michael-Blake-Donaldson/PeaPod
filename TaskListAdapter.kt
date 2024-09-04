package com.example.emitracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class TaskListAdapter(context: Context, private val tasks: List<Task>) :
    ArrayAdapter<Task>(context, 0, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)
        }

        val task = tasks[position]

        val taskNameText = listItemView?.findViewById<TextView>(R.id.taskNameText)
        val completedAtText = listItemView?.findViewById<TextView>(R.id.completedAtText)

        taskNameText?.text = task.taskName

        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val date = Date(task.completedAt)
        completedAtText?.text = sdf.format(date)

        return listItemView!!
    }
}
