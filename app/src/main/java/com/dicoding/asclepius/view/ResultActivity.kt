package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.database.HistoryRoomDatabase
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.helper.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var historyResult: History? = null
    private lateinit var nviewModelHis: UpHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nviewModelHis = obtainViewModel(this@ResultActivity)

        // Menampilkan hasil gambar, prediksi, dan confidence score.
        val imageString = intent.getStringExtra(EXTRA_IMAGE_URI)
        if (imageString != null) {
            val imageUri = Uri.parse(imageString)
            displayImage(imageUri)

            val imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        Log.d(TAG, "Error: $error")
                    }

                    override fun onResults(result: List<Classifications>?, inferenceTime: Long) {
                        result?.let { showResult(it, imageString) }
                        nviewModelHis.insert(historyResult!!)
                        showToast("Success Save To History")
                    }
                }
            )
            imageClassifierHelper.classifyStaticImage(imageUri)
        } else {
            Log.e(TAG, "No Image Provided")
            finish()
        }


    }

    private fun obtainViewModel(activity: AppCompatActivity): UpHistoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[UpHistoryViewModel::class.java]
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun showResult(result: List<Classifications>, uri: String) {
        val topResult = result[0]
        val label = topResult.categories[0].label
        val score = topResult.categories[0].score

        fun Float.formatToString(): String {
            return String.format("%.2f%%", this * 100)
        }

        // Membuat objek History dari hasil prediksi
        historyResult = History(
            uri = uri,
            label = label,
            confidence = score,
        )


        // Menampilkan hasil prediksi di UI
        binding.resultText.text = "$label ${score.formatToString()}"
    }



    private fun displayImage(uri: Uri) {
        Log.d(TAG, "Display Image: $uri")
        binding.resultImage.setImageURI(uri)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "img_uri"
        const val TAG = "imagepPicker"
    }
}
