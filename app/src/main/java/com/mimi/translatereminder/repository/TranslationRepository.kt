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
    private val sharedPrefs = SharedPreferencesUtil()

    fun addEntity(entity: Entity) {
        db.insertAll(entity)
    }

    fun saveEntity(entity: Entity) {
        db.updateEntity(entity)
    }

    fun selectItemById(id: Int) = db.selectItemById(id).firstOrNull()

    fun getRandomItems(excludeId: Int, count: Int): List<Entity> {
        return db.getRandomItems(excludeId = excludeId, limit = count)
    }

    fun getLearningItems(count: Int): List<Entity> {
        return db.getLearningItems(limit = count)
    }

    fun getReviewItems(count: Int): List<Entity> {
        return db.getReviewItems(limit = count)
    }

    fun getMistakenItems(count: Int): List<Entity> {
        return db.getMistakenItems(limit = count)
    }

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
        return getLearningItems(count)
    }

    fun retrieveWrongItems(context: Context): List<Entity> {
        val count = sharedPrefs.getWrongItemsPerSession(context)
        return getMistakenItems(count)
    }

    fun retrieveReviewItems(context: Context): List<Entity> {
        val count = sharedPrefs.getReviewItemsPerSession(context)
        return getReviewItems(count)
    }
}