package com.dicoding.asclepius.view
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private var currentImageUri: Uri? = null
    private val requestPermissionLauncher =

        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun checkForPermission() : Boolean {
        val requiredPermission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            READ_MEDIA_IMAGES
        } else {
            READ_EXTERNAL_STORAGE
        }

        val permission = ContextCompat.checkSelfPermission(
            this,
            requiredPermission
        )

        return permission == PackageManager.PERMISSION_GRANTED

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!checkForPermission()){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                requestPermissionLauncher.launch(READ_MEDIA_IMAGES)
            } else {
                requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
            }
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage(it)
            } ?: run {
                showToast("No image picked")
            }
        }

        bindUIComponents()
        bottomNavigationView.selectedItemId = R.id.bottom_home
        handleTabButtonPress()

    }

    private fun bindUIComponents() {
        bottomNavigationView = findViewById(R.id.bottomNav)
    }
    private fun handleTabButtonPress() {
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_home -> {
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_search -> {
                    loadActivities(NewsActivity())
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



    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        binding.previewImageView.setImageURI(currentImageUri)
    }

    private fun analyzeImage(uri: Uri) {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        moveToResult(uri)
    }

    private fun moveToResult(uri: Any?) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        const val READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES
        const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    }

}