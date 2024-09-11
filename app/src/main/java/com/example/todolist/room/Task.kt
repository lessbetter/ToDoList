package com.example.todolist.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
class Task (
                var title: String,
                var description: String,
                var createTime: String,
                var dueTime: String,
                var dueTimeForShow: String,
                var status: Boolean,            //true: completed       false: not completed yet
                var notifications: Boolean,
                var category: String,
                var attachments: String,
                @PrimaryKey(autoGenerate = true) var user_id: Int = 0){


}
