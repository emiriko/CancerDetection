package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.local.room.PredictionDatabase
import com.dicoding.asclepius.view.Predictions.PredictionRepository

object Injection {
    fun provideRepository(context: Context): PredictionRepository {
        val database = PredictionDatabase.getInstance(context)
        val dao = database.predictionDao()
        return PredictionRepository.getInstance(dao)
    }
}