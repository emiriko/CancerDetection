package com.dicoding.asclepius.view.Predictions

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.asclepius.data.local.entity.PredictionEntity
import com.dicoding.asclepius.data.local.room.PredictionDao
import com.dicoding.asclepius.helper.Result

class PredictionRepository private constructor(
    private val predictionDao: PredictionDao,
) {
    fun getAllPredictions(): LiveData<Result<List<PredictionEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val localData: LiveData<Result<List<PredictionEntity>>> =
                predictionDao.getAllPredictions().map { Result.Success(it) }
            emitSource(localData)
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "An error occurred"))
        }
    }
    
    suspend fun savePrediction(prediction: PredictionEntity) {
        predictionDao.insert(prediction)
    }
    
    companion object {
        @Volatile
        private var instance: PredictionRepository? = null
        fun getInstance(
            predictionDao: PredictionDao,
        ): PredictionRepository =
            instance ?: synchronized(this) {
                instance ?: PredictionRepository(predictionDao)
            }.also { instance = it }
    }
}