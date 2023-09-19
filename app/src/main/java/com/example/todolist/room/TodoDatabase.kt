package com.example.todolist.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities= [Task::class], version = 3, exportSchema = true)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object{
        private var instance: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase?{
            if(instance == null){
                instance = Room.databaseBuilder(
                    context,
                    TodoDatabase::class.java,
                    "todo_table")
                    .fallbackToDestructiveMigration()
                    .build()

            }
            return instance
        }

        fun deleteInstanceOfDatabase(){
            instance = null
        }
    }
}