package com.mimi.translatereminder.repository

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.repository.local.room.ArchivedDao

/**
 * Created by Mimi on 18/03/2018.
 */

class ArchivedTestDao(var list: ArrayList<Entity>) : ArchivedDao {
    override fun insert(entity: Entity): Long {
        entity.id = list.size
        list.add(entity)
        return entity.id.toLong()
    }

    override fun updateEntity(vararg entity: Entity) {
    }

    override fun selectAll() = list

    override fun selectItemById(id: Int) = list.filter { it.id == id }

    override fun findSentences(itemId: Int) = list.filter { it.type == Entity.TYPE_SENTENCE && it.parentId == itemId }

    override fun delete(vararg entities: Entity) {
        list.removeAll(entities)
    }

    override fun deleteAll() {
        list.clear()
    }

}