package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.adapters.TodoRVAdapter
import com.example.todolist.databinding.FragmentMainBinding
import com.example.todolist.databinding.FragmentTaskBinding
import com.example.todolist.viewmodel.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text

class TaskFragment: Fragment() {
    private val viewModel: TodoViewModel by activityViewModels()
    private lateinit var binding: FragmentTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title: TextView = binding.taskTitle
        val description: TextView = binding.taskDescriptionText
        val category: TextView = binding.taskCategoryText
        val deleteBTN: Button = binding.deleteBtn
        val editBTN: Button = binding.editBtn

        title.text = viewModel.curTask.value!!.title
        //description.text = viewModel.curTask.value


//        val fab: FloatingActionButton = binding.fab
//        var todoAdapter : TodoRVAdapter
//        listOfTasks = viewModel.getAllTasks()
//        binding.favRecyclerView.layoutManager = LinearLayoutManager(activity)
//        listOfTasks.observe(viewLifecycleOwner) {
//            if (it.isNotEmpty()) {
//                todoAdapter = TodoRVAdapter(it)
//                binding.favRecyclerView.adapter = todoAdapter
//            }
//        }
//
//        fab.setOnClickListener {
//            findNavController().navigate(R.id.action_mainFragment_to_addTaskFragment)
//        }

    }
}