package com.example.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.todolist.room.Task
import com.example.todolist.room.TodoRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking

class TodoViewModel (application: Application): AndroidViewModel(application) {

    var todoRepository: TodoRepository = TodoRepository(application)
    //var allTasks: Deferred<LiveData<MutableList<Task>>> = todoRepository.getAllTasksAsync()
    private val _curTask = MutableLiveData<Task>()
    val curTask: LiveData<Task> get() = _curTask
//    private val _listOfAllTasks = MutableLiveData<MutableList<Task>>(mutableListOf())
//    val listOfAllTasks: LiveData<MutableList<Task>> get() = getAllTasks()
    val tasksUsingFlow: LiveData<MutableList<Task>> = todoRepository.tasksFlow.asLiveData()
    //var allTasksSorted: Deferred<LiveData<List<Task>>> = todoRepository.getAllTasksSortByAscFinishTime()

    fun insertTask(task: Task){
        todoRepository.insertTask(task)
    }

    fun updateTask(task: Task){
        todoRepository.updateTask(task)
    }

    fun deleteTask(task: Task){
        todoRepository.deleteTask(task)
    }

//    fun getAllTasks(): LiveData<MutableList<Task>> = runBlocking {
//        allTasks.await()
//        }

    fun deleteAllRows(){
        todoRepository.deleteAllRows()
    }

    fun setCurTask(task: Task){
        _curTask.value = task
    }

//    fun getAllTasksSortByAscFinishTime(): LiveData<List<Task>> = runBlocking {
//        allTasksSorted.await()
//    }
}