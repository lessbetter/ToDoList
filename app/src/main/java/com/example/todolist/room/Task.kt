package com.example.todolist.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task (
                @PrimaryKey(autoGenerate = true) var user_id: Int = 0,
                var title: String,
                var description: String,
                var startTime: String,
                var finishTime: String,
                var status: Boolean,            //is it finished or not
                var notifications: Boolean,
                var category: String,
                var attachments: String){


}
