package com.example.todolist.room

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TodoRepository(application: Application) {

     var todoDao: TodoDao

    init {
        val database:TodoDatabase? = TodoDatabase.getInstance(application.applicationContext)

        todoDao = database!!.todoDao()
    }

    fun insertTask(task: Task)=
        CoroutineScope(Dispatchers.IO).launch {
            todoDao.insert(task)
        }


    fun updateTask(task: Task)=
        CoroutineScope(Dispatchers.IO).launch {
            todoDao.update(task)
        }

    fun deleteTask(task: Task)=
        CoroutineScope(Dispatchers.IO).launch {
            todoDao.delete(task)
        }

    fun getAllTasksAsync(): Deferred<LiveData<List<Task>>> =
        CoroutineScope(Dispatchers.IO).async {
            todoDao.getAllTasks()
        }

    fun deleteAllRows() =
        CoroutineScope(Dispatchers.IO).launch {
            todoDao.deleteAllRows()
        }

//    fun getAllTasksSortByAscFinishTime(): Deferred<LiveData<List<Task>>> =
//        CoroutineScope(Dispatchers.IO).async {
//            todoDao.getAllTasksSortByAscFinishTime()
//        }
}