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

    @Query ("SELECT * FROM task_table ORDER BY dueTime ASC")
    fun getAllTasksSortByAscFinishTime(): LiveData<MutableList<Task>>

    @Query ("SELECT * FROM task_table WHERE status=0 ORDER BY dueTime ASC")
    fun getNotCompletedTasks(): LiveData<MutableList<Task>>

    @Query ("SELECT * FROM task_table WHERE category=:category ORDER BY dueTime ASC")
    fun getCategoryTasks(category: String): LiveData<MutableList<Task>>

    @Query ("SELECT * FROM task_table WHERE category=:category AND status=0 ORDER BY dueTime ASC")
    fun getCategoryNotCompletedTasks(category: String): LiveData<MutableList<Task>>

    @Query ("SELECT * FROM task_table WHERE title LIKE :title  ORDER BY dueTime ASC")
    fun searchAll(title: String): LiveData<MutableList<Task>>

    @Query ("SELECT * FROM task_table WHERE status=0 AND title LIKE :title or LOWER(title) like LOWER(:title) ORDER BY dueTime ASC")
    fun searchUnfinished(title: String): LiveData<MutableList<Task>>

    @Query ("SELECT * FROM task_table WHERE category=:category AND title LIKE :title or LOWER(title) like LOWER(:title) ORDER BY dueTime ASC")
    fun searchAllCategorised(category: String, title: String): LiveData<MutableList<Task>>

    @Query ("SELECT * FROM task_table WHERE category=:category AND status=0 AND title LIKE :title or LOWER(title) like LOWER(:title) ORDER BY dueTime ASC")
    fun searchUnfinishedCategorised(category: String, title: String): LiveData<MutableList<Task>>

    @Query ("SELECT DISTINCT category FROM task_table ORDER BY category ASC")
    fun getCategories(): LiveData<MutableList<String>>

    @Query ("SELECT user_id FROM task_table ORDER BY user_id DESC LIMIT 1")
    fun getLastID(): LiveData<Int>
}