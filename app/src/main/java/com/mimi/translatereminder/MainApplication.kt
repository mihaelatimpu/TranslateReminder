package com.mimi.translatereminder

import android.app.Application
import android.arch.persistence.room.Room
import com.mimi.translatereminder.repository.local.room.Database
import com.mimi.translatereminder.repository.local.room.TranslationDao
import com.mimi.translatereminder.utils.appModules
import org.koin.android.ext.android.startAndroidContext

/**
 * Created by Mimi on 05/12/2017.
 *
 */

class MainApplication : Application(){
    lateinit var translationDao:TranslationDao

    override fun onCreate() {
        super.onCreate()

        val database = Room.databaseBuilder(this,
                Database::class.java, "translation_db")
                .addMigrations(Database.MIGRATION_1_2, Database.MIGRATION_2_3)
                .build()
        translationDao = database.translationDao()
        // Start Koin
        startAndroidContext(this, appModules())
    }
}