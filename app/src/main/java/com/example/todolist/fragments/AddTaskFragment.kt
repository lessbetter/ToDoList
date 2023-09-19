package com.example.todolist.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.todolist.databinding.FragmentAddTaskBinding
import com.example.todolist.room.Task
import com.example.todolist.viewmodel.TodoViewModel
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class AddTaskFragment: Fragment() {
    private val viewModel: TodoViewModel by activityViewModels()
    private lateinit var binding: FragmentAddTaskBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(layoutInflater)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateEditText: EditText = binding.date
        val timeEditText: EditText = binding.time
        val addButton: Button = binding.addButton
        var dueTime: LocalTime? = null

        dateEditText.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    //dateEditText.setText(dat)
                    dateEditText.setText(String.format("%02d-%02d-%04d",dayOfMonth,monthOfYear,year))
                },year,month,day
            )
            datePickerDialog.show()
        }

        timeEditText.setOnClickListener {
            if(dueTime == null)
                dueTime = LocalTime.now()
            val listener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                dueTime = LocalTime.of(selectedHour,selectedMinute)
                timeEditText.setText(String.format("%02d:%02d",dueTime!!.hour,dueTime!!.minute))
            }
            val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)

            dialog.show()
        }

        addButton.setOnClickListener {
            val title: String = binding.taskTitle.text.toString()
            val description: String = binding.taskDescription.text.toString()
            val category: String = binding.taskCategory.text.toString()
            val taskDate: String = binding.date.text.toString()
            val taskTime: String = binding.time.text.toString()
            val notifications: Boolean
            if(binding.notif.isChecked()){
                notifications = TRUE
            }
            else
                notifications = FALSE
            val attachments: String = binding.attachments.text.toString()

            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val current = LocalDateTime.now().format(formatter)
            Log.d(TAG, "my Message")
            Log.d(TAG, title.toString())
            //Log.d(TAG, title+description+current+taskTime+notifications+category+attachments)
            val task = Task(title)
            //val task = Task(title,description,current,taskTime,FALSE,notifications,category,attachments)
            viewModel.insertTask(task)
        }


    }

}