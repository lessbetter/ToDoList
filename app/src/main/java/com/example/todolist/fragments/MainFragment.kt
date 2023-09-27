package com.example.todolist.fragments

import android.os.Bundle
import android.util.Log
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
    //private lateinit var todoRVAdapter: TodoRVAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var listOfTasks: LiveData<MutableList<Task>>
    private lateinit var adapter : TodoRVAdapter

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

        listOfTasks = viewModel.getAllTasks()

        val fab: FloatingActionButton = binding.fab
        adapter = TodoRVAdapter(mutableListOf())
//        adapter = TodoRVAdapter(mutableListOf(),object :TodoRVAdapter.onItemClickListener{
//            override fun onItemClick(position: Int) {
////                viewModel.setCurTask(listOfTasks.value!!.elementAt(position))
////                findNavController().navigate(R.id.action_mainFragment_to_taskFragment)
//            }
//        })


        binding.taskRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.taskRecyclerView.adapter = adapter
        listOfTasks.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.setNewData(it)
                //adapter = TodoRVAdapter(it)
                //binding.taskRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
        adapter.setOnItemClickListener(object :TodoRVAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                //Log.d(TAG,"test")
                viewModel.setCurTask(listOfTasks.value!!.elementAt(position))
                findNavController().navigate(R.id.action_mainFragment_to_taskFragment)
            }
        })

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addTaskFragment)
        }

    }

}