package com.mimi.translatereminder.utils

import android.content.Context
import android.content.Intent
import android.os.Environment
import com.mimi.translatereminder.R
import java.io.*


/**
 * Created by Mimi on 06/12/2017.
 *
 */
class FileUtil {
    companion object {
        const val EXPORT_FILENAME = "exported.json"
        const val EXPORT_ARCHIVED_FILENAME = "exported_archived.json"
    }

    fun readTextFromImportResources(context: Context): String {
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

    fun writeExportedDataToNewFile(json: String, fileName: String = EXPORT_FILENAME): String? {
        var path: String?
        try {
            path = getPathForDownloadsFolder(fileName)
            val file = FileWriter(path)
            file.write(json)
            file.flush()
            file.close()
        } catch (e: IOException) {
            e.printStackTrace()
            path = null
        }

        return path
    }

    fun getPathForDownloadsFolder(fileName: String): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + fileName
    }


    fun createGetContentIntent(): Intent {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return intent
    }
}