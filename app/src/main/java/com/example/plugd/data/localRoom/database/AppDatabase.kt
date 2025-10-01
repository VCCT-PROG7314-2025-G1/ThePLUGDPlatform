package com.example.plugd.data.localRoom.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.plugd.data.localRoom.dao.UserDao
import com.example.plugd.data.localRoom.dao.EventDao
import com.example.plugd.data.localRoom.entity.UserEntity
import com.example.plugd.data.localRoom.entity.EventEntity

@Database(entities = [UserEntity::class, EventEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // important for development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}