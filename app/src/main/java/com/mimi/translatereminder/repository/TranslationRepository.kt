package com.mimi.translatereminder.repository

import com.mimi.translatereminder.dto.Translation
import com.mimi.translatereminder.repository.local.room.TranslationDao

/**
 * Created by Mimi on 05/12/2017.
 *
 */
class TranslationRepository(private val db: TranslationDao) {

    fun addTranslation(translation: Translation) {
        db.insertAll(translation)
    }

    fun getAll() = db.selectAll()
}