package com.mimi.translatereminder.repository.local.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mimi.translatereminder.dto.Entity

/**
 * Created by Mimi on 05/12/2017.
 *
 */

@Database(entities = arrayOf(Entity::class), version = 1)
abstract class Database : RoomDatabase() {
    abstract fun translationDao(): TranslationDao
}