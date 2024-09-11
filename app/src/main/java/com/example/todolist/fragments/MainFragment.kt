package com.example.todolist.fragments

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
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
    private lateinit var sortedListOfTasks: LiveData<MutableList<Task>>
    private lateinit var adapter : TodoRVAdapter
    private lateinit var categoriesList: LiveData<MutableList<String>>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
        var flag=false
        var hide = sharedPreferences.getBoolean("hideCompleted",false)
        var category = sharedPreferences.getString("category","All")

        val listener: SharedPreferences.OnSharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener{
                sharedPreferences, key ->
            if(key=="hideCompleted"){
                hide = sharedPreferences.getBoolean("hideCompleted",false)
            }
            if(key=="category"){
                category = sharedPreferences.getString("category","All")
            }
            category?.let { updateListOfTasks(it,hide) }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)



        listOfTasks = viewModel.tasksUsingFlow
        categoriesList = viewModel.getCategories()

        category?.let { updateListOfTasks(it,hide) }

        if(sortedListOfTasks.value?.isNotEmpty() == true){
            sortedListOfTasks.value?.forEach{
                if(it.status==false){
                    if(viewModel.isCompleted(it.dueTimeForShow)){
                        it.status=true
                        viewModel.updateTask(it)
                        flag=true
                    }
                }
            }
            if(flag==true){
                sortedListOfTasks = viewModel.getAllTasksSortByAscFinishTime()
            }
        }



        if(hide && category=="All"){
            sortedListOfTasks = viewModel.getUnfinishedTasks()

        }



        val fab: FloatingActionButton = binding.fab
        val settingsFab: FloatingActionButton = binding.settingsFab
        adapter = TodoRVAdapter(mutableListOf())



        binding.taskRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.taskRecyclerView.adapter = adapter



        categoriesList.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                viewModel.setCategoryList(it)
            }else{
                viewModel.setCategoryList(mutableListOf())
            }
        }


        sortedListOfTasks.observe(viewLifecycleOwner){
            if (it.isNotEmpty()) {

                adapter.setNewData(it)
                adapter.notifyDataSetChanged()
            }
            else{
                adapter.setNewData(mutableListOf())
            }
        }

        adapter.setOnItemClickListener(object :TodoRVAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                //Log.d(TAG,"test")
                if(sortedListOfTasks.value!!.elementAt(position).user_id>=0){
                    viewModel.setCurTask(sortedListOfTasks.value!!.elementAt(position))
                    findNavController().navigate(R.id.action_mainFragment_to_taskFragment)
                }
            }
        })

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addTaskFragment)
        }

        settingsFab.setOnClickListener{

            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }

    }
    private fun updateListOfTasks(category: String, hide: Boolean){
        sortedListOfTasks = viewModel.getAllTasksSortByAscFinishTime()
        if(category!="All" && !hide){
            sortedListOfTasks = viewModel.getCategoryTasks(category)
        }else if(category!="All" && hide){
            sortedListOfTasks = viewModel.getCategoryNotFinishedTasks(category)
        }else if(category=="All" && hide){
            sortedListOfTasks = viewModel.getUnfinishedTasks()
        }
    }

}