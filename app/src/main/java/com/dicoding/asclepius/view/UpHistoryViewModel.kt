package com.dicoding.asclepius.view

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.database.HistoryRepository

class UpHistoryViewModel (application: Application) : ViewModel() {

    private val mNoteRepository: HistoryRepository = HistoryRepository(application)

    fun insert(note: History) {
        mNoteRepository.insert(note)
    }


}