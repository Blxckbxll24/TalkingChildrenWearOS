package com.blxckbxll24.talkingchildrenwearos.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.blxckbxll24.talkingchildrenwearos.domain.model.ActivityData
import com.blxckbxll24.talkingchildrenwearos.domain.model.HeartRateData
import com.blxckbxll24.talkingchildrenwearos.domain.model.SensorData

@Database(
    entities = [HeartRateData::class, ActivityData::class, SensorData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TalkingChildrenDatabase : RoomDatabase() {
    
    abstract fun heartRateDao(): HeartRateDao
    abstract fun activityDao(): ActivityDao
    abstract fun sensorDao(): SensorDao
    
    companion object {
        @Volatile
        private var INSTANCE: TalkingChildrenDatabase? = null
        
        fun getDatabase(context: Context): TalkingChildrenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TalkingChildrenDatabase::class.java,
                    "talking_children_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}