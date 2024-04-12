package com.dicoding.asclepius.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.News.NewsActivity
import com.dicoding.asclepius.view.Predictions.PredictionActivity
import com.yalantis.ucrop.UCrop
import java.io.File
    
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var predictionPositive: String
    private lateinit var predictionNegative: String
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        with(binding) {
            galleryButton.setOnClickListener { startGallery() }
            analyzeButton.setOnClickListener { analyzeImage() }
            newsButton.setOnClickListener { moveToNews() }
            historyButton.setOnClickListener { moveToPrediction() }
        }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            UCrop.of(uri, Uri.fromFile(File.createTempFile("temp", ".jpg", cacheDir)))
                .withAspectRatio(1f, 1f)
                .start(this)
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    
    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        if (currentImageUri != null) {
            imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onResults(cancerScore: Float, nonCancerScore: Float) {
                        predictionPositive = "Cancer: ${cancerScore * 100}%"
                        predictionNegative = "Non-Cancer: ${nonCancerScore * 100}%"
                        moveToResult(currentImageUri!!, cancerScore, nonCancerScore)
                    }
                    
                    override fun onError(error: String) {
                        showToast(error)
                    }
                }
            )
            
            imageClassifierHelper.classifyStaticImage(currentImageUri!!)
        } else {
            showToast("No image selected")
        }
        
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            currentImageUri = resultUri
            showImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Log.e("Crop Error", "Crop error: $cropError")
        }
    }
    
    private fun moveToPrediction() {
        val intent = Intent(this, PredictionActivity::class.java)
        startActivity(intent)
    }
    
    private fun moveToNews() {
        val intent = Intent(this, NewsActivity::class.java)
        startActivity(intent)
    }
    
    private fun moveToResult(imageUri: Uri, cancerScore: Float, nonCancerScore: Float) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri.toString())
        intent.putExtra(ResultActivity.EXTRA_CANCER_SCORE, cancerScore)
        intent.putExtra(ResultActivity.EXTRA_NON_CANCER_SCORE, nonCancerScore)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}