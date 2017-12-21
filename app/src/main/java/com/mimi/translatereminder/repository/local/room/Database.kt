package com.mimi.translatereminder.repository.local.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import com.mimi.translatereminder.dto.Entity
import java.util.*


/**
 * Created by Mimi on 05/12/2017.
 *
 */

@Database(entities = [(Entity::class)], version = 5)
abstract class Database : RoomDatabase() {
    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                        "ALTER TABLE '${TranslationDao.TABLE}' ADD COLUMN 'state' " +
                                "INTEGER NOT NULL DEFAULT ${Entity.firstLearningState}")
            }
        }
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                        "ALTER TABLE '${TranslationDao.TABLE}' ADD COLUMN 'nextReview' " +
                                "INTEGER NOT NULL DEFAULT ${Calendar.getInstance().timeInMillis}")
                database.execSQL(
                        "ALTER TABLE '${TranslationDao.TABLE}' ADD COLUMN 'stateBeforeBeingWrong' " +
                                "INTEGER NOT NULL DEFAULT ${Entity.firstLearningState}")
            }
        }
        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                replace(database, 5, Entity.firstReviewState)
                replace(database, 9, Entity.firstMistakeState)
            }
        }
        val MIGRATION_4_5: Migration = object : Migration(4,5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                        "ALTER TABLE '${TranslationDao.TABLE}' ADD COLUMN 'lastReview' " +
                                "INTEGER NOT NULL DEFAULT ${Calendar.getInstance().timeInMillis}")
            }
        }

        private fun replace(database: SupportSQLiteDatabase, what: Int, with: Int) {
            database.execSQL(
                    "UPDATE ${TranslationDao.TABLE} SET ${TranslationDao.state} = $with " +
                            "WHERE ${TranslationDao.state} = $what")

        }
    }

    abstract fun translationDao(): TranslationDao

}