package com.example.todolist.viewmodel

import android.app.Application
import android.app.PendingIntent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import com.example.todolist.room.Task
import com.example.todolist.room.TodoRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TodoViewModel (application: Application): AndroidViewModel(application) {

    var todoRepository: TodoRepository = TodoRepository(application)

    private val _curTask = MutableLiveData<Task>()
    val curTask: LiveData<Task> get() = _curTask

    val tasksUsingFlow: LiveData<MutableList<Task>> = todoRepository.tasksFlow.asLiveData()
    val allTasksSorted: Deferred<LiveData<MutableList<Task>>> = todoRepository.getAllTasksSortByAscFinishTime()


    private val _categoryList = MutableLiveData<MutableList<String>>()
    val categoryList: LiveData<MutableList<String>> get() = _categoryList




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

    fun setCategoryList(categories: MutableList<String>){
        _categoryList.value = categories
    }

    fun getAllTasksSortByAscFinishTime(): LiveData<MutableList<Task>> = runBlocking {
        allTasksSorted.await()
    }

    fun getUnfinishedTasks(): LiveData<MutableList<Task>> = runBlocking {
        todoRepository.getUnfinishedTasks().await()
    }

    fun getCategories(): LiveData<MutableList<String>> = runBlocking {
        todoRepository.getCategories().await()
    }

    fun getCategoryTasks(category: String): LiveData<MutableList<Task>> = runBlocking {
        todoRepository.getCategoryTasks(category).await()
    }

    fun getCategoryNotFinishedTasks(category: String): LiveData<MutableList<Task>> = runBlocking {
        todoRepository.getCategoryNotCompletedTasks(category).await()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isCompleted(date: String): Boolean{
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val dateTime = LocalDateTime.parse(date,formatter)
        val now = LocalDateTime.now()
        return dateTime.isBefore(now)
    }

}