package com.mimi.translatereminder.repository.local.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.mimi.translatereminder.dto.Entity
import android.arch.persistence.room.Update



/**
 * Created by Mimi on 05/12/2017.
 *
 */
@Dao
interface TranslationDao {
    companion object {
        const val TABLE = "entity"
    }

    @Insert
    fun insertAll(vararg entities: Entity)

    @Update
    fun updateEntity(vararg entity: Entity)


    @Query("SELECT * from $TABLE")
    fun selectAll(): List<Entity>

    @Query("SELECT * from $TABLE WHERE id=:id")
    fun selectItemById(id:Int): List<Entity>

    @Query("SELECT * from $TABLE WHERE germanWord=:german")
    fun getEntitiesByGermanVersion(german: String): List<Entity>

    @Query("SELECT * from $TABLE WHERE translation=:translation")
    fun getEntityByTranslation(translation: String): List<Entity>

    @Delete
    fun delete(vararg entities: Entity)

    @Query("DELETE FROM $TABLE")
    fun deleteAll()
}