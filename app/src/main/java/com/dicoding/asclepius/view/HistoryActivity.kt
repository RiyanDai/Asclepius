package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.helper.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        bindUIComponents()
        bottomNavigationView.selectedItemId = R.id.bottom_history
        handleTabButtonPress()

        historyViewModel = obtainViewModel(this@HistoryActivity)

        historyViewModel.getAllNotes().observe(this) {
            showRecyclerView(it as ArrayList<History>)
        }


    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[HistoryViewModel::class.java]
    }

    private fun showRecyclerView(historyList: ArrayList<History>) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvNotes.layoutManager = layoutManager
        binding.rvNotes.setHasFixedSize(true)
        val adapter = HistoryAdapter(historyList)
        binding.rvNotes.adapter = adapter
    }


    private fun bindUIComponents() {
        bottomNavigationView = findViewById(R.id.bottomNav)
    }
    private fun handleTabButtonPress() {
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_home -> {
                    loadActivities(MainActivity())
                    return@setOnItemSelectedListener true
                }

                R.id.bottom_search -> {
                    loadActivities(NewsActivity())
                    return@setOnItemSelectedListener true
                }

                R.id.bottom_history -> {
                    return@setOnItemSelectedListener true
                }

            }
            return@setOnItemSelectedListener false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun loadActivities(activity: AppCompatActivity) {
        startActivity(Intent(applicationContext, activity::class.java))
        finish()
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private const val READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES
        private const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    }
}