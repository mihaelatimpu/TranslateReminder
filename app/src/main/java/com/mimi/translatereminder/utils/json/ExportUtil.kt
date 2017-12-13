package com.mimi.translatereminder.utils.json

import com.google.gson.Gson
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.dto.ImportExportJson

/**
 * Created by Mimi on 07/12/2017.
 *
 */

class ExportUtil(private val items:List<Entity>) {
    fun start(): String {
        val exportedObject = ImportExportJson(items)
        return Gson().toJson(exportedObject)
    }

}