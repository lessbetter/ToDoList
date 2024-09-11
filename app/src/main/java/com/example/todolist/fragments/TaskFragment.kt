package com.example.todolist.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTaskBinding
import com.example.todolist.viewmodel.TodoViewModel
import org.w3c.dom.Text

class TaskFragment: Fragment() {
    private val viewModel: TodoViewModel by activityViewModels()
    private lateinit var binding: FragmentTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title: TextView = binding.taskTitle
        val description: TextView = binding.taskDescription
        val category: TextView = binding.taskCategory
        val dueTime: TextView = binding.taskDuetime
        val status: TextView = binding.status
        val deleteBTN: Button = binding.deleteBtn
        val editBTN: Button = binding.editBtn


        title.text = viewModel.curTask.value!!.title
        description.text = viewModel.curTask.value!!.description
        category.text = viewModel.curTask.value!!.category
        dueTime.text = viewModel.curTask.value!!.dueTimeForShow

        if(viewModel.curTask.value!!.status==true){
            status.text = "Deadline for this task has been reached"
        }else{
            status.text = "Deadline for this task has not yet been reached"
        }
        deleteBTN.setOnClickListener {
            viewModel.deleteTask(viewModel.curTask.value!!)
            findNavController().popBackStack()
        }
        editBTN.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_editTaskFragment)
        }
    }
}