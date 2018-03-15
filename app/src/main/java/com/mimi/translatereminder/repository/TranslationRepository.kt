package com.mimi.translatereminder.repository

import android.content.Context
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.repository.local.room.EntityDao
import com.mimi.translatereminder.repository.local.sharedprefs.SharedPreferencesUtil
import java.util.*

/**
 * Created by Mimi on 05/12/2017.
 *
 */
class TranslationRepository(private val itemDb: EntityDao) {
    private val sharedPrefs = SharedPreferencesUtil()

    fun addEntity(entity: Entity) =
            itemDb.insert(entity)


    fun saveEntity(entity: Entity) {
        itemDb.updateEntity(entity)
    }

    fun selectItemById(id: Int) = itemDb.selectItemById(id).firstOrNull()

    fun getRandomItems(excludeId: Int, count: Int): List<Entity> {
        return itemDb.getRandomItems(excludeId = excludeId, limit = count)
    }

    fun getLearningItems(count: Int): List<Entity> {
        return itemDb.getLearningItems(limit = count)
    }

    fun getReviewItems(count: Int): List<Entity> {
        return itemDb.getReviewItems(limit = count,
                currentTimeInMillis = Calendar.getInstance().timeInMillis)
    }


    fun getOverflowItems(count: Int): List<Entity> {
        return itemDb.getOverflow(limit = count)
    }

    fun getMistakenItems(count: Int): List<Entity> {
        return itemDb.getMistakenItems(limit = count)
    }

    fun deleteAll() {
        itemDb.deleteAll()
    }

    fun delete(item: Entity) {
        itemDb.delete(item)
    }

    fun findEntityByGermanWord(germanVersion: String) = itemDb.getEntitiesByGermanVersion(germanVersion)

    fun findEntityByTranslation(translation: String) = itemDb.getEntityByTranslation(translation)

    fun getAll() = itemDb.selectAll()

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

    fun retrieveListeningItems(context: Context): List<Entity> {
        val count = sharedPrefs.getListeningItemsPerSession(context)
        return if (count == -1)
            getAll()
        else
            getReviewItems(count)
    }

    fun findSentences(parentId: Int) = itemDb.findSentences(parentId)
}