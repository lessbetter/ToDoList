package com.example.todolist.fragments

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Telephony.Sms.Intents
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.example.todolist.databinding.FragmentAddTaskBinding
import com.example.todolist.notifications.Notification
import com.example.todolist.notifications.messageExtra
import com.example.todolist.notifications.notificationID
import com.example.todolist.notifications.titleExtra
import com.example.todolist.room.Task
import com.example.todolist.viewmodel.TodoViewModel
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

class AddTaskFragment: Fragment() {
    private val viewModel: TodoViewModel by activityViewModels()
    private lateinit var binding: FragmentAddTaskBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(layoutInflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateEditText: EditText = binding.date
        val timeEditText: EditText = binding.time
        val addButton: Button = binding.addButton
        var dueTime: LocalTime? = null
        var selectedDate: String = ""

        dateEditText.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    //val dat = (dayOfMonth.toString() + "-" + monthOfYear.toString() + "-" + year)
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

        addButton.setOnClickListener {
            val title: String = binding.taskTitle.text.toString()
            val description: String = binding.taskDescription.text.toString()
            val category: String = binding.taskCategory.text.toString()
            val taskDate: String = binding.date.text.toString()
            val taskTime: String = binding.time.text.toString()
            val taskDue: String = "$taskDate $taskTime"
            val notifications: Boolean = binding.notif.isChecked()
            val attachments: String = binding.attachments.text.toString()
            val status = viewModel.isCompleted(taskDue)

            //val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            val current = LocalDateTime.now().format(formatter)
            //Log.d(TAG, notifications.toString())
            Log.d(TAG, title+description+current+taskDue+notifications+category+attachments)
            //val task = Task(title,description,current,selectedDate,taskDue,notifications,category)



            if(title.isNotEmpty() && taskDate.isNotEmpty() && taskTime.isNotEmpty() && category.isNotEmpty() && selectedDate.isNotEmpty() && !status){
                val task = Task(title,description,current,selectedDate,taskDue,status,notifications,category)
                viewModel.insertTask(task)
                binding.taskTitle.setText("")
                binding.taskDescription.setText("")
                binding.taskCategory.setText("")
                binding.date.setText("")
                binding.time.setText("")
                if(binding.notif.isChecked()) {
                    binding.notif.toggle()
                    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
                    val test = sharedPreferences.getString("time","")
                    if (test != null) {
                        Log.d(TAG, test)
                        scheduleNotification(title,taskDue,test.toInt())
                    }else
                        scheduleNotification(title,taskDue,0)

                }
                binding.attachments.setText("")
                Toast.makeText(activity,"Task added to the list",Toast.LENGTH_SHORT).show()
            }
            else if(!status){
                Toast.makeText(activity,"Not enough parameters has been set",Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(activity,"Select a date from the future",Toast.LENGTH_SHORT).show()
            }
        }


    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun scheduleNotification(title: String, date: String, time: Int) {
        val intent = Intent(context, Notification::class.java)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, "There are "+time.toString()+" minutes left to complete this task")

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        var dateTime = LocalDateTime.parse(date,formatter)

        dateTime= dateTime.minusMinutes(time.toLong())

        val tempTime = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context?.let { getSystemService(it, AlarmManager::class.java) } as AlarmManager

        if(alarmManager.canScheduleExactAlarms()){
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                tempTime,
                pendingIntent
            )
        }
    }

}