package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.PredictionEntity

@Database(entities = [PredictionEntity::class], version = 1)
abstract class PredictionDatabase : RoomDatabase() {

    abstract fun predictionDao(): PredictionDao

    companion object {
        @Volatile
        private var INSTANCE: PredictionDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): PredictionDatabase {
            if (INSTANCE == null) {
                synchronized(PredictionDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        PredictionDatabase::class.java, "Predictions.db"
                    )
                        .build()
                }
            }
            return INSTANCE as PredictionDatabase
        }
    }
}