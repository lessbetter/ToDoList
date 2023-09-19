package com.example.todolist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AddTaskViewModel(application: Application): AndroidViewModel(application) {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> get() = _category

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> get() = _date

    private val _time = MutableLiveData<String>()
    val time: LiveData<String> get() = _time

    private val _notifications = MutableLiveData<Boolean>()
    val notifications: LiveData<Boolean> get() = _notifications

    private val _attachments = MutableLiveData<String>()
    val attachments: LiveData<String> get() = _attachments


}