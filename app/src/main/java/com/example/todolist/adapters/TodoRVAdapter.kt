package com.example.todolist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.TaskRowBinding
import com.example.todolist.room.Task

class TodoRVAdapter(private val listOfTasks: List<Task>):RecyclerView.Adapter<TodoRVAdapter.ViewHolder>() {
    inner class ViewHolder(binding: TaskRowBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.taskTitle
        val notif = binding.notif
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TaskRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = listOfTasks.elementAt(position).title
        if(listOfTasks.elementAt(position).notifications){
            holder.notif.setImageResource(R.drawable.ic_notif)
        }
        else holder.notif.setImageResource(R.drawable.ic_notif_off)
    }

    override fun getItemCount(): Int {
        return listOfTasks.size
    }
}