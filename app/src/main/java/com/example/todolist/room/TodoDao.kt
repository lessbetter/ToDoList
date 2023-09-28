package com.example.todolist.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query ("SELECT * FROM task_table")
    fun getAllTasks(): LiveData<MutableList<Task>>

    @Query ("DELETE FROM task_table")
    fun deleteAllRows()

    @Query ("SELECT * from task_table")
    fun getTasksFlow(): Flow<MutableList<Task>>

//    @Query ("SELECT * FROM task_table ORDER BY finishTime ASC")
//    fun getAllTasksSortByAscFinishTime(): LiveData<List<Task>>
}