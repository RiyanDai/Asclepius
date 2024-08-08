package com.dicoding.asclepius.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.service.newsRepository
import com.dicoding.asclepius.view.newsAdapter.newsListAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val API_KEY = "f27008ce99ca4ee98f73ff93c39c4a75"
    val mbinding: ActivityNewsBinding by lazy { ActivityNewsBinding.inflate(layoutInflater) }
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter : newsListAdapter
    private var repository =  newsRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mbinding.root)
        recyclerView = mbinding.recyclerViewNews

        bindUIComponents()
        bottomNavigationView.selectedItemId = R.id.bottom_search
        handleTabButtonPress()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val articles = repository.getNews("us", API_KEY)
                newsAdapter = newsListAdapter(articles)
                recyclerView.adapter = newsAdapter
                recyclerView.layoutManager = LinearLayoutManager(parent)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

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
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_history -> {
                    loadActivities(HistoryActivity())
                    return@setOnItemSelectedListener true
                }

            }
            return@setOnItemSelectedListener false
        }
    }

    private fun loadActivities(activity: AppCompatActivity) {
        startActivity(Intent(applicationContext, activity::class.java))
        finish()
    }
}