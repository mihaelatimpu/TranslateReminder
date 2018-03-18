package com.mimi.translatereminder

import android.app.Application
import android.arch.persistence.room.Room
import com.mimi.translatereminder.repository.TranslationRepository
import com.mimi.translatereminder.repository.local.room.ArchivedDatabase
import com.mimi.translatereminder.repository.local.room.Database
import com.mimi.translatereminder.utils.appModules
import org.koin.android.ext.android.startAndroidContext

/**
 * Created by Mimi on 05/12/2017.
 *
 */

class MainApplication : Application() {
    lateinit var repository: TranslationRepository

    override fun onCreate() {
        super.onCreate()

        val database = Room.databaseBuilder(this,
                Database::class.java, "translation_db")
                .addMigrations(Database.MIGRATION_1_2, Database.MIGRATION_2_3,
                        Database.MIGRATION_3_4, Database.MIGRATION_4_5,
                        Database.MIGRATION_5_6, Database.MIGRATION_6_7)
                .build()
        val archivedDatabase = Room.databaseBuilder(this,
                ArchivedDatabase::class.java, "archived_translation_db")
                .addMigrations(Database.MIGRATION_1_2, Database.MIGRATION_2_3,
                        Database.MIGRATION_3_4, Database.MIGRATION_4_5,
                        Database.MIGRATION_5_6, Database.MIGRATION_6_7)
                .build()
        repository = TranslationRepository(
                database.translationDao(), archivedDatabase.archivedDao())
        // Start Koin
        startAndroidContext(this, appModules())
    }
}