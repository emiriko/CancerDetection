package com.dicoding.asclepius.view.Predictions

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.local.entity.PredictionEntity
import com.dicoding.asclepius.databinding.ActivityPredictionBinding
import com.dicoding.asclepius.helper.Result
import com.dicoding.asclepius.view.ResultActivity
import com.dicoding.asclepius.view.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class PredictionActivity : AppCompatActivity() {
    private val binding: ActivityPredictionBinding by lazy {
        ActivityPredictionBinding.inflate(layoutInflater)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)

        val viewModel: PredictionViewModel by viewModels {
            factory
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvPrediction.layoutManager = layoutManager
        
        viewModel.getAllPredictions().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvPrediction.adapter = PredictionAdapter().apply {
                        submitList(result.data)
                        
                        setOnItemClickCallback(object : PredictionAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: PredictionEntity) {
                                Intent(
                                    this@PredictionActivity,
                                    ResultActivity::class.java
                                ).also {
                                    it.putExtra(ResultActivity.EXTRA_IMAGE_URI, data.imagePath)
                                    it.putExtra(ResultActivity.EXTRA_CANCER_SCORE, data.cancerScore)
                                    it.putExtra(ResultActivity.EXTRA_NON_CANCER_SCORE, data.nonCancerScore)
                                    it.putExtra(ResultActivity.EXTRA_IS_NEW, false)
                                    startActivity(it)
                                }
                            }
                        })
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, result.error, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}