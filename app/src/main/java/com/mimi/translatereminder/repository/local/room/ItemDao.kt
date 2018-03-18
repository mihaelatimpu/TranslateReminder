package com.mimi.translatereminder.repository.local.room

import com.mimi.translatereminder.dto.Entity

/**
 * Created by Mimi on 18/03/2018.
 */

interface ItemDao {

    fun insert(entity: Entity): Long

    fun updateEntity(vararg entity: Entity)


    fun selectAll(): List<Entity>

    fun selectItemById(id: Int): List<Entity>

    fun findSentences(itemId: Int): List<Entity>

    fun delete(vararg entities: Entity)

    fun deleteAll()
}