package com.example.todolist.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTaskBinding
import com.example.todolist.notifications.Notification
import com.example.todolist.notifications.messageExtra
import com.example.todolist.notifications.notificationID
import com.example.todolist.notifications.titleExtra
import com.example.todolist.viewmodel.TodoViewModel
import java.io.File


class TaskFragment: Fragment() {
    private val viewModel: TodoViewModel by activityViewModels()
    private lateinit var binding: FragmentTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = it.data?.data
                // Use this Uri
            }
        }

        val title: TextView = binding.taskTitle
        val description: TextView = binding.taskDescription
        val category: TextView = binding.taskCategory
        val dueTime: TextView = binding.taskDuetime
        val status: TextView = binding.status
        val deleteBTN: Button = binding.deleteBtn
        val editBTN: Button = binding.editBtn
        val attachments: Spinner = binding.spinner2
        var att_text: TextView = binding.attText


        title.text = viewModel.curTask.value!!.title
        description.text = viewModel.curTask.value!!.description
        category.text = viewModel.curTask.value!!.category
        dueTime.text = viewModel.curTask.value!!.dueTimeForShow

        if(viewModel.curTask.value!!.status==true){
            status.text = "Deadline for this task has been reached"
        }else{
            status.text = "Deadline for this task has not yet been reached"
        }
        if(viewModel.curTask.value!!.attachments.length>2){
            attachments.visibility = View.VISIBLE
            att_text.visibility = View.VISIBLE
            var attachmentsList: List<String> = convertToList(viewModel.curTask.value!!.attachments)
            var namesList: List<String> = getNamesFromUri(attachmentsList)
            attachments.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,namesList)
            //attachments.setSelection(attachments.selectedItemPosition, false)
            var flag: Int = 0
            attachments.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if(++flag>1 && p3>0){
                        val path = attachmentsList[p3.toInt()-1]
                        val uri: Uri = Uri.parse(path)

                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(uri)
                        intent.flags = FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION
                        Log.d(TAG,uri.toString())
                        resultLauncher.launch(intent)
                    }


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }
        deleteBTN.setOnClickListener {
            if(viewModel.curTask.value!!.notifications){
                viewModel.scheduleNotification(viewModel.curTask.value!!.title,viewModel.curTask.value!!.dueTimeForShow,5,viewModel.curTask.value!!.user_id+1,true)
//                val manager = context?.let { it1 -> getSystemService(it1, AlarmManager::class.java) } as AlarmManager
//                //manager.cancel(viewModel.curTask.value!!.user_id)
//
//                val intent = Intent(context, Notification::class.java)
//                intent.putExtra(notificationID.toString(),viewModel.curTask.value!!.user_id)
//                intent.putExtra(titleExtra,viewModel.curTask.value!!.title)
//                intent.putExtra(messageExtra,"There are 5 minutes left to complete this task")
//                val pendingIntent = PendingIntent.getBroadcast(context,viewModel.curTask.value!!.user_id,intent,
//                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//                manager.cancel(pendingIntent)
//                pendingIntent.cancel()
            }
            viewModel.deleteTask(viewModel.curTask.value!!)
            findNavController().popBackStack()
        }
        editBTN.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_editTaskFragment)
        }
    }

    private fun getNamesFromUri(attachmentsList: List<String>): List<String> {
        var lista: MutableList<String> = mutableListOf("")
        attachmentsList.forEach {
            lista.add(getNameFromURI(it.toUri()))
        }
        return lista as List<String>
    }

    private fun convertToList(attachments: String): List<String> {
       return attachments.split(";")
    }
    @SuppressLint("Range")
    fun getNameFromURI(uri: Uri): String {
        val c: Cursor? = requireContext().contentResolver.query(uri, null, null, null, null)
        if (c != null) {
            c.moveToFirst()
            return c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        else
            return ""

    }
}