package com.mimi.translatereminder.repository

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.repository.local.room.TranslationDao

/**
 * Created by Mimi on 05/12/2017.
 *
 */
class TranslationRepository(private val db: TranslationDao) {

    fun addEntity(entity: Entity) {
        db.insertAll(entity)
    }

    fun deleteAll() {
        db.deleteAll()
    }
    fun delete(item:Entity){
        db.delete(item)
    }

    fun findEntityByGermanWord(germanVersion: String)
            = db.getEntitiesByGermanVersion(germanVersion)

    fun findEntityByTranslation(translation: String)
            = db.getEntityByTranslation(translation)

    fun getAll() = db.selectAll()
}