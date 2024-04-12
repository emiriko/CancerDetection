package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.PredictionEntity

@Dao
interface PredictionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(prediction: PredictionEntity)

    @Query("SELECT * FROM predictions")
    fun getAllPredictions(): LiveData<List<PredictionEntity>>
}