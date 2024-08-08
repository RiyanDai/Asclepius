package com.dicoding.asclepius.database

import android.app.Application
import androidx.lifecycle.LiveData

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository(application: Application) {
    private val mNotesDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = HistoryRoomDatabase.getDatabase(application)
        mNotesDao = db.historyDao()
    }

    fun getAllNotes(): LiveData<List<History>> = mNotesDao.getAll()

    fun insert(note: History) {
        executorService.execute { mNotesDao.insert(note) }
    }

}