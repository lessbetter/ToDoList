package com.example.todolist.fragments

import android.annotation.SuppressLint
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
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentEditTaskBinding
import com.example.todolist.room.Task
import com.example.todolist.viewmodel.TodoViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class EditTaskFragment: Fragment() {
    private val viewModel: TodoViewModel by activityViewModels()
    private lateinit var binding: FragmentEditTaskBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateEditText: EditText = binding.date
        val timeEditText: EditText = binding.time
        val editButton: Button = binding.editButton
        val notifSwitch: Switch = binding.notif
        var dueTime: LocalTime? = null

        val titleEdit: EditText = binding.taskTitle
        val descriptionEdit: EditText = binding.taskDescription
        val categoryEdit: EditText = binding.taskCategory
        titleEdit.setText(viewModel.curTask.value!!.title)
        descriptionEdit.setText(viewModel.curTask.value!!.description)
        categoryEdit.setText(viewModel.curTask.value!!.category)
        if(viewModel.curTask.value!!.notifications){
            notifSwitch.toggle()
        }


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

        editButton.setOnClickListener {
            viewModel.curTask.value!!.title = binding.taskTitle.text.toString()
            viewModel.curTask.value!!.description = binding.taskDescription.text.toString()
            viewModel.curTask.value!!.category = binding.taskCategory.text.toString()
            //viewModel.curTask.value!!.taskDate = binding.date.text.toString()
            //viewModel.curTask.value!!.taskTime = binding.time.text.toString()
            viewModel.curTask.value!!.notifications = binding.notif.isChecked()
            //viewModel.curTask.value!!.attachments = binding.attachments.text.toString()

            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val current = LocalDateTime.now().format(formatter)
            Log.d(TAG, viewModel.curTask.value!!.category.toString())

            if(viewModel.curTask.value!!.title.length!=0){

                viewModel.updateTask(viewModel.curTask.value!!)
                //viewModel.getAllTasks()
                viewModel.setCurTask(viewModel.curTask.value!!)
                Toast.makeText(activity,"Task edited successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }
}