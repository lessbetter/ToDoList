package com.example.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.todolist.room.Task
import com.example.todolist.room.TodoRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking

class TodoViewModel (application: Application): AndroidViewModel(application) {

    var todoRepository: TodoRepository = TodoRepository(application)
    var allTasks: Deferred<LiveData<List<Task>>> = todoRepository.getAllTasksAsync()
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

    fun getAllTasks(): LiveData<List<Task>> = runBlocking {
        allTasks.await()
    }

    fun deleteAllRows(){
        todoRepository.deleteAllRows()
    }

//    fun getAllTasksSortByAscFinishTime(): LiveData<List<Task>> = runBlocking {
//        allTasksSorted.await()
//    }
}