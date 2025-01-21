package com.example.todolist.viewmodel

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import com.example.todolist.notifications.Notification
import com.example.todolist.notifications.messageExtra
import com.example.todolist.notifications.openFragment
import com.example.todolist.notifications.task_id
import com.example.todolist.notifications.titleExtra
import com.example.todolist.room.Task
import com.example.todolist.room.TodoRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import java.security.AccessController.getContext
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TodoViewModel (application: Application): AndroidViewModel(application) {

    var todoRepository: TodoRepository = TodoRepository(application)

    private val _curTask = MutableLiveData<Task>()
    val curTask: LiveData<Task> get() = _curTask

    val tasksUsingFlow: LiveData<MutableList<Task>> = todoRepository.tasksFlow.asLiveData()
    val allTasksSorted: Deferred<LiveData<MutableList<Task>>> = todoRepository.getAllTasksSortByAscFinishTime()

    private val _testList = MutableLiveData<MutableList<Task>>()
    val testList: LiveData<MutableList<Task>> get() = _testList

    private val _lastId = MutableLiveData<Int>()
    val lastId: LiveData<Int> get() = _lastId

    private val _taskFromId = MutableLiveData<Task>()
    val taskFromId: LiveData<Task> get() = _taskFromId




    private val _categoryList = MutableLiveData<MutableList<String>>()
    val categoryList: LiveData<MutableList<String>> get() = _categoryList

    private val _filePath = MutableLiveData<String>()
    val filePath: LiveData<String> get() = _filePath

    private var _test = String()
    val test: String get() = _test




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

    fun setLastID(id: Int){
        _lastId.value = id
    }

    fun setCategoryList(categories: MutableList<String>){
        _categoryList.value = categories
    }

    fun setFilePath(path: String){
        _filePath.value = path
    }

    fun setTest(path: String){
        _test = path
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

    fun searchAll(title: String): LiveData<MutableList<Task>> = runBlocking {
        todoRepository.searchAll(title).await()
    }

    fun searchUnfinished(title: String): LiveData<MutableList<Task>> = runBlocking {
        todoRepository.searchUnfinished(title).await()
    }

    fun searchAllCategorised(category: String, title: String): LiveData<MutableList<Task>> = runBlocking {
        todoRepository.searchAllCategorised(category,title).await()
    }

    fun searchUnfinishedCategorised(category: String, title: String): LiveData<MutableList<Task>> = runBlocking {
        todoRepository.searchUnfinishedCategorised(category,title).await()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isCompleted(date: String): Boolean{
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val dateTime = LocalDateTime.parse(date,formatter)
        val now = LocalDateTime.now()
        return dateTime.isBefore(now)
    }

    fun getLastID(): LiveData<Int> = runBlocking{
        todoRepository.getLastID().await()
    }

    fun getTaskFromId(id: Int): LiveData<Task> = runBlocking {
        todoRepository.getTaskFromId(id).await()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun scheduleNotification(title: String, date: String, time: Int, id: Int, cancel: Boolean) {

        val intent = Intent(getApplication(), Notification::class.java)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, "There are "+time.toString()+" minutes left to complete this task")
        intent.putExtra(openFragment,"TaskFragment")
        intent.putExtra(task_id,id.toString())

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        var dateTime = LocalDateTime.parse(date,formatter)

        dateTime= dateTime.minusMinutes(time.toLong())

        val tempTime = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val pendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
//        val alarmManager = getApplication().let {
//            ContextCompat.getSystemService(
//                it,
//                AlarmManager::class.java
//            )
//        } as AlarmManager

        val alarmManager = getSystemService(getApplication(),AlarmManager::class.java) as AlarmManager

        if(!cancel){
            if(alarmManager.canScheduleExactAlarms()){
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    tempTime,
                    pendingIntent
                )

            }
        }


        if(cancel){
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

//    fun getNamesFromUri(attachmentsList: List<String>): List<String> {
//        var lista: MutableList<String> = mutableListOf("")
//        attachmentsList.forEach {
//            lista.add(getNameFromURI(it.toUri()))
//        }
//        return lista as List<String>
//    }
//
//    fun convertToList(attachments: String): List<String> {
//        return attachments.split(";")
//    }
//    @SuppressLint("Range", "Recycle")
//    fun getNameFromURI(uri: Uri): String {
//        val c: Cursor? = requireContext().contentResolver.query(uri, null, null, null, null)
//        if (c != null) {
//            c.moveToFirst()
//            return c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME))
//        }
//        else
//            return ""
//
//    }

}