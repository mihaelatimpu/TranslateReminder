package com.mimi.translatereminder.repository.local.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import com.mimi.translatereminder.MainApplication
import com.mimi.translatereminder.dto.Entity
import java.util.*

/**
 * Created by Mimi on 18/03/2018.
 */

@Database(entities = [(Entity::class)], version = MainApplication.DB_VERSION)
abstract class ArchivedDatabase : RoomDatabase() {
    abstract fun archivedDao(): ArchivedDao
}
