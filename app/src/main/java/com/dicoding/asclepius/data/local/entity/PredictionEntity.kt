package com.dicoding.asclepius.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "predictions")
data class PredictionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    
    @ColumnInfo(name = "imagePath")
    var imagePath: String = "",

    @ColumnInfo(name = "prediction")
    var prediction: Prediction,

    @ColumnInfo(name = "cancer_score")
    var cancerScore: Float = 0f,
    
    @ColumnInfo(name = "non_cancer_score")
    var nonCancerScore: Float = 0f,
    
    @ColumnInfo(name = "confidence")
    var confidence: Float = 0f,
)

enum class Prediction {
    CANCER,
    NON_CANCER,
}