package com.dicoding.asclepius.view

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.net.toUri
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.Prediction
import com.dicoding.asclepius.data.local.entity.PredictionEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.Predictions.PredictionViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.snackbar.Snackbar

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
        val cancerScore = intent.getFloatExtra(EXTRA_CANCER_SCORE, 0f)
        val nonCancerScore = intent.getFloatExtra(EXTRA_NON_CANCER_SCORE, 0f)
        val isNew = intent.getBooleanExtra(EXTRA_IS_NEW, true)
        
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val predictionViewModel: PredictionViewModel by viewModels {
            factory
        }
        
        with(binding) {
            if (imageUri != null) {
                resultImage.setImageURI(imageUri.toUri())
            }
            resultText.text = if (cancerScore > nonCancerScore) {
                "Cancer: ${cancerScore * 100}%"
            } else {
                "Non Cancer: ${nonCancerScore * 100}%"
            }
            
            // Piechart
            setupPiechart(cancerScore, nonCancerScore)
            
            // Button Related
            if(!isNew) {
                saveButton.isClickable = false
                saveButton.isActivated = false
                saveButton.isEnabled = false
            }
            
            saveButton.setOnClickListener {
                val prediction = PredictionEntity(
                    imagePath = imageUri ?: "",
                    prediction = if (cancerScore > nonCancerScore) Prediction.CANCER else Prediction.NON_CANCER,
                    cancerScore = cancerScore,
                    nonCancerScore = nonCancerScore,
                    confidence = if (cancerScore > nonCancerScore) cancerScore else nonCancerScore
                )
                predictionViewModel.savePrediction(prediction)
                Snackbar.make(it, "Prediction saved", Snackbar.LENGTH_SHORT).show()
                saveButton.isClickable = false
                saveButton.isActivated = false
                saveButton.isEnabled = false
            }
        }
    }
    
    private fun setupPiechart(cancerScore: Float, nonCancerScore: Float) {
        pieChart = findViewById(R.id.pieChart)
        
        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.setDragDecelerationFrictionCoef(0.95f)
        
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.WHITE)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.setHoleRadius(58f)
        pieChart.setTransparentCircleRadius(61f)

        pieChart.setDrawCenterText(true)

        pieChart.setRotationAngle(0f)

        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)

        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(cancerScore))
        entries.add(PieEntry(nonCancerScore))

        val dataSet = PieDataSet(entries, "Prediction")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.purple_200))
        colors.add(resources.getColor(R.color.eton_blue))

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.setData(data)

        pieChart.highlightValues(null)

        pieChart.invalidate()
    }
    
    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_CANCER_SCORE = "extra_cancer_score"
        const val EXTRA_NON_CANCER_SCORE = "extra_non_cancer_score"
        const val EXTRA_IS_NEW = "extra_is_new"
    }
}