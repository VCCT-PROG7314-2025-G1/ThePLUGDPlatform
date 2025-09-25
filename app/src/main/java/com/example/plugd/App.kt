package com.example.plugd

import android.app.Application
import androidx.room.Room
import com.example.plugd.database.AppDatabase
import com.google.firebase.FirebaseApp

class App : Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Room database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "plugd_db"
        ).build()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

    }
}