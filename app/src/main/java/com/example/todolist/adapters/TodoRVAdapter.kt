package com.example.todolist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.TaskRowBinding
import com.example.todolist.fragments.MainFragment
import com.example.todolist.room.Task
import com.example.todolist.viewmodel.TodoViewModel

class TodoRVAdapter(private val listOfTasks: MutableList<Task>)
    :RecyclerView.Adapter<TodoRVAdapter.ViewHolder>() {
    private var mListener: onItemClickListener? = null

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setNewData(list: MutableList<Task>){
        listOfTasks.removeAll(listOfTasks)
        listOfTasks.addAll(list)

    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
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
        holder.itemView.setOnClickListener {
            if (mListener != null) {
                mListener!!.onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfTasks.size
    }
    inner class ViewHolder(binding: TaskRowBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.taskTitle
        val notif = binding.notif

    }
}