package com.mimi.translatereminder.repository.local.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.mimi.translatereminder.dto.Translation

/**
 * Created by Mimi on 05/12/2017.
 *
 */
@Dao
interface TranslationDao {
    @Insert
    fun insertAll(vararg translations:Translation)

    @Query("SELECT * from translation")
    fun selectAll():List<Translation>

    @Delete
    fun deleteAll(vararg translations:Translation)
}