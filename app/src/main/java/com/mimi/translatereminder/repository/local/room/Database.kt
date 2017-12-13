package com.mimi.translatereminder.repository.local.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import com.mimi.translatereminder.dto.Entity


/**
 * Created by Mimi on 05/12/2017.
 *
 */

@Database(entities = arrayOf(Entity::class), version = 2)
abstract class Database : RoomDatabase() {
    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                        "ALTER TABLE '${TranslationDao.TABLE}' ADD COLUMN 'state' " +
                                "INTEGER NOT NULL DEFAULT ${Entity.STATE_LEARNING_1}")
            }
        }
    }

    abstract fun translationDao(): TranslationDao

}