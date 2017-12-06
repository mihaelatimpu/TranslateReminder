package com.mimi.translatereminder.utils

import android.R.raw
import android.content.Context
import com.mimi.translatereminder.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter


/**
 * Created by Mimi on 06/12/2017.
 *
 */
class FileUtil {
    fun readTextFromImportResources(context: Context):String {
        val `is` = context.resources.openRawResource(R.raw.translations)
        val writer = StringWriter()
        val buffer = CharArray(1024)
            val reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
            var n: Int = reader.read(buffer)
            while (n != -1) {
                writer.write(buffer, 0, n)
                n = reader.read(buffer)
            }
        val jsonString = writer.toString()
        writer.close()
        return jsonString
    }
}