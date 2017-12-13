package com.mimi.translatereminder.utils.json

import com.google.gson.Gson
import com.mimi.translatereminder.dto.ImportExportJson
import com.mimi.translatereminder.repository.TranslationRepository

/**
 * Created by Mimi on 06/12/2017.
 *
 */
class ImportUtil(val string: String, val repository: TranslationRepository) {
    fun start() {
        repository.deleteAll()
        val imported = Gson().fromJson(string, ImportExportJson::class.java)
        imported.values.forEach {
            repository.addEntity(it)
        }
    }
}