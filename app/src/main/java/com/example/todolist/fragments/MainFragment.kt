package com.example.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.adapters.TodoRVAdapter
import com.example.todolist.databinding.FragmentMainBinding
import com.example.todolist.room.Task
import com.example.todolist.viewmodel.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment: Fragment() {

    private val viewModel: TodoViewModel by activityViewModels()
    private lateinit var binding: FragmentMainBinding
    private lateinit var todoRVAdapter: TodoRVAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var listOfTasks: LiveData<List<Task>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab: FloatingActionButton = binding.fab
        var todoAdapter : TodoRVAdapter
        listOfTasks = viewModel.getAllTasks()
        binding.favRecyclerView.layoutManager = LinearLayoutManager(activity)
        listOfTasks.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                todoAdapter = TodoRVAdapter(it)
                binding.favRecyclerView.adapter = todoAdapter
            }
        }

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addTaskFragment)
        }

    }

}