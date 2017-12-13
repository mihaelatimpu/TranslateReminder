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
        val EXPORT_FILENAME = "exported.json"
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

    fun writeExportedDataToNewFile(json: String): String? {
     /*   val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                EXPORT_FILENAME)
        file.parentFile.mkdirs()
        if (!file.exists())
            file.createNewFile()
        try {

            val f = FileOutputStream(file)
            val pw = PrintWriter(f)
            pw.print(json)
            pw.flush()
            pw.close()
            f.close()
            *//*
                   val writer = FileWriter(file)
                   writer.write(json)
                   writer.flush()
                   writer.close()*//*
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file*/
        var path: String?
        try {
            path = getPathForDownloadsFolder(EXPORT_FILENAME)
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