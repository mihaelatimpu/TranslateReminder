package com.mimi.translatereminder.repository

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.repository.local.room.EntityDao

/**
 * Created by Mimi on 18/03/2018.
 */
class EntityTestDao(var list: ArrayList<Entity>) : EntityDao {
    override fun insert(entity: Entity): Long {
        entity.id = list.size
        list.add(entity)
        return entity.id.toLong()
    }

    override fun updateEntity(vararg entity: Entity) {
    }

    override fun selectAll() = list

    override fun selectItemById(id: Int) = list.filter { it.id == id }

    override fun getEntitiesByGermanVersion(german: String) = list.filter { it.germanWord == german }

    override fun getEntityByTranslation(translation: String) = list.filter { it.translation == translation }

    override fun getEntityByState(state: Int, limit: Int) = list.filter { it.state == state }.take(limit)

    override fun getRandomItems(excludeId: Int, limit: Int) = list.filter { it.id != excludeId }.shuffled().take(limit)

    override fun getLearningItems(limit: Int) = list.filter { Entity.isLearningState(it.state) }.shuffled().take(limit)

    override fun getReviewItems(limit: Int, currentTimeInMillis: Long) = list.filter { Entity.isReviewingState(it.state) }.shuffled().take(limit)

    override fun getOverflow(limit: Int) = list.shuffled().take(limit)

    override fun getMistakenItems(limit: Int) = list.filter { Entity.isWrongState(it.state) }.shuffled().take(limit)

    override fun findSentences(itemId: Int) = list.filter { it.type == Entity.TYPE_SENTENCE && it.parentId == itemId }

    override fun delete(vararg entities: Entity) {
        list.removeAll(entities)
    }

    override fun deleteAll() {
        list.clear()
    }

}