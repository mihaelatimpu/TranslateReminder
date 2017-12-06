package com.mimi.translatereminder.utils.json

import com.google.gson.Gson
import com.mimi.translatereminder.dto.ImportedJson
import com.mimi.translatereminder.repository.TranslationRepository

/**
 * Created by Mimi on 06/12/2017.
 *
 */
class ImportUtil(val string: String, val repository: TranslationRepository) {
    fun start() {
        repository.deleteAll()
        val imported = Gson().fromJson(string, ImportedJson::class.java)
        imported.words.forEach {
            repository.addEntity(it.toEntity())
        }
    }
}