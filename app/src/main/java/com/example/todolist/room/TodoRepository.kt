package com.example.todolist.room

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TodoRepository(application: Application) {

    var todoDao: TodoDao
    val tasksFlow: Flow<MutableList<Task>> get() = todoDao.getTasksFlow()

//    val categories: Flow<MutableList<String>>
//        get() = todoDao.getCategories()

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

    fun getAllTasksAsync(): Deferred<LiveData<MutableList<Task>>> =
        CoroutineScope(Dispatchers.IO).async {
            todoDao.getAllTasks()
        }

    fun deleteAllRows() =
        CoroutineScope(Dispatchers.IO).launch {
            todoDao.deleteAllRows()
        }

    fun getAllTasksSortByAscFinishTime(): Deferred<LiveData<MutableList<Task>>> =
        CoroutineScope(Dispatchers.IO).async {
            todoDao.getAllTasksSortByAscFinishTime()
        }

    fun getUnfinishedTasks(): Deferred<LiveData<MutableList<Task>>> =
        CoroutineScope(Dispatchers.IO).async {
            todoDao.getNotCompletedTasks()
        }

    fun getCategoryTasks(category: String): Deferred<LiveData<MutableList<Task>>> =
        CoroutineScope(Dispatchers.IO).async {
            todoDao.getCategoryTasks(category)
        }
    fun getCategoryNotCompletedTasks(category: String): Deferred<LiveData<MutableList<Task>>> =
        CoroutineScope(Dispatchers.IO).async {
            todoDao.getCategoryNotCompletedTasks(category)
        }

    fun getCategories(): Deferred<LiveData<MutableList<String>>> =
        CoroutineScope(Dispatchers.IO).async {
            todoDao.getCategories()
        }
}