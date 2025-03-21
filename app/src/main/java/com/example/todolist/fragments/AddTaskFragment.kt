package com.example.todolist.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.provider.Telephony.Sms.Intents
import android.text.style.TabStopSpan.Standard
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.INVISIBLE
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.AdapterView.VISIBLE
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CursorAdapter
import android.widget.EditText
import android.widget.SimpleAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.todolist.R
import com.example.todolist.adapters.MySpinnerAdapter
import com.example.todolist.data.AttachmentContent
import com.example.todolist.databinding.FragmentAddTaskBinding
import com.example.todolist.notifications.Notification
import com.example.todolist.notifications.messageExtra
import com.example.todolist.notifications.notificationID
import com.example.todolist.notifications.titleExtra
import com.example.todolist.room.Task
import com.example.todolist.viewmodel.TodoViewModel
import java.io.File
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.io.path.toPath

class AddTaskFragment: Fragment() {
    private val viewModel: TodoViewModel by activityViewModels()
    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var filePath: String
    private lateinit var adapter: MySpinnerAdapter
    //private lateinit var getResult:

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddTaskBinding.inflate(layoutInflater)
        return binding.root
    }


    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var attachmentFiles: MutableList<AttachmentContent> = mutableListOf()

        val getResult = registerForActivityResult(ActivityResultContracts.OpenDocument()){
            if(it?.toString()?.isNotEmpty() == true){
                viewModel.setFilePath(it.toString())
                requireContext().contentResolver.query(it,null,null,null,null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    cursor.moveToFirst()

                    attachmentFiles.add(AttachmentContent(it, Path.of(it.path),cursor.getString(nameIndex)))
                }
                requireContext().contentResolver?.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        val temp = viewModel.getLastID()

        temp.observe(viewLifecycleOwner){
            if(it != null){
                viewModel.setLastID(it)
            }else{
                viewModel.setLastID(0)
            }
        }
        var thisLastId: Int

        var categoryList: Array<CharSequence> = arrayOf()

        viewModel.categoryList.value?.forEach {
            categoryList += it
        }
        categoryList+="Set new category"

        var filesList: MutableList<String> = mutableListOf()


        val dateEditText: EditText = binding.date
        val timeEditText: EditText = binding.time
        val addButton: Button = binding.addButton
        var dueTime: LocalTime? = null
        var selectedDate: String = ""
        val attachmentsButton: Button = binding.addAttachment
        val attachmentsText: TextView = binding.attachments
        var filesString: String = ""
        var catText: String = ""
        //val attachmentsEditText: EditText = binding.attachments
        //var lastID = viewModel.getLastID()

        val spinner: Spinner = binding.spinner3

//        ArrayAdapter.createFromResource(requireContext(),categoryList,android.R.layout.simple_spinner_item).also{ adapter ->
//
//        }
        //adapter = MySpinnerAdapter()
        ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,categoryList).also{ adapter ->
            spinner.adapter = adapter
        }

        //spinner.onItemSelectedListener = MySpinnerAdapter()
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                TODO("Not yet implemented")
                if (parent != null) {
                    catText = parent.getItemAtPosition(position).toString()
                    if(catText == "Set new category"){
                        binding.taskCategory.visibility = VISIBLE
                        binding.taskCategory.setText("")
                    }
                    else{
                        binding.taskCategory.visibility = INVISIBLE
                        binding.taskCategory.setText(parent.getItemAtPosition(position).toString())
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
            }

        }

        //binding.taskCategory.text = spinner.onItemSelectedListener.toString()

//        spinner.onItemSelectedListener.also { onItemSelectedListener ->
//            override fun onItemSelected(){
//
//            }
//        }

//        AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
//                // An item is selected. You can retrieve the selected item using
//                // parent.getItemAtPosition(pos).
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Another interface callback.
//            }
//        }

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

        viewModel.filePath.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                if(filesString.length>2){
                    filesString += ";"+it
                }else{
                    filesString += it
                }
                filesList.add(it)
                attachmentsText.text = filesString
                //attachmentsEditText.setText(it)
            }
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

        attachmentsButton.setOnClickListener {
            //val intent = Intent().setType("*/*")
            //getResult.launch("*/*")'
            //val intent = Intent().setType("*/*")
            //getResult.
            val temp: Array<String> = arrayOf(Intent.CATEGORY_OPENABLE,"*/*")
            getResult.launch(temp)
            if(viewModel.filePath.value!= null){
                Log.d(TAG,viewModel.filePath.value.toString())
            }


        }

        addButton.setOnClickListener {
            if(binding.taskCategory.text.toString() == "Set new category"){
                Toast.makeText(activity,"This category name is not permitted",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(binding.date.text.isNotEmpty() && binding.time.text.isNotEmpty()){
                val title: String = binding.taskTitle.text.toString()
                val description: String = binding.taskDescription.text.toString()
                val category: String = binding.taskCategory.text.toString()
                val taskDate: String = binding.date.text.toString()
                val taskTime: String = binding.time.text.toString()
                val taskDue: String = "$selectedDate $taskTime"
                val taskDueForShow: String = "$taskDate $taskTime"
                val notifications: Boolean = binding.notif.isChecked()
                val attachments: String = binding.attachments.text.toString()
                val status = viewModel.isCompleted(taskDueForShow)

                //val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                val current = LocalDateTime.now().format(formatter)
                //Log.d(TAG, notifications.toString())
                Log.d(TAG, title+description+current+taskDueForShow+notifications+category+attachments)
                //val task = Task(title,description,current,selectedDate,taskDue,notifications,category)
//                if(viewModel.lastId.value!=null){
//                    Log.d(TAG,viewModel.lastId.value.toString())
//                }



                if(title.isNotEmpty() && taskDate.isNotEmpty() && taskTime.isNotEmpty() && category.isNotEmpty() && selectedDate.isNotEmpty() && !status){
                    val task = Task(title,description,current,taskDue,taskDueForShow,status,notifications,category,filesString)
                    viewModel.insertTask(task)

                    if(viewModel.lastId.value!=null){
                        thisLastId = viewModel.lastId.value!!
                    }else{
                        thisLastId = 0
                    }

                    binding.taskTitle.setText("")
                    binding.taskDescription.setText("")
                    binding.taskCategory.setText("")
                    binding.date.setText("")
                    binding.time.setText("")
                    filesString = ""
                    if(binding.notif.isChecked()) {
                        binding.notif.toggle()
                        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
                        val test = sharedPreferences.getString("time","5")
                        if (test != null) {
                            Log.d(TAG, test)
                            viewModel.scheduleNotification(title,taskDueForShow,test.toInt(),thisLastId+1,false)
                            //scheduleNotification(title,taskDueForShow,test.toInt(),thisLastId+1)
                        }else
                            viewModel.scheduleNotification(title,taskDueForShow,0,thisLastId+1,false)
                            //scheduleNotification(title,taskDueForShow,0,thisLastId+1)

                    }
                    if(attachmentFiles.isNotEmpty()){
                        attachmentFiles.forEach({ file ->
                            var tempFile = File(requireContext().filesDir.path)
                            //var tempFile2 = File(URI.create())
                            var tempURI = URI(file.fileUri.toString())
                            var ttemp = file.fileUri.toFile()

                            Files.copy(ttemp.toPath(),requireContext().filesDir.toPath(),StandardCopyOption.REPLACE_EXISTING)
                            //tempFile2.copy
                        })
                    }
                    if(filesList.isNotEmpty()){

                        filesList.forEach({ file ->
                            //var tempFile = File(context.filesDir,)
                        })
                    }
                    binding.attachments.text = ""
                    Toast.makeText(activity,"Task added to the list",Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_addTaskFragment_to_mainFragment)
                    //lastID = viewModel.getLastID()
                }
                else if(!status){
                    Toast.makeText(activity,"Not enough parameters has been set",Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(activity,"Select a date from the future",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(activity,"Not enough parameters has been set",Toast.LENGTH_SHORT).show()
            }

        }


    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun scheduleNotification(title: String, date: String, time: Int, id: Int) {
        val intent = Intent(context, Notification::class.java)
        intent.putExtra(notificationID.toString(),id)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, "There are "+time.toString()+" minutes left to complete this task")


        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        var dateTime = LocalDateTime.parse(date,formatter)

        dateTime= dateTime.minusMinutes(time.toLong())

        val tempTime = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
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

    fun openFile(pickerInitialURI: URI){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply{
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialURI)
        }

        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val filePath = it.data?.data?.path
            }
        }

        //registerForActivityResult(Intent.createChooser(intent, "Select a file"),777)

        //startActivity(intent)
    }

}