package com.mimi.translatereminder.repository.local.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import com.mimi.translatereminder.dto.Entity
import java.util.*

/**
 * Created by Mimi on 18/03/2018.
 */

@Database(entities = [(Entity::class)], version = 7)
abstract class ArchivedDatabase : RoomDatabase() {
    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                        "ALTER TABLE '${EntityDao.TABLE}' ADD COLUMN 'state' " +
                                "INTEGER NOT NULL DEFAULT ${Entity.firstLearningState}")
            }
        }
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                        "ALTER TABLE '${EntityDao.TABLE}' ADD COLUMN 'nextReview' " +
                                "INTEGER NOT NULL DEFAULT ${Calendar.getInstance().timeInMillis}")
                database.execSQL(
                        "ALTER TABLE '${EntityDao.TABLE}' ADD COLUMN 'stateBeforeBeingWrong' " +
                                "INTEGER NOT NULL DEFAULT ${Entity.firstLearningState}")
            }
        }
        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                replace(database, 5, Entity.firstReviewState)
                replace(database, 9, Entity.firstMistakeState)
            }
        }
        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                        "ALTER TABLE '${EntityDao.TABLE}' ADD COLUMN 'lastReview' " +
                                "INTEGER NOT NULL DEFAULT ${Calendar.getInstance().timeInMillis}")
            }
        }
        val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                        "ALTER TABLE '${EntityDao.TABLE}' ADD COLUMN 'type' " +
                                "INTEGER NOT NULL DEFAULT ${Entity.TYPE_WORD}")
            }
        }
        val MIGRATION_6_7: Migration = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                        "ALTER TABLE '${EntityDao.TABLE}' ADD COLUMN 'parentId' " +
                                "INTEGER NOT NULL DEFAULT -1")
            }
        }

        private fun replace(database: SupportSQLiteDatabase, what: Int, with: Int) {
            database.execSQL(
                    "UPDATE ${EntityDao.TABLE} SET ${EntityDao.state} = $with " +
                            "WHERE ${EntityDao.state} = $what")

        }
    }

    abstract fun archivedDao(): ArchivedDao
}
