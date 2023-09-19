package com.example.todolist.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.todolist.R
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.fragments.EditTaskFragment
import com.example.todolist.fragments.MainFragment
import com.example.todolist.fragments.TaskFragment
import com.example.todolist.viewmodel.TodoViewModel

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val viewModel: TodoViewModel by viewModels()

//        val mainFragment = MainFragment()
//        val taskFragment = TaskFragment()
//        val editTaskFragment = EditTaskFragment()


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }
}