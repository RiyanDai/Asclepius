package com.dicoding.asclepius.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history : History)

    @Query("SELECT * from history ORDER BY imageUri ASC")
    fun getAll(): LiveData<List<History>>
}