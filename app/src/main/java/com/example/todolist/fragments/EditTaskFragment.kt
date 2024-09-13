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
import androidx.preference.PreferenceManager
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
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateEditText: EditText = binding.date
        val timeEditText: EditText = binding.time
        val editButton: Button = binding.editButton
        val notifSwitch: Switch = binding.notif
        var dueTime: LocalTime? = null
        var selectedDate: String = ""

        val titleEdit: EditText = binding.taskTitle
        val descriptionEdit: EditText = binding.taskDescription
        val categoryEdit: EditText = binding.taskCategory
        titleEdit.setText(viewModel.curTask.value!!.title)
        descriptionEdit.setText(viewModel.curTask.value!!.description)
        categoryEdit.setText(viewModel.curTask.value!!.category)
        if(viewModel.curTask.value!!.notifications){
            notifSwitch.toggle()
        }

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val dateTime = LocalDateTime.parse(viewModel.curTask.value!!.dueTimeForShow,formatter)

        dateEditText.setText(String.format("%02d-%02d-%04d",dateTime.dayOfMonth,dateTime.monthValue,dateTime.year))
        timeEditText.setText(String.format("%02d:%02d",dateTime.hour,dateTime.minute))


        dateEditText.setOnClickListener {
            val c = Calendar.getInstance()
            val year = dateTime.year
            val month = dateTime.monthValue
            val day = dateTime.dayOfMonth

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    //dateEditText.setText(dat)
                    dateEditText.setText(String.format("%02d-%02d-%04d",dayOfMonth,monthOfYear+1,year))
                    selectedDate = String.format("%04d-%02d-%02d",year,monthOfYear+1,dayOfMonth)
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
            val taskDate = binding.date.text.toString()
            val taskTime = binding.time.text.toString()
            viewModel.curTask.value!!.dueTime = "$selectedDate $taskTime"
            viewModel.curTask.value!!.dueTimeForShow = "$taskDate $taskTime"

            if(binding.notif.isChecked){
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
                val test = sharedPreferences.getString("time","")
                if (test != null) {
                    Log.d(TAG, test)
                    viewModel.scheduleNotification(viewModel.curTask.value!!.title,viewModel.curTask.value!!.dueTimeForShow,test.toInt(),viewModel.curTask.value!!.user_id+1,false)
                }else
                    viewModel.scheduleNotification(viewModel.curTask.value!!.title,viewModel.curTask.value!!.dueTimeForShow,0,viewModel.curTask.value!!.user_id+1,false)

            }

            //viewModel.curTask.value!!.notifications = binding.notif.isChecked()
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