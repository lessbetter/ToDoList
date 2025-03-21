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

    fun getTaskFromId(id: Int): Deferred<LiveData<Task>> =
        CoroutineScope(Dispatchers.IO).async{
            todoDao.getTaskFromId(id)
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

    fun searchAll(title: String): Deferred<LiveData<MutableList<Task>>> =
        CoroutineScope(Dispatchers.IO).async {
            todoDao.searchAll(title)
        }
    fun searchUnfinished(title: String): Deferred<LiveData<MutableList<Task>>> =
        CoroutineScope(Dispatchers.IO).async {
            todoDao.searchUnfinished(title)
        }

    fun searchAllCategorised(category: String, title: String): Deferred<LiveData<MutableList<Task>>> =
        CoroutineScope(Dispatchers.IO).async {
            todoDao.searchAllCategorised(category,title)
        }
    fun searchUnfinishedCategorised(category: String, title: String): Deferred<LiveData<MutableList<Task>>> =
        CoroutineScope(Dispatchers.IO).async {
            todoDao.searchUnfinishedCategorised(category,title)
        }

    fun getCategories(): Deferred<LiveData<MutableList<String>>> =
        CoroutineScope(Dispatchers.IO).async {
            todoDao.getCategories()
        }

    fun getLastID(): Deferred<LiveData<Int>> =
        CoroutineScope(Dispatchers.IO).async{
            todoDao.getLastID()
        }
}