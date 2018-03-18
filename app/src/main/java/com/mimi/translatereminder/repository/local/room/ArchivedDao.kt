package com.mimi.translatereminder.repository.local.room

import android.arch.persistence.room.*
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.repository.local.room.EntityDao.Companion.TABLE
import com.mimi.translatereminder.repository.local.room.EntityDao.Companion.id
import com.mimi.translatereminder.repository.local.room.EntityDao.Companion.nextReview
import com.mimi.translatereminder.repository.local.room.EntityDao.Companion.parentId
import com.mimi.translatereminder.repository.local.room.EntityDao.Companion.state
import com.mimi.translatereminder.repository.local.room.EntityDao.Companion.type


/**
 * Created by Mimi on 05/12/2017.
 *
 */
@Dao
interface ArchivedDao:ItemDao {

    @Insert
    override fun insert(entity: Entity):Long

    @Update
    override fun updateEntity(vararg entity: Entity)


    @Query("SELECT * from $TABLE "+
            "ORDER BY $state ASC, $nextReview ASC")
    override fun selectAll(): List<Entity>

    @Query("SELECT * from $TABLE WHERE $id=:id")
    override fun selectItemById(id:Int): List<Entity>

    @Query("SELECT * from $TABLE " +
            "WHERE $type = ${Entity.TYPE_SENTENCE} " +
            "AND $parentId = :itemId")
    override fun findSentences(itemId:Int):List<Entity>

    @Delete
    override fun delete(vararg entities: Entity)

    @Query("DELETE FROM $TABLE")
    override fun deleteAll()
}