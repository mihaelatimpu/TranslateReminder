package com.mimi.translatereminder.dto

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by Mimi on 05/12/2017.
 * The base entity of the application
 */
@Entity(tableName = "Translation")
class Translation(
        @PrimaryKey(autoGenerate = true)
        val id:Int = -1,
        val germanWord:String,
        val translation:String,
        val dateAdded:Long = Calendar.getInstance().timeInMillis,
        val reviewCount:Int = 0,
        val mistakesCount:Int = 0)
