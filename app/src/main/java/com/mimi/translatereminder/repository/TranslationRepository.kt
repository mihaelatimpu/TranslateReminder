package com.mimi.translatereminder.repository

import android.content.Context
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.repository.local.room.TranslationDao
import com.mimi.translatereminder.repository.local.sharedprefs.SharedPreferencesUtil

/**
 * Created by Mimi on 05/12/2017.
 *
 */
class TranslationRepository(private val db: TranslationDao) {
    val sharedPrefs = SharedPreferencesUtil()

    fun addEntity(entity: Entity) {
        db.insertAll(entity)
    }

    fun saveEntity(entity: Entity) {
        db.updateEntity(entity)
    }

    fun selectItemById(id: Int) = db.selectItemById(id).firstOrNull()

    fun deleteAll() {
        db.deleteAll()
    }

    fun delete(item: Entity) {
        db.delete(item)
    }

    fun findEntityByGermanWord(germanVersion: String)
            = db.getEntitiesByGermanVersion(germanVersion)

    fun findEntityByTranslation(translation: String)
            = db.getEntityByTranslation(translation)

    fun getAll() = db.selectAll()

    fun retrieveLearningItems(context: Context): List<Entity> {
        val count = sharedPrefs.getLearningItemsPerSession(context)
        val states = listOf(Entity.STATE_LEARNING_4, Entity.STATE_LEARNING_3,
                Entity.STATE_LEARNING_2, Entity.STATE_LEARNING_1)
        return retrieveItems(states, count)

    }

    private fun retrieveItems(states: List<Int>, limit: Int): List<Entity> {
        val list = ArrayList<Entity>()
        states.forEach {
            if (list.size >= limit)
                return list
            list.addAll(db.getEntityByState(it, limit - list.size))
        }
        return list
    }
}