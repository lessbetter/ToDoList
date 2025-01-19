package com.example.todolist.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.todolist.R
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.fragments.EditTaskFragment
import com.example.todolist.fragments.MainFragment
import com.example.todolist.fragments.TaskFragment
import com.example.todolist.notifications.channelID
import com.example.todolist.room.Task
import com.example.todolist.viewmodel.TodoViewModel

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    val viewModel: TodoViewModel by viewModels()
    val openFragment = "TaskFragment"
    val task_id = "1"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        createNotificationChannel()

        //val viewModel: TodoViewModel by viewModels()

//        val mainFragment = MainFragment()
//        val taskFragment = TaskFragment()
//        val editTaskFragment = EditTaskFragment()


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        if(intent.getStringExtra(openFragment).equals("TaskFragment")){
            val id = intent.getStringExtra(task_id)?.toInt() ?: 1
            val task: LiveData<Task> = viewModel.getTaskFromId(id)
            task.observe(this){
                if(it.title.isNotEmpty()){
                    openFragment(it)
                }
            }

        }
    }
    private fun openFragment(task: Task){
        viewModel.setCurTask(task)
        navController.navigate(R.id.action_mainFragment_to_taskFragment)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notif Channel"
            val descriptionText = "A Description of the channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
