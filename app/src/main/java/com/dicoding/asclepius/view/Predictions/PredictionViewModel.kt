package com.dicoding.asclepius.view.Predictions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.entity.PredictionEntity
import kotlinx.coroutines.launch

class PredictionViewModel(private val predictionRepository: PredictionRepository) : ViewModel() {
    fun getAllPredictions() = predictionRepository.getAllPredictions()
    
    fun savePrediction(prediction: PredictionEntity) = viewModelScope.launch {
        predictionRepository.savePrediction(prediction)
    }
}